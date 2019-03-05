package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.Code;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @BindView(R.id.edit_yanzhengma)
    EditText edit_yanzhengma;
    @BindView(R.id.yanzhengma_layout)
    LinearLayout yanzhengma_layout;
    @BindView(R.id.yanzhenma_tv)
    TextView yanzhenma_tv;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.check_password_show)
    CheckBox check_password_show;
    @BindView(R.id.btn_update)
    RelativeLayout btn_update;

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

        setContentLayout(R.layout.activity_forget_password);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
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
                StatService.trackCustomKVEvent(getApplicationContext(), "Forget_password_verifying_code", prop);


                if (TextUtils.isEmpty(edit_phone_number.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!StringsHelper.isMobileNO(edit_phone_number.getText().toString())){
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }  else {
                    yanzhengma_layout.setEnabled(false);//设置不可点击，等待60秒过后可以点击
                    timer.start();
                    //获取验证码接口
                    Get_YanZhengMa();
                }
            }
        });
        check_password_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText的密码为可见的
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "back_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "Forget_password_return", prop);
                finish();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_phone_number.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!StringsHelper.isMobileNO(edit_phone_number.getText().toString())){
                    Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(edit_yanzhengma.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if ("".equals(edit_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 请求网络注册
                    UpdatePassword();
                }
            }


        });
    }
    private void UpdatePassword() {
        Map<String, String> params = new HashMap<>();
        params.put("password", edit_password.getText().toString());
        params.put("code", edit_yanzhengma.getText().toString());
        params.put("tel", edit_phone_number.getText().toString());
        Log.e("精神科发尽快圣诞节",params.toString());
        subscription = Network.getInstance("修改密码", getApplicationContext())
                .update_password(params, new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                    @Override
                    public void on_post_entity(List s) {
                        Logger.e("修改密码成功：" + s);
                        Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {
                    }
                    @Override
                    public void onError(String error) {
                        Logger.e("修改密码失败：" + error);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }
    private void Get_YanZhengMa() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        subscription = Network.getInstance("接收验证码", getApplicationContext())
                .get_update_password_yanzhengma(params,
                        new ProgressSubscriberNew<>(Code.class, new GsonSubscriberOnNextListener<Code>() {
                            @Override
                            public void on_post_entity(Code code) {
                                Logger.e("接收验证码成功：" + code.getCode());
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
    public void widgetClick(View v) {

    }
}
