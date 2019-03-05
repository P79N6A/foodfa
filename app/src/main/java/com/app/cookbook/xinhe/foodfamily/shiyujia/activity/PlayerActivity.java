package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.common.utils.StringUtil;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
//import com.aliyun.apsaravideo.recorder.AliyunVideoRecorder;
//import com.aliyun.svideo.sdk.external.struct.common.CropKey;
//import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
//import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
//import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
//import com.aliyun.svideo.sdk.external.struct.recorder.FlashType;
//import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.aliyun.demo.snap.SnapRecorderSetting;
import com.app.cookbook.xinhe.foodfamily.R;
import com.aliyun.common.utils.StorageUtils;

import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.jiguang.MainActivity;
import com.app.cookbook.xinhe.foodfamily.main.FoodFamilyActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.RegisterActivity;
import com.app.cookbook.xinhe.foodfamily.main.SelectLabelActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.BackEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.KeyEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.FileUtils;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.eventbus.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.util.eventbus.NetWorkEvent;
import com.app.cookbook.xinhe.foodfamily.util.progress.MixTextProgressBar;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;
import com.google.gson.Gson;
import com.seek.biscuit.Biscuit;
import com.seek.biscuit.CompressResult;
import com.seek.biscuit.OnCompressCompletedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.aliyun.apsaravideo.recorder.AliyunVideoRecorder;
import com.aliyun.demo.util.Utils;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
import com.aliyun.svideo.sdk.external.struct.recorder.FlashType;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.tencent.stat.StatService;


