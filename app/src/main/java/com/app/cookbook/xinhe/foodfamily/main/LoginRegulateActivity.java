package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginRegulateActivity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.wechat_bind_type)
    TextView wechat_bind_type;
    @BindView(R.id.wechat_switch_btn)
    Switch wechat_switch_btn;
    @BindView(R.id.weibo_bind_type)
    TextView weibo_bind_type;
    @BindView(R.id.weibo_switch_btn)
    Switch weibo_switch_btn;
    @BindView(R.id.bind_hint)
    TextView bind_hint;

    //第三方昵称
    private String login_name;
    //第三方头像
    private String login_avatar;
    //第三方性别
    private String login_sex;
    //第三方unionid
    private String login_unionid;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 400:
                    isWechatClick = true;
                    weChatType = "1";
                    wechat_switch_btn.setChecked(true);
                    wechat_bind_type.setText(Contacts.weixin_name);
                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                    break;
                case 401:
                    isWeiboClick = true;
                    weiboType = "1";
                    weibo_switch_btn.setChecked(true);
                    weibo_bind_type.setText(Contacts.weibo_name);
                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                    break;
                case 402:
                    isWechatClick = false;
                    wechat_switch_btn.setChecked(false);
                    wechat_bind_type.setText("未绑定");
                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                    break;
                case 403:
                    isWeiboClick = false;
                    weibo_switch_btn.setChecked(false);
                    weibo_bind_type.setText("未绑定");
                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                    break;
            }
        }
    };


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login_regulate);
        ButterKnife.bind(this);
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    private boolean isWechatClick = false;
    private String weChatType = "0";
    private boolean isWeiboClick = false;
    private String weiboType = "0";

    @Override
    public void doBusiness(Context mContext) {
        if ("1".equals(Contacts.weixin_login)) {
            wechat_switch_btn.setChecked(false);
            wechat_bind_type.setText("未绑定");
            wechat_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
        } else if ("2".equals(Contacts.weixin_login)) {
            wechat_switch_btn.setChecked(true);
            wechat_bind_type.setText(Contacts.weixin_name);
            wechat_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
        }
        if ("1".equals(Contacts.weibo_login)) {
            weibo_switch_btn.setChecked(false);
            weibo_bind_type.setText("未绑定");
            weibo_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
        } else if ("2".equals(Contacts.weibo_login)) {
            weibo_switch_btn.setChecked(true);
            weibo_bind_type.setText(Contacts.weibo_name);
            weibo_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wechat_switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isWechatClick == false) {
                    if (isChecked) {
                        wechat_switch_btn.setChecked(false);
                        bind_hint.setVisibility(View.GONE);
                        loginWeChat();
                    } else {
                        PopWindowHelper.public_tishi_pop(LoginRegulateActivity.this, "确定解绑微信", "", "否", "是", new DialogCallBack() {
                            @Override
                            public void save() {
                                relieve_wechat_request();
                            }

                            @Override
                            public void cancel() {
                                weChatType = "1";
                                isWechatClick = true;
                                if ("1".equals(Contacts.weixin_login)) {
                                    wechat_switch_btn.setChecked(false);
                                    wechat_bind_type.setText("未绑定");
                                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                                } else if ("2".equals(Contacts.weixin_login)) {
                                    wechat_switch_btn.setChecked(true);
                                    wechat_bind_type.setText(Contacts.weixin_name);
                                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                                }
                            }
                        });
                    }
                } else {
                    isWechatClick = false;
                    if (!"1".equals(weChatType)) {
                        wechat_switch_btn.setChecked(false);
                    }
                }
            }
        });
        weibo_switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isWeiboClick == false) {
                    if (isChecked) {
                        weibo_switch_btn.setChecked(false);
                        bind_hint.setVisibility(View.GONE);
                        loginweibo();
                    } else {
                        PopWindowHelper.public_tishi_pop(LoginRegulateActivity.this, "确定解绑微博", "", "否", "是", new DialogCallBack() {
                            @Override
                            public void save() {
                                relieve_weibo_request();
                            }

                            @Override
                            public void cancel() {
                                isWeiboClick = true;
                                weiboType = "1";
                                if ("1".equals(Contacts.weibo_login)) {
                                    weibo_switch_btn.setChecked(false);
                                    weibo_bind_type.setText("未绑定");
                                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                                } else if ("2".equals(Contacts.weibo_login)) {
                                    weibo_switch_btn.setChecked(true);
                                    weibo_bind_type.setText(Contacts.weibo_name);
                                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                                }
                            }
                        });
                    }
                } else {
                    isWeiboClick = false;
                    if (!"1".equals(weiboType)) {
                        weibo_switch_btn.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) {

    }

    //微信登录
    private void loginWeChat() {
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
                Log.e("123", "       微信登录成功        " + platform.getDb().exportData());
                login_name = platform.getDb().getUserName();
                Contacts.weixin_name = login_name;
                login_avatar = platform.getDb().getUserIcon();
                login_sex = platform.getDb().getUserGender();
                login_unionid = platform.getDb().get("unionid").toString();
                init_wechat_request(platform.getDb().getUserId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("123", "   微信报错        " + throwable.getMessage());
                isWechatClick = true;
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("123", "onCancel ---->  微信登录取消");
                isWechatClick = true;
                if ("1".equals(Contacts.weixin_login)) {
                    wechat_switch_btn.setChecked(false);
                    wechat_bind_type.setText("未绑定");
                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                } else if ("2".equals(Contacts.weixin_login)) {
                    wechat_switch_btn.setChecked(true);
                    wechat_bind_type.setText(Contacts.weixin_name);
                    wechat_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                }
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    //微博登录
    private void loginweibo() {
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
                Log.e("123", "      微博登录成功         " + platform.getDb().exportData());
                login_name = platform.getDb().getUserName();
                Contacts.weibo_name = login_name;
                login_avatar = platform.getDb().getUserIcon();
                login_sex = platform.getDb().getUserGender();
                login_unionid = platform.getDb().get("unionid").toString();
                init_weibo_request(platform.getDb().getUserId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("123", "   微博报错        " + throwable.getMessage());
                isWeiboClick = true;
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("123", "onCancel ---->  微博登录取消");
                isWeiboClick = true;
                if ("1".equals(Contacts.weibo_login)) {
                    weibo_switch_btn.setChecked(false);
                    weibo_bind_type.setText("未绑定");
                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_777777));
                } else if ("2".equals(Contacts.weibo_login)) {
                    weibo_switch_btn.setChecked(true);
                    weibo_bind_type.setText(Contacts.weibo_name);
                    weibo_bind_type.setTextColor(getResources().getColor(R.color.color_292c31));
                }
            }
        });
        Weibo.SSOSetting(false);
        Weibo.showUser(null);
    }

    //绑定微信
    private void init_wechat_request(String opentid) {
        Map<String, String> params = new HashMap<>();
        params.put("weixin_login", opentid);
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
        Log.e("123", "         绑定微信 params      " + params);
        subscription = Network.getInstance("绑定微信", this)
                .bind_wechat_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List token) {
                                Log.e("123", "      绑定微信         ");
                                Contacts.weixin_login = "2";
                                Message msg = new Message();
                                msg.what = 400;
                                handler.sendMessage(msg);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "      error       " + error);
                                if ("绑定失败".equals(error)) {
                                    bind_hint.setVisibility(View.VISIBLE);
                                    bind_hint.setText("该微信号已绑定，请更换绑定或先解绑");
                                }
                            }
                        }, this, false));
    }


    //绑定微博
    private void init_weibo_request(String opentid) {
        Map<String, String> params = new HashMap<>();
        params.put("weibo_login", opentid);
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
        Log.e("123", "         绑定微博 params      " + params);
        subscription = Network.getInstance("绑定微博", this)
                .bind_weibo_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List token) {
                                Log.e("123", "      绑定微博         ");
                                Contacts.weibo_login = "2";
                                Message msg = new Message();
                                msg.what = 401;
                                handler.sendMessage(msg);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("绑定失败".equals(error)) {
                                    bind_hint.setVisibility(View.VISIBLE);
                                    bind_hint.setText("该微博号已绑定，请更换绑定或先解绑");
                                }
                            }
                        }, this, false));
    }

    //解除微信绑定
    private void relieve_wechat_request() {
        subscription = Network.getInstance("解除微信绑定", this)
                .untie_wechat_request(new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                    @Override
                    public void on_post_entity(List token) {
                        Log.e("123", "      解除微信绑定         ");
                        Contacts.weixin_login = "1";
                        Message msg = new Message();
                        msg.what = 402;
                        handler.sendMessage(msg);
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "      解除微信绑定   error      " + error);
                    }
                }, this, false));
    }

    //解除微博绑定
    private void relieve_weibo_request() {
        subscription = Network.getInstance("解除微博绑定", this)
                .untie_weibo_request(new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                    @Override
                    public void on_post_entity(List token) {
                        Log.e("123", "      解除微博绑定         ");
                        Contacts.weibo_login = "1";
                        Message msg = new Message();
                        msg.what = 403;
                        handler.sendMessage(msg);
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "      解除微博绑定 error        " + error);
                    }
                }, this, false));
    }


}
