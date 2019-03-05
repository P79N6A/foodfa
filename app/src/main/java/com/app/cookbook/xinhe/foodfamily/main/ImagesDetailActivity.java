package com.app.cookbook.xinhe.foodfamily.main;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.util.AppUtil;
import com.app.cookbook.xinhe.foodfamily.util.SavePictureUtil;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.SmoothImageView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

/**
 * 描述：图片详情展示
 *
 * @author hp
 * @time 2016/10/26 0026 15:26
 */
public class ImagesDetailActivity extends AppCompatActivity {

    public static final String INTENT_IMAGE_URL_TAG = "INTENT_IMAGE_URL_TAG";
    public static final String INTENT_IMAGE_X_TAG = "INTENT_IMAGE_X_TAG";
    public static final String INTENT_IMAGE_Y_TAG = "INTENT_IMAGE_Y_TAG";
    public static final String INTENT_IMAGE_W_TAG = "INTENT_IMAGE_W_TAG";
    public static final String INTENT_IMAGE_H_TAG = "INTENT_IMAGE_H_TAG";

    private String mImageUrl;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private SmoothImageView mSmoothImageView;
    private LinearLayout save_btn;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_detail);

        AppUtil.getInstance().verifyStoragePermissions(ImagesDetailActivity.this);
        save_btn = (LinearLayout) findViewById(R.id.save_btn_lin);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        setTranslucentStatus(true);
        initViewsAndEvents();


        //保存网络图片到本地
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavePictureUtil.getInstance().savePic(ImagesDetailActivity.this, mBitmap
                        , SavePictureUtil.getInstance().getFilePath("/images"),
                        SavePictureUtil.getInstance().getFileNameByTime());
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    private void getBundleExtras(Bundle extras) {
        mImageUrl = extras.getString(INTENT_IMAGE_URL_TAG);
        mLocationX = extras.getInt(INTENT_IMAGE_X_TAG);
        mLocationY = extras.getInt(INTENT_IMAGE_Y_TAG);
        mWidth = extras.getInt(INTENT_IMAGE_W_TAG);
        mHeight = extras.getInt(INTENT_IMAGE_H_TAG);
    }


    @Override
    public void onBackPressed() {
//        mSmoothImageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (isFinishing()) {
//            overridePendingTransition(0, 0);
//        }
    }


    private void initViewsAndEvents() {
        mSmoothImageView = (SmoothImageView) findViewById(R.id.images_detail_smooth_image);
        mSmoothImageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        mSmoothImageView.transformIn();

//        Picasso.with(this)
//                .load(mImageUrl)
//                .error(R.drawable.morenbg)
//                .into(mSmoothImageView);

        Glide.with(getApplicationContext())
             //   .asBitmap()
                .load(mImageUrl)
                .error(R.drawable.icon_logo)
                .into(mSmoothImageView);

        mSmoothImageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                Logger.e("123","  mode=   " + mode);
                if (mode == 2) {
                    finish();
                }
            }
        });

        if (null != mSmoothImageView) {
            mSmoothImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSmoothImageView.transformOut();
                }
            });
        }


    }

    /**
     * set status bar translucency
     *
     * @param on
     */
    private void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    /**
     * 禁用back键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
