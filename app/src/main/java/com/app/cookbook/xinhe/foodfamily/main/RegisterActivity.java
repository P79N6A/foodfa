package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.jiguang.TagAliasOperatorHelper;
import com.app.cookbook.xinhe.foodfamily.main.entity.Code;
import com.app.cookbook.xinhe.foodfamily.main.entity.Token;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.AndroidWorkaround;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.check_password_show)
    CheckBox check_password_show;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @BindView(R.id.btn_login)
    RelativeLayout btn_login;
    @BindView(R.id.edit_yanzhengma)
    EditText edit_yanzhengma;
    @BindView(R.id.edit_yaoqingma)
    EditText edit_yaoqingma;
    @BindView(R.id.goto_register)
    TextView goto_register;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.yanzhengma_layout)
    LinearLayout yanzhengma_layout;
    @BindView(R.id.yanzhenma_tv)
    TextView yanzhenma_tv;
    //邀请通道
    @BindView(R.id.yao_aisle)
    ImageView yao_aisle;

    private String yanzhengma_number;

    private boolean is_enable_register = true;
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private boolean issend = false;

    //邀请码
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_register2);
        //初始化黄牛刀
        ButterKnife.bind(this);
        Contacts.addActivity(this);
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";

        code = getIntent().getStringExtra("code");
        Log.e("123", "     code    " + code);

        /**为了适配华为屏幕软键盘*/
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
            //mContextView.setPadding(0,0,0,50);
        }
        check_password_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText的密码为可见的
                    edit_password.setInputType(TYPE_CLASS_TEXT);
                } else {
                    //设置密码为隐藏的
                    edit_password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                }
                edit_password.setSelection(edit_password.getText().toString().length());//设置光标位置在文本框末尾
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//可以注册
                    is_enable_register = true;
                } else {//不可以注册
                    is_enable_register = false;
                }
            }
        });

        //发送验证码
        yanzhengma_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Properties prop = new Properties();
                prop.setProperty("name", "yanzhengma_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "User_registration_verifying_code", prop);

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
        //注册
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户注册事件，统计用户点击注册按钮的次数
                Properties prop = new Properties();
                prop.setProperty("name", "btn_login");
                StatService.trackCustomKVEvent(getApplicationContext(), "Register", prop);

                if (TextUtils.isEmpty(edit_phone_number.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!StringsHelper.isMobileNO(edit_phone_number.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edit_yanzhengma.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edit_password.getText().toString()) && edit_password.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "密码不能为空并且不能少于六位！", Toast.LENGTH_SHORT).show();
                } else if (!is_enable_register) {
                    Toast.makeText(getApplicationContext(), "请先勾选用户协议！", Toast.LENGTH_SHORT).show();

                } else {
                    if (issend == false) {
                        // 请求网络注册
                        KuaiSuRegister();
                    }
                }


            }
        });

        goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, ServiceActivity.class);
                startActivity(intent);

            }
        });
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "back_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "User_registration_return", prop);

                finish();
            }
        });

        //邀请通道
        yao_aisle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, InvitationCodeActivity.class);
                startActivity(intent);
            }
        });

        edit_yanzhengma.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_yaoqingma.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(Contacts.invitationCode)) {
            yao_aisle.setImageResource(R.drawable.icon_yao_off);
        } else {
            yao_aisle.setImageResource(R.drawable.icon_yao_on);
        }
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

    private String Token;

    private void KuaiSuRegister() {
        Map<String, String> params = new HashMap<>();
        params.put("password", edit_password.getText().toString());
        params.put("identifying_code", edit_yanzhengma.getText().toString());
//        params.put("invitation_code", code);
        if (!TextUtils.isEmpty(Contacts.invitationCode)) {
            params.put("invitation_code", Contacts.invitationCode);
        }
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        params.put("tel", edit_phone_number.getText().toString());
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("123", "      注册  params    " + params);
        subscription = Network.getInstance("注册", getApplicationContext())
                .fastRegister(params, new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                    @Override
                    public void on_post_entity(Token o) {
                        issend = true;
                        if (!TextUtils.isEmpty(Contacts.invitationCode)) {
                            Contacts.invitationCode = "";
                        }
                        Toast.makeText(RegisterActivity.this, "恭喜您注册成功", Toast.LENGTH_SHORT).show();
                        Token = o.getToken();
                        if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                            Log.e("token此时是空的", SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString());
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", Token);
                        } else {
                            Log.e("token此时不是空的", SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString());
                            SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", Token);
                        }
                        //设置极光别名
                        TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                        tagAliasBean.setAliasAction(false);
                        tagAliasBean.setAction(1);
                        Set<String> set = new HashSet<>();
                        set.add(o.getTag());
                        tagAliasBean.setTags(set);
                        //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                        tagAliasOperatorHelper.handleAction(RegisterActivity.this, 2, tagAliasBean);
                        Log.e("服务器返回的别名", "" + o.getTag());

                        Contacts.isStart = true;
                        MyApplication.destoryActivity("FoodFamilyActivity");
                        //跳转完善个人信息页
                        Intent intent = new Intent(RegisterActivity.this, PerfectInformationActivity.class);
                        startActivity(intent);
                        Contacts.typeMeg = "1";
                        //跳转首页
                        Contacts.finishAll();
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123","注册失败：" + error);
                        if ("邀请码已经被使用".equals(error)) {
                            Contacts.invitationCode = "";
                        }
                        issend = false;
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }


    private void Get_YanZhengMa() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        subscription = Network.getInstance("接收验证码", getApplicationContext())
                .get_yanzhengma(params,
                        new ProgressSubscriberNew<>(Code.class, new GsonSubscriberOnNextListener<Code>() {
                            @Override
                            public void on_post_entity(Code code) {
                                Log.e("123","接收验证码成功：" + code.getCode());
                                yanzhengma_number = code.getCode();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","接收验证码报错：" + error);
                                Toast.makeText(getApplicationContext(), "接收验证码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }, this, true));

    }


    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(RegisterActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(RegisterActivity.this);
        }
    }
}
