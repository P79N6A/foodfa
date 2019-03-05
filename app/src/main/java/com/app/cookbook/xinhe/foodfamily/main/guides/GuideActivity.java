package com.app.cookbook.xinhe.foodfamily.main.guides;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FoodFamilyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.OnHomeKeyPressListener;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;

public class GuideActivity extends AppCompatActivity implements OnHomeKeyPressListener {

    private static final String TAG = GuideActivity.class.getSimpleName();
    private static final int DURATION = 4 * 1000;

    private RelativeLayout rootView;
    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private TextView entrance_btn;
    private View gradientIv;

    private ColorPagerAdapter pagerAdapter;
    private ValueAnimator gradientAnim;

    private int totalDistance;//小黑点要移动的全部距离
    private int startX;//小黑点开始位置

    /*黑色移动圆点*/
    private TextView animIndicator;

    int[] colors = new int[]{
            Color.parseColor("#ffffffff"),//
            Color.parseColor("#ffffffff"),//
            Color.parseColor("#ffffffff"),//
            Color.parseColor("#ffffffff"),//
            Color.parseColor("#ffffffff")
    };

    private ViewPager.OnPageChangeListener pageChangeListener =
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if (pagerAdapter.getCount() > 0) {

                        float length = (position + positionOffset) / (pagerAdapter.getCount() - 1);
                        int progress = (int) (length * DURATION);

                        float path = length * GuideActivity.this.totalDistance;
                        ViewCompat.setTranslationX(animIndicator, GuideActivity.this.startX + path);

                        /*设置过度颜色*/
                        gradientAnim.setCurrentPlayTime(progress);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    /*关闭硬件加速*/
                    if (state != ViewPager.SCROLL_STATE_IDLE) {
                        final int childCount = viewPager.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            viewPager.getChildAt(i).setLayerType(View.LAYER_TYPE_NONE, null);
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == pagerAdapter.getCount() - 1) {
                        animIndicator.setVisibility(View.GONE);
                        indicatorLayout.setVisibility(View.GONE);
                        entrance_btn.setVisibility(View.VISIBLE);
                    } else {
                        animIndicator.setVisibility(View.VISIBLE);
                        indicatorLayout.setVisibility(View.VISIBLE);
                        entrance_btn.setVisibility(View.GONE);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.guide_activity);

        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        Log.e("123", "       手机型号      " + Contacts.brand);

        rootView = (RelativeLayout) findViewById(R.id.splash_layout_root_view);
        gradientIv = findViewById(R.id.main_layout_gradient_iv);
        indicatorLayout = (LinearLayout) findViewById(R.id.splash_layout_pager_indicator_ll);
        entrance_btn = findViewById(R.id.entrance_btn);

        GuideActivity.this.setupAnim();

        viewPager = (ViewPager) findViewById(R.id.splash_layout_gradient_viewpager);
        pagerAdapter = new ColorPagerAdapter(GuideActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        GuideActivity.this.setupIndicator();
        entrance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                date();
                Intent intent = new Intent(GuideActivity.this, FoodFamilyActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    //监听HOME键
    @Override
    public void onHomeKeyPress() {
//        date();
    }

    @Override
    public void onRecentApps() {

    }

    private void date() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            //第一次进入跳转
            editor.putBoolean("isfer", false);
            editor.commit();
        }
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(GuideActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(GuideActivity.this);

        }
    }


    private void setupAnim() {

        gradientAnim = ObjectAnimator.ofInt(gradientIv, "backgroundColor", colors);
        gradientAnim.setEvaluator(new ArgbEvaluator());
        gradientAnim.setDuration(DURATION);
    }

    private void setupIndicator() {

        final Drawable indicatorNormal =
                getResources().getDrawable(R.drawable.indicator_normal_background);
        final Drawable indicatorSelected =
                getResources().getDrawable(R.drawable.indicator_selected_background);

        int size = getResources().getDimensionPixelSize(R.dimen.material_20dp);

        indicatorLayout.removeAllViews();
        final TextView[] indicators = new TextView[ColorPagerEnum.values().length];
        for (int i = 0; i < indicators.length; i++) {

            indicators[i] = new TextView(GuideActivity.this);
            indicators[i].setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(61, 12);

            if (i != indicators.length - 1) {
                params.setMargins(0, 0, 43, 0);
            } else {
                params.setMargins(0, 0, 0, 0);
            }
            indicators[i].setLayoutParams(params);
            indicators[i].setBackgroundDrawable(indicatorNormal);
            indicatorLayout.addView(indicators[i]);
        }

        animIndicator = new TextView(GuideActivity.this);
        animIndicator.setLayoutParams(new LinearLayout.LayoutParams(61, 12));
        animIndicator.setBackgroundDrawable(indicatorSelected);
        rootView.addView(animIndicator);

        indicatorLayout.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        indicatorLayout.getViewTreeObserver().removeOnPreDrawListener(this);

                        Rect rootRect = new Rect();
                        Point globalOffset = new Point();
                        rootView.getGlobalVisibleRect(rootRect, globalOffset);

                        Rect firstRect = new Rect();
                        indicatorLayout.getChildAt(0).getGlobalVisibleRect(firstRect);
                        firstRect.offset(-globalOffset.x, -globalOffset.y);

                        Rect lastRect = new Rect();
                        indicatorLayout.getChildAt(indicators.length - 1).getGlobalVisibleRect(lastRect);

                        GuideActivity.this.totalDistance = lastRect.left - indicatorLayout.getLeft();
                        GuideActivity.this.startX = firstRect.left;

                        ViewCompat.setTranslationX(animIndicator, firstRect.left);
                        if (Contacts.brand.equals("HUAWEI(HUAWEI VNS-DL00)")) {
                            ViewCompat.setTranslationY(animIndicator, firstRect.bottom + 96);
                        } else {
                            ViewCompat.setTranslationY(animIndicator, firstRect.top);
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        this.skipTv.setOnClickListener(null);
//        this.nextBtn.setOnClickListener(null);
//        this.doneTv.setOnClickListener(null);
        this.viewPager.removeOnPageChangeListener(pageChangeListener);
    }
}
