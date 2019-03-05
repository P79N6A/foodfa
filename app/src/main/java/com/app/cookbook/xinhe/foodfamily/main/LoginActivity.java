package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.jiguang.TagAliasOperatorHelper;
import com.app.cookbook.xinhe.foodfamily.main.entity.Token;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.MyMsg;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.StatGameUser;
import com.tencent.stat.StatService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import rx.Subscription;
import rx.functions.Action1;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.check_password_show)
    CheckBox check_password_show;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;
    @BindView(R.id.register_tv)
    TextView register_tv;
    @BindView(R.id.btn_login)
    RelativeLayout btn_login;
    @BindView(R.id.qiehuan_password_login)
    TextView qiehuan_password_login;
    @BindView(R.id.wangji_password)
    TextView wangji_password;
    @BindView(R.id.tv_colse)
    TextView tv_colse;
    //微信登录
    @BindView(R.id.wechat_login)
    TextView wechat_login;
    //微博登陆
    @BindView(R.id.weibo_login)
    TextView weibo_login;
    @BindView(R.id.rests_login)
    LinearLayout rests_login;
    @BindView(R.id.phone_layout)
    LinearLayout phone_layout;

    //第三方登录id
    private String login_openId;
    //第三方昵称
    private String login_name;
    //第三方头像
    private String login_avatar;
    //第三方性别
    private String login_sex;
    //第三方unionid
    private String login_unionid;
    //第三方类型
    private String login_type;
    /**
     * Rxjava
     */
    protected Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login2);
        //初始化黄牛刀
        ButterKnife.bind(this);
        initView();
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(this);
        }
    }

    private void initView() {
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        tv_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_phone_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//设置输入数字
        edit_phone_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//设置只能输入11位

        check_password_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码框内容
                    edit_password.setInputType(TYPE_CLASS_TEXT);
                } else {
                    //隐藏密码框内容
                    edit_password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                }
                edit_password.setSelection(edit_password.getText().toString().length());//设置光标位置在文本框末尾
            }
        });


        //注册
        RxUtils.clickView(register_tv)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Properties prop = new Properties();
                        prop.setProperty("name", "register_tv");
                        StatService.trackCustomKVEvent(LoginActivity.this, "User_registration_register", prop);
//                        startActivity(RegisterInvitationActivity.class);
//                        startActivity(RegisterActivity.class);
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        //登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计用户点击登录的次数
                Properties prop = new Properties();
                prop.setProperty("name", "btn_login");
                StatService.trackCustomKVEvent(LoginActivity.this, "Password_login", prop);
                String phone = edit_phone_number.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.substring(0, 1).toUpperCase();
                    Log.e("123", "    " + phone);
                    if (TextUtils.isEmpty(edit_phone_number.getText())) {
                        Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (phone.equals("9")) {
                            if (TextUtils.isEmpty(edit_password.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                //请求网络登录
                                Login();
                            }
                        } else if (phone.equals("1")) {
                            if (!StringsHelper.isMobileNO(edit_phone_number.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                //请求网络登录
                                Login();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //切换密码登录
        qiehuan_password_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //埋点
                Properties prop = new Properties();
                prop.setProperty("name", "qiehuan_password_login");
                StatService.trackCustomKVEvent(LoginActivity.this, "Handover_authentication_code_login", prop);
//                startActivity(YanZhengMaLogin.class);
                Intent intent = new Intent(LoginActivity.this, YanZhengMaLogin.class);
                startActivity(intent);
                finish();
            }
        });
        //忘记密码
        wangji_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "wangji_password");
                StatService.trackCustomKVEvent(LoginActivity.this, "Forget_password", prop);
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);

                startActivity(intent);
