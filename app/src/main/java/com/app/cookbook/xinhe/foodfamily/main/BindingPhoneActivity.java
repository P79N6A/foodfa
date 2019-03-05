package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.jiguang.TagAliasOperatorHelper;
import com.app.cookbook.xinhe.foodfamily.main.entity.Code;
import com.app.cookbook.xinhe.foodfamily.main.entity.Token;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.progress.ProgressHUD;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindingPhoneActivity extends BaseActivity implements DialogInterface.OnCancelListener {

    @BindView(R.id.close_btn)
    ImageView close_btn;
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @BindView(R.id.edit_code_number)
    EditText edit_code_number;
    @BindView(R.id.confirm_btn)
    RelativeLayout confirm_btn;
    @BindView(R.id.yanzhengma_layout)
    LinearLayout yanzhengma_layout;
    @BindView(R.id.yanzhenma_tv)
    TextView yanzhenma_tv;

    //第三方登录id
    private String login_openId = null;
    //第三方昵称
    private String login_name = null;
    //第三方头像
    private String login_avatar = null;
    //第三方性别
    private String login_sex = null;
    //第三方unionid
    private String login_unionid = null;
    //第三方类型
    private String login_type = null;

    private ProgressHUD mProgressHUD;

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_binding_phone);
        ButterKnife.bind(this);

        login_openId = getIntent().getStringExtra("login_openId");
        login_name = getIntent().getStringExtra("login_name");
        login_avatar = getIntent().getStringExtra("login_avatar");
        login_sex = getIntent().getStringExtra("login_sex");
        login_unionid = getIntent().getStringExtra("login_unionid");
        login_type = getIntent().getStringExtra("login_type");

        Log.e("123", "        " + login_openId + "   " + login_name + "     " + login_avatar + "       " + login_sex + "         " + login_unionid + "         " + login_type);
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
        edit_code_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//设置只能输入11位

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edit_phone_number.getText().toString();
                String code = edit_code_number.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(BindingPhoneActivity.this, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(code)) {
                    Toast.makeText(BindingPhoneActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
                    if ("1".equals(login_type)) {
                        mProgressHUD = ProgressHUD.show(BindingPhoneActivity.this, "正在登录中.....", false, BindingPhoneActivity.this);
                        init_wechat_request(phone, code);
                    } else if ("2".equals(login_type)) {
                        mProgressHUD = ProgressHUD.show(BindingPhoneActivity.this, "正在登录中.....", false, BindingPhoneActivity.this);
                        init_weibo_request(phone, code);
                    }
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) {

    }

    private void Get_YanZhengMa() {
        Map<String, String> params = new HashMap<>();
        params.put("tel", edit_phone_number.getText().toString());
        subscription = Network.getInstance("接收验证码", getApplicationContext())
                .code_bind_phone_request(params,
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

    //微信绑定
    private void init_wechat_request(String tel, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", "");
        params.put("phone_version", Contacts.brand);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("weixin_login", login_openId);
        params.put("tel", tel);
        params.put("identifying_code", code);
        if (TextUtils.isEmpty(login_sex)) {
            params.put("sex", "1");
        } else {
            if ("m".equals(login_sex)) {
                params.put("sex", "1");
            } else if ("f".equals(login_sex)) {
                params.put("sex", "2");
            } else {
                params.put("sex", login_sex);
            }
        }
        params.put("name", login_name);
        params.put("avatar", login_avatar);
        params.put("wechat_unionid", login_unionid);
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("123", "         微信绑定 params      " + params);
        subscription = Network.getInstance("微信绑定", this)
                .wechat_bind_phone_request(params,
                        new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                            @Override
                            public void on_post_entity(Token token) {
                                Log.e("123", "          ------------> 微信绑定成功   ");
                                Contacts.isPage = "1";
                                XGPushManager.setTag(BindingPhoneActivity.this, token.getTag());

                                //设置极光别名
                                TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                                tagAliasBean.setAliasAction(false);
                                tagAliasBean.setAction(1);
                                Set<String> set = new HashSet<>();
                                set.add(token.getTag());
                                tagAliasBean.setTags(set);
                                //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                                tagAliasOperatorHelper.handleAction(BindingPhoneActivity.this, 2, tagAliasBean);
                                Log.e("服务器返回的别名", "" + token.getTag());


                                SharedPreferencesHelper.put(getApplicationContext(), "UserName", edit_phone_number.getText().toString());
                                SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", Contacts.brand);
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                                    finish();
                                } else {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                                    finish();
                                }

                                MyApplication.is_first = true;
                                Contacts.isStart = true;
                                Contacts.typeMeg = "1";
                                if (mProgressHUD != null) {
                                    mProgressHUD.dismiss();
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "微信绑定失败  " + error);
                                Toast.makeText(BindingPhoneActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }

    //微博绑定
    private void init_weibo_request(String tel, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", "");
        params.put("phone_version", Contacts.brand);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("weibo_login", login_openId);
        params.put("tel", tel);
        params.put("identifying_code", code);
        if (TextUtils.isEmpty(login_sex)) {
            params.put("sex", "1");
        } else {
            if ("m".equals(login_sex)) {
                params.put("sex", "1");
            } else if ("f".equals(login_sex)) {
                params.put("sex", "2");
            } else {
                params.put("sex", "1");
            }
        }
        params.put("name", login_name);
        params.put("avatar", login_avatar);
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("123", "         微博绑定 params      " + params);
        subscription = Network.getInstance("微博绑定", this)
                .weibo_bind_phone_request(params,
                        new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                            @Override
                            public void on_post_entity(Token token) {
                                Log.e("123", "          ------------> 微博绑定成功   ");
                                Contacts.isPage = "1";
                                XGPushManager.setTag(BindingPhoneActivity.this, token.getTag());

                                //设置极光别名
                                TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                                tagAliasBean.setAliasAction(false);
                                tagAliasBean.setAction(1);
                                Set<String> set = new HashSet<>();
                                set.add(token.getTag());
                                tagAliasBean.setTags(set);
                                //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                                tagAliasOperatorHelper.handleAction(BindingPhoneActivity.this, 2, tagAliasBean);
                                Log.e("服务器返回的别名", "" + token.getTag());


                                SharedPreferencesHelper.put(getApplicationContext(), "UserName", edit_phone_number.getText().toString());
                                SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", Contacts.brand);
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                                    finish();
                                } else {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                                    finish();
                                }

                                MyApplication.is_first = true;
                                Contacts.isStart = true;
                                if (mProgressHUD != null) {
                                    mProgressHUD.dismiss();
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "微博绑定失败  " + error);
                            }
                        }, this, false));
    }


    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
