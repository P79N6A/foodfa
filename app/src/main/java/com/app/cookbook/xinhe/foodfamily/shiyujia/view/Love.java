package com.app.cookbook.xinhe.foodfamily.shiyujia.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/***
 * 仿抖音点赞
 */
public class Love extends RelativeLayout {
    private Context mContext;
    float[] num = {-30, -20, 0, 20, 30};//随机心形图片角度
    //点击次数
    private int count = 0;
    //第一次点击时间
    private long firstClick = 0;
    //第二次点击时间
    private long secondClick = 0;
    /**
     * 两次点击时间间隔，单位毫秒
     */
    private final int totalTime = 300;

    public Love(Context context) {
        super(context);
        initView(context);
    }

    public Love(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Love(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    private boolean isdeaw = false;
    ImageView image = null;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

//        if (isdeaw == false) {
//            image = new ImageView(mContext);
//            LayoutParams params = new LayoutParams(100, 100);
//            params.leftMargin = getWidth() - 200;
//            params.topMargin = getHeight() / 2 - 300;
//            image.setImageDrawable(getResources().getDrawable(R.drawable.icon_answer_collect));
//            image.setLayoutParams(params);
//            addView(image);
//            image.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "这里是点击爱心的动画，待展示", Toast.LENGTH_SHORT).show();
//                }
//            });
//            isdeaw = true;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (1 == count) {
            //记录第一次点击时间
            firstClick = System.currentTimeMillis();
        } else if (2 == count) {
            //记录第二次点击时间
            secondClick = System.currentTimeMillis();
            //判断二次点击时间间隔是否在设定的间隔时间之内
            if (secondClick - firstClick < totalTime) {
                final ImageView imageView = new ImageView(mContext);
                LayoutParams params = new LayoutParams(230, 230);
                params.leftMargin = (int) event.getX() - 150;
                params.topMargin = (int) event.getY() - 300;
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_answer_collect_on));
                imageView.setLayoutParams(params);
                addView(imageView);
                EventBus.getDefault().post(new MessageEvent("501"));
//                image.setImageDrawable(getResources().getDrawable(R.drawable.icon_answer_collect_on));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                        .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                        .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                        .with(alpha(imageView, 0, 1, 100, 0))
                        .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                        .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                        .with(translationY(imageView, 0, -600, 800, 400))
                        .with(alpha(imageView, 1, 0, 300, 400))
                        .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                        .with(scale(imageView, "scaleY", 1, 3f, 700, 400));
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        removeViewInLayout(imageView);
                    }
                });
                count = 0;
                firstClick = 0;
            } else {
                firstClick = secondClick;
                count = 1;
            }
            secondClick = 0;
        }
        count++;
        return super.onTouchEvent(event);
    }

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }
}
