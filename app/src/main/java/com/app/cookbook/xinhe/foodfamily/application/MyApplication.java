package com.app.cookbook.xinhe.foodfamily.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.ImageView;

import com.app.cookbook.xinhe.foodfamily.callback.ErrorCallback;
import com.app.cookbook.xinhe.foodfamily.main.tengxun.MTACrashModule;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.mob.MobApplication;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ql0571.loadmanager.core.LoadManager;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.hybrid.StatHybridHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by shiyujia02 on 2017/8/3.
 */

public class MyApplication extends MobApplication {
    private static final String TAG = MyApplication.class.getSimpleName();
    public static List<Integer> list = new ArrayList<>();

    private static MyApplication _application;//本类的实例
    public static OkHttpClient okHttpClient;

    //qcl用来在主线程中刷新ui
    private static Handler mHandler;
    public static String registrationId;
    public static boolean is_first = true;

    public static String[] lvjings=null;

    //保存List集合
    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    private static Map<String, Activity> destoryMap = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();
        _application = this;
        //阿里视频录制
        //initLeakCanary();//集成报错监听
        com.aliyun.vod.common.httpfinal.QupaiHttpFinal.getInstance().initOkHttpFinal();

        //初始化handler
        mHandler = new Handler();
        //分享
        MobSDK.init(getApplicationContext(), "24d50841ade64", "580fbb90e238c8398314e8a59d1569f7");

        //关于极光推送
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        registrationId = JPushInterface.getRegistrationID(this);
        Log.e("极光返回注册ID", "" + registrationId);
        SharedPreferencesHelper.put(this, "jiguang_id", registrationId);


        //注册okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //网络监听（重连）
        LoadManager.beginBuilder()
                .addCallback(new ErrorCallback())
                .commit();

        //接入腾讯埋点
        StatisticsDataAPI.instance(this);
        StatService.setContext(this);
        StatConfig.setTLinkStatus(true);
        StatHybridHandler.init(this);
        initMTAConfig(true);
        StatService.registerActivityLifecycleCallbacks(this);
        MTACrashModule.initMtaCrashModule(this);
        Log.e("接入腾讯埋点Mid", StatConfig.getMid(getApplicationContext()));
        //视频播放初始化
        initUniversalImageLoader();

        StatConfig.setTLinkStatus(true);



    }
    private void initLeakCanary() {
        //排除一些Android Sdk引起的泄漏
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
                .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
                .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
                .instanceField("android.app.ActivityThread$ActivityClientRecord", "activity")
                .instanceField("android.media.MediaScannerConnection", "mContext")
                .build();

        LeakCanary.refWatcher(this)
                .listenerServiceClass(DisplayLeakService.class)
                .excludedRefs(excludedRefs)
                .buildAndInstall();
    }
    //视频播放初始化
    private void initUniversalImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void initMTAConfig(boolean isDebugMode) {
        if (isDebugMode) {
            StatConfig.setDebugEnable(true);

        } else {
            StatConfig.setDebugEnable(false);
            StatConfig.setAutoExceptionCaught(true);
            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
            StatConfig.setSendPeriodMinutes(10);
        }

        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).addCrashCallback(new StatCrashCallback() {

            @Override
            public void onJniNativeCrash(String tombstoneMsg) {
                Log.d("Test", "Native crash happened, tombstone message:" + tombstoneMsg);
            }

            @Override
            public void onJavaCrash(Thread thread, Throwable throwable) {
                Log.d("Test", "Java crash happened, thread: " + thread + ",Throwable:" + throwable.toString());
            }
        });

    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }

    @Override
    public void onTerminate() {


        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }

    /**
     * 在主线程中刷新UI的方法
     **/
    public static void runOnUIThread(Runnable r) {
        MyApplication.getMainHandler().post(r);
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static MyApplication getApplication() {
        return _application;
    }
}
