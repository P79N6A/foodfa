package com.app.cookbook.xinhe.foodfamily.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
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
import com.app.cookbook.xinhe.foodfamily.main.entity.UploadImageEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.matisse.Matisse;
import com.app.cookbook.xinhe.foodfamily.util.matisse.MimeType;
import com.app.cookbook.xinhe.foodfamily.util.matisse.engine.impl.GlideEngine;
import com.app.cookbook.xinhe.foodfamily.util.matisse.filter.Filter;
import com.app.cookbook.xinhe.foodfamily.util.matisse.internal.entity.CaptureStrategy;
import com.app.cookbook.xinhe.foodfamily.util.matisse.listener.OnCheckedListener;
import com.app.cookbook.xinhe.foodfamily.util.matisse.listener.OnSelectedListener;
import com.app.cookbook.xinhe.foodfamily.util.ui.GifSizeFilter;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;
import com.seek.biscuit.Biscuit;
import com.seek.biscuit.CompressResult;
import com.seek.biscuit.OnCompressCompletedListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import hei.permission.PermissionActivity;
import jp.wasabeef.richeditor.RichEditor;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditerTestActivity extends PermissionActivity {
    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.action_undo)
    ImageView action_undo;
    @BindView(R.id.action_redo)
    ImageView action_redo;
    @BindView(R.id.action_bold)
    ImageView action_bold;
    @BindView(R.id.action_italic)
    ImageView action_italic;
    @BindView(R.id.action_subscript)
    ImageView action_subscript;
    @BindView(R.id.action_superscript)
    ImageView action_superscript;
    @BindView(R.id.action_strikethrough)
    ImageView action_strikethrough;
    @BindView(R.id.action_underline)
    ImageView action_underline;
    @BindView(R.id.action_heading1)
    ImageView action_heading1;
    @BindView(R.id.action_heading2)
    ImageView action_heading2;
    @BindView(R.id.action_heading3)
    ImageView action_heading3;
    @BindView(R.id.action_heading4)
    ImageView action_heading4;
    @BindView(R.id.action_heading5)
    ImageView action_heading5;
    @BindView(R.id.action_heading6)
    ImageView action_heading6;
    @BindView(R.id.action_txt_color)
    ImageView action_txt_color;
    @BindView(R.id.action_bg_color)
    ImageView action_bg_color;
    @BindView(R.id.action_indent)
    ImageView action_indent;
    @BindView(R.id.action_outdent)
    ImageView action_outdent;
    @BindView(R.id.action_align_left)
    ImageView action_align_left;
    @BindView(R.id.action_align_center)
    ImageView action_align_center;
    @BindView(R.id.action_align_right)
    ImageView action_align_right;
    @BindView(R.id.action_insert_bullets)
    ImageView action_insert_bullets;
    @BindView(R.id.action_insert_numbers)
    ImageView action_insert_numbers;
    @BindView(R.id.action_blockquote)
    ImageView action_blockquote;
    @BindView(R.id.action_insert_image)
    ImageView action_insert_image;
    @BindView(R.id.action_insert_link)
    ImageView action_insert_link;
    @BindView(R.id.action_insert_checkbox)
    ImageView action_insert_checkbox;
    private int max_num = 9;
    private int cureent_numl;
    private static final int REQUEST_CODE_CHOOSE = 23;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editer_test);
        ButterKnife.bind(this);


        mEditor.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        //mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(18);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(20, 10, 20, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("发布你的提问...");
        //mEditor.setInputEnabled(false);

        action_undo.setOnClickListener(onClickListener);
        action_redo.setOnClickListener(onClickListener);
        action_bold.setOnClickListener(onClickListener);
        action_italic.setOnClickListener(onClickListener);
        action_subscript.setOnClickListener(onClickListener);
        action_superscript.setOnClickListener(onClickListener);
        action_strikethrough.setOnClickListener(onClickListener);
        action_underline.setOnClickListener(onClickListener);
        action_heading1.setOnClickListener(onClickListener);
        action_heading2.setOnClickListener(onClickListener);
        action_heading3.setOnClickListener(onClickListener);
        action_heading4.setOnClickListener(onClickListener);
        action_heading5.setOnClickListener(onClickListener);
        action_heading6.setOnClickListener(onClickListener);
        action_txt_color.setOnClickListener(onClickListener);
        action_bg_color.setOnClickListener(onClickListener);
        action_indent.setOnClickListener(onClickListener);
        action_outdent.setOnClickListener(onClickListener);
        action_align_left.setOnClickListener(onClickListener);
        action_align_center.setOnClickListener(onClickListener);
        action_align_right.setOnClickListener(onClickListener);
        action_insert_bullets.setOnClickListener(onClickListener);
        action_insert_numbers.setOnClickListener(onClickListener);
        action_blockquote.setOnClickListener(onClickListener);
        action_insert_image.setOnClickListener(onClickListener);
        action_insert_link.setOnClickListener(onClickListener);
        action_insert_checkbox.setOnClickListener(onClickListener);

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
            // addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        max_num = 9;

    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        mEditor.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent mdata) {
        super.onActivityResult(requestCode, resultCode, mdata);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            insertImagesSync(mdata);
            Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(mdata)));
        }
    }

    Biscuit mBiscuit;
    private Subscription subsInsert;
    ProgressUtil progress_upload;
    Subscriber<? super String> subscriber;
    MyThread thread = new MyThread();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress_upload = new ProgressUtil();
            progress_upload.showProgressDialog(EditerTestActivity.this, "载入中...");

        }
    };
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            mEditor.insertImage(bundle.getString("url"), "");
        }
    };


    //可以终止的线程
    public class MyThread extends Thread {
        public void run() {
            handler.sendEmptyMessage(0);
            while (true) {
                if (this.isInterrupted()) {
                    //System.out.println("线程被停止了！");
                    progress_upload.dismissProgressDialog();
                    return;
                }
                //System.out.println("Time: " + System.currentTimeMillis());
            }
        }
    }

    int iamge_list_length = 0;
    int images_index = 0;

    private void insertImagesSync(final Intent data) {
        thread.start();
        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> msubscriber) {
                subscriber = msubscriber;
                try {
                    List<String> paths = Matisse.obtainPathResult(data);
                    ArrayList<String> array_path = new ArrayList<>();
                    array_path.addAll(paths);
                    for (int i = 0; i < array_path.size(); i++) {
                        Log.e("选择的图片地址", array_path.get(i));
                    }
                    //可以同时插入多张图片
                    mBiscuit = Biscuit.with(EditerTestActivity.this)
                            .path(array_path) //可以传入一张图片路径，也可以传入一个图片路径列表
                            .loggingEnabled(true)//是否输出log 默认输出
//                                          .quality(50)//质量压缩值（0...100）默认已经非常接近微信，所以没特殊需求可以不用自定义
                            .originalName(true) //使用原图名字来命名压缩后的图片，默认不使用原图名字,随机图片名字
                            .listener(new OnCompressCompletedListener() {
                                @Override
                                public void onCompressCompleted(CompressResult compressResult) {
                                    ArrayList arrayList = new ArrayList();
                                    arrayList.addAll(compressResult.mSuccessPaths);
                                    iamge_list_length = arrayList.size();
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        Log.e("111数据库飞机手榴弹", arrayList.size() + "");
                                        images_index = i;
                                        subscriber.onNext(arrayList.get(i).toString());
                                    }
                                }
                            })//压缩完成监听
                            .targetDir(Environment.getExternalStorageDirectory() + "/images/")//自定义压缩保存路径
//                          .executor(executor) //自定义实现执行，注意：必须在子线程中执行 默认使用AsyncTask线程池执行
//                          .ignoreAlpha(true)//忽略alpha通道，对图片没有透明度要求可以这么做，默认不忽略。
//                          .compressType(Biscuit.SAMPLE)//采用采样率压缩方式，默认是使用缩放压缩方式，也就是和微信的一样。
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
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("咖啡机克鲁赛德", "图片插入完成");
                        Toast.makeText(getApplicationContext(), "图片插入完成", Toast.LENGTH_SHORT).show();
                        //           mprogressUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("空间环境是否会对", e.getMessage());
                        Toast.makeText(getApplicationContext(), "图片插入失败", Toast.LENGTH_SHORT).show();
                        //        mprogressUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onNext(final String imagePath) {
                        Log.e("上传图片的本地地址", imagePath);
                        //et_new_content.insertImage(imagePath, et_new_content.getMeasuredWidth());
                        //请求阿里上传图片
                        upload_user_image("syjapppic", Network.bucketPath + StringsHelper.getUUID() + ".jpg", imagePath);
                        /*new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                HttpURLConnection conn = null;//声明连接对象
                                String urlStr = "https://app1.shiyujia.com/answerApi/qiniu/upload";
                                InputStream is = null;
                                String resultData = "";
                                try {
                                    URL url = new URL(urlStr); //URL对象
                                    conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                                    conn.setRequestMethod("GET"); //使用get请求

                                    if (conn.getResponseCode() == 200) {//返回200表示连接成功
                                        is = conn.getInputStream(); //获取输入流
                                        InputStreamReader isr = new InputStreamReader(is);
                                        BufferedReader bufferReader = new BufferedReader(isr);
                                        String inputLine = "";
                                        while ((inputLine = bufferReader.readLine()) != null) {
                                            resultData += inputLine + "\n";
                                        }
                                        JSONObject object = new JSONObject(resultData);
                                        String code = object.getString("code");
                                        if ("1".equals(code)) {
                                            JSONObject data = object.getJSONObject("data");
                                            String token = data.getString("token");
                                            ToKen = token;
                                            // Toast.makeText(MainActivity.this, "       获取Token     " + token, Toast.LENGTH_SHORT).show();
                                            Log.e("123", "        get方法取回内容：" + token + "  ----->  " + resultData);
                                        }
                                    }
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("iamge_path",imagePath);
                                    message.setData(bundle);
                                    post_image_handel.sendMessage(message);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();*/
                    }
                });
    }

    private String uploud_iamge;
    ArrayList<UploadImageEntity> upload_iamge_paths = new ArrayList<>();

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
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
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

        /*//多张图片同步上传
        // 文件元信息的设置是可选的
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("application/octet-stream"); // 设置content-type
        // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
        // put.setMetadata(metadata);

        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
            Log.e("PutObject", "上传图片成功");
            //progressUtil.dismissProgressDialog();
            uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
            //Log.e("上传图片地址:", uploud_iamge);
            UploadImageEntity uploadImageEntity = new UploadImageEntity();
            uploadImageEntity.setIamge_path(uploadFilePath);
            uploadImageEntity.setUpload_iamge_path(uploud_iamge);
            upload_iamge_paths.add(uploadImageEntity);
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("图片当前上传进度：", "currentSize: " + currentSize + " 图片总上传进度: " + totalSize);
                //开始loading
                //progress_upload.showProgressDialog(LijiHuidaActivity.this, "上传中...");
            }
        });
        */

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "上传图片成功");
                //progressUtil.dismissProgressDialog();
                uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                Log.e("数据库法律", uploud_iamge);

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", uploud_iamge);
                        message.setData(bundle);
                        handler1.sendMessage(message);
                    }
                }.start();

                if (images_index == iamge_list_length - 1) {
                    subscriber.onCompleted();
                    try {
                        Thread.sleep(500);
                        thread.interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
        // task.cancel(); // 可以取消任务
        task.waitUntilFinished(); //可以等待直到任务完成

    }


    private boolean isChanged;
    private boolean bgisChanged;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_undo:
                    mEditor.undo();
                    break;
                case R.id.action_redo:
                    mEditor.redo();
                    break;
                case R.id.action_bold:
                    mEditor.setBold();
                    break;
                case R.id.action_italic:
                    mEditor.setItalic();
                    break;
                case R.id.action_subscript:
                    mEditor.setSubscript();
                    break;
                case R.id.action_superscript:
                    mEditor.setSuperscript();
                    break;
                case R.id.action_strikethrough:
                    mEditor.setStrikeThrough();
                    break;
                case R.id.action_underline:
                    mEditor.setUnderline();
                    break;
                case R.id.action_heading1:
                    mEditor.setHeading(1);
                    break;
                case R.id.action_heading2:
                    mEditor.setHeading(2);
                    break;
                case R.id.action_heading3:
                    mEditor.setHeading(3);
                    break;
                case R.id.action_heading4:
                    mEditor.setHeading(4);
                    break;
                case R.id.action_heading5:
                    mEditor.setHeading(5);
                    break;
                case R.id.action_heading6:
                    mEditor.setHeading(6);
                    break;
                case R.id.action_txt_color:
                    mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                    isChanged = !isChanged;
                    break;
                case R.id.action_bg_color:
                    mEditor.setTextBackgroundColor(bgisChanged ? Color.TRANSPARENT : Color.YELLOW);
                    bgisChanged = !bgisChanged;
                    break;
                case R.id.action_indent:
                    mEditor.setIndent();
                    break;
                case R.id.action_outdent:
                    mEditor.setOutdent();
                    break;
                case R.id.action_align_left:
                    mEditor.setAlignLeft();
                    break;
                case R.id.action_align_center:
                    mEditor.setAlignCenter();
                    break;
                case R.id.action_align_right:
                    mEditor.setAlignRight();
                    break;
                case R.id.action_insert_bullets:
                    mEditor.setBullets();
                    break;
                case R.id.action_insert_numbers:
                    mEditor.setNumbers();
                    break;
                case R.id.action_blockquote:
                    mEditor.setBlockquote();
                    break;
                case R.id.action_insert_image:
//                    mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
//                            "dachshund");
                    checkPermission(new CheckPermListener() {
                        @Override
                        public void superPermission() {
                            // 如果拥有权限的话，就让其插入图片
                            cureent_numl = 0;
                            //遍历图片数量设置index

                            /*List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
                            for (int i = 0; i < editList.size(); i++) {
                                RichTextEditor.EditData itemData = editList.get(i);
                                if (itemData.imagePath != null) {
                                    cureent_numl++;
                                }
                            }*/

                            String reg = "<img.*?>";
                            Pattern pattern = Pattern.compile(reg);
                            if (null != mEditor.getHtml()) {
                                Matcher matcher = pattern.matcher(mEditor.getHtml());//sendString为网页源码
                                //使用find()方法查找第一个匹配的对象
                                boolean result = matcher.find();
                                //使用循环找出 html里所有的img标签
                                while (result) {
                                    //继续查找下一个匹配对象
                                    System.out.println("img标签===》" + matcher.group());
                                    result = matcher.find();
                                    cureent_numl++;
                                }
                            }


                            if (cureent_numl == max_num) {//已经满了9张
                                cureent_numl = 9;
                            } else if (cureent_numl < max_num) {
                                max_num = max_num - cureent_numl;
                            }

                            if (cureent_numl == 9) {
                                Toast.makeText(getApplicationContext(), "只能上传9张图片哦～", Toast.LENGTH_SHORT).show();
                            } else if (cureent_numl < 9) {
                                Observable.create(new Observable.OnSubscribe<String>() {
                                    @Override
                                    public void call(Subscriber<? super String> subscriber) {
                                        subscriber.onNext("");
                                    }
                                }).subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Matisse.from(EditerTestActivity.this)
                                                .choose(MimeType.ofImage(), false)//显示图片的类型
                                                .countable(true)//是否有序选择图片
                                                .showSingleMediaType(true)//仅仅显示一种媒体类型
                                                .thumbnailScale(0.85f)//缩放比例
                                                .capture(true)
                                                .captureStrategy(
                                                        new CaptureStrategy(true, "com.app.cookbook.xinhe.foodfamily.fileprovider"))
                                                .maxSelectable(max_num)//最大的选择数量
                                                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))//添加过滤器
                                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//	每个图片方格的大小
                                                .thumbnailScale(0.85f)
                                                .imageEngine(new GlideEngine())//使用图片的加载方式
                                                .setOnSelectedListener(new OnSelectedListener() {
                                                    @Override
                                                    public void onSelected(
                                                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                                    }
                                                })
                                                .originalEnable(true)
                                                .maxOriginalSize(5)
                                                .setOnCheckedListener(new OnCheckedListener() {
                                                    @Override
                                                    public void onCheck(boolean isChecked) {
                                                        // DO SOMETHING IMMEDIATELY HERE
                                                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                                                    }
                                                })
                                                .forResult(REQUEST_CODE_CHOOSE);
                                    }
                                });
                            }
                        }
                    }, R.string.quanxian, PERMISSIONS);

                    break;
                case R.id.action_insert_link:
                    mEditor.insertLink("https://www.baidu.com/", "百度");
                    break;
                case R.id.action_insert_checkbox:
                    mEditor.insertTodo();
                    break;


            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEditor.destroy();//销魂webview
    }
}
