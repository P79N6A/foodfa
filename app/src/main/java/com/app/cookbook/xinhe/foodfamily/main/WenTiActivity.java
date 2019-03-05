package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.app.cookbook.xinhe.foodfamily.main.entity.AdviceLeixing;
import com.app.cookbook.xinhe.foodfamily.main.entity.AliEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberM;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.SystemUtil;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.HorizontalListView;
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


public class WenTiActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.edit_context)
    EditText edit_context;
    @BindView(R.id.edit_number)
    EditText edit_number;
    @BindView(R.id.btn_tijiao)
    RelativeLayout btn_tijiao;
    @BindView(R.id.select_tv1)
    TextView select_tv1;
    @BindView(R.id.select_tv2)
    TextView select_tv2;
    @BindView(R.id.select_tv3)
    TextView select_tv3;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.camare_grid)
    HorizontalListView camare_grid;
    List<String> upload_image_list = new ArrayList<>();
    private CheckBox[] checkBoxes = new CheckBox[3];
    //private File cacheFile;
    //private String cachPath;

    private File cameraFile;
    private static final int CROP_PHOTO = 1003;
    private String uploud_iamge;


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
        setContentLayout(R.layout.activity_wen_ti);
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

    CamareAdapter camareAdapter;

    @Override
    public void doBusiness(Context mContext) {
        //打开相册选择头像
        iamge_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(edit_context.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "反馈内容不能为空！", Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    //请求网络登录
                    SubmitContent();
                }
            }
        });
        checkBoxes[0] = (CheckBox) findViewById(R.id.checkbox);
        checkBoxes[1] = (CheckBox) findViewById(R.id.checkbox2);
        checkBoxes[2] = (CheckBox) findViewById(R.id.checkbox3);

        checkBoxes[0].setOnCheckedChangeListener(this);
        checkBoxes[1].setOnCheckedChangeListener(this);
        checkBoxes[2].setOnCheckedChangeListener(this);

        initFankuiLeiXing();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        camareAdapter = new CamareAdapter(getApplicationContext(), upload_image_list);
        camare_grid.setAdapter(camareAdapter);

    }


    List<AdviceLeixing> adviceLeixings = new ArrayList<>();

    private void initFankuiLeiXing() {
        subscription = Network.getInstance("获取用户类型", getApplicationContext())
                .get_user_leixing(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<AdviceLeixing>>>() {
                    @Override
                    public void onNext(Bean<List<AdviceLeixing>> result) {
                        Logger.e("用户类型成功：");
                        adviceLeixings = result.getData();
                        select_tv1.setText(adviceLeixings.get(0).getName());
                        select_tv2.setText(adviceLeixings.get(1).getName());
                        select_tv3.setText(adviceLeixings.get(2).getName());

                    }

                    @Override
                    public void onError(String error) {
                        Logger.e("用户类型失败：" + error);

                    }
                }, this, true));
    }



    StringBuffer stringBuffer = new StringBuffer();
    private void SubmitContent() {
        Map<String, Object> params = new HashMap<>();
        params.put("controller", "2");
        //获取手机型号
        params.put("model", SystemUtil.getSystemModel());
        //获取系统版本
        params.put("phone_version", SystemUtil.getSystemVersion());
        //获取APP版本
        params.put("app_version", SystemUtil.getVersionName(getApplicationContext()));
        params.put("content", edit_context.getText().toString());
        if ("".equals(edit_context.getText().toString())) {
        } else {
            params.put("tel", edit_number.getText().toString());
        }
        stringBuffer.setLength(0);
        //参数值必须为数组
        for (int i = 0; i < upload_image_list.size(); i++) {
            if (i == upload_image_list.size() - 1) {
                stringBuffer.append(upload_image_list.get(i));
            } else {
                stringBuffer.append(upload_image_list.get(i)).append(",");
            }
        }
        params.put("path", stringBuffer.toString());

        if (checkBoxes[0].isChecked()) {
            params.put("type_pid", "1");
        } else if (checkBoxes[1].isChecked()) {
            params.put("type_pid", "2");
        } else if (checkBoxes[2].isChecked()) {
            params.put("type_pid", "3");
        } else {
            params.put("type_pid", "4");
        }

        Log.e("反馈过去的参数", params.toString());
        subscription = Network.getInstance("消息页面获取通知界面", WenTiActivity.this)
                .fankui(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Logger.e("用户反馈成功：");
                                Toast.makeText(getApplicationContext(), "用户反馈成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("用户反馈失败：" + error);
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, WenTiActivity.this, true));
    }

    class CamareAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater mInflater;
        List<String> upload_list = new ArrayList<>();

        public CamareAdapter(Context mcontext, List<String> mupload_list) {
            context = mcontext;
            upload_list = mupload_list;
            mInflater = LayoutInflater.from(mcontext);
        }

        @Override
        public int getCount() {
            return upload_list.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return upload_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void updateItemsData(List<String> list) {
            this.upload_list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.upload_image_item, parent, false);
                holder.upload_image = convertView.findViewById(R.id.upload_image);
                holder.delete_iamge = convertView.findViewById(R.id.delete_iamge);
                holder.delete_view = convertView.findViewById(R.id.delete_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (upload_list.size() == 3) {
                if (position == 3) {
                    holder.delete_iamge.setVisibility(View.INVISIBLE);
                    holder.upload_image.setVisibility(View.GONE);
                } else {
                    Glide.with(getApplicationContext())
                            .load(upload_list.get(position))
                            .into(holder.upload_image);
                    holder.delete_iamge.setVisibility(View.VISIBLE);
                }
            } else {
                if (position == upload_list.size()) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.zhaopian)
                            .into(holder.upload_image);
                    holder.delete_iamge.setVisibility(View.INVISIBLE);
                } else {
                    Glide.with(getApplicationContext())
                            .load(upload_list.get(position))
                            .into(holder.upload_image);
                    holder.delete_iamge.setVisibility(View.VISIBLE);
                }
            }
            holder.delete_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (upload_list.size() > 0) {
                        upload_list.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            //只有点击最后一个加号才会添加图片
            holder.upload_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (upload_list.size() != 3) {
                        if (position == upload_list.size()) {
                            //弹出popwindow调出相册
                            createView();
                        }
                    }

                }
            });

            return convertView;

        }
    }

    class ViewHolder {
        ImageView upload_image, delete_iamge;
        LinearLayout delete_view;
    }

    Dialog dialog;
    private void createView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.user_header_dialog, null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
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

        btn_picture.setOnClickListener(new View.OnClickListener() {// 图库
            @Override
            public void onClick(View v) {
                EasyPhotos.createAlbum(WenTiActivity.this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                        .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                        .setPuzzleMenu(false)
                        .setCount(1)
                        .setOriginalMenu(false, true, null)
                        .start(101);
                dialog.dismiss();

                /*checkPermission(new PermissionActivity.CheckPermListener() {
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
                                Matisse.from(WenTiActivity.this)
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
                }, R.string.quanxian, PERMISSIONS);*/

            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {// 相机
            @Override
            public void onClick(View v) {
                EasyPhotos.createAlbum(WenTiActivity.this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                        .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                        .setPuzzleMenu(false)
                        .setCount(1)
                        .setOriginalMenu(false, true, null)
                        .start(101);
                dialog.dismiss();

                /*checkPermission(new PermissionActivity.CheckPermListener() {
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
                                Matisse.from(WenTiActivity.this)
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
                }, R.string.quanxian, PERMISSIONS);*/

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {// 取消
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                        Log.e("本地图片地址", cameraFile.getPath());
                        get_aliyun_params(cameraFile.getPath());
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

                        upload_user_image(result.getAccessKeyId()
                                , result.getAccessKeySecret()
                                , result.getSecurityToken()
                                , "syjapppic",
                                Network.bucketPath + "feedback/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg"
                                , image_path);
                    }

                    @Override
                    public void onError(String error) {
                        Logger.e("获取阿里云的数据失败");
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
                upload_image_list.add(uploud_iamge);
                dialog.dismiss();
                for (int i = 0; i < upload_image_list.size(); i++) {
                    Log.e("上传头像地址:", upload_image_list.get(i));
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                }).start();
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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            camareAdapter.notifyDataSetChanged();

        }
    };




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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//定义输出的File Uri
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < checkBoxes.length; i++) {
                //不等于当前选中的就变成false
                if (checkBoxes[i].getText().toString().equals(buttonView.getText().toString())) {
                    checkBoxes[i].setChecked(true);
                } else {
                    checkBoxes[i].setChecked(false);
                }
            }
        }
    }
}
