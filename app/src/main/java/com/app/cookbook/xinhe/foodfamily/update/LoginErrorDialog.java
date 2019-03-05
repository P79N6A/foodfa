package com.app.cookbook.xinhe.foodfamily.update;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.Token;
import com.app.cookbook.xinhe.foodfamily.main.entity.UserEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.SettingActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.StatGameUser;
import com.tencent.stat.StatService;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by 18030150 on 2018/5/2.
 */

public class LoginErrorDialog extends Activity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    //取消登录
    @BindView(R.id.cancel_login)
    LinearLayout cancel_login;
    //现在登录
    @BindView(R.id.now_login)
    LinearLayout now_login;
    //用户名
    private String UserName;
    /**
     * Rxjava
     */
    protected Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.login_error_dialog);
        //初始化黄牛刀
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        String errorMes = getIntent().getStringExtra("errorMes");
        tv_title.setText(errorMes);
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        cancel_login.setOnClickListener(this);
        now_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_login:
                Contacts.isPage = "0";
                SharedPreferencesHelper.remove(LoginErrorDialog.this, "login_token");//清除token
                EventBus.getDefault().post(new MessageEvent("1"));
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.now_login:
                Login();
                break;
        }
    }


    private void Login() {
        UserName = SharedPreferencesHelper.get(getApplicationContext(), "UserName", "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        if (!"".equals(SharedPreferencesHelper.get(getApplicationContext(), "pigeonData", ""))) {
            String pigeonData = SharedPreferencesHelper.get(getApplicationContext(), "pigeonData", "").toString();
            params.put("pigeon", pigeonData);
        }
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("登录参数", params.toString());
        subscription = Network.getInstance("登录", getApplicationContext())
                .new_login(params, new ProgressSubscriberNew<>(
                        Token.class, new GsonSubscriberOnNextListener<Token>() {
                    @Override
                    public void on_post_entity(Token token) {
                        Log.e("123", "  登录成功： " + token.getToken() + "     token.getTag()    " + token.getTag());
                        Contacts.isPage = "1";
                        XGPushManager.setTag(LoginErrorDialog.this, token.getTag());
                        //Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                        SharedPreferencesHelper.remove(LoginErrorDialog.this, "login_token");//清除token
                        if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                        } else {
                            SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
                        }

                        MyApplication.is_first = true;
                        Contacts.isStart = true;
                        //登录成功之后，加载腾讯埋点统计
                        init_maidian_tongji_login();
                        Toast.makeText(LoginErrorDialog.this, "登录成功", Toast.LENGTH_SHORT).show();
                        get_user_information();
                        overridePendingTransition(0, 0);
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "登录失败：" + error);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }

                }, this, false, "登录中..."));
    }

    private void get_user_information() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", LoginErrorDialog.this)
                .get_user_information(params,
                        new ProgressSubscriberNew<>(UserEntity.class, new GsonSubscriberOnNextListener<UserEntity>() {
                            @Override
                            public void on_post_entity(UserEntity userEntity) {
                                Log.e("123", "获取用户信息成功：" + userEntity.getName());
                                //设置页面的信息
                                SharedPreferencesHelper.put(LoginErrorDialog.this, "userId", userEntity.getUid() + "");
                                EventBus.getDefault().post(new MessageEvent("1"));
                                //跳转完善个人信息页
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取用户信息报错：" + error);
                            }
                        }, LoginErrorDialog.this, false));
    }


    /**
     * 登录成功之后，加载腾讯埋点统计
     */
    private void init_maidian_tongji_login() {
        if (!TextUtils.isEmpty(UserName)) {
            StatGameUser gameUser = new StatGameUser();
            gameUser.setWorldName(UserName);
            StatService.reportGameUser(getApplicationContext(), gameUser);
            StatService.trackCustomEvent(getApplicationContext(), "login", "true");
            Properties prop = new Properties();
            prop.setProperty("user_phone_on_login", UserName);
            StatService.trackCustomKVEvent(getApplicationContext(), "OnLogin", prop);
        }
    }


}
