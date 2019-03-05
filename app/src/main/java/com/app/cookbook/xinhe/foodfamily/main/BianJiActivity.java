package com.app.cookbook.xinhe.foodfamily.main;

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
import android.view.inputmethod.InputMethodManager;
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
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.entity.AliEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.UserEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberM;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.OnHomeKeyPressListener;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.toast.RunBeyToast;
import com.app.cookbook.xinhe.foodfamily.util.ui.CustomPopupWindow;
import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BianJiActivity extends BaseActivity implements OnHomeKeyPressListener {
    @BindView(R.id.select_head_image)
    CircleImageView select_head_image;
    @BindView(R.id.xingbie_layout)
    RelativeLayout xingbie_layout;
    @BindView(R.id.sex_value)
    TextView sex_value;
    @BindView(R.id.iamge_back)
    ImageView back_layout;
    @BindView(R.id.wentifankui)
    RelativeLayout wentifankui;
    @BindView(R.id.touxiang_layout)
    RelativeLayout touxiang_layout;
    @BindView(R.id.save_btn)
    TextView save_btn;
    @BindView(R.id.edit_nicheng)
    EditText edit_nicheng;
    @BindView(R.id.qianming_edit)
    EditText qianming_edit;
    @BindView(R.id.zhiye_edit)
    EditText zhiye_edit;
    @BindView(R.id.btn_exit)
    RelativeLayout btn_exit;
    @BindView(R.id.setting_layout)
    RelativeLayout setting_layout;

    private File cacheFile;
    private String cachPath;
    private Bitmap headBitmap;

    private File cameraFile;

    private static final int CROP_PHOTO = 1003;

    private String uploud_iamge;

    public static String qianming_name = "";
    public static String zhiye_name = "";
    public static String nicheng_name = "";
    public static String sex_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_bian_ji);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        back_layout.setFocusable(true);
        back_layout.setFocusableInTouchMode(true);
        back_layout.requestFocus();
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {
        get_user_information();
        //打开相册选择头像
        cachPath = getDiskCacheDir(getApplicationContext()) + "/head_image.jpg";
        cacheFile = getCacheFile(new File(getDiskCacheDir(getApplicationContext())), "head_image.jpg");

        //选择相片
        touxiang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPhotos.createAlbum(BianJiActivity.this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                        .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                        .setPuzzleMenu(false)
                        .setCount(1)
                        .setOriginalMenu(false, true, null)
                        .start(101);
            }
        });

        //选择性别
        xingbie_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_sex_popwindow();
            }
        });
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                finish();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String name = edit_nicheng.getText().toString();
//                        String qianming = qianming_edit.getText().toString();
//                        String zhiye = zhiye_edit.getText().toString();
//                        if (!nicheng_name.equals(name) || !qianming_name.equals(qianming) || !zhiye.equals(zhiye_name) || !TextUtils.isEmpty(sexType) || !TextUtils.isEmpty(uploud_iamge)) {
//                            PopWindowHelper.public_tishi_pop(BianJiActivity.this, "", "是否保存修改资料？", "放弃", "保存", new DialogCallBack() {
//                                @Override
//                                public void save() {
//                                    save_information();
//                                }
//
//                                @Override
//                                public void cancel() {
//                                    finish();
//                                }
//                            });
//                        } else {
//                            finish();
//                        }
//                    }
//                }, 500);//1秒后取消dialog和弹出分享框
            }
        });

        //问题反馈页面
        wentifankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WenTiActivity.class);
            }
        });

        /**
         * 点击保存信息
         */
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_information();
            }
        });

        //退出登录
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow.public_tishi_pop(BianJiActivity.this,
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
        /**
         * 设置
         */
        setting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    private void setTextChanged() {
        edit_nicheng.addTextChangedListener(new TextWatcher() {
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
                if (s.length() == 18) {
                    RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多18个字"
                            , R.color.black, 255
                    );
                    runBeyToast.show();
                }
            }
        });
        qianming_edit.addTextChangedListener(new TextWatcher() {
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
        zhiye_edit.addTextChangedListener(new TextWatcher() {
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

    private void get_user_information() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", this)
                .get_user_information(params,
                        new ProgressSubscriberNew<>(UserEntity.class, new GsonSubscriberOnNextListener<UserEntity>() {
                            @Override
                            public void on_post_entity(UserEntity userEntity) {
                                Log.e("123","获取用户信息成功：");
                                //设置页面的信息
                                set_user_information(userEntity);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","获取用户信息报错：" + error);
                            }
                        }, this, true));
    }


    private void set_user_information(UserEntity data) {
        if (null != data.getAvatar() && !TextUtils.isEmpty(data.getAvatar())) {
            //设置头像
            Glide.with(this).load(data.getAvatar())
                    .error(R.drawable.touxiang)
                    .into(select_head_image);
        }
        if (null != data.getName() && !TextUtils.isEmpty(data.getName())) {
            nicheng_name = data.getName();
            edit_nicheng.setText(nicheng_name);
            edit_nicheng.setSelection(nicheng_name.length());
        }
        if (null != data.getPersonal_signature() && !TextUtils.isEmpty(data.getPersonal_signature())) {
            qianming_name = data.getPersonal_signature();
            qianming_edit.setText(qianming_name);
            qianming_edit.setSelection(qianming_name.length());
        } else {
            qianming_edit.setText("");
        }
        if (null != data.getProfession() && !TextUtils.isEmpty(data.getProfession())) {
            zhiye_name = data.getProfession();
            zhiye_edit.setText(zhiye_name);
            zhiye_edit.setSelection(zhiye_name.length());
        } else {
            zhiye_edit.setText("");
        }
        sex_name = String.valueOf(data.getSex());
        if (data.getSex() == 1) {//男
            sex_value.setVisibility(View.VISIBLE);
            sex_value.setText("男");
        } else if (data.getSex() == 2) {
            sex_value.setVisibility(View.VISIBLE);
            sex_value.setText("女");
        } else {
            sex_value.setText("未说明");
        }
        setTextChanged();
    }

    private void ClearSource() {
        SharedPreferencesHelper.remove(getApplicationContext(), "login_token");
        startActivity(LoginActivity.class);
        finish();

    }

    //保存用户信息
    private void save_information() {
        if ("".equals(edit_nicheng.getText().toString())) {
            Toast.makeText(getApplicationContext(), "昵称不能为空！", Toast.LENGTH_SHORT).show();
        } else if ("".equals(sex_value.getText().toString())) {
            Toast.makeText(getApplicationContext(), "性别不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //请求网络设置个人资料
            Set_UserInformation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        qianming_edit.setText(qianming_name);
//        edit_nicheng.setText(nicheng_name);
//        zhiye_edit.setText(zhiye_name);

    }

    /**
     * 设置用户信息
     */
    private void Set_UserInformation() {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(uploud_iamge)) {
            params.put("avatar", uploud_iamge);
        }
        if (!nicheng_name.equals(edit_nicheng.getText().toString())) {
            params.put("name", edit_nicheng.getText().toString());
        }
        if (!qianming_name.equals(qianming_edit.getText().toString())) {
            params.put("personal_signature", qianming_edit.getText().toString());
        }
        if (!zhiye_name.equals(zhiye_edit.getText().toString())) {
            params.put("profession", zhiye_edit.getText().toString());
        }
        if (!sex_name.equals(sexType)) {
            params.put("sex", sexType);
        }
        Log.e("设置个人资料传递参数：", params.toString());
        subscription = Network.getInstance("消息页面获取通知界面", getApplicationContext())
                .set_information(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List result) {
                                Toast.makeText(getApplicationContext(), "已保存！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","消息页面获取通知界面报错：" + error);
                            }
                        }, this, true));
    }

    /**
     * 选择性别弹窗
     */
    private String sexType = "";

    private void select_sex_popwindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.select_sex_layout, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
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

        //设置逻辑交互
        LinearLayout select_nan = view.findViewById(R.id.select_nan);
        select_nan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexType = "1";
                sex_value.setText("男");
                dialog.dismiss();
            }
        });
        LinearLayout select_nv = view.findViewById(R.id.select_nv);
        select_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexType = "2";
                sex_value.setText("女");
                dialog.dismiss();
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

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //相册回调
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                cameraFile = new File(resultPaths.get(0));
                if (!cameraFile.getParentFile().exists()) {
                    cameraFile.getParentFile().mkdirs();
                }
                startPhotoZoom(cameraFile, 350);
                return;
            }


        } else if (RESULT_CANCELED == resultCode) {
            Log.e("关闭了相册", "关闭了相册");
            //Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }

        switch (requestCode) {
            case CROP_PHOTO:
                Log.e("进来了打开相机", "进来了打开相机");
                try {
                    if (resultCode == RESULT_OK) {
                        headBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
                    }
                    if (headBitmap != null) {
                        select_head_image.setImageBitmap(null);
                        select_head_image.setImageBitmap(headBitmap);
                        Log.e("本地图片地址", cachPath);
                        get_aliyun_params(cachPath);

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
        Log.e("123","获取阿里云的数据url" + net_url);

        subscription = Network.getInstance("获取阿里云的数据", getApplicationContext())
                .get_ali(net_url, new ProgressSubscriberM<>(new SubscriberOnNextListener<AliEntity>() {
                    @Override
                    public void onNext(AliEntity result) {
                        Log.e("123","获取阿里云的数据成功");
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
                        Log.e("123","获取阿里云的数据失败");
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
        // 更多信息可查看sample 中 sts 使用方式(https://github.com/aliyun/aliyun-oss-android-sdk/tree/master/app/src/main/java/com/alibaba/sdk/android/oss/app)
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
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
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
        task.waitUntilFinished();
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

    @Override
    public void widgetClick(View v) {
        PopWindowHelper.public_tishi_pop(BianJiActivity.this, "食与家提示", "是否保存修改资料？", "放弃", "保存", new DialogCallBack() {
            @Override
            public void save() {
                save_information();
            }

            @Override
            public void cancel() {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 把当前Activity运行至后台
            //save_information();
            // 隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            finish();
//            String name = edit_nicheng.getText().toString();
//            String qianming = qianming_edit.getText().toString();
//            String zhiye = zhiye_edit.getText().toString();
//            if (!nicheng_name.equals(name) || !qianming_name.equals(qianming) || !zhiye.equals(zhiye_name) || !TextUtils.isEmpty(sexType) || !TextUtils.isEmpty(uploud_iamge)) {
//                PopWindowHelper.public_tishi_pop(BianJiActivity.this, "食与家提示", "是否保存修改资料？", "放弃", "保存", new DialogCallBack() {
//                    @Override
//                    public void save() {
//                        save_information();
//                    }
//
//                    @Override
//                    public void cancel() {
//                        finish();
//                    }
//                });
//            } else {
//                finish();
//            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //监听HOME键
    @Override
    public void onHomeKeyPress() {

    }

    @Override
    public void onRecentApps() {

    }

    @Override
    public void onBackPressed() {

    }


    /**
     * 打开相册弹窗
     */
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
                EasyPhotos.createAlbum(BianJiActivity.this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                        .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                        .setPuzzleMenu(false)
                        .setCount(1)
                        .setOriginalMenu(false, true, null)
                        .start(101);

                dialog.dismiss();
            }
        });
        btn_photo.setOnClickListener(
                new View.OnClickListener() {// 相机
                    @Override
                    public void onClick(View v) {
                        EasyPhotos.createAlbum(BianJiActivity.this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                                .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                                .setPuzzleMenu(false)
                                .setCount(1)
                                .setOriginalMenu(false, true, null)
                                .start(101);

                        dialog.dismiss();
                    }
                });
    }
}