public class PlayerActivity extends AppCompatActivity {

    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.back_btn)
    LinearLayout back_btn;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.movie_image_fengmian)
    ImageView movie_image_fengmian;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.select_fengmian)
    LinearLayout select_fengmian;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.biaoqian_tv)
    TextView biaoqian_tv;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.fabu_btn)
    ImageView fabu_btn;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.content_tv)
    EditText content_tv;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.progress)
    MixTextProgressBar progress;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.finish_btn)
    ImageView finish_btn;
    @BindView(com.app.cookbook.xinhe.foodfamily.R.id.refresh_again_btn)
    LinearLayout refresh_again_btn;
    @BindView(R.id.rel_pregress_error)
    RelativeLayout rel_pregress_error;
    private int resolutionMode, ratioMode;
    String[] effectDirs;
    private VideoQuality videoQuality;
    private static final int REQUEST_RECORD = 2001;
    public static Bitmap sure_bitmap = null;
    public static String classification_name = "";
    public static String classification_id = "";
    public String biaoqian_id = "";
    private String upload_iamge_path;
    VODSVideoUploadClient vodsVideoUploadClient;
    String local_path;
    private String requestID = null;//传空或传递访问STS返回的requestID
    private String expriedTime = "2020-01-05T12:31:08Z";//STS的过期时间
    private String videoId = null;
    //ProgressUtil progress_upload;
    public static String selectStr = "";
    public static String selectStrName = "";
    String[] lvjings = new String[10];
    boolean is_first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**初始化阿里视频上传*/
        vodsVideoUploadClient = new VODSVideoUploadClientImpl(this.getApplicationContext());
        vodsVideoUploadClient.init();
        //阿里视频录制
        ali_play_demo();

        setContentView(R.layout.activity_player);

        //初始化黄牛刀
        ButterKnife.bind(this);
        //影藏虚拟键
        hideBottomUIMenu();

        /**动态设置距离状态栏高度*/
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(0, 0, 0, 0);
            } else {
                lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(0, 140, 0, 0);
            } else {
                lp.setMargins(0, 70, 0, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(0, 0, 0, 0);
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        }
        lin_layout.setLayoutParams(lp);


        //关于拍摄好之后的页面逻辑
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWindowHelper.public_tishi_pop(PlayerActivity.this, "食与家提示", "要放弃编辑视频吗？", "取消", "放弃", new DialogCallBack() {
                    @Override
                    public void save() {
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });

        //选择封面
        RxUtils.clickView(select_fengmian)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("获取到的机型", Build.MODEL);
                        if (Build.MODEL.equals("MI MAX 2")) {//特殊机型
                            Intent intent = new Intent(PlayerActivity.this, FengMianSelectActivity.class);
                            intent.putExtra("luzhi_path", luzhi_path);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PlayerActivity.this, FengMianSelect2Activity.class);
                            intent.putExtra("luzhi_path", luzhi_path);
                            startActivity(intent);
                        }


                    }
                });
        //选择标签
        biaoqian_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PlayerActivity.this, SelectLabelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("img_data", "");
                bundle.putString("enter_select_add_images", "enter_player_activity");
                bundle.putString("flag", "5");
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });


        //获取各种key
        getVodUploadInfo();

        //发布
        fabu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classification_id.length()<=0){
                    Toast.makeText(getApplicationContext(), "标签不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (StringUtil.isEmpty(accessKeyId)) {
                        Toast.makeText(getApplicationContext(), "The specified parameter accessKeyId cannot be null", Toast.LENGTH_LONG).show();
                        return;
                    } else if (StringUtil.isEmpty(accessKeySecret)) {
                        Toast.makeText(getApplicationContext(), "The specified parameter \"accessKeySecret\" cannot be null", Toast.LENGTH_LONG).show();
                        return;
                    } else if (StringUtil.isEmpty(securityToken)) {
                        Toast.makeText(getApplicationContext(), "The specified parameter \"securityToken\" cannot be null", Toast.LENGTH_LONG).show();
                        return;
                    }
                    /**提交图片*/
                    //获取本地图片
                    local_path = saveImageToGallery(getApplicationContext(), sure_bitmap);
                    //显示进度条
                    insertImagesSync(local_path);
                    //首先储存全局的值
                    FoodFamilyActivity.luzhi_path = luzhi_path;
                    FoodFamilyActivity.local_path = local_path;
                    FoodFamilyActivity.accessKeyId = accessKeyId;
                    FoodFamilyActivity.accessKeySecret = accessKeySecret;
                    FoodFamilyActivity.securityToken = securityToken;
                    FoodFamilyActivity.content_tv = content_tv.getText().toString();
                    FoodFamilyActivity.classification_id = classification_id;
                    Log.e("是点击看简历", "jkfjdsl" + classification_id);
                    FoodFamilyActivity.upload_iamge_path = upload_iamge_path;
                    EventBus.getDefault().post(new MessageEvent(1));

                    finish();
                }

            }
        });
        //进度条初始化
        progress.setMax(100);
        progress.setProgress(0);

        EventBus.getDefault().register(this);


        //上传视频重试
        refresh_again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connect_net_work) {
                    Log.e("点击重新上传", "点击重新上传");
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

        //监听文字框的字数
        content_tv.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                temp = s;
                selectionStart = content_tv.getSelectionStart();
                selectionEnd = content_tv.getSelectionEnd();
                if (temp.length() > 60) {
                    Toast.makeText(PlayerActivity.this, "只能输入六十个字",
                            Toast.LENGTH_SHORT).show();
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    content_tv.setText(s);
                    content_tv.setSelection(tempSelection);
                }
            }
        });
        //禁用键盘回车键
        content_tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });


    }

    /**
     * 集成阿里短视频录制demo
     */
    private void ali_play_demo() {
        if (null != MyApplication.lvjings && MyApplication.lvjings.length == 10) {
            lvjings = MyApplication.lvjings;
            for (int i = 0; i < MyApplication.lvjings.length; i++) {
                if (null != lvjings[i]) {
                    Log.e("选择的滤镜", lvjings[i]);
                }
            }
            start_play();
        } else {
            copyAssets();
        }

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
                EventBus.getDefault().post(new MessageEvent(-100));
                //设置上传失败逻辑
                Message message1 = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("net_work", "no_work");
                message1.setData(bundle);
                handler.sendMessage(message1);
                EventBus.getDefault().post(new MessageEvent(-100));
            }

            @Override
            public void onUploadProgress(long uploadedSize, long totalSize) {
                Log.e("上传进度onUploadProgress", "" + uploadedSize * 100 / totalSize);
                mprogress = uploadedSize * 100 / totalSize;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("progress", (int) mprogress);
                message.setData(bundle);
                handler.sendMessage(message);
                //全局发送进度
                EventBus.getDefault().post(new MessageEvent((int) mprogress));
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

    private boolean connect_net_work;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(NetWorkEvent netWorkEvent) {
        if (netWorkEvent.getMessage().equals("work")) {
            connect_net_work = true;
        } else {
            connect_net_work = false;
            //设置进度条断开逻辑
            Log.e("网络连接已断开", "网络连接已断开");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("net_work", "no_work");
            message.setData(bundle);
            handler.sendMessage(message);
            EventBus.getDefault().post(new MessageEvent(-100));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sure_bitmap = null;
        classification_name = "";
        classification_id = "";
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
//        accessKeyId = "";
//        accessKeySecret = "";
//        securityToken = "";

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (null != bundle.get("net_work") && bundle.get("net_work").equals("no_work")) {//网络连接已断开
                Toast.makeText(getApplicationContext(), "网络连接已断开~", Toast.LENGTH_SHORT).show();
                vodsVideoUploadClient.pause();
                //设置上传失败逻辑
                if (progress.isShown()) {
                    //显示红色部分
                    rel_pregress_error.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
                return;
            }
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
                    progress.setVisibility(View.GONE);
                }
            }
        }
    };

    private void fabu_content() {
        Map<String, Object> params = new HashMap<>();
        params.put("video_id", videoId);
        params.put("face_path", upload_iamge_path);
        if (biaoqian_id.length() > 0) {
            params.put("class_ids", biaoqian_id);
        }
        params.put("content", content_tv.getText().toString());
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("发布视频参数", params.toString());
        Subscription subscription = Network.getInstance("发布视频", getApplicationContext())
                .fabu_movie(params,
                        new ProgressSubscriberNew<>(BackEntity.class, new GsonSubscriberOnNextListener<BackEntity>() {
                            @Override
                            public void on_post_entity(BackEntity backEntity) {
                                Log.e("发布视频成功：", backEntity.getId());
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("发布视频失败：", error);
                            }
                        }, this, false));
    }


    static String accessKeyId;
    static String accessKeySecret;
    static String securityToken;

    public void getVodUploadInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //2构造Request,
        //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(Network.main_url +"aliyun/sts-servers/video_sts.php?userid=1").build();

        //3将Request封装成call
        Call call = okHttpClient.newCall(request);

        //4，执行call，这个方法是异步请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //成功调用
                //获取网络访问返回的字符串
                String string = response.body().string();
                Log.e("获取秘钥成功", string);

                Gson gson = new Gson();
                KeyEntity keyEntity = gson.fromJson(string, KeyEntity.class);
                accessKeyId = keyEntity.getAccessKeyId();
                accessKeySecret = keyEntity.getAccessKeySecret();
                securityToken = keyEntity.getSecurityToken();

            }
        });

    }


    Subscriber<? super String> subscriber;
    Biscuit mBiscuit;

    private void insertImagesSync(final String mimage_path) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> msubscriber) {
                subscriber = msubscriber;
                //添加压缩版本
                try {
                    //循环压缩
                    mBiscuit = Biscuit.with(PlayerActivity.this)
                            .path(mimage_path) //可以传入一张图片路径，也可以传入一个图片路径列表
                            .loggingEnabled(true)//是否输出log 默认输出
                            //  .quality(50)//质量压缩值（0...100）默认已经非常接近微信，所以没特殊需求可以不用自定义
                            //     .originalName(false) //使用原图名字来命名压缩后的图片，默认不使用原图名字,随机图片名字
                            .listener(new OnCompressCompletedListener() {
                                @Override
                                public void onCompressCompleted(CompressResult compressResult) {
                                    if (mimage_path.contains(".gif")) {//如果是动态图，不压缩
                                        //保存到压缩文件夹
                                        File file = new File(FileUtils.getImageDir());
                                        if (!file.exists()) {
                                            file.mkdir();
                                        }
                                        if (!TextUtils.isEmpty(FileUtils.getImageDir())) {
                                            String last = FileUtils.getImageDir().substring(FileUtils.getImageDir().length() - 1, FileUtils.getImageDir().length());
                                            if (!last.equals(File.separator)) {
                                                throw new IllegalArgumentException("targetDir must be end with " + File.separator);
                                            }
                                        }
                                        Log.e("保存到的目录：", file.getAbsolutePath());
                                        //将文件复制到指定目录
                                        String s = mimage_path;
                                        String temp[] = s.replaceAll("\\\\", "/").split("/");
                                        String fileName = "";
                                        if (temp.length > 1) {
                                            fileName = temp[temp.length - 1];//获取图片的名称
                                            String new_path = copyFile(s, FileUtils.getImageDir() + System.currentTimeMillis() + fileName);
                                            subscriber.onNext(new_path);
                                        }
                                    } else {
                                        subscriber.onNext(mimage_path);
                                    }

                                }

                            })//压缩完成监听
                            .targetDir(FileUtils.getImageDir())//自定义压缩保存路径
//                              .executor(executor) //自定义实现执行，注意：必须在子线程中执行 默认使用AsyncTask线程池执行
                            //   .ignoreAlpha(true)//忽略alpha通道，对图片没有透明度要求可以这么做，默认不忽略。
                            //    .compressType(Biscuit.SAMPLE)//采用采样率压缩方式，默认是使用缩放压缩方式，也就是和微信的一样。
                            .ignoreLessThan(100)//忽略小于100kb的图片不压缩，返回原图路径
                            .build();
                    mBiscuit.asyncCompress();


                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }

        })
                .onBackpressureBuffer()
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("图片上传完成", "图片上传完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("图片上传失败", e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        Log.e("插入进去的图片", imagePath);
                        //请求阿里上传图片
                        //判断图片格式是否是gif
                        if (imagePath.contains(".gif")) {
                            upload_user_image("syjapppic", Network.bucketPath + "Imprinting/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".gif", imagePath);
                        } else {
                            upload_user_image("syjapppic", Network.bucketPath + "Imprinting/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg", imagePath);
                        }

                    }
                });
    }

    public String copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

        return newPath;

    }

    /**
     * 上传图片到阿里服务器
     * * @param bucketName
     *
     * @param objectKey
     * @param uploadFilePath
     */
    private void upload_user_image(
            String bucketName
            , final String objectKey
            , final String uploadFilePath) {

        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        //动态获取token验证
        OSSCredentialProvider credentialProvider1 = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                String net_url = Network.main_url + "/aliyun/sts-servers/sts.php?userid=1";
                // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
                // 如果因为某种原因获取失败，可直接返回nil
                // 下面是一些获取token的代码，比如从您的server获取
                try {
                    URL stsUrl = new URL(net_url);
                    HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                    Network.trustAllHttpsCertificates();//跳过ssl验证
                    InputStream input = conn.getInputStream();
                    String jsonText = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
                    JSONObject jsonObjs = new JSONObject(jsonText);
                    String ak = jsonObjs.getString("AccessKeyId");
                    String sk = jsonObjs.getString("AccessKeySecret");
                    String token = jsonObjs.getString("SecurityToken");
                    String expiration = jsonObjs.getString("Expiration");
                    return new OSSFederationToken(ak, sk, token, expiration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(9); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        //开启可以在控制台看到日志，并且会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv  默认不开启
        //日志会记录oss操作行为中的请求数据，返回数据，异常信息
        //例如requestId,response header等
        //android_version：5.1  android版本
        //mobile_model：XT1085  android手机型号
        //network_state：connected  网络状况
        //network_type：WIFI 网络连接类型
        //具体的操作行为信息:
        //[2017-09-05 16:54:52] - Encounter local execpiton: //java.lang.IllegalArgumentException: The bucket name is invalid.
        //A bucket name must:
        //1) be comprised of lower-case characters, numbers or dash(-);
        //2) start with lower case or numbers;
        //3) be between 3-63 characters long.
        //------>end of log
        OSSLog.enableLog();//开启日志记录
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider1);

        // 构造上传请求(同步上传)
        PutObjectRequest put = new PutObjectRequest(
                bucketName,
                objectKey,
                uploadFilePath);


        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.e("图片当前上传进度：", "currentSize: " + currentSize + " 图片总上传进度: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "上传图片成功");
                upload_iamge_path = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                subscriber.onCompleted();

                //上传视频
                upload_movie();

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        //task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); //可以等待直到任务完成

    }

    public String saveImageToGallery(Context context, Bitmap bmp) {
        String path = Environment.getExternalStorageDirectory() + "/images";
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String fileName = "Fengmian_" + System.currentTimeMillis() + ".jpg";
        File file = new File(fileFolder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    @Override
    protected void onResume() {
        super.onResume();
        movie_image_fengmian.setImageBitmap(sure_bitmap);
        biaoqian_id = classification_id;
        if (classification_name.equals("")) {
            biaoqian_tv.setText(R.string.wenben19);
        } else {
            biaoqian_tv.setText("# " + classification_name.replace(",", "#") + " #");
        }

    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
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

//        for (int i = 0; i < lvjings.length; i++) {
//            if (null != lvjings[i]) {
//                Log.e("选择的滤镜", lvjings[i]);
//            }
//        }


    }

    private String luzhi_path = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RECORD) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_CROP) {
                    String path = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                    Log.e("录制的路径", "文件路径为 " + path);
                    luzhi_path = path;
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(path);
                    // 视频封面
                    Bitmap videoCoverBitmap = media.getFrameAtTime();
                    sure_bitmap = videoCoverBitmap;

                } else if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    Log.e("111录制的路径", "文件路径为 " + data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
                    luzhi_path = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH);
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH));
                    // 视频封面
                    Bitmap videoCoverBitmap = media.getFrameAtTime();
                    sure_bitmap = videoCoverBitmap;
                    //localVideoEntity.bitmapPath = saveVideoBitmap( videoCoverBitmap );//此处我将bitmap转换成了Path路径

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this,"用户取消录制",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private String saveVideoBitmap(Bitmap bitmap) {
        File file = new File(getCacheDir() + File.separator + UUID.randomUUID() + ".jpg");
        try {
            if (!file.getParentFile().exists()) file.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Utils.copyAll(PlayerActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                initAssetPath();

                start_play();

            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void start_play() {
        int min = 3000;
        int max = 60000;
        int gop = 5;
        resolutionMode = AliyunSnapVideoParam.RESOLUTION_540P;
        videoQuality = VideoQuality.HD;
        ratioMode = AliyunSnapVideoParam.RATIO_MODE_1_1;
        //是否显示浮层
        is_first = Boolean.parseBoolean(SharedPreferencesHelper.get(getApplicationContext(), "is_first_play", "").toString());
        AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                .setResolutionMode(resolutionMode)
                .setRatioMode(ratioMode)
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                .setFilterList(lvjings)
                .setBeautyLevel(80)
                .setBeautyStatus(true)
                .setCameraType(CameraType.BACK)
                .setFlashType(FlashType.OFF)
                .setNeedClip(true)
                .setMaxDuration(max)
                .setMinDuration(min)
                .setVideoQuality(videoQuality)
                .setGop(gop)
                /**
                 * 裁剪参数
                 */
                .setFrameRate(25)
                .setCropMode(VideoDisplayMode.FILL)
                //显示分类SORT_MODE_VIDEO视频;SORT_MODE_PHOTO图片;SORT_MODE_MERGE图片和视频
                .setSortMode(AliyunSnapVideoParam.SORT_MODE_VIDEO)
                .build();
        AliyunVideoRecorder.startRecordForResult(PlayerActivity.this, REQUEST_RECORD, recordParam, is_first);
        SharedPreferencesHelper.put(getApplicationContext(), "is_first_play", "false");
    }

}
