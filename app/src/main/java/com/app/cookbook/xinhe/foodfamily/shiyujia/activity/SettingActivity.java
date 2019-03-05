package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.InRegardToAppActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginRegulateActivity;
import com.app.cookbook.xinhe.foodfamily.main.UpdatePasswordActivity;
import com.app.cookbook.xinhe.foodfamily.main.UpdatePhoneActivity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.DataCleanManager;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.ui.CustomPopupWindow;
import com.tencent.stat.StatService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.yonghuxieyi_layout)
    RelativeLayout yonghuxieyi_layout;
    @BindView(R.id.qingchu_huancun_layout)
    RelativeLayout qingchu_huancun_layout;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.tuichulogin_layout)
    RelativeLayout tuichulogin_layout;
    @BindView(R.id.update_password_layout)
    RelativeLayout update_password_layout;
    @BindView(R.id.shoujihao_layout)
    RelativeLayout shoujihao_layout;
    @BindView(R.id.save_size_tv)
    TextView save_size_tv;
    @BindView(R.id.login_regulate)
    RelativeLayout login_regulate;
    @BindView(R.id.wifi_switch_btn)
    Switch wifi_switch_btn;

    private boolean isWifi;
    private String is_wifi;


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_setting2);
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

    @Override
    public void doBusiness(Context mContext) {
        is_wifi = getIntent().getStringExtra("is_wifi");
        if ("1".equals(is_wifi)) {
            wifi_switch_btn.setChecked(false);
        } else if ("2".equals(is_wifi)) {
            wifi_switch_btn.setChecked(true);
        }

        //用户协议
        yonghuxieyi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(ServiceActivity.class);
                Properties prop = new Properties();
                prop.setProperty("name", "yonghuxieyi_layout");
                StatService.trackCustomKVEvent(SettingActivity.this, "Install_Install_about", prop);
                startActivity(InRegardToAppActivity.class);
            }
        });

        //清除缓存
        qingchu_huancun_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "qingchu_huancun_layout");
                StatService.trackCustomKVEvent(SettingActivity.this, "Install_Install_cache", prop);

                CustomPopupWindow.public_tishi_pop(SettingActivity.this,
                        "食与家提示",
                        "确认清除缓存吗？",
                        "取消",
                        "确定",
                        new DialogCallBack() {
                            @Override
                            public void save() {
                                Log.e("开始发来看看代理商", Environment.getExternalStorageDirectory() + "/images/");
                                //清除压缩图片缓存
                                //Biscuit.clearCache(Environment.getExternalStorageDirectory() + "/images/");
                                //清除缓存
                                DataCleanManager.deleteFolderFile(Environment.getExternalStorageDirectory() + "/images/", true);

                                if (save_size_tv.getText().toString().equals("0k")) {
                                    Toast.makeText(getApplicationContext(), "当前没有缓存信息", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "缓存清理完成", Toast.LENGTH_SHORT).show();
                                    get_huancun_size();
                                }
                            }

                            @Override
                            public void cancel() {

                            }
                        });
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tuichulogin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "tuichulogin_layout");
                StatService.trackCustomKVEvent(SettingActivity.this, "Install_Install_exit", prop);
                CustomPopupWindow.public_tishi_pop(SettingActivity.this,
                        "提示",
                        "确认退出登录？",
                        "取消",
                        "确定",
                        new DialogCallBack() {
                            @Override
                            public void save() {
                                //清除token退出登录
                                ClearSource();
                            }

                            @Override
                            public void cancel() {

                            }
                        });
            }
        });
        //修改密码
        update_password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "update_password_layout");
                StatService.trackCustomKVEvent(SettingActivity.this, "Install_Install_change_password", prop);

                startActivity(UpdatePasswordActivity.class);
            }
        });
        //更改手机号
        shoujihao_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "shoujihao_layout");
                StatService.trackCustomKVEvent(SettingActivity.this, "Install_Install_change_number", prop);

                startActivity(UpdatePhoneActivity.class);
            }
        });
        login_regulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginRegulateActivity.class);
                startActivity(intent);
            }
        });

        wifi_switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isWifi == true) {
                    isWifi = false;
                    wifi_switch_btn.setChecked(false);
                    getWIFIStart("1");
                    SharedPreferencesHelper.put(SettingActivity.this, "isWiFI", "1");
                } else {
                    isWifi = true;
                    wifi_switch_btn.setChecked(true);
                    getWIFIStart("2");
                    SharedPreferencesHelper.put(SettingActivity.this, "isWiFI", "2");
                }
            }
        });
    }

    //点赞
    private void getWIFIStart(String isWifi) {
        Map<String, String> params = new HashMap<>();
        params.put("is_wifi", isWifi);
        Log.e("修改wifi状态：", params.toString());
        subscription = Network.getInstance("修改wifi状态", this)
                .getUpdateWifi(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "       修改wifi状态        " + result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(SettingActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    private void get_huancun_size() {
        File file = new File(Environment.getExternalStorageDirectory() + "/images/");
        save_size_tv.setText(DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(file)));
    }

    @Override
    protected void onResume() {
        //获取缓存文件夹大小
        get_huancun_size();
        super.onResume();
    }

    private void ClearSource() {
        SharedPreferencesHelper.remove(SettingActivity.this, "login_token");//清除token
        startActivity(LoginActivity.class);
        finish();

    }

    @Override
    public void widgetClick(View v) {

    }
}
