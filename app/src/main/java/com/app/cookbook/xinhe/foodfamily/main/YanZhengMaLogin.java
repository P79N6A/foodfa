package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.entity.Code;
import com.app.cookbook.xinhe.foodfamily.main.entity.Token;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YanZhengMaLogin extends BaseActivity {
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @BindView(R.id.edit_yanzhengma)
    EditText edit_yanzhengma;
    @BindView(R.id.yanzhengma_layout)
    LinearLayout yanzhengma_layout;
    @BindView(R.id.yanzhenma_tv)
    TextView yanzhenma_tv;
    @BindView(R.id.btn_login)
    RelativeLayout btn_login;
    @BindView(R.id.wangji_password)
    TextView wangji_password;
    @BindView(R.id.register_tv)
    TextView register_tv;
    @BindView(R.id.text_word)
    RelativeLayout text_word;

    private String yanzhengma_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_yan_zheng_ma_login);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            yanzhengma_layout.setBackgroundResource(R.drawable.yanzhengma_bg);
            yanzhenma_tv.setTextColor(Color.parseColor("#565656"));
            yanzhenma_tv.setText(millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            yanzhengma_layout.setBackgroundResource(R.drawable.yanzhengma_bg2);
            yanzhenma_tv.setTextColor(Color.parseColor("#333333"));
            yanzhenma_tv.setText("重新获取");
            yanzhengma_layout.setEnabled(true);
        }
    };

    @Override
    public void doBusiness(Context mContext) {
        edit_yanzhengma.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//设置只能输入11位

        yanzhengma_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "yanzhengma_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "Handover_authentication_code_login_verifying_code", prop);


                if (TextUtils.isEmpty(edit_phone_number.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!StringsHelper.isMobileNO(edit_phone_number.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    yanzhengma_layout.setEnabled(false);//设置不可点击，等待60秒过后可以点击
                    timer.start();
                    //获取验证码接口
                    Get_YanZhengMa();
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "btn_login");
                StatService.trackCustomKVEvent(getApplicationContext(), "Handover_authentication_code_login_sign_in", prop);

                if (TextUtils.isEmpty(edit_phone_number.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!StringsHelper.isMobileNO(edit_phone_number.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(edit_yanzhengma.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //请求网络登录
                    Login();
                }
            }
        });
        wangji_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPasswordActivity.class);
            }
        });
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(RegisterInvitationActivity.class);
                startActivity(RegisterActivity.class);
            }
        });
        text_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "text_word");
                StatService.trackCustomKVEvent(YanZhengMaLogin.this, "Switch_on_password_login", prop);

                startActivity(LoginActivity.class);
                finish();
            }
        });
    }

    private void Login() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        params.put("code", edit_yanzhengma.getText().toString());
        params.put("registration_id", MyApplication.registrationId);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("验证码登录参数", params.toString());
        subscription = Network.getInstance("登录", getApplicationContext())
                .yanzhengma_login(params, new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                    @Override
                    public void on_post_entity(Token o) {
                        Log.e("登录成功：", o.getToken());
                        //Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();

                        if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                            Log.e("token此时是空的", SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString());
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", o.getToken());
                            startActivity(FoodFamilyActivity.class);
                        } else {
                            Log.e("token此时不是空的", SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString());
                            SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", o.getToken());
                            startActivity(FoodFamilyActivity.class);
                        }
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Logger.e("登录失败：" + error);
                        if (error.equals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 58 path $.data")) {
                            //代表转换失败,重新登录
                            Login2();
                        } else if (error.equals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 52 path $.data")) {
                            //代表密码失败,重新登录
                            Login2();
                        } else {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, this, false));
    }

    private void Login2() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        params.put("code", edit_yanzhengma.getText().toString());
        params.put("registration_id", MyApplication.registrationId);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        subscription = Network.getInstance("登录", getApplicationContext())
                .yanzhengma_login2(params, new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                    @Override
                    public void on_post_entity(List list) {

                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }

    private void Get_YanZhengMa() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        subscription = Network.getInstance("接收验证码", getApplicationContext())
                .get_login_yanzhengma(params,
                        new ProgressSubscriberNew<>(Code.class, new GsonSubscriberOnNextListener<Code>() {
                            @Override
                            public void on_post_entity(Code code) {
                                Logger.e("接收验证码成功：" + code.getCode());
                                yanzhengma_number = code.getCode();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("接收验证码报错：" + error);
                                Toast.makeText(getApplicationContext(), "接收验证码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }, this, true));
    }

    @Override
    public void onBackPressed() {
//        PopWindowHelper.public_tishi_pop(YanZhengMaLogin.this, "食与家提示", "是否退回桌面？", "取消", "确定", new DialogCallBack() {
//            @Override
//            public void save() {
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        });
        finish();
    }

    @Override
    public void widgetClick(View v) {

    }
}
