package com.app.cookbook.xinhe.foodfamily.util.otherUi;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.app.cookbook.xinhe.foodfamily.util.ui.VerticalScrollView;

/**
 * Created by 18030150 on 2018/4/8.
 */

public class ObservableScrollView extends ScrollView {
    private OnScrollListener mListener;
    private int downY;
    private int offsetY;
    private OnScollChangedListener onScollChangedListener = null;


    public interface OnScrollListener {
        void onScroll(int scrollY);

        void onScrollToTop();

        void onScrollToBottom();

//        void onbottom();
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        if (mListener != null) {
        mListener.onScroll(t);
//        }
        if (onScollChangedListener != null) {
            onScollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
        this.onScollChangedListener = onScollChangedListener;
    }

    public interface OnScollChangedListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //记录按下时的Y坐标
                    downY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //记录滑动时的Y坐标
                    int moveY = (int) ev.getY();
                    //计算出一个差值
                    offsetY = moveY - downY;
                    downY = moveY;
                    break;
                case MotionEvent.ACTION_UP:
                    //当手指抬起时判断差值的大小
                    if (offsetY < 0) {//如果小于0，则说明用户手指向上滑动
                        mListener.onScrollToBottom();
                    } else if (offsetY > 0) {//如果大于0，则说明用户手指向下滑动
                        mListener.onScrollToTop();
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }


}
