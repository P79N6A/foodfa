package com.app.cookbook.xinhe.foodfamily.util.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author majinxin
 * @description 仿美丽说底部拖拽效果
 * @date 2015年9月17日
 */
public class DragLayout extends RelativeLayout {
    /**
     * 速度常量
     */
    private static final int SNAP_VELOCITY = 800;

    /**
     * 要被拖拽的View
     */
    private View mContentView;

    /**
     * 底部外漏高度
     */
    private int bottomFlexHeight = 50;
    /**
     * 最大可偏移Y
     */
    private int mMaxtranslationY;
    /**
     * 可识别触摸事件的高度
     */
    private int touchHeight = 60;

    /**
     * 是否需要处理触摸事件
     */
    private boolean isDeal;

    /**
     * 速度追踪对象
     */
    private VelocityTracker velocityTracker;

    private ScrollTopListener mTopListener;
    private ScrollBottomListener mBottomListener;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        // 拿到唯一的contentView
        super.onFinishInflate();
        mContentView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /**
         * 整个布局layout后，将contentview移到底部
         */
        mMaxtranslationY = mContentView.getHeight() - bottomFlexHeight;
        mContentView.setY(mMaxtranslationY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                if (!isInRect(downX, downY)) {
                    isDeal = false;
                } else {
                    isDeal = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (isDeal) {
                    float moveY = event.getY();
                    if (moveY >= 0) {
                        mContentView.setY(moveY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int velocityY = getScrollYVelocity();
                if (isDeal) {
                    if (mContentView.getTranslationY() < (mMaxtranslationY / 2)) {
                        if (velocityY > SNAP_VELOCITY) {
                            scrollToBottom();
                        } else {
                            // 属性动画移动到Top
                            scrollToTop();
                        }
                    } else {
                        // 向上的速度足够，也向上滑
                        if (velocityY <= -SNAP_VELOCITY) {
                            scrollToTop();
                        } else {
                            // 属性动画移动到下面
                            scrollToBottom();
                        }
                    }
                }
                isDeal = false;
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private void scrollToTop() {
        ObjectAnimator topAnimation = ObjectAnimator.ofFloat(mContentView, "translationY",
                mContentView.getTranslationY(), 0);
        if (mTopListener != null) {
            topAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mTopListener.onScrollTop();
                }
            });
        }
        topAnimation.start();
    }

    private void scrollToBottom() {
        ObjectAnimator bottomAnimation = ObjectAnimator.ofFloat(mContentView, "translationY",
                mContentView.getTranslationY(), (mMaxtranslationY));
        if (mBottomListener != null) {
            bottomAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBottomListener.onScrollBottom();
                }
            });
        }
        bottomAnimation.start();
    }

    /**
     * 判断按下的点是否在指定区域内,不在区域内不做处理
     */
    private boolean isInRect(float downX, float downY) {
        if (downX > mContentView.getLeft() && downX < mContentView.getRight()) {
            if (downY >= mContentView.getTranslationY() && downY <= mContentView.getTranslationY() + touchHeight) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加用户的速度跟踪器
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    /**
     * 移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 获取Y方向的滑动速度
     */
    private int getScrollYVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getYVelocity();
        return velocity;
    }

    public void setmTopListener(ScrollTopListener mTopListener) {
        this.mTopListener = mTopListener;
    }

    public void setmBottomListener(ScrollBottomListener mBottomListener) {
        this.mBottomListener = mBottomListener;
    }

    /**
     * @author rzq
     * @description 滑动到底部事件监听
     * @date 2015年9月17日
     */
    public interface ScrollBottomListener {
        public void onScrollBottom();
    }

    /**
     * @author rzq
     * @description 滑动到顶部事件监听
     * @date 2015年9月17日
     */
    public interface ScrollTopListener {
        public void onScrollTop();
    }
}