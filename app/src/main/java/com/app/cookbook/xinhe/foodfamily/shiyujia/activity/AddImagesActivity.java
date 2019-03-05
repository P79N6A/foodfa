package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.SelectLabelActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.BackEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImagePathEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.resource.ValueResources;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.DataCleanManager;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.FileUtils;
import com.app.cookbook.xinhe.foodfamily.util.ScreenUtilsHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.keyboard.KeyboardStateObserver;
import com.app.cookbook.xinhe.foodfamily.util.ui.CCRSortableNinePhotoLayout;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.models.puzzle.Line;
import com.orhanobut.logger.Logger;
import com.seek.biscuit.Biscuit;
import com.seek.biscuit.CompressResult;
import com.seek.biscuit.OnCompressCompletedListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddImagesActivity extends AppCompatActivity implements CCRSortableNinePhotoLayout.Delegate {
    /**
     * 拖拽排序九宫格控件
     */
    @BindView(R.id.snpl_moment_add_photos)
    CCRSortableNinePhotoLayout mPhotosSnpl;
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.biaoqian_tv)
    TextView biaoqian_tv;
    @BindView(R.id.fabu_btn)
    LinearLayout fabu_btn;
    @BindView(R.id.save_caogao_btn)
    LinearLayout save_caogao_btn;
    @BindView(R.id.edit_text)
    EditText edit_text;
    @BindView(R.id.save_caogao_tv)
    TextView save_caogao_tv;
    @BindView(R.id.tuozhuai_fuceng)
    LinearLayout tuozhuai_fuceng;
    private boolean is_fabu_caogao = false;
    ArrayList<ImagePathEntity> upload_iamge_paths = new ArrayList<>();
    private boolean is_save_caogao = false;
    private String update_content = "";
    private static String update_id = "";
    public static List<String> iamge_paths = new ArrayList<>();
    public static String classification_id = "";
    public static String classification_name = "";
    public static String selectStr = "";
    public static String selectStrName = "";
    private static String flag = "";
    private static List<ImageTextDetails.ClassDataBean> biaoqians = new ArrayList<>();
    private boolean is_first_enter = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        setchenjing();
        setContentView(R.layout.activity_add_images);
        //初始化黄牛刀
        ButterKnife.bind(this);

        //影藏虚拟键
        hideBottomUIMenu();

        /**动态设置距离状态栏高度*/
        //setlayout(false);

        //设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        //判断是不是从编辑入口进来的数据
        if (null != getIntent().getStringExtra("flag")) {
            flag = getIntent().getStringExtra("flag");
            if (flag.equals("1")) {//编辑
                iamge_paths.addAll(getIntent().getStringArrayListExtra("update_image_paths"));
                update_id = getIntent().getStringExtra("update_id");
                update_content = getIntent().getStringExtra("update_content");
                edit_text.setText(update_content);
                if (null != getIntent().getExtras().getSerializable("biaoqians")) {
                    biaoqians = getIntent().getExtras().getParcelableArrayList("biaoqians");
                    //设置标签控件
                    List<String> lists = new ArrayList<>();
                    List<String> list_ids = new ArrayList<>();
                    for (int i = 0; i < biaoqians.size(); i++) {
                        lists.add(biaoqians.get(i).getTitle());
                        list_ids.add(biaoqians.get(i).getId());
                    }
                    classification_id = update_biaoqian_patams(list_ids);
                    classification_name = update_biaoqian_names(lists);
                    //设置缓存好的标签
                    try {
                        selectStrName = SharedPreferencesHelper.SceneList2String(lists);
                        selectStr = SharedPreferencesHelper.SceneList2String(list_ids);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //影藏存草稿按钮
                save_caogao_btn.setVisibility(View.GONE);
                save_caogao_tv.setVisibility(View.GONE);
                Log.e("就放开手懒觉福利", "iamge_paths:" + iamge_paths.size()
                        + "update_id:" + update_id + "update_content:" + update_content
                        + "biaoqians:" + biaoqians.size());
            } else if (flag.equals("5")) {//编辑草稿箱
                iamge_paths.addAll(getIntent().getStringArrayListExtra("update_image_paths"));
                update_id = getIntent().getStringExtra("update_id");
                update_content = getIntent().getStringExtra("update_content");
                edit_text.setText(update_content);
                String biaoqian_names = getIntent().getStringExtra("biaoqian_string");
                if (null != biaoqian_names && biaoqian_names.length() >= 1) {
                    classification_name = biaoqian_names;//设置本页标签
                    classification_id = classification_name;//接口没有传下来ID
                    List<String> lists = new ArrayList<>();
                    String[] strArray = null;
                    strArray = convertStrToArray(classification_name);
                    for (int i = 0; i < strArray.length; i++) {
                        lists.add(strArray[i]);
                    }
                    Log.e("123", "   lists.size()     " + lists.size());
                    try {
                        selectStrName = SharedPreferencesHelper.SceneList2String(lists);
                        selectStr = SharedPreferencesHelper.SceneList2String(lists);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("草稿箱就放开手懒觉福利", "iamge_paths:" + iamge_paths.size()
                        + "update_id:" + update_id + "update_content:" + update_content
                        + "biaoqians:" + biaoqian_names);
            }
        }


        //获取传递过来的数据
        if (null != getIntent().getStringArrayListExtra("iamges_paths")) {
            iamge_paths.addAll(getIntent().getStringArrayListExtra("iamges_paths"));
        }

        //返回按钮
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    select_save_popwindow2();
                } else {
                    select_save_popwindow();
                }
            }
        });

        //选择标签
        biaoqian_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddImagesActivity.this, SelectLabelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("img_data", "");
                bundle.putString("enter_select_add_images", "enter_select_add_images");
                bundle.putString("flag", "4");
                intent1.putExtras(bundle);
                startActivity(intent1);

            }
        });

        //发布图文
        fabu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_text.getText().toString().replace(" ", "");
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "发布内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (iamge_paths.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "发布图片不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (classification_name.length() <= 0 || classification_id.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "标签不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    is_save_caogao = false;
                    is_fabu_caogao = true;
                    //首先先上传图片
                    insertImagesSync((ArrayList<String>) iamge_paths);
                }
            }
        });

        //存草稿
        save_caogao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_text.getText().toString().replace(" ", "");
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "发布内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (iamge_paths.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "发布图片不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    is_save_caogao = true;
                    //首先先上传图片
                    insertImagesSync((ArrayList<String>) iamge_paths);
                }
            }
        });

        KeyboardStateObserver.getKeyboardStateObserver(this).
                setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                    @Override
                    public void onKeyboardShow() {
                        Log.e("键盘弹出", "键盘弹出");
                        setlayout(true);
                    }

                    @Override
                    public void onKeyboardHide() {
                        Log.e("键盘收回", "键盘收回");
                        setlayout(false);
                    }
                });

        MyApplication.addDestoryActivity(this, "AddImagesActivity");


        //判断是否影藏浮层
        is_first_enter = Boolean.parseBoolean(SharedPreferencesHelper.get(getApplicationContext(), "is_first_enter_tuwen", "").toString());
        if(is_first_enter){
            tuozhuai_fuceng.setVisibility(View.VISIBLE);
        }else{
            tuozhuai_fuceng.setVisibility(View.GONE);
        }
        lin_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //点击的开始位置
                    case MotionEvent.ACTION_DOWN:
                        tuozhuai_fuceng.setVisibility(View.GONE);
                        SharedPreferencesHelper.put(getApplicationContext(), "is_first_enter_tuwen", "false");
                        break;
                }
                return true;
            }
        });
        edit_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //点击的开始位置
                    case MotionEvent.ACTION_DOWN:
                        tuozhuai_fuceng.setVisibility(View.GONE);
                        SharedPreferencesHelper.put(getApplicationContext(), "is_first_enter_tuwen", "false");
                        break;
                }
                return false;
            }
        });

    }

    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    private void setlayout(boolean is_show_bottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                // lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
                if (is_show_bottom) {
                    lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, ScreenUtilsHelper.dip2px(getApplicationContext(), 50));
                } else {
                    lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
                }
            } else {
                lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(0, 140, 0, 0);
            } else {
                lp.setMargins(0, 30, 0, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                if (is_show_bottom) {
                    lp.setMargins(0, 30, 0, ScreenUtilsHelper.dip2px(getApplicationContext(), 50));
                } else {
                    lp.setMargins(0, 30, 0, 0);
                }
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        }
        lin_layout.setLayoutParams(lp);
    }

    /* 标签转1,2，3
     * @return
     */
    private String update_biaoqian_patams(List<String> strings) {
        String strlist = strings.toString();
        int len = strlist.length() - 1;
        String ids = strlist.substring(1, len).replace("", "");//"keyids":”1,2,3”
        return ids;
    }

    private String update_biaoqian_names(List<String> strings) {
        String strlist = strings.toString();
        int len = strlist.length() - 1;
        String ids = strlist.substring(1, len).replace("", "");//"keyids":”1,2,3”
        return ids;
    }

    private void setchenjing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(AddImagesActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(AddImagesActivity.this);
        }
    }


    Handler bili_handel = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String iamge_path = msg.getData().getString("image_net_path");
            Log.e("数据库费", iamge_path);
            //获取图片真正的宽高
            Glide.with(AddImagesActivity.this)
                    .load(iamge_path)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            ImagePathEntity imagePathEntity = new ImagePathEntity();
                            imagePathEntity.setLocal_path("");
                            imagePathEntity.setNet_path(iamge_path);
                            if(width==height){
                                imagePathEntity.setBili("1:1");

                            }else{
                                imagePathEntity.setBili("3:4");
                            }
                            upload_iamge_paths.add(imagePathEntity);
                            if (upload_iamge_paths.size() == iamge_list_length) {
                                progress_upload.dismissProgressDialog();
                                subscriber.onCompleted();
                                //发布内容
                                if (flag.equals("1")) {//说明是编辑
                                    bianji_content();
                                } else if (flag.equals("5")) {//编辑草稿箱
                                    if (is_fabu_caogao) {//发布草稿
                                        fabu_caogao();
                                    } else {
                                        update_caogao();
                                    }
                                } else {//说明是保存草稿或者发布
                                    if (is_save_caogao) {
                                        save_caogao_content();
                                    } else {
                                        fabu_content();
                                    }
                                }
                            }

                        }
                    });
        }
    };

    int iamge_list_length = 0;
    Subscriber<? super String> subscriber;
    Biscuit mBiscuit;
    ProgressUtil progress_upload;
    List<String> images_local_paths = new ArrayList<>();

    private void insertImagesSync(final ArrayList<String> mselectedPhotoList) {
        progress_upload = new ProgressUtil();
        if (is_save_caogao) {
            progress_upload.showProgressDialog(AddImagesActivity.this, "草稿保存中...");
        } else {
            progress_upload.showProgressDialog(AddImagesActivity.this, "图片发布中...");
        }
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> msubscriber) {
                subscriber = msubscriber;
                iamge_list_length = mselectedPhotoList.size();
                images_local_paths.addAll(mselectedPhotoList);
                //添加压缩版本
                try {
                    for (int i = 0; i < mselectedPhotoList.size(); i++) {
                        if (mselectedPhotoList.get(i).contains("https://syjapppic.oss")) {//说明是网络图片，不用再上传
                            Log.e("进来了网络图片", "进来了网络图片" + i);
                            int finalI = i;
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("image_net_path", mselectedPhotoList.get(finalI));
                                    message.setData(bundle);
                                    bili_handel.sendMessage(message);
                                }
                            };
                            thread.start();
                        } else {
                            //走压缩流程,循环压缩
                            Log.e("进来了本地图片", "进来了本地图片");
                            String local_path = mselectedPhotoList.get(i);//本地地址
                            mBiscuit = Biscuit.with(AddImagesActivity.this)
                                    .path(local_path) //可以传入一张图片路径，也可以传入一个图片路径列表
                                    .loggingEnabled(true)//是否输出log 默认输出
                                    .quality(80)//质量压缩值（0...100）默认已经非常接近微信，所以没特殊需求可以不用自定义
                                    .originalName(true) //使用原图名字来命名压缩后的图片，默认不使用原图名字,随机图片名字
                                    .listener(new OnCompressCompletedListener() {
                                        @Override
                                        public void onCompressCompleted(CompressResult compressResult) {
//                                            List<String> paths = new ArrayList<>();
//                                            paths.addAll(compressResult.mSuccessPaths);
                                            String upload_path = compressResult.mSuccessPaths.get(0);//压缩后的新地址
                                            iamge_list_length = mselectedPhotoList.size();

                                            if (upload_path.contains(".gif")) {//如果是动态图，不压缩
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
                                                String s = local_path;
                                                String temp[] = s.replaceAll("\\\\", "/").split("/");
                                                String fileName = "";
                                                if (temp.length > 1) {
                                                    fileName = temp[temp.length - 1];//获取图片的名称
                                                    String new_path = copyFile(s, FileUtils.getImageDir() + System.currentTimeMillis() + fileName);
                                                    subscriber.onNext(new_path);
                                                }
                                            } else {
                                                subscriber.onNext(upload_path);
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

                        }
                    }


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
                            upload_user_image("syjapppic", Network.bucketPath + "question/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".gif", imagePath);
                        } else {
                            upload_user_image("syjapppic", Network.bucketPath + "question/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg", imagePath);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iamge_paths.size() > 0) {
            //    Log.e("应该清空的", "应该清空的" + iamge_paths.size());
            iamge_paths.clear();
            iamge_paths = new ArrayList<>();
        }
        update_id = "";
        selectStr = "";
        selectStrName = "";
        flag = "";
        classification_id = "";
        classification_name = "";
        biaoqians.clear();
        //为了防止相册图片重复出现，删除缓存中的图片信息
        File file = new File(Environment.getExternalStorageDirectory() + "/images/");
        String image_str_size = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(file));
        if (image_str_size.equals("0k")) {
        } else {
            DataCleanManager.deleteFolderFile(Environment.getExternalStorageDirectory() + "/images/", true);
        }
        ValueResources.enter_jia = "";

    }


    /**
     * 上传图片到阿里服务器
     * * @param bucketName
     *
     * @param objectKey
     * @param muploadFilePath
     */

    private void upload_user_image(String bucketName, final String objectKey,
                                   final String muploadFilePath) {
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
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
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
                muploadFilePath);


        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //   Log.e("图片当前上传进度：", "currentSize: " + currentSize + " 图片总上传进度: " + totalSize);
            }
        });


        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                // Log.e("PutObject", "上传图片成功");
                String uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                ImagePathEntity bili_imagePathEntity = new ImagePathEntity();
                bili_imagePathEntity.setLocal_path(muploadFilePath);
                bili_imagePathEntity.setNet_path(uploud_iamge);
                upload_iamge_paths.add(bili_imagePathEntity);

                if (upload_iamge_paths.size() == iamge_list_length) {
                    progress_upload.dismissProgressDialog();
                    subscriber.onCompleted();

                    //发布内容
                    if (flag.equals("1")) {//说明是编辑
                        bianji_content();
                    } else if (flag.equals("5")) {//编辑草稿箱
                        if (is_fabu_caogao) {//发布草稿
                            fabu_caogao();
                        } else {
                            update_caogao();
                        }
                    } else {//说明是保存草稿或者发布
                        if (is_save_caogao) {
                            save_caogao_content();
                        } else {
                            fabu_content();
                        }
                    }

                }


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


    /**
     * 编辑图文
     */
    private void bianji_content() {
        Map<String, Object> params = new HashMap<>();
        Gson g = new Gson();
        String jsonString = g.toJson(need_update_iamge_method(upload_iamge_paths));
        params.put("img_data", jsonString);
        params.put("content", edit_text.getText().toString());
        if ("".equals(classification_id)) {
        } else {
            params.put("class_ids", classification_id);
        }
        params.put("app_version", Contacts.VersionName);
        params.put("id", update_id);
        Log.e("编辑图文参数", params.toString());

        Subscription subscription = Network.getInstance("编辑图文", getApplicationContext())
                .save_content_bianji(params,
                        new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                            @Override
                            public void on_post_entity(Object backEntity) {
                                flag = "";
                                MyApplication.destoryActivity("CamareActivity");
                                finish();
                                MyApplication.destoryActivity("AddImagesActivity");

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                flag = "";
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                flag = "";
                                //Log.e("123", "保存草稿失败：" + error);
                            }
                        }, this, false));

    }

    /**
     * 保存草稿箱
     */
    private void save_caogao_content() {
        Map<String, Object> params = new HashMap<>();
        Gson g = new Gson();
        String jsonString = g.toJson(need_update_iamge_method(upload_iamge_paths));
        params.put("img_data", jsonString);
        params.put("content", edit_text.getText().toString());
        if ("".equals(classification_name)) {
        } else {
            params.put("class_ids", classification_name);//发布草稿箱只需要传名字
        }
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("保存草稿图文参数", params.toString());

        Subscription subscription = Network.getInstance("发布图文", getApplicationContext())
                .save_content_caogao(params,
                        new ProgressSubscriberNew<>(BackEntity.class, new GsonSubscriberOnNextListener<BackEntity>() {
                            @Override
                            public void on_post_entity(BackEntity backEntity) {
                                Log.e("123", "保存草稿图文成功：" + backEntity.getId());
                                MyApplication.destoryActivity("CamareActivity");
                                finish();
                                MyApplication.destoryActivity("AddImagesActivity");
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "保存草稿失败：" + error);
                            }
                        }, this, true));
    }

    /**
     * 发布图文
     */
    private void fabu_content() {
        Map<String, Object> params = new HashMap<>();
        Gson g = new Gson();
        String jsonString = g.toJson(need_update_iamge_method(upload_iamge_paths));
        Log.e("图片数组参数：", jsonString);
        params.put("img_data", jsonString);
        params.put("content", edit_text.getText().toString());
        if ("".equals(classification_id)) {
        } else {
            params.put("class_ids", classification_id);
        }
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("发布图文参数", params.toString());

        Subscription subscription = Network.getInstance("发布图文", getApplicationContext())
                .fabu_content(params,
                        new ProgressSubscriberNew<>(BackEntity.class, new GsonSubscriberOnNextListener<BackEntity>() {
                            @Override
                            public void on_post_entity(BackEntity backEntity) {
                                Log.e("123", "发布图文成功：" + backEntity.getId());
                                MyApplication.destoryActivity("CamareActivity");
                                finish();
                                MyApplication.destoryActivity("AddImagesActivity");
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "发布图文失败：" + error);
                            }
                        }, this, false));

    }


    /**
     * 编辑草稿箱
     */
    private void update_caogao() {
        Map<String, Object> params = new HashMap<>();
        Gson g = new Gson();
        String jsonString = g.toJson(need_update_iamge_method(upload_iamge_paths));
        params.put("img_data", jsonString);
        params.put("content", edit_text.getText().toString());
        if ("".equals(classification_name)) {
        } else {
            params.put("class_ids", classification_name);
        }
        params.put("app_version", Contacts.VersionName);
        params.put("draft_id", update_id);
        Log.e("编辑图文草稿参数", params.toString());
        Subscription subscription = Network.getInstance("编辑草稿图文", getApplicationContext())
                .save_content_caogao_bianji(params,
                        new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                            @Override
                            public void on_post_entity(Object backEntity) {
                                flag = "";
                                Logger.e("编辑草稿图文成功：", "");
                                MyApplication.destoryActivity("CamareActivity");
                                MyApplication.destoryActivity("AddImagesActivity");
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                flag = "";
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                flag = "";
                                Logger.e("保存草稿失败：" + error);
                            }
                        }, this, false));
    }

    /**
     * 发布草稿箱
     */
    private void fabu_caogao() {
        Map<String, Object> params = new HashMap<>();
        Gson g = new Gson();
        String jsonString = g.toJson(need_update_iamge_method(upload_iamge_paths));
        params.put("img_data", jsonString);
        params.put("content", edit_text.getText().toString());
        if ("".equals(classification_name)) {
        } else {
            params.put("class_ids", classification_name);
        }
        params.put("app_version", Contacts.VersionName);
        params.put("draft_id", update_id);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("发布草稿参数", params.toString());
        Subscription subscription = Network.getInstance("发布图文", getApplicationContext())
                .fabu_caogao_content(params,
                        new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                            @Override
                            public void on_post_entity(Object backEntity) {
                                flag = "";
                                Logger.e("发布草稿成功：", "");
                                MyApplication.destoryActivity("CamareActivity");
                                MyApplication.destoryActivity("AddImagesActivity");
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                flag = "";
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                flag = "";
                                Logger.e("发布草稿失败：" + error);
                            }
                        }, this, false));
    }


    List<Map<String, String>> list = new ArrayList<>();

    private List<Map<String, String>> need_update_iamge_method
            (ArrayList<ImagePathEntity> iamge_paths) {
        for (int k = 0; k < images_local_paths.size(); k++) {//遍历本地图片路径
            for (ImagePathEntity imagePathEntity : iamge_paths) {//遍历网络图片路径
                Map<String, String> map = null;
                if (imagePathEntity.getNet_path().equals(images_local_paths.get(k))) {//如果是纯网络图片就直接添加,没有本地图片情况下
                    map = new HashMap<>();
                    map.put("path", imagePathEntity.getNet_path());
                    if(null!=imagePathEntity.getBili()){
                        map.put("size", imagePathEntity.getBili());
                        Log.e("网络图片的比例",imagePathEntity.getBili());
                    }
                } else {
                    if (images_local_paths.get(k).equals(imagePathEntity.getLocal_path())) {//根据本地图片选择顺序来判断，有本地图片情况下
                        map = new HashMap<>();
                        map.put("path", imagePathEntity.getNet_path());
                        //获取本地图片真实比例
                        int width = getImageWidthHeight(imagePathEntity.getLocal_path())[0];
                        int height = getImageWidthHeight(imagePathEntity.getLocal_path())[1];
                        if (width == height) {
                            map.put("size", "1:1");
                        } else {
                            map.put("size", "3:4");
                        }
                    }
                }
                if (null != map) {
                    list.add(map);
                }
            }

        }

        return list;
    }

    /**
     * 获取本地图片宽和高的比例
     *
     * @param path
     * @return
     */
    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
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
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
    }


    /**
     * 点击控件的加号按钮
     *
     * @param sortableNinePhotoLayout
     * @param view
     * @param position
     * @param models
     */
    @Override
    public void onClickAddNinePhotoItem(CCRSortableNinePhotoLayout
                                                sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        //首先销毁之前的CamareActivity
        Intent intent = new Intent(AddImagesActivity.this, CamareActivity.class);
        if (flag.equals("1") || flag.equals("5")) {//编辑
            ValueResources.enter_jia = "bianji_true";
        } else {
            ValueResources.enter_jia = "true";
        }
        //ValueResources.select_iamges_size = iamge_paths.size();
        CamareActivity.strings = iamge_paths;//把现在选择好的图片传过去
        CamareActivity.fabu_content = edit_text.getText().toString().replace(" ", "");


        startActivity(intent);
    }

    @Override
    public void onClickDeleteNinePhotoItem(CCRSortableNinePhotoLayout
                                                   sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);

    }

    @Override
    public void onClickNinePhotoItem(CCRSortableNinePhotoLayout sortableNinePhotoLayout, View
            view, int position, String model, ArrayList<String> models) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("数据库离开", "数据库离开" + flag);
        //添加虚拟键
        hideBottomUIMenu();
        //设置九宫格图片内容

        mPhotosSnpl.setData(iamge_paths);
        //设置发布内容
        if (CamareActivity.fabu_content.length() > 0) {
            edit_text.setText(CamareActivity.fabu_content);
        } else if (SelectLabelActivity.fabu_content.length() > 0) {
            edit_text.setText(SelectLabelActivity.fabu_content);
        }
        //设置标签
        if (classification_name.equals("")) {
            biaoqian_tv.setText(R.string.wenben13);
        } else {
            Log.e("放假", classification_name);
            biaoqian_tv.setText("# " + classification_name.replace(",", "#") + " #");
        }
    }

    /**
     * 是否保存草稿
     */
    Dialog dialog;

    private void select_save_popwindow() {
        hideBottomUIMenu();
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.select_save_layout, null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);

        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //影藏虚拟键
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        dialog.show();
        LinearLayout select_save = view.findViewById(R.id.select_save);
        select_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(AddImagesActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AddImagesActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    String content = edit_text.getText().toString().replace(" ", "");
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(getApplicationContext(), "发布内容不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (iamge_paths.size() <= 0) {
                        Toast.makeText(getApplicationContext(), "发布图片不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //说明是保存草稿
                        is_save_caogao = true;
                        //首先先上传图片
                        insertImagesSync((ArrayList<String>) iamge_paths);
                        dialog.dismiss();
                    }
                }
            }
        });
        LinearLayout select_no = view.findViewById(R.id.select_no);
        select_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    Dialog dialog2;

    private void select_save_popwindow2() {
        hideBottomUIMenu();
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.select_save_layout2, null);
        dialog2 = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog2.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog2.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog2.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //影藏虚拟键
        dialog2.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialog2.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        dialog2.show();
        LinearLayout select_no = view.findViewById(R.id.select_no);
        select_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                finish();
            }
        });
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }


    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        Log.e("虚拟键盘高度", result + "");
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


}
