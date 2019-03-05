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
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.VideoView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.FengmianSelectAdapter;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.seekbar.MSeekBar;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class FengMianSelect2Activity extends AppCompatActivity {
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.movie_video_fengmian)
    VideoView movie_video_fengmian;
    @BindView(R.id.seekbar)
    MSeekBar seekbar;
    @BindView(R.id.sure_btn)
    ImageView sure_btn;


    FengmianSelectAdapter fengmianSelectAdapter;
    List<Bitmap> bitmaps = new ArrayList<>();
    int totalTime;
    String luzhi_path;
    boolean isTouch = false;
    int currentTime;
    Bitmap sure_bitmap = null;
    Bitmap first_bitmap = null;
    FFmpegMediaMetadataRetriever mmr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feng_mian_select2);
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
        //设置视频路径
        movie_video_fengmian.setVideoPath(luzhi_path);
        //做截取封面耗时操作
        new MyAsyncTask().execute(luzhi_path);


        //获取视频当前时间
        movie_video_fengmian.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                totalTime = movie_video_fengmian.getDuration();//毫秒

            }
        });
        //设置拖动进度条逻辑
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouch) {
                    currentTime = (int) (((float) progress / 100) * totalTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                //获取第一帧图像的bitmap对象 单位是微秒
                Bitmap bitmap = mmr.getFrameAtTime((long) (currentTime * 1000), FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                Log.e("发送的家框架",currentTime * 1000+"");
                int width = DensityUtil.dip2px(getApplicationContext(), 222);
                int border = DensityUtil.dip2px(getApplicationContext(), 10);
                Bitmap bitmap1 = getRoundBitmapByShader(bitmap, width, width, 0, border);
                sure_bitmap = bitmap;
                //设置seekbar的进度条样式
                Drawable drawable = new BitmapDrawable(bitmap1);
                seekbar.setThumb(drawable);
                movie_video_fengmian.seekTo(currentTime);

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

    }



    ProgressUtil progressUtil;
    Drawable drawable;
    class MyAsyncTask extends AsyncTask<String, Void, List<Bitmap>> {
        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressUtil = new ProgressUtil();
            progressUtil.showProgressDialog(FengMianSelect2Activity.this,"视频封面截取中...");
        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected List<Bitmap> doInBackground(String... strings) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(luzhi_path);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration_time = Long.valueOf(duration);
            for (int i = 0; i <5; i++) {
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
            progressUtil.dismissProgressDialog();
            //开始播放器,因为不需要播放，所以自定位到前1秒
            movie_video_fengmian.seekTo(1000);
            //movie_video_fengmian.start();


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
        movie_video_fengmian.stopPlayback();

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
