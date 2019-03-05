package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.FengmianSelectAdapter;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.seekbar.MSeekBar;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class FengMianSelectActivity extends AppCompatActivity {
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.movie_video_fengmian)
    ImageView movie_video_fengmian;
    @BindView(R.id.seekbar)
    MSeekBar seekbar;
    @BindView(R.id.sure_btn)
    ImageView sure_btn;
    FengmianSelectAdapter fengmianSelectAdapter;
    List<Bitmap> bitmaps = new ArrayList<>();
    String luzhi_path;
    Bitmap sure_bitmap = null;
    Bitmap first_bitmap = null;
    FFmpegMediaMetadataRetriever mmr;
    private MediaPlayer mediaPlayer;
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feng_mian_select);
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

        //选取封面
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化横向ecyclerView
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        recyclerView.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
        //获取视频路径
        luzhi_path = getIntent().getStringExtra("luzhi_path");
        //初始化视频截取封面控件
        mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(luzhi_path);
        //做截取封面耗时操作
        Log.e("路径", luzhi_path);
        new MyAsyncTask().execute(luzhi_path);

        //设置拖动进度条逻辑
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Bitmap bitmap=null;
                if((max-seekBar.getProgress())<1000){//为了防止最后一张图片乱码
                    //获取第一帧图像的bitmap对象 单位是微秒
                    Log.e("最后一针的图片",mediaPlayer.getCurrentPosition()+"");
                    bitmap = mmr.getFrameAtTime((long) ((mediaPlayer.getCurrentPosition())-1000 * 1000), FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                }else{
                    Log.e("每一帧图片",mediaPlayer.getCurrentPosition()+"");
                    bitmap = mmr.getFrameAtTime((long) (mediaPlayer.getCurrentPosition() * 1000), FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                }
                if(bitmap!=null){
                    Log.e("数据了会计师开发", "获取的图片的宽" + bitmap.getWidth() + "获取的图片的高" + bitmap.getHeight());
                    Log.e("发送的家框架", mediaPlayer.getCurrentPosition()  + "");
                    int width = DensityUtil.dip2px(getApplicationContext(), 222);
                    int border = DensityUtil.dip2px(getApplicationContext(), 10);
                    Bitmap bitmap1 = getRoundBitmapByShader(bitmap, width, width, 0, border);
                    sure_bitmap = bitmap;
                    //设置seekbar的进度条样式
                    Drawable drawable = new BitmapDrawable(bitmap1);
                    seekbar.setThumb(drawable);
                    handler.postDelayed(run, 1000);
                }


            }
        });

        //确认选择的封面
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sure_bitmap == null) {
                    sure_bitmap = first_bitmap;
                }
                PlayerActivity.sure_bitmap = sure_bitmap;
                finish();
            }
        });
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(luzhi_path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
         max = mediaPlayer.getDuration();
        mediaPlayer.start();
        seekbar.setProgress(1000);
        seekbar.setMax(max);
        //获取第一张截图

    }

    Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            mediaPlayer.seekTo(seekbar.getProgress());
            movie_video_fengmian.setImageBitmap(sure_bitmap);
            handler.postDelayed(run, 100);
        }
    };



    ProgressUtil progressUtil;
    Drawable drawable;
    class MyAsyncTask extends AsyncTask<String, Void, List<Bitmap>> {
        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressUtil = new ProgressUtil();
            progressUtil.showProgressDialog(FengMianSelectActivity.this, "视频封面截取中...");
        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected List<Bitmap> doInBackground(String... strings) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(luzhi_path);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration_time = Long.valueOf(duration);
            for (int i = 0; i < 5; i++) {
                Bitmap bitmap = mmr.getFrameAtTime((1000 + (i * (duration_time / 5) * 1000)), FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                Log.e("获取的秒数", (1000 + (i * (duration_time / 5) * 1000) / 1000) + "");
                bitmaps.add(bitmap);
            }
            Log.e("视频总时长：", duration_time + "");
            //设置seekbar第一张图片
            FFmpegMediaMetadataRetriever media = new FFmpegMediaMetadataRetriever();
            media.setDataSource(luzhi_path);
            Bitmap videoCoverBitmap = media.getFrameAtTime();
            int width = DensityUtil.dip2px(getApplicationContext(), 219);
            int border = DensityUtil.dip2px(getApplicationContext(), 10);
            Bitmap bitmap1 = getRoundBitmapByShader(videoCoverBitmap, width, width, 0, border);
            sure_bitmap = videoCoverBitmap;
            first_bitmap = bitmap1;
            //设置seekbar的进度条样式
            drawable = new BitmapDrawable(bitmap1);
            return bitmaps;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(List<Bitmap> mbitmaps) {
            super.onPostExecute(mbitmaps);
            //设置适配器
            fengmianSelectAdapter = new FengmianSelectAdapter(bitmaps, getApplicationContext());
            recyclerView.setAdapter(fengmianSelectAdapter);
            //设置进度条图片
            seekbar.setThumb(drawable);
            //设置第一次进来的封面
            movie_video_fengmian.setImageBitmap(sure_bitmap);
            progressUtil.dismissProgressDialog();



        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sure_bitmap = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sure_bitmap = null;
    }

    /**
     * 给bitmap添加白色边框
     *
     * @param bitmap
     * @param outWidth
     * @param outHeight
     * @param radius
     * @param boarder
     * @return
     */
    public static Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius, int boarder) {
        if (bitmap == null) {
            return null;
        }
        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint();
        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        //创建矩形区域并且预留出border
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);
        //把传入的bitmap绘制到圆角矩形区域内
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint();
            boarderPaint.setColor(Color.WHITE);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }


    @Override
    protected void onStop() {
        super.onStop();

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


}
