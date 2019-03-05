package com.app.cookbook.xinhe.foodfamily.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AliEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberM;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.matisse.Matisse;
import com.app.cookbook.xinhe.foodfamily.util.matisse.MimeType;
import com.app.cookbook.xinhe.foodfamily.util.matisse.engine.impl.GlideEngine;
import com.app.cookbook.xinhe.foodfamily.util.matisse.filter.Filter;
import com.app.cookbook.xinhe.foodfamily.util.matisse.internal.entity.CaptureStrategy;
import com.app.cookbook.xinhe.foodfamily.util.matisse.listener.OnCheckedListener;
import com.app.cookbook.xinhe.foodfamily.util.matisse.listener.OnSelectedListener;
import com.app.cookbook.xinhe.foodfamily.util.toast.RunBeyToast;
import com.app.cookbook.xinhe.foodfamily.util.ui.GifSizeFilter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hei.permission.PermissionActivity;
import rx.Observable;
import rx.Subscriber;

public class PerfectInformationActivity extends BaseActivity {

    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //跳过
    @BindView(R.id.jump_over)
    TextView jump_over;
    //头像总布局
    @BindView(R.id.hand_layout)
    FrameLayout hand_layout;
    //头像控件
    @BindView(R.id.touxiang_image)
    CircleImageView touxiang_image;
    //性别男未选
    @BindView(R.id.man_sex_off)
    ImageView man_sex_off;
    //性别女未选
    @BindView(R.id.woman_sex_off)
    ImageView woman_sex_off;
    //性别男已选
    @BindView(R.id.man_sex_on)
    ImageView man_sex_on;
    //性别女已选
    @BindView(R.id.woman_sex_on)
    ImageView woman_sex_on;
    //昵称
    @BindView(R.id.user_name)
    EditText user_name;
    //签名
    @BindView(R.id.user_sig)
    EditText user_sig;
    //职业
    @BindView(R.id.user_job)
    EditText user_job;
    //确定
    @BindView(R.id.ok_btn)
    TextView ok_btn;
    private File cacheFile;
    private String cachPath;
    private String uploud_iamge;
    private File cameraFile;
    private static final int CROP_PHOTO = 1003;
    private Bitmap headBitmap;
    //选择性别
    private String sexType = "0";


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_perfect_information);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        //打开相册选择头像
        cachPath = getDiskCacheDir(getApplicationContext()) + "/head_image.jpg";
        cacheFile = getCacheFile(new File(getDiskCacheDir(getApplicationContext())), "head_image.jpg");
        jump_over.setOnClickListener(this);
        hand_layout.setOnClickListener(this);
        man_sex_off.setOnClickListener(this);
        woman_sex_off.setOnClickListener(this);
        man_sex_on.setOnClickListener(this);
        woman_sex_on.setOnClickListener(this);
        ok_btn.setOnClickListener(this);

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法
                if (!"0".equals(sexType) && !TextUtils.isEmpty(s) && !TextUtils.isEmpty(uploud_iamge)) {
                    ok_btn.setBackgroundResource(R.drawable.login_btn_bg);
                } else {
                    ok_btn.setBackgroundResource(R.drawable.guide_btn_bg2);
                }
                if (s.length() == 18) {
                    RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多18个字"
                            , R.color.black, 255
                    );
                    runBeyToast.show();
                }
            }
        });

        user_sig.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法
                if (s.length() == 20) {
                    RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多20个字"
                            , R.color.black, 255
                    );
                    runBeyToast.show();
                }
            }
        });
        user_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法
                if (s.length() == 10) {
                    RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多10个字"
                            , R.color.black, 255
                    );
                    runBeyToast.show();
                }
            }
        });
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.jump_over:
                Contacts.typeMeg = "1";
                finish();
                break;
            case R.id.hand_layout:
                seceleImage();
                break;
            case R.id.man_sex_off:
                sexType = "1";
                man_sex_on.setVisibility(View.VISIBLE);
                man_sex_off.setVisibility(View.GONE);
                woman_sex_on.setVisibility(View.GONE);
                woman_sex_off.setVisibility(View.VISIBLE);
                if (!"0".equals(sexType) && !TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(uploud_iamge)) {
                    ok_btn.setBackgroundResource(R.drawable.login_btn_bg);
                }
                break;
            case R.id.woman_sex_off:
                sexType = "2";
                woman_sex_on.setVisibility(View.VISIBLE);
                woman_sex_off.setVisibility(View.GONE);
                man_sex_on.setVisibility(View.GONE);
                man_sex_off.setVisibility(View.VISIBLE);
                if (!"0".equals(sexType) && !TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(uploud_iamge)) {
                    ok_btn.setBackgroundResource(R.drawable.login_btn_bg);
                }
                break;
            case R.id.man_sex_on:
                sexType = "0";
                man_sex_on.setVisibility(View.GONE);
                man_sex_off.setVisibility(View.VISIBLE);
                woman_sex_on.setVisibility(View.GONE);
                woman_sex_off.setVisibility(View.VISIBLE);
                if ("0".equals(sexType) || TextUtils.isEmpty(user_name.getText().toString()) || TextUtils.isEmpty(uploud_iamge)) {
                    ok_btn.setBackgroundResource(R.drawable.guide_btn_bg2);
                }
                break;
            case R.id.woman_sex_on:
                sexType = "0";
                woman_sex_on.setVisibility(View.GONE);
                woman_sex_off.setVisibility(View.VISIBLE);
                man_sex_on.setVisibility(View.GONE);
                man_sex_off.setVisibility(View.VISIBLE);
                if ("0".equals(sexType) || TextUtils.isEmpty(user_name.getText().toString()) || TextUtils.isEmpty(uploud_iamge)) {
                    ok_btn.setBackgroundResource(R.drawable.guide_btn_bg2);
                }
                break;
            case R.id.ok_btn:
                if (!"0".equals(sexType) && !TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(uploud_iamge)) {
                    String name = user_name.getText().toString();
                    String personal_signature = user_sig.getText().toString();
                    String profeddion = user_job.getText().toString();
                    delete_daan_submit(uploud_iamge, name, sexType, personal_signature, profeddion);
                } else {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //完善个人信息
    private void delete_daan_submit(String avatar, String name, String sex, String personal_signature, String profession) {
        Map<String, String> params = new HashMap<>();
        params.put("avatar", avatar);
        params.put("name", name);
        params.put("sex", sex);
        params.put("personal_signature", personal_signature);
        params.put("profession", profession);
        Log.e("123", "    完善个人信息：   " + params.toString());
        subscription = Network.getInstance("完善个人信息", this)
                .user_data_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List mlocation) {
                                Log.e("123", "完善个人信息成功：");
                                Contacts.typeMeg = "1";
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "完善个人信息报错：" + error);
                                Toast.makeText(PerfectInformationActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    private void seceleImage() {
        //动态申请权限
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                //动态申请权限
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
                        Matisse.from(PerfectInformationActivity.this)
                                .choose(MimeType.ofImage(), false)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        new CaptureStrategy(true, "com.app.cookbook.xinhe.foodfamily.fileprovider"))
                                .maxSelectable(1)

                                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                .gridExpectedSize(
                                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .thumbnailScale(0.85f)
                                .imageEngine(new GlideEngine())
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
        }, R.string.quanxian, PERMISSIONS);

    }


    private File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisse.obtainPathResult(data);
            for (int k = 0; k < paths.size(); k++) {
                cameraFile = new File(paths.get(k));
                if (!cameraFile.getParentFile().exists()) {
                    cameraFile.getParentFile().mkdirs();
                }
            }
            startPhotoZoom(cameraFile, 350);

        }

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case CROP_PHOTO:
                Log.e("进来了打开相机", "进来了打开相机");
                try {
                    if (resultCode == RESULT_OK) {
                        headBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
                    }
                    if (headBitmap != null) {
                        touxiang_image.setImageBitmap(null);
                        touxiang_image.setImageBitmap(headBitmap);
                        Log.e("本地图片地址", cachPath);
                        get_aliyun_params(cachPath);
                        if (!"0".equals(sexType) && !TextUtils.isEmpty(user_name.getText().toString())) {
                            ok_btn.setBackgroundResource(R.drawable.login_btn_bg);
                        } else {
                            ok_btn.setBackgroundResource(R.drawable.guide_btn_bg2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    /**
     * 得到阿里返回的数据字段
     *
     * @param image_path
     */
    private void get_aliyun_params(final String image_path) {
        String net_url = Network.main_url + "/aliyun/sts-servers/sts.php?userid=1";
        Logger.e("获取阿里云的数据url" + net_url);

        subscription = Network.getInstance("获取阿里云的数据", getApplicationContext())
                .get_ali(net_url, new ProgressSubscriberM<>(new SubscriberOnNextListener<AliEntity>() {
                    @Override
                    public void onNext(AliEntity result) {
                        Logger.e("获取阿里云的数据成功");
                        //上传头像
                        upload_user_image(result.getAccessKeyId()
                                , result.getAccessKeySecret()
                                , result.getSecurityToken()
                                , "syjapppic",
                                Network.bucketPath + "users/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg"
                                , image_path);

                    }


                    @Override
                    public void onError(String error) {
                        Logger.e("获取阿里云的数据失败");
                        Toast.makeText(PerfectInformationActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }


    /**
     * 上传图片到阿里服务器
     *
     * @param accessKeyId
     * @param secretKeyId
     * @param securityToken
     * @param bucketName
     * @param objectKey
     * @param uploadFilePath
     */
    private void upload_user_image(
            String accessKeyId
            , String secretKeyId
            , String securityToken
            , String bucketName
            , final String objectKey
            , String uploadFilePath) {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 在移动端建议使用STS方式初始化OSSClient。
        OSSCredentialProvider credentialProvider =
                new OSSStsTokenCredentialProvider(accessKeyId,
                        secretKeyId,
                        securityToken);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(
                bucketName,
                objectKey,
                uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "上传图片成功");
                uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                Log.e("上传头像地址:", uploud_iamge);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    Log.e("sadjfkljdkf", "dsfa");
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String uriToPath(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(getApplicationContext(), file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 360);
            intent.putExtra("outputY", 360);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 打开相册弹窗
     */
    private static final int REQUEST_CODE_CHOOSE = 23;
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void createView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.user_header_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
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
        dialog.show();

        LinearLayout btn_picture = (LinearLayout) window.findViewById(R.id.btn_picture);
        LinearLayout btn_photo = (LinearLayout) window.findViewById(R.id.btn_photo);
        LinearLayout btn_cancle = (LinearLayout) window.findViewById(R.id.select_dismiss);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_picture.setOnClickListener(new View.OnClickListener() {// 图库
            @Override
            public void onClick(View v) {
                checkPermission(new PermissionActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        //动态申请权限
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
                                Matisse.from(PerfectInformationActivity.this)
                                        .choose(MimeType.ofImage(), false)
                                        .countable(true)
                                        .capture(true)
                                        .captureStrategy(
                                                new CaptureStrategy(true, "com.app.cookbook.xinhe.foodfamily.fileprovider"))
                                        .maxSelectable(1)

                                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                        .gridExpectedSize(
                                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine())
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

                        dialog.dismiss();
                    }
                }, R.string.quanxian, PERMISSIONS);


            }
        });
        btn_photo.setOnClickListener(
                new View.OnClickListener() {// 相机
                    @Override
                    public void onClick(View v) {

                        checkPermission(new PermissionActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
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
                                        Matisse.from(PerfectInformationActivity.this)
                                                .choose(MimeType.ofImage(), false)
                                                .countable(true)
                                                .capture(true)
                                                .captureStrategy(
                                                        new CaptureStrategy(true, "com.app.cookbook.xinhe.foodfamily.fileprovider"))
                                                .maxSelectable(1)

                                                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                                .gridExpectedSize(
                                                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                                .thumbnailScale(0.85f)
                                                .imageEngine(new GlideEngine())
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
                                dialog.dismiss();
                            }
                        }, R.string.quanxian, PERMISSIONS);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Contacts.typeMeg = "1";
            finish();
        }
        return false;
    }

}
