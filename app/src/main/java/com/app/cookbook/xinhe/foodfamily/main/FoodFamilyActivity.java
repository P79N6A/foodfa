package com.app.cookbook.xinhe.foodfamily.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.demo.snap.SnapRecorderSetting;
import com.aliyun.demo.util.Utils;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.callback.MovieBtnBack;
import com.app.cookbook.xinhe.foodfamily.jiguang.MainActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.BackEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.Location;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.UserEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.UserRanking;
import com.app.cookbook.xinhe.foodfamily.main.entity.VersionMsgName;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.services.HomeKeyInterceptor;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CamareActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.NoAccessActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PlayerActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.DiscoverFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.HomeFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.QuestionsAndAnswersFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.update.InvitationDialog;
import com.app.cookbook.xinhe.foodfamily.update.LogoutDialog;
import com.app.cookbook.xinhe.foodfamily.update.PermisionUtils;
import com.app.cookbook.xinhe.foodfamily.update.UpdateManager;
import com.app.cookbook.xinhe.foodfamily.update.Version;
import com.app.cookbook.xinhe.foodfamily.util.OnHomeKeyPressListener;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.app.cookbook.xinhe.foodfamily.util.eventbus.NetWorkEvent;
import com.app.cookbook.xinhe.foodfamily.util.netlisten.NetWorkChangReceiver;
import com.app.cookbook.xinhe.foodfamily.util.popwindow.MoreWindow;
import com.app.cookbook.xinhe.foodfamily.util.progress.MixTextProgressBar;
import com.app.cookbook.xinhe.foodfamily.util.ui.NoScrollViewPager;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import hei.permission.EasyPermissions;
import q.rorbin.badgeview.QBadgeView;
import rx.Subscription;

public class FoodFamilyActivity extends BaseActivity implements OnHomeKeyPressListener, LogoutDialog.OnLogoutClickListener, InvitationDialog.OnLogoutClickListener {
    @BindView(R.id.btn_home)
    LinearLayout btn_home;
    @BindView(R.id.btn_shipu)
    LinearLayout btn_shipu;
    @BindView(R.id.btn_tiwen)
    LinearLayout btn_tiwen;
    @BindView(R.id.btn_movie)
    LinearLayout btn_movie;
    @BindView(R.id.btn_mine)
    LinearLayout btn_mine;
    @BindView(R.id.home_img)
    ImageView home_img;
    @BindView(R.id.shipu_img)
    ImageView shipu_img;
    @BindView(R.id.mine_img)
    ImageView mine_img;
    @BindView(R.id.home_tv)
    TextView home_tv;
    @BindView(R.id.shipu_tv)
    TextView shipu_tv;
    @BindView(R.id.movie_tv)
    TextView movie_tv;
    @BindView(R.id.mine_tv)
    TextView mine_tv;
    @BindView(R.id.movie_img)
    ImageView movie_img;
    @BindView(R.id.viewpager_content)
    NoScrollViewPager viewpager_content;
    @BindView(R.id.guides_bg)
    LinearLayout guides_bg;
    @BindView(R.id.id_container)
    RelativeLayout idContainer;
    @BindView(R.id.finish_btn)
    ImageView finish_btn;
    @BindView(R.id.refresh_again_btn)
    LinearLayout refresh_again_btn;
    @BindView(R.id.rel_pregress_error)
    RelativeLayout rel_pregress_error;
    String[] effectDirs;

    private List<Fragment> tabFragments = new ArrayList<>();

    private String xiaoxi_code;
    private String index_page = "";
    public MyHandler handler;

    private static boolean update_messege_ui = false;
    private HomeKeyInterceptor mHomeKeyInterceptor;
    private String is_message_enter = "";
    /**
     * 版本更新
     */
    public Version version = new Version();
    private LogoutDialog dialog;
    //是否强制更新
    private String is_force;

    private QBadgeView qBadgeView;

