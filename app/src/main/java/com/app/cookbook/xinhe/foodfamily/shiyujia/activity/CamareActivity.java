package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.callback.CameraBack;
import com.app.cookbook.xinhe.foodfamily.main.FoodFamilyActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.CameraSelectAdapter;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.resource.ValueResources;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.GlideEngine;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.camera_surface_view.CameraSurfaceView;
import com.app.cookbook.xinhe.foodfamily.util.camera_surface_view.ClipSquareImageView;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.models.puzzle.Line;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class CamareActivity extends AppCompatActivity {
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.horizontalscrollview)
    RecyclerView horizontalscrollview;
    @BindView(R.id.camera_btn)
    ImageView camera_btn;
    @BindView(R.id.chicun_btn)
    ImageView chicun_btn;
    @BindView(R.id.cameraSurfaceView)
    CameraSurfaceView cameraSurfaceView;
    @BindView(R.id.camera_layout)
    LinearLayout camera_layout;
    @BindView(R.id.xiangce_btn)
    LinearLayout xiangce_btn;
    @BindView(R.id.image_view_layout)
    LinearLayout image_view_layout;
    @BindView(R.id.seniorcropimageView)
    ClipSquareImageView seniorcropimageView;
    @BindView(R.id.shanchu_btn)
    ImageView shanchu_btn;
    @BindView(R.id.paizhao_btn)
    ImageView paizhao_btn;
    @BindView(R.id.next_btn)
    TextView next_btn;
    @BindView(R.id.back_img)
    ImageView back_img;
    @BindView(R.id.fuceng)
    RelativeLayout fuceng;

    public static List<String> strings = new ArrayList<>();
    private int max_num = 0;
    private CameraSelectAdapter cameraSelectAdapter;
    //public static int selecrt_iamges_number = 0;
    public static String fabu_content = "";
    //String enter_jia = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        setchenjing();
        setContentView(R.layout.activity_camare);
        //初始化黄牛刀
        ButterKnife.bind(this);
        /**动态设置距离状态栏高度*/
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
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
                lp.setMargins(0, 30, 0, 0);
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        }
        lin_layout.setLayoutParams(lp);
        //影藏虚拟键
        hideBottomUIMenu();

        //返回按钮
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置尺寸状态
        set_paizhao_moshi();

        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        horizontalscrollview.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
        cameraSelectAdapter = new CameraSelectAdapter(strings, getApplicationContext());
        cameraSelectAdapter.setmOnItemClickListener(new CameraSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                set_images_item(position);
            }
        });
        //删除图片
        RxUtils.clickView(shanchu_btn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //设置图片可选数
                        ValueResources.select_iamges_size =ValueResources.select_iamges_size-1;


                        //删除选择的图片
                        strings.remove(CameraSelectAdapter.select_positonn);
                        CameraSelectAdapter.select_positonn=0;
                        cameraSelectAdapter.notifyDataSetChanged();

                        //设置默认选择图片
                        if(strings.size()>0){
                            //设置图片地址
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(strings.get(CameraSelectAdapter.select_positonn));
                                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                seniorcropimageView.setImageBitmap(bitmap);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{//打开预览
                            image_view_layout.setVisibility(View.GONE);
                            camera_layout.setVisibility(View.VISIBLE);
                            //恢复到选择尺寸状态
                            shanchu_btn.setVisibility(View.GONE);
                            chicun_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
        horizontalscrollview.setAdapter(cameraSelectAdapter);


        //预览相机
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int width = dm.widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cameraSurfaceView.getLayoutParams();
        params.height = width;//设置当前控件布局的高度
        params.width = width;
        cameraSurfaceView.setLayoutParams(params);//将设置好的布局参数应用到控件中
        Log.e("相机的初始化","height:"+params.height);
        //影藏的布局
        LinearLayout.LayoutParams params_layout = (LinearLayout.LayoutParams) seniorcropimageView.getLayoutParams();
        params_layout.height = width;//设置当前控件布局的高度
        params_layout.width = width;
        seniorcropimageView.setLayoutParams(params_layout);//将设置好的布局参数应用到控件中

        //切换尺寸按钮
        chicun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) cameraSurfaceView.getLayoutParams();
                if(cameraSurfaceView.getVisibility()==View.VISIBLE){
                    seniorcropimageView.setVisibility(View.GONE);
                }
                //获取当前控件的布局对象
                if (params1.width == params1.height) {
                    params1.height = width;//设置当前控件布局的高度
                    params1.width = width * 3 / 4;
                    params1.gravity = Gravity.CENTER_HORIZONTAL;
                    cameraSurfaceView.setLayoutParams(params1);//将设置好的布局参数应用到控件中
                    chicun_btn.setImageResource(R.drawable.yibiyi);
                } else {
                    params1.height = width;//设置当前控件布局的高度
                    params1.width = width;
                    cameraSurfaceView.setLayoutParams(params1);//将设置好的布局参数应用到控件中
                    chicun_btn.setImageResource(R.drawable.sibisan);
                }
            }
        });

        //相机拍照按钮
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("选择的图片数量",ValueResources.select_iamges_size+"");
                if (ValueResources.select_iamges_size == 9) {
                    Toast.makeText(getApplicationContext(), "只能上传9张图片哦～", Toast.LENGTH_SHORT).show();
                } else if (ValueResources.select_iamges_size  < 9) {
                    cameraSurfaceView.capture(new CameraBack() {
                        @Override
                        public void return_picture_path(String path) {
                            ValueResources.select_iamges_size++;
                            strings.add(path);
                            cameraSelectAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "只能上传9张图片哦～", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //切换剪裁的拍照按钮
        paizhao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_paizhao_moshi();
                //恢复横向列表的选中状态
                CameraSelectAdapter.select_positonn = -1;
                cameraSelectAdapter.notifyDataSetChanged();
            }
        });

        //打开相册
        xiangce_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置最多只能上传9张图片
                if (ValueResources.select_iamges_size  > 9) {
                    Toast.makeText(getApplicationContext(), "只能上传9张图片哦～", Toast.LENGTH_SHORT).show();
                } else if (ValueResources.select_iamges_size  <= 9) {
                    max_num = 9-ValueResources.select_iamges_size;
                    EasyPhotos.createAlbum(CamareActivity.this, true, GlideEngine.getInstance())
                            .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                            .setPuzzleMenu(false)
                            .setCount(max_num)
                            .setOriginalMenu(false, true, null)
                            .start(101);
                }
            }
        });

        //下一步按钮
        RxUtils.clickView(next_btn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //图片统一剪裁
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) cameraSurfaceView.getLayoutParams();
                        List<String> bitmaps = new ArrayList<>();
                        //获取当前控件的布局对象
                        if (params1.width == params1.height) {//当前按照1:1的比例剪裁
                            //创建bitmap
                            for (int i = 0; i < strings.size(); i++) {//因为编辑需要区分本地和网络的图片
                                if(strings.get(i).contains("https://syjapppic.oss")){//如果是网络图片
                                    bitmaps.add(strings.get(i));
                                }else{
                                    FileInputStream fis = null;
                                    try {
                                        fis = new FileInputStream(strings.get(i));
                                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                        seniorcropimageView.setImageBitmap(bitmap);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    seniorcropimageView.setBorderWeight(1, 1);
                                    Bitmap imageViewBitmap = seniorcropimageView.clip();
                                    String path = saveImage(imageViewBitmap, Environment.getExternalStorageDirectory() + "/images/", StringsHelper.getRandom() + String.valueOf(System.currentTimeMillis()));
                                    bitmaps.add(path);
                                }
                            }
                        } else {//当前按照3:4的比例剪裁
                            for (int i = 0; i < strings.size(); i++) {
                                if(strings.get(i).contains("https://syjapppic.oss")){//如果是网络图片
                                    bitmaps.add(strings.get(i));
                                }else{
                                    FileInputStream fis = null;
                                    try {
                                        fis = new FileInputStream(strings.get(i));
                                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                        seniorcropimageView.setImageBitmap(bitmap);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    seniorcropimageView.setBorderWeight(3, 4);
                                    Bitmap imageViewBitmap = seniorcropimageView.clip();
                                    String path = saveImage(imageViewBitmap, Environment.getExternalStorageDirectory() + "/images/", StringsHelper.getRandom() + String.valueOf(System.currentTimeMillis()));
                                    bitmaps.add(path);
                                }
                            }
                        }


                        if (bitmaps.size() == 0) {
                            Toast.makeText(CamareActivity.this, "请先添加图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(ValueResources.enter_jia.equals("true")){
                            Log.e("从哪进来的","从添加进来的");
                            AddImagesActivity.iamge_paths = (ArrayList<String>) bitmaps;
                            finish();
                        }else if(ValueResources.enter_jia.equals("bianji_true")){//编辑草稿箱入口进来的
                            Log.e("从哪进来的","从编辑草稿箱进来的");
                            AddImagesActivity.iamge_paths = (ArrayList<String>) bitmaps;
                            finish();
                        }else{
                            Log.e("从哪进来的","正常流程进来的");
                            Intent intent = new Intent(CamareActivity.this, AddImagesActivity.class);
                            intent.putStringArrayListExtra("iamges_paths", (ArrayList<String>) bitmaps);
                            startActivity(intent);
                            finish();
                            MyApplication.destoryActivity("CamareActivity");
                        }


                    }
                });
        MyApplication.addDestoryActivity(CamareActivity.this,"CamareActivity");

        //是否显示浮层
        String is_first = SharedPreferencesHelper.get(getApplicationContext(),"is_first","").toString();
        if(is_first.equals("true")){
            fuceng.setVisibility(View.VISIBLE);
            //Log.e("放暑假",fuceng+"");
        }else{
            fuceng.setVisibility(View.GONE);
            //Log.e("111放暑假",fuceng+"");

        }

        fuceng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuceng.setVisibility(View.GONE);
            }
        });

        SharedPreferencesHelper.put(getApplicationContext(), "is_first", "false");
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
            StatusBarUtil.setStatusBarColor(CamareActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(CamareActivity.this);
        }
    }

    private void set_images_item(int position) {
        //设置删除状态
        set_jiancai_moshi();

        //设置隐藏的选中状态的imageview,避免首次黑屏
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.more_image);
        seniorcropimageView.setImageBitmap(bmp);

        //设置图片地址，根据比例
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) cameraSurfaceView.getLayoutParams();
        //获取当前控件的布局对象
        if (params1.width == params1.height) {//当前按照1:1的比例剪裁
            seniorcropimageView.setBorderWeight(1, 1);
        } else {
            seniorcropimageView.setBorderWeight(3, 4);
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(strings.get(position));
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            if (bitmap != null) {
                seniorcropimageView.setImageBitmap(bitmap);
                seniorcropimageView.setVisibility(View.VISIBLE);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //设置选中状态
        CameraSelectAdapter.select_positonn = position;
        cameraSelectAdapter.notifyDataSetChanged();
    }

    public static String saveImage(Bitmap bmp, String path, String fileName) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".jpg");
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
        //Log.e("几十块房间里的", file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    /**
     * 拍照模式
     */
    private void set_paizhao_moshi() {
        image_view_layout.setVisibility(View.INVISIBLE);
        camera_layout.setVisibility(View.VISIBLE);
        shanchu_btn.setVisibility(View.GONE);
        chicun_btn.setVisibility(View.VISIBLE);
        paizhao_btn.setVisibility(View.GONE);
        camera_btn.setVisibility(View.VISIBLE);
    }

    /**
     * 剪裁模式
     */
    private void set_jiancai_moshi() {
        image_view_layout.setVisibility(View.VISIBLE);
        camera_layout.setVisibility(View.INVISIBLE);
        shanchu_btn.setVisibility(View.VISIBLE);
        chicun_btn.setVisibility(View.GONE);
        paizhao_btn.setVisibility(View.VISIBLE);
        camera_btn.setVisibility(View.GONE);
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
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(CamareActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(CamareActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //影藏虚拟键
        hideBottomUIMenu();
        ValueResources.select_iamges_size=strings.size();
        if(ValueResources.enter_jia.equals("true")){
            Log.e("从哪进来的","从添加进来的");
            back_img.setVisibility(View.INVISIBLE);
            back_btn.setVisibility(View.INVISIBLE);
            next_btn.setText("完成");
        }else if(ValueResources.enter_jia.equals("bianji_true")){//编辑草稿箱入口进来的
            Log.e("从哪进来的","从编辑草稿箱进来的");
            back_img.setVisibility(View.INVISIBLE);
            back_btn.setVisibility(View.INVISIBLE);
            next_btn.setText("完成");
        }else{
            back_img.setVisibility(View.VISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            next_btn.setText("下一步");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraSelectAdapter.select_positonn = -1;
        ValueResources.select_iamges_size=0;
        fabu_content="";
        strings.clear();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                for (int i = 0; i < resultPhotos.size(); i++) {
                    Log.e("图片地址", resultPhotos.get(i).path);
                }
                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
                strings.addAll(resultPaths);
                //设置当前已选参数
                ValueResources.select_iamges_size=ValueResources.select_iamges_size+1;
                cameraSelectAdapter.notifyDataSetChanged();
                return;
            }


        } else if (RESULT_CANCELED == resultCode) {
            Log.e("关闭了相册", "关闭了相册");
            //Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }


}