//                startActivity(ForgetPasswordActivity.class);
            }
        });
        //微信登录
        wechat_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWeChat(LoginActivity.this);
            }
        });
        //微博登陆
        weibo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWeibo(LoginActivity.this);
            }
        });

    }


    private void Login() {
        Map<String, String> params = new HashMap<>();
        params.put("name", edit_phone_number.getText().toString());
        params.put("password", edit_password.getText().toString());
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
        Log.e("123", "        登录        " + params);
        subscription = Network.getInstance("登录", getApplicationContext())
                .login(params, new ProgressSubscriberNew<>(
                        Token.class, new GsonSubscriberOnNextListener<Token>() {
                    @Override
                    public void on_post_entity(Token token) {
                        Log.e("123", "  登录成功： " + token.getToken() + "     token.getTag()    " + token.getTag());
                        Contacts.isPage = "1";
                        XGPushManager.setTag(LoginActivity.this, token.getTag());

                        //设置极光别名
                        TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                        tagAliasBean.setAliasAction(false);
                        tagAliasBean.setAction(1);
                        Set<String> set = new HashSet<>();
                        set.add(token.getTag());
                        tagAliasBean.setTags(set);
                        //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                        tagAliasOperatorHelper.handleAction(LoginActivity.this, 2, tagAliasBean);
                        Log.e("服务器返回的别名", "" + token.getTag());


                        SharedPreferencesHelper.put(getApplicationContext(), "UserName", edit_phone_number.getText().toString());
                        SharedPreferencesHelper.put(getApplicationContext(), "UserPassword", edit_password.getText().toString());
                        SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", Contacts.brand);
                        if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                            finish();
                        } else {
                            SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                            SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                            finish();
                        }
                        requestUserMsg();
                        MyApplication.is_first = true;
                        Contacts.isStart = true;
                        //登录成功之后，加载腾讯埋点统计
                        init_maidian_tongji_login();
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "     登录失败：" + error);
                        Contacts.isPage = "1";
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }

                }, this, false, "登录中..."));
    }

    //微信登录
    private void loginWeChat(Context context) {
        final Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isClientValid()) {
            //客户端可用
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                login_openId = platform.getDb().getUserId();
                login_name = platform.getDb().getUserName();
                login_avatar = platform.getDb().getUserIcon();
                login_sex = platform.getDb().getUserGender();
                login_type = "1";
                login_unionid = platform.getDb().get("unionid").toString();
                Log.e("123", "");
                init_wechat_request(platform.getDb().getUserId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("123", "onError ---->  登录失败" + throwable.toString());
                Log.e("123", "onError ---->  登录失败" + throwable.getStackTrace().toString());
                Log.e("123", "onError ---->  登录失败" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("123", "onCancel ---->  登录取消");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    //微信登录
    private void loginWeibo(Context context) {
        final Platform Weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (Weibo.isClientValid()) {
            //客户端可用
        }
        if (Weibo.isAuthValid()) {
            Weibo.removeAccount(true);
        }
        Weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                Log.e("123", "               " + platform.getDb().exportData());
                login_openId = platform.getDb().getUserId();
                login_name = platform.getDb().getUserName();
                login_avatar = platform.getDb().getUserIcon();
                login_sex = platform.getDb().getUserGender();
                login_type = "2";
                login_unionid = platform.getDb().get("unionid").toString();
                init_weibo_request(platform.getDb().getUserId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("123", "onError ---->  登录失败" + throwable.toString());
                Log.e("123", "onError ---->  登录失败" + throwable.getStackTrace().toString());
                Log.e("123", "onError ---->  登录失败" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("123", "onCancel ---->  登录取消");
            }
        });
        Weibo.SSOSetting(false);
        Weibo.showUser(null);
    }

    //微信登錄
    private void init_wechat_request(String opentid) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", "");
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        params.put("weixin_login", opentid);
        params.put("name", login_name);
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("123", "         微信登錄 params      " + params);
        subscription = Network.getInstance("微信登錄", this)
                .check_wechat_request(params,
                        new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                            @Override
                            public void on_post_entity(Token token) {
                                Contacts.isPage = "1";
                                XGPushManager.setTag(LoginActivity.this, token.getTag());

                                //设置极光别名
                                TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                                tagAliasBean.setAliasAction(false);
                                tagAliasBean.setAction(1);
                                Set<String> set = new HashSet<>();
                                set.add(token.getTag());
                                tagAliasBean.setTags(set);
                                //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                                tagAliasOperatorHelper.handleAction(LoginActivity.this, 2, tagAliasBean);
                                Log.e("服务器返回的别名", "" + token.getTag());


                                SharedPreferencesHelper.put(getApplicationContext(), "UserName", edit_phone_number.getText().toString());
                                SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", Contacts.brand);
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                                    finish();
                                } else {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                                    finish();
                                }
                                requestUserMsg();
                                MyApplication.is_first = true;
                                Contacts.isStart = true;
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("未绑定手机号码".equals(error)) {
                                    Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                    intent.putExtra("login_openId", login_openId);
                                    intent.putExtra("login_name", login_name);
                                    intent.putExtra("login_avatar", login_avatar);
                                    intent.putExtra("login_sex", login_sex);
                                    intent.putExtra("login_type", login_type);
                                    intent.putExtra("login_unionid", login_unionid);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, this, false));
    }

    //微博登陆
    private void init_weibo_request(String opentid) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", "");
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        params.put("weibo_login", opentid);
        params.put("name", login_name);
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
        }
        Log.e("123", "         微博登陆 params      " + params);
        subscription = Network.getInstance("微博登陆", this)
                .check_weibo_request(params,
                        new ProgressSubscriberNew<>(Token.class, new GsonSubscriberOnNextListener<Token>() {
                            @Override
                            public void on_post_entity(Token token) {
                                Contacts.isPage = "1";
                                XGPushManager.setTag(LoginActivity.this, token.getTag());

                                //设置极光别名
                                TagAliasOperatorHelper tagAliasOperatorHelper = TagAliasOperatorHelper.getInstance();
                                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                                tagAliasBean.setAliasAction(false);
                                tagAliasBean.setAction(1);
                                Set<String> set = new HashSet<>();
                                set.add(token.getTag());
                                tagAliasBean.setTags(set);
                                //tagAliasOperatorHelper.handleAction(LoginActivity.this,2,edit_phone_number.getText().toString());
                                tagAliasOperatorHelper.handleAction(LoginActivity.this, 2, tagAliasBean);
                                Log.e("服务器返回的别名", "" + token.getTag());


                                SharedPreferencesHelper.put(getApplicationContext(), "UserName", edit_phone_number.getText().toString());
                                SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", Contacts.brand);
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                                    finish();
                                } else {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
                                    SharedPreferencesHelper.put(getApplicationContext(), "login_token", token.getToken());
//                                    finish();
                                }
                                requestUserMsg();
                                MyApplication.is_first = true;
                                Contacts.isStart = true;
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("未绑定手机号码".equals(error)) {
                                    Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                    intent.putExtra("login_openId", login_openId);
                                    intent.putExtra("login_name", login_name);
                                    intent.putExtra("login_avatar", login_avatar);
                                    intent.putExtra("login_sex", login_sex);
                                    intent.putExtra("login_type", login_type);
                                    intent.putExtra("login_unionid", login_unionid);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, this, false));
    }


    private void requestUserMsg() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", LoginActivity.this)
                .getOwnInformation(
                        new ProgressSubscriberNew<>(MyMsg.class, new GsonSubscriberOnNextListener<MyMsg>() {
                            @Override
                            public void on_post_entity(MyMsg userEntity) {
                                Log.e("123", "获取用户信息成功：" + userEntity.getName());
                                SharedPreferencesHelper.put(LoginActivity.this, "isWiFI", userEntity.getIs_wifi());
                                //设置页面的信息
                                SharedPreferencesHelper.put(LoginActivity.this, "user_id", userEntity.getId() + "");
                                SharedPreferencesHelper.put(LoginActivity.this, "userId", userEntity.getId() + "");
                                Contacts.weixin_login = userEntity.getWeixin_login();
//                                Contacts.weixin_name = userEntity.getWechat_name();

                                Contacts.weibo_login = userEntity.getWeibo_login();
//                                Contacts.weibo_name = userEntity.getWeibo_name();

                                //对用户ID进行缓存
                                SharedPreferencesHelper.put(LoginActivity.this, "tel_phone", userEntity.getTel() + "");
                                SharedPreferencesHelper.put(LoginActivity.this, "user_name", userEntity.getName() + "");
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
                        }, LoginActivity.this, false));
    }


    /**
     * 登录成功之后，加载腾讯埋点统计
     */
    private void init_maidian_tongji_login() {
        StatGameUser gameUser = new StatGameUser();
        gameUser.setWorldName(edit_phone_number.getText().toString());
        StatService.reportGameUser(getApplicationContext(), gameUser);
        StatService.trackCustomEvent(getApplicationContext(), "login", "true");
        Properties prop = new Properties();
        prop.setProperty("user_phone_on_login", edit_phone_number.getText().toString());
        StatService.trackCustomKVEvent(LoginActivity.this, "OnLogin", prop);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getSystem() {
        String SYS = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                SYS = SYS_EMUI;//华为
            } else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
                SYS = SYS_FLYME;//魅族
            }
        } catch (IOException e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }


}