    public static FoodFamilyActivity activity;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    MoreWindow mMoreWindow;
    @BindView(R.id.progress)
    MixTextProgressBar progress;
    VODSVideoUploadClient vodsVideoUploadClient;
    private String videoId = null;
    private String expriedTime = "2020-01-05T12:31:08Z";//STS的过期时间
    private String requestID = null;//传空或传递访问STS返回的requestID
    //需要传过来的值
    public static String luzhi_path = "";
    public static String local_path = "";
    public static String accessKeyId = "";
    public static String accessKeySecret = "";
    public static String securityToken = "";
    public static String content_tv = "";
    public static String classification_id = "";
    public static String upload_iamge_path = "";
    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    String[] lvjings = new String[10];

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        if (null != parms) {
            if (null != parms.getString("xiaoxi_code")) {
                xiaoxi_code = parms.getString("xiaoxi_code");
            } else {
                xiaoxi_code = "-1";
            }
            if (null != parms.getString("index_page")) {
                index_page = parms.getString("index_page");
            }
            if (null != parms.getString("is_message_enter")) {
                is_message_enter = parms.getString("is_message_enter");
            }
        } else {
            xiaoxi_code = "-1";
        }
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) {
                int i = msg.what;
                if (i == 2) {
                    update_messege_ui = true;
                } else if (i == 3) {//代表消息数量为0
                    update_messege_ui = false;
                } else {
                    update_messege_ui = false;
                }
                if (viewpager_content.getCurrentItem() == 2) {
                    //当跳转到第二个页面的时候
                    if (update_messege_ui) {
                        init_jiaobiao();
                    } else {
                        qBadgeView.hide(true);
                        movie_img.setImageResource(R.drawable.icon_issue_on);
                    }
                } else {
                    if (update_messege_ui) {
                        init_jiaobiao();
                    } else {
                        qBadgeView.hide(true);
                        movie_img.setImageResource(R.drawable.icon_issue_off);
                    }
                }
            }
        }
    }


    private void init_jiaobiao() {
        if (SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString().length() > 0) {
            if (xiaoxi_numner > 0) {
                Contacts.mesgNumber = String.valueOf(xiaoxi_numner);
            }
        }
    }


    public MyHandler getHandler() {
        if (null != handler) {
            return handler;
        } else {
            MyHandler myHandler = new MyHandler();
            return myHandler;
        }
    }

    @Override
    protected void onPause() {
        xiaoxi_code = "-1";
        super.onPause();
    }

    @Override
    public void initView() {
        steepStatusBar();
        setContentLayout(R.layout.activity_food_family);
        activity = this;
        //初始化埋点
        String appkey = "ANYAYD329D2I";
        try {
            StatService.startStatService(this, appkey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            e.printStackTrace();
        }
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHomeKeyInterceptor = HomeKeyInterceptor.getInstance(this);
        mHomeKeyInterceptor.setListener(this);

        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        //注册信鸽
//        getXGPushManager();
        getguidse();
        PermisionUtils.verifyStoragePermissions(FoodFamilyActivity.this);

        qBadgeView = new QBadgeView(getApplicationContext());

        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.button, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级


        //弹窗
        mMoreWindow = new MoreWindow(this);
        mMoreWindow.init(idContainer);

        //注册eventbus
        EventBus.getDefault().register(this);
        //进度条初始化
        progress.setMax(100);
        progress.setProgress(0);
        vodsVideoUploadClient = new VODSVideoUploadClientImpl(this.getApplicationContext());
        vodsVideoUploadClient.init();
        //上传视频重试
        refresh_again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connect_net_work) {
                    Log.e("首页点击重新上传", "首页点击重新上传");
                    vodsVideoUploadClient.init();
                    //重新上传
                    upload_movie();
                } else {
                    Toast.makeText(getApplicationContext(), "请先恢复网络连接再上传~", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //关闭progress按钮
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.GONE);
                rel_pregress_error.setVisibility(View.GONE);
                vodsVideoUploadClient.cancel();
            }
        });
        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;

        //解压缩滤镜
        copyAssets();

    }

    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Utils.copyAll(FoodFamilyActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                initAssetPath();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initAssetPath() {
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator + Utils.QU_NAME + File.separator;
        File filter = new File(new File(path), "filter");

        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        effectDirs = new String[list.length + 1];
        effectDirs[0] = null;
        for (int i = 0; i < list.length; i++) {
            effectDirs[i + 1] = filter.getPath() + "/" + list[i];

        }
        //设置需要的滤镜
        lvjings[9] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/思念";
        lvjings[8] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/鲜果";
        lvjings[7] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/经典";
        lvjings[6] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/阳光";
        lvjings[5] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/黑白";
        lvjings[4] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/闪耀";
        lvjings[3] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/光圈";
        lvjings[2] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/柔柔";
        lvjings[1] = "/storage/emulated/0/Android/data/com.app.cookbook.xinhe.foodfamily/cache/AliyunDemo/filter/优雅";
        lvjings[0] = null;
        if (lvjings.length == 10) {
            MyApplication.lvjings = lvjings;
        }

    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    long mprogress;

    private void upload_movie() {
        /**阿里上传视频*/
        //参数请确保存在，如不存在SDK内部将会直接将错误throw Exception
        // 文件路径保证存在之外因为Android 6.0之后需要动态获取权限，请开发者自行实现获取"文件读写权限".
        VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
                .setMaxRetryCount(20)
                .setConnectionTimeout(10 * 1000)
                .setSocketTimeout(10 * 1000)
                .build();
        SvideoInfo svideoInfo = new SvideoInfo();
        svideoInfo.setTitle(new File(luzhi_path).getName());
        svideoInfo.setDesc("");
        svideoInfo.setCateId(1);
        VodSessionCreateInfo vodSessionCreateInfo = new VodSessionCreateInfo.Builder()
                .setImagePath(local_path)
                .setVideoPath(luzhi_path)
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setSecurityToken(securityToken)
                .setRequestID(requestID)
                .setExpriedTime(expriedTime)
                .setIsTranscode(true)
                .setSvideoInfo(svideoInfo)
                .setPartSize(500 * 1024)
                .setVodHttpClientConfig(vodHttpClientConfig)
                .build();
        vodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
            @Override
            public void onUploadSucceed(String mvideoId, String imageUrl) {
                Log.e("PlayerActivity", "onUploadSucceed" + "videoId:" + videoId + "imageUrl" + imageUrl);
                videoId = mvideoId;
                //请求接口传递数据
                fabu_content();

            }

            @Override
            public void onUploadFailed(String code, String message) {
                Log.e("PlayerActivity", "onUploadFailed" + "code" + code + "message" + message);
                EventBus.getDefault().post(new com.app.cookbook.xinhe.foodfamily.util.eventbus.MessageEvent(-100));
                //设置上传失败逻辑
                Message message1 = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("net_work", "no_work");
                message1.setData(bundle);
                net_progress_handel.sendMessage(message1);
                EventBus.getDefault().post(new com.app.cookbook.xinhe.foodfamily.util.eventbus.MessageEvent(-100));
            }

            @Override
            public void onUploadProgress(long uploadedSize, long totalSize) {
                Log.e("上传进度onUploadProgress", "" + uploadedSize * 100 / totalSize);
                mprogress = uploadedSize * 100 / totalSize;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("progress", (int) mprogress);
                message.setData(bundle);
                progress_handler.sendMessage(message);
                //全局发送进度
                EventBus.getDefault().post(new com.app.cookbook.xinhe.foodfamily.util.eventbus.MessageEvent((int) mprogress));


            }

            @Override
            public void onSTSTokenExpried() {
                Log.e("PlayerActivity", "onSTSTokenExpried");
                //STS token过期之后刷新STStoken，如正在上传将会断点续传
//                accessKeyId = "STS.GzVkp3WpkLr97TdEUsght1Miz";
//                accessKeySecret = "7dvXT6PHN2uiGmyVKj5gxYHjXg9AnPnjHafmeSxusXHt";
//                securityToken = "CAIShwJ1q6Ft5B2yfSjIprnjIMqHuq9K+7DSNXLVoVUma+dY3ojCmDz2IHtKenZsCegav/Q3nW1V7vsdlrBtTJNJSEnDKNF36pk" +
//                        "S6g66eIvGvYmz5LkJ0CFkgKx3T0yV5tTbRsmkZvW/E67fSjKpvyt3xqSAAlfGdle5MJqPpId6Z9AMJGeRZiZHA9EkSWkPtsgWZzmrQpTLCBPxhXf" +
//                        "KB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392qesP1P5UyZ8YvC4nthLRMG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLGKrIIzfFclN/hiQ" +
//                        "vMZ9KWjj55mveDfmoHw0RFJMPGNr7Ie1VZgqhqAAZbSW08OCELjDZHWX4jRWvIJVEEPiXZ1eW4M1NmiKiDi7RSx4R7PuNLOW" +
//                        "S51tJbZJZxi99KpstJtVff1T5Ss9pP1PKPGmnvrP13heb0lhgE3nlJkreTlNGmqxcb1VIA9CGNeAh5IjIR7mBgKxX7vcOt+sBkcd9ibs4XI5sYvtuC3";
                expriedTime = "2020-01-05T09:17:25Z";
                vodsVideoUploadClient.refreshSTSToken(accessKeyId, accessKeySecret, securityToken, expriedTime);
            }

            @Override
            public void onUploadRetry(String code, String message) {
                //上传重试的提醒
                Log.e("PlayerActivity", "onUploadRetry" + "code" + code + "message" + message);
            }

            @Override
            public void onUploadRetryResume() {
                //上传重试成功的回调.告知用户重试成功
                Log.e("PlayerActivity", "onUploadRetryResume");
            }
        });

    }

    private void fabu_content() {
        Map<String, Object> params = new HashMap<>();
        params.put("video_id", videoId);
        params.put("face_path", upload_iamge_path);
        if ("".equals(classification_id)) {
        } else {
            params.put("class_ids", classification_id);
        }
        params.put("content", content_tv);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("发布视频参数", params.toString());
        Subscription subscription = Network.getInstance("发布视频", getApplicationContext())
                .fabu_movie(params,
                        new ProgressSubscriberNew<>(BackEntity.class, new GsonSubscriberOnNextListener<BackEntity>() {
                            @Override
                            public void on_post_entity(BackEntity backEntity) {
                                Log.e("123", "发布视频成功：" + backEntity.getId());

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
//                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "发布视频失败：" + error);
                            }
                        }, this, false));
    }

    private boolean connect_net_work;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(NetWorkEvent netWorkEvent) {
        if (netWorkEvent.getMessage().equals("work")) {
            connect_net_work = true;
            Log.e("首页网络连接已连接", "首页网络连接已连接");

        } else {
            connect_net_work = false;
            //设置进度条断开逻辑
            Log.e("首页网络连接已断开", "首页网络连接已断开");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("net_work", "no_work");
            message.setData(bundle);
            net_progress_handel.sendMessage(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(com.app.cookbook.xinhe.foodfamily.util.eventbus.MessageEvent message) {
        if (message.getMessage() == -100) {//代表上传失败
            //设置上传失败逻辑
            if (progress.getProgress() > 0) {
                //显示红色部分
                Message message1 = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("net_work", "no_work");
                message1.setData(bundle);
                net_progress_handel.sendMessage(message1);
            }

        } else {
            Message progress_message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("progress", message.getMessage());
            progress_message.setData(bundle);
            progress_handler.sendMessage(progress_message);
        }
        Log.e("精神科龙卷风", message.getMessage() + "");
    }

    Handler net_progress_handel = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (null != bundle.get("net_work") && bundle.get("net_work").equals("no_work")) {//网络连接已断开
                Toast.makeText(getApplicationContext(), "网络连接已断开~", Toast.LENGTH_SHORT).show();
                //设置上传失败逻辑
                if (progress.isShown()) {
                    //显示红色部分
                    rel_pregress_error.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
                return;
            }
        }
    };

    Handler progress_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int mprogress = (int) bundle.get("progress");
            if (mprogress > 0) {
                progress.setVisibility(View.VISIBLE);
                rel_pregress_error.setVisibility(View.GONE);
                progress.setProgress((int) mprogress);
                progress.setText("当前进度：" + mprogress + "%");
                Log.e("上传视频进度", "当前进度：" + mprogress + "%");
                if (mprogress == 100) {
                    progress.setProgress((int) mprogress);
                    progress.setText("当前进度：" + mprogress + "%");
                    progress.setText("上传完成!");
                    //延迟秒执行
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            /**
                             *要执行的操作
                             */
                            handler_close.sendEmptyMessage(0);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1500);
                }
            }
        }
    };

    Handler handler_close = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列onGetMessage
            mHandler = null;
        }
        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
        Log.e("进来了destroy", "进来了destroy");

        VideoPlayerManager.getInstance().onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        luzhi_path = "";
        local_path = "";
        accessKeyId = "";
        accessKeySecret = "";
        securityToken = "";
        content_tv = "";
        classification_id = "";
        upload_iamge_path = "";
    }


    private void steepStatusBar() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        if (isfer) {
            //第一次进入跳转
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                StatusBarUtil.setStatusBarColor(FoodFamilyActivity.this, R.color.color_70000000);
            } else {
                StatusBarUtil.transparencyBar(FoodFamilyActivity.this);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                StatusBarUtil.setStatusBarColor(FoodFamilyActivity.this, R.color.color_00000000);
            } else {
                StatusBarUtil.transparencyBar(FoodFamilyActivity.this);
            }
        }
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

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final long DELAY = 5000;

    @Override
    public void doBusiness(Context mContext) {
        //显示UI
        btn_home.setOnClickListener(this);
        btn_shipu.setOnClickListener(this);
        btn_movie.setOnClickListener(this);
        btn_mine.setOnClickListener(this);
        btn_tiwen.setOnClickListener(this);
        guides_bg.setOnClickListener(this);

        tabFragments.clear();
        tabFragments.add(HomeFragment.homeInstance(""));
        tabFragments.add(DiscoverFragment.newInstance(""));
        tabFragments.add(QuestionsAndAnswersFragment.newInstance(""));
        tabFragments.add(MyFragment.myInstance(""));
        ContentPagerAdapterMy contentAdapter = new ContentPagerAdapterMy(getSupportFragmentManager());
        viewpager_content.setAdapter(contentAdapter);
        viewpager_content.setCurrentItem(0);

        if (xiaoxi_code.equals("5")) {
            xianshi_three();
        } else {
            xianshi_one();
        }

        //从通知点击进来
        if (index_page.equals("3")) {
            viewpager_content.setCurrentItem(2);
            xianshi_three();
        } else if (index_page.equals("xiaoxi3")) {
            viewpager_content.setCurrentItem(2);
            xianshi_three();
        }

        viewpager_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    VideoPlayerManager.getInstance().onPause(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private String user_id_get;

    private void get_user_information() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", this)
                .get_user_information(params,
                        new ProgressSubscriberNew<>(UserEntity.class, new GsonSubscriberOnNextListener<UserEntity>() {
                            @Override
                            public void on_post_entity(UserEntity userEntity) {
                                Log.e("123", "获取用户信息成功：");
                                //设置页面的信息
                                user_id_get = userEntity.getUid() + "";
                                SharedPreferencesHelper.put(getApplicationContext(), "user_id", user_id_get + "");
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取用户信息报错：" + error);
                            }
                        }, this, false));
    }

    private final Runnable mRegularAction = new Runnable() {
        @Override
        public void run() {
            //每隔五秒请求一次
            initDatetwo(getApplicationContext());//请求网络
            mHandler.postDelayed(this, DELAY);
        }
    };

    private int xiaoxi_numner = 0;

    private void initDatetwo(Context context) {
        subscription = Network.getInstance("获取到消息数量", context)
                .get_location_shouye(
                        new ProgressSubscriberNew<>(Location.class, new GsonSubscriberOnNextListener<Location>() {
                            @Override
                            public void on_post_entity(Location location) {
                                int a = location.getAnswer_number();
                                int b = location.getFollow_count();
                                int c = location.getSys_count();
                                int d = location.getThumbs_count();
                                int f = location.getComment_count();
                                int e = a + b + c + d + f;
                                xiaoxi_numner = e;
                                if (e > 0) {
                                    //通知FoodFamlyActivity更新UI
                                    new Thread(new DownThread()).start();//启动线程
                                } else {
                                    new Thread(new DownThread2()).start();//启动线程
                                }

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, context, false));
    }

    /**
     * 版本更新
     */
    private void initUpData() {
        Map<String, String> params = new HashMap<>();
        params.put("version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        if (null != SharedPreferencesHelper.get(getApplicationContext(), "jiguang_id", "")) {
            String registrationId = SharedPreferencesHelper.get(FoodFamilyActivity.this, "jiguang_id", "").toString();
            params.put("registration_id", registrationId);
//                Log.e("123", "          极光签名        " + registrationId);
        }
        subscription = Network.getInstance("启动APP记录", this)
                .updata_request(params,
                        new ProgressSubscriberNew<>(VersionMsgName.class, new GsonSubscriberOnNextListener<VersionMsgName>() {
                            @Override
                            public void on_post_entity(VersionMsgName result) {
                                is_force = result.getIs_force();
                                String versionNumber = result.getVersion();
                                String content = result.getContent();
                                Contacts.upDataName = content;
                                if (result.getUrl() != null) {
                                    if (!TextUtils.isEmpty(result.getUrl())) {
                                        Contacts.appAddress = result.getUrl();
                                    }
                                }
                                if (!TextUtils.isEmpty(versionNumber) && !TextUtils.isEmpty(Contacts.VersionName)) {
                                    String str = getReplaceString(versionNumber, ".");
                                    String str2 = getReplaceString(Contacts.VersionName, ".");
                                    //后台版本
                                    double num = Double.valueOf(str);
                                    //当前版本
                                    double num2 = Double.valueOf(str2);
                                    Log.e("123", "      num      " + num + "   num2  " + num2);
                                    if (num > num2) {
                                        PermisionUtils.verifyStoragePermissions(FoodFamilyActivity.this);
                                        updateVersion(is_force);
                                    }
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取到消息数量报错：" + error);
                            }
                        }, this, false));
    }


    /**
     * 获取用户排名
     */
    private void user_ranking_question() {
        subscription = Network.getInstance("获取用户排名", this)
                .user_ranking_request(
                        new ProgressSubscriberNew<>(UserRanking.class, new GsonSubscriberOnNextListener<UserRanking>() {
                            @Override
                            public void on_post_entity(UserRanking result) {
                                Log.e("123", "获取用户排名成功");
                                String info = "恭喜您成功注册食与家账号，您是食与家第" + result.getRanking() + "位受邀者。感谢您对食与家的支持。";
                                getInvitation(info, result.getRanking());
                                Contacts.typeMeg = "0";
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取用户排名报错：" + error);
                            }
                        }, this, false));
    }

    private InvitationDialog invitationDialog;

    private void getInvitation(String info, String number) {
        invitationDialog = new InvitationDialog(FoodFamilyActivity.this);
        invitationDialog.setOnLogoutClickListener(FoodFamilyActivity.this, info, number);
        invitationDialog.show();
    }


    //每个一秒发一个通知
    class DownThread implements Runnable {
        private Handler mhandler;

        public DownThread() {
            this.mhandler = getHandler();
        }

        public void run() {
            try {
                Thread.sleep(100);
                Message msg = new Message();
                msg.what = 2;
                this.mhandler.sendMessage(msg);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class DownThread2 implements Runnable {
        private Handler mhandler;

        public DownThread2() {
            this.mhandler = getHandler();
        }

        public void run() {
            try {
                Thread.sleep(100);
                Message msg = new Message();
                msg.what = 3;
                this.mhandler.sendMessage(msg);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        if (index_page.length() == 0) {
            //版本更新
            initUpData();
        }
        get_user_information();
        if ("1".equals(SharedPreferencesHelper.get(this, "isHomeMsg", ""))) {
//            if ("1".equals(Contacts.typeMeg)) {
//                user_ranking_question();
//            }
            if (null == mHandler) {
                mHandler = new Handler(Looper.getMainLooper());
            }
            mHandler.removeCallbacks(mRegularAction);
            mHandler.post(mRegularAction);
            init_jiaobiao();
        }

        //清除极光通知
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }

    }


    //使其不会被销毁
    @Override
    public void finish() {
        moveTaskToBack(true);
    }

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                //按钮提示音
                Contacts.selectTab = "0";
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                viewpager_content.setCurrentItem(0);
                xianshi_one();
                setBackTop();
                break;
            case R.id.btn_shipu:
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Contacts.selectTab = "1";
                Properties prop = new Properties();
                prop.setProperty("name", "btn_shipu");
                StatService.trackCustomKVEvent(getApplicationContext(), "Found", prop);
                viewpager_content.setCurrentItem(1);
                xianshi_two();
                break;

            case R.id.btn_movie:
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Contacts.selectTab = "0";
                Properties prop2 = new Properties();
                prop2.setProperty("name", "btn_movie");
                StatService.trackCustomKVEvent(getApplicationContext(), "News", prop2);
//                if ("".equals(SharedPreferencesHelper.get(FoodFamilyActivity.this, "login_token", ""))) {
//                    Intent intent = new Intent(FoodFamilyActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                } else {
                viewpager_content.setCurrentItem(2);
                xianshi_three();
//                }
                break;

            case R.id.btn_mine:
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Contacts.selectTab = "0";
                Properties prop3 = new Properties();
                prop3.setProperty("name", "btn_mine");
                StatService.trackCustomKVEvent(getApplicationContext(), "Myself", prop3);
                if ("".equals(SharedPreferencesHelper.get(FoodFamilyActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(FoodFamilyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
//                    getguidse();
                    viewpager_content.setCurrentItem(3);
                    xianshi_four();
                }
                break;
            case R.id.btn_tiwen:
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Contacts.selectTab = "0";
                Properties prop1 = new Properties();
                prop1.setProperty("name", "btn_tiwen");
                StatService.trackCustomKVEvent(getApplicationContext(), "Put_questions", prop1);
                if ("".equals(SharedPreferencesHelper.get(FoodFamilyActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(FoodFamilyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //设置弹窗
                    //select_more_popwindow();
                    showMoreWindow();

                }
                break;
            case R.id.guides_bg:
                date();
                guides_bg.setVisibility(View.GONE);
                break;
        }
    }

    public static Dialog m_dialog;

    private void select_more_popwindow() {
        LayoutInflater factory = LayoutInflater.from(FoodFamilyActivity.this);
        View view = factory.inflate(R.layout.more_popwindow2, null);

        //三个发布按钮
        LinearLayout movie_btn = view.findViewById(R.id.movie_btn);
        LinearLayout camera_btn = view.findViewById(R.id.camera_btn);
        LinearLayout question_btn = view.findViewById(R.id.question_btn);
        //跳转视频页面
        movie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断有无权限
                Intent intent1;
                if (EasyPermissions.hasPermissions(getApplicationContext(), PERMISSIONS)) {
                    intent1 = new Intent(FoodFamilyActivity.this, NoAccessActivity.class);
                } else {
                    intent1 = new Intent(FoodFamilyActivity.this, CamareActivity.class);
                }
                startActivity(intent1);
                m_dialog.dismiss();
            }
        });


        //跳转相机页面
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断有无权限
                Intent intent1;
                if (EasyPermissions.hasPermissions(getApplicationContext(), PERMISSIONS)) {
                    intent1 = new Intent(FoodFamilyActivity.this, CamareActivity.class);
                } else {
                    intent1 = new Intent(FoodFamilyActivity.this, NoAccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("put_value", "camera");
                    intent1.putExtras(bundle);
                }
                startActivity(intent1);
                m_dialog.dismiss();

            }
        });
        //跳转发布页面
        question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FoodFamilyActivity.this, AddQuestionActivity.class);
                intent1.putExtra("flag", 0);//新建提问
                startActivity(intent1);
                m_dialog.dismiss();

            }
        });
        //取消
        ImageView select_dismiss = view.findViewById(R.id.select_dismiss);

        m_dialog = new Dialog(FoodFamilyActivity.this, R.style.transparentFrameWindowStyle2);
        m_dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = m_dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = FoodFamilyActivity.this.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        m_dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        m_dialog.setCanceledOnTouchOutside(true);
        m_dialog.show();


        //取消
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "select_dismiss");
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_answer_cancel", prop);
                m_dialog.dismiss();
            }
        });
    }

    private void getguidse() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            //第一次进入跳转
            guides_bg.setVisibility(View.VISIBLE);
            editor.putBoolean("isfer", false);
            editor.commit();
        }
    }

    private void setBackTop() {
        EventBus.getDefault().post(new MessageEvent("200"));
    }

    class ContentPagerAdapterMy extends FragmentPagerAdapter {

        public ContentPagerAdapterMy(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //注销，使其不销毁fragment4
            super.destroyItem(container, position, object);
        }
    }

    private void xianshi_one() {
        home_img.setImageResource(R.drawable.icon_home_on);
        shipu_img.setImageResource(R.drawable.icon_discover_off);
        movie_img.setImageResource(R.drawable.icon_issue_off);
        mine_img.setImageResource(R.drawable.icon_user_off);

    }

    private void xianshi_two() {
        home_img.setImageResource(R.drawable.icon_home_off);
        shipu_img.setImageResource(R.drawable.icon_discover_on);
        movie_img.setImageResource(R.drawable.icon_issue_off);
        mine_img.setImageResource(R.drawable.icon_user_off);
    }

    private void xianshi_three() {
        home_img.setImageResource(R.drawable.icon_home_off);
        shipu_img.setImageResource(R.drawable.icon_discover_off);
        movie_img.setImageResource(R.drawable.icon_issue_on);
        mine_img.setImageResource(R.drawable.icon_user_off);
    }

    private void xianshi_four() {
        //跳转到登录页面
        home_img.setImageResource(R.drawable.icon_home_off);
        shipu_img.setImageResource(R.drawable.icon_discover_off);
        movie_img.setImageResource(R.drawable.icon_issue_off);
        mine_img.setImageResource(R.drawable.icon_user_on);
    }

    @Override
    public void onBackPressed() {
        PopWindowHelper.public_tishi_pop(FoodFamilyActivity.this, "食与家提示", "是否退回桌面？", "取消", "确定", new DialogCallBack() {
            @Override
            public void save() {
                date();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void date() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor = shared.edit();
        //第一次进入跳转
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(FoodFamilyActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(FoodFamilyActivity.this);
        }
        if (isfer) {
            editor.putBoolean("isfer", false);
            editor.commit();
        }
    }

    /**
     * 版本更新
     */
    //s是需要删除某个子串的字符串s1是需要删除的子串
    public String getReplaceString(String s, String s1) {
        int postion = s.indexOf(s1);
        int length = s1.length();
        int Length = s.length();
        String newString = s.substring(0, postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串
    }

    private void updateVersion(String is_force) {
        String info = Contacts.upDataName;
        dialog = new LogoutDialog(this, is_force);
        dialog.setOnLogoutClickListener(FoodFamilyActivity.this, info, "新版本上线了");
        dialog.show();
        if (dialog != null) {
//            Log.e("123", "         ----------->       " + dialog.isShowing());
            if (dialog.isShowing() == true) {
                index_page = "1";
            }
        }
    }

    @Override
    public void logoutOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout_confirm:
                if (PermisionUtils.lacksPermissions(this)) {//读写权限没开启
                    PermisionUtils.verifyStoragePermissions(FoodFamilyActivity.this);
                } else {
                    //读写权限已开启
                    if (!TextUtils.isEmpty(Contacts.upDataName)) {
                        UpdateManager manager = new UpdateManager(this, is_force);
                        manager.showDownloadDialog();
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (invitationDialog != null) {
                        invitationDialog.dismiss();
                    }
                }
                break;
            case R.id.btn_logout_cancle:
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (invitationDialog != null) {
                    invitationDialog.dismiss();
                }
                break;
            case R.id.btn_logout_consent:
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (invitationDialog != null) {
                    invitationDialog.dismiss();

                }
                break;
        }
    }

    //监听HOME键
    @Override
    public void onHomeKeyPress() {
//        if (null != dialog) {
//            dialog.dismiss();
//        }
        date();
    }

    @Override
    public void onRecentApps() {

    }

    private void showMoreWindow() {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init(idContainer);
        }

        mMoreWindow.showMoreWindow(idContainer, new MovieBtnBack() {
            @Override
            public void btn_onclick() {
                //判断有无权限
                if (EasyPermissions.hasPermissions(FoodFamilyActivity.this, PERMISSIONS)) {
                    if (progress.isShown() || rel_pregress_error.getVisibility() == View.VISIBLE) {
                        Toast.makeText(FoodFamilyActivity.this, "视频正在发布中", Toast.LENGTH_SHORT).show();
                    } else {
                        //进入视频拍摄页面
                        Intent recorder = new Intent(FoodFamilyActivity.this, PlayerActivity.class);
                        startActivity(recorder);
                    }

                } else {
                    Intent intent = new Intent(FoodFamilyActivity.this, NoAccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("put_value", "video");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 信鸽
     */
    private void getXGPushManager() {
        XGPushConfig.enableDebug(this, false);
        XGPushConfig.getToken(this);
        /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.e("123", "注册成功，设备token为：" + data);
                        SharedPreferencesHelper.put(getApplicationContext(), "pigeonData", data);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.e("123", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                    }
                });
        // 获取token
        XGPushConfig.getToken(this);
    }
}