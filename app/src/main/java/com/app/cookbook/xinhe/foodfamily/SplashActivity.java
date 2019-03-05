package com.app.cookbook.xinhe.foodfamily;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.FoodFamilyActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.SplashDate;
import com.app.cookbook.xinhe.foodfamily.main.guides.GuideActivity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.tencent.stat.StatService;

import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import rx.Subscription;

public class SplashActivity extends AppCompatActivity {
    /**
     * Rxjava
     */
    protected Subscription subscription;
    boolean isFirstIn = false;
    SharedPreferences preferences;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.test_round)
    RadioButton test_round;
    @BindView(R.id.official_round)
    RadioButton official_round;
    @BindView(R.id.yunwei_round)
    RadioButton yunwei_round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        //初始化黄牛刀
        ButterKnife.bind(this);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == test_round.getId()) {//选择测试环境
                    Log.e("选择环境", "测试环境");
                    Network.main_url = "https://app1.shiyujia.com/";
                    Network.bucketPath = "appDebug";
                    Network.ShareImage = "https://s3-011-shinho-syj-uat-bjs.s3.cn-north-1.amazonaws.com.cn/syjapp/2018_07/app.png";
                    Network.ShareUrl = Network.main_url + "answerPhone2/index.html#/";
                    Network.VideoUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/videos?id=";
                    Network.ImageTextUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/images?id=";
                    init_data();//初始化数据

                } else if (checkedId == official_round.getId()) {//选择正式环境
                    Log.e("选择环境", "正式环境");
                    Network.main_url = "https://app.shiyujia.com/";
                    Network.bucketPath = "appProduction";
                    Network.ShareImage = "https://s3-014-shinho-syj-prd-bjs.s3.cn-north-1.amazonaws.com.cn/syjapp/2018_07/app.png";
                    Network.ShareUrl = Network.main_url + "answerPhone/index.html#/";
                    Network.VideoUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/videos?id=";
                    Network.ImageTextUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/images?id=";
                    init_data();//初始化数据

                } else if (checkedId == yunwei_round.getId()) {
                    Log.e("选择环境", "运维环境");
                    Network.main_url = "https://app2.shiyujia.com/";
                    Network.bucketPath = "appDebug";
                    Network.ShareImage = "https://s3-014-shinho-syj-prd-bjs.s3.cn-north-1.amazonaws.com.cn/syjapp/2018_07/app.png";
                    Network.ShareUrl = Network.main_url + "answerPhone/index.html#/";
                    Network.VideoUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/videos?id=";
                    Network.ImageTextUrl = Network.main_url + "answerPhone2/index.html?from=singlemessage#/images?id=";
                    init_data();//初始化数据
                }
            }
        });
    }

    private void init_data() {
        for (int i = 0; i < MyApplication.list.size(); i++) {
            JPushInterface.clearNotificationById(this, MyApplication.list.get(i));
        }
        String brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        SharedPreferencesHelper.put(getApplicationContext(), "UserBrand", brand);

        // 埋点进入首页事件,统计用户进入首页的次数
        Properties prop = new Properties();
        prop.setProperty("name", "init_splash");
        StatService.trackCustomKVEvent(getApplicationContext(), "HomePage", prop);

        //判断是不是进引导页
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        if (isfer) {
            //第一次进入跳转
            Log.e("123", "       第一次进入        " + isfer);
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            //请求网络获取启动页
            if ("1".equals(SharedPreferencesHelper.get(getApplicationContext(), "isHomeMsg", ""))) {
                init_splash_net();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //超时处理
                        Intent intent = new Intent(SplashActivity.this, FoodFamilyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        }

        //判断是不是第一次进启动页，设置浮层引导
        preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            Log.e("第一次进来", "第一次进来");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first", "true");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first_play", "true");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first_enter_tuwen", "true");
        } else {
            Log.e("第二次进来", "第二次进来");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first", "false");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first_play", "false");
            SharedPreferencesHelper.put(getApplicationContext(), "is_first_enter_tuwen", "false");
        }
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
    }

    private void date(String error) {
        if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
            if (error.equals("断网")) {
                if (null == mHandler) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.removeCallbacks(mRegularAction);
                mHandler.post(mRegularAction);
            } else {
                Intent intent = new Intent(SplashActivity.this, FoodFamilyActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (error.equals("断网")) {
                if (null == mHandler) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.removeCallbacks(mRegularAction);
                mHandler.post(mRegularAction);
            } else {
                Intent intent = new Intent(SplashActivity.this, FoodFamilyActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final long DELAY = 3000;


    @Override
    protected void onStop() {
        super.onStop();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }
        //清空第一次进来的状态
        SharedPreferences preferences = getSharedPreferences(
                "first_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();

    }


    private final Runnable mRegularAction = new Runnable() {

        @Override
        public void run() {
            //每隔3秒请求一次

            panduan_net();

            mHandler.postDelayed(this, DELAY);
        }

    };

    private void panduan_net() {
        //判断是不是用户断网了
        ConnectivityManager connMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        if (isMobileConn || isWifiConn) {
            Intent intent = new Intent(SplashActivity.this, FoodFamilyActivity.class);
            startActivity(intent);
            finish();
        } else {


        }
    }

    private void init_splash_net() {
        Log.e("获取启动页首页", "获取启动页首页");
        subscription = Network.getInstance("获取启动页首页", this)
                .get_qidongye(new ProgressSubscriberNew<>(SplashDate.class, new GsonSubscriberOnNextListener<SplashDate>() {
                    @Override
                    public void on_post_entity(SplashDate splashDate) {
                        Log.e("123", "获取获取启动页成功：");
                        String image_path = "";
                        for (int i = 0; i < splashDate.getPath().size(); i++) {
                            if (splashDate.getPath().get(i).getController().equals("2")) {
                                if (splashDate.getPath().get(i).getSize_id().equals("6")) {
                                    image_path = splashDate.getPath().get(i).getPath();
                                    //设置启动页图片
//                                    Glide.with(getApplicationContext())
//                                            .load(image_path)
//                                            //.centerCrop()
//                                            //.placeholder(R.drawable.morenbgtu)
//                                            .into(splash_image);
                                }
                            }
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    date("");
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                                    startActivity(intent);
//                                    startActivity(FoodFamilyActivity.class);
                                    finish();

                                }
                            }
                        }).start();
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {
                    }

                    @Override
                    public void onError(final String error) {
                        Log.e("123", "获取获取启动页报错：" + error);
                        //设置默认启动页
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //验证是不是登录状态
                                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                                    if (error.equals("断网")) {
                                        if (null == mHandler) {
                                            mHandler = new Handler(Looper.getMainLooper());
                                        }
                                        mHandler.removeCallbacks(mRegularAction);
                                        mHandler.post(mRegularAction);
                                    } else {
                                        date(error);
                                    }
                                } else {
                                    if (error.equals("断网")) {
                                        if (null == mHandler) {
                                            mHandler = new Handler(Looper.getMainLooper());
                                        }
                                        mHandler.removeCallbacks(mRegularAction);
                                        mHandler.post(mRegularAction);
                                    } else {
                                        date(error);
                                    }
                                }
                            }
                        }).start();


                    }
                }, this, false));
    }


}
