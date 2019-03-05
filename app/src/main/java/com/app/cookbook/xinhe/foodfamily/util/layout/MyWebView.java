package com.app.cookbook.xinhe.foodfamily.util.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.app.cookbook.xinhe.foodfamily.util.DateUtils;

public class MyWebView extends WebView {

    private OnTouchScreenListener onTouchScreenListener;

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyWebView(Context context) {
        super(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && (event.getEventTime() - event.getDownTime()) < 250 && (event.getEventTime() - event.getDownTime()) > 50) {
            if (onTouchScreenListener != null)
                onTouchScreenListener.onReleaseScreen();
        }
        return super.onTouchEvent(event);
    }

    public interface OnTouchScreenListener {
//        void onTouchScreen();

        void onReleaseScreen();
    }

    public void setOnTouchScreenListener(OnTouchScreenListener onTouchScreenListener) {
        this.onTouchScreenListener = onTouchScreenListener;
    }


    //滑动监听
    private OnScrollChangeListener mOnScrollChangeListener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // webview的高度
        float webcontent = getContentHeight() * getScale();
        // 当前webview的高度
        float webnow = getHeight() + getScrollY();
        if (Math.abs(webcontent - webnow) < 1) {
            //处于底端
            mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            //处于顶端
            mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
        } else {
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {

        public void onPageEnd(int l, int t, int oldl, int oldt);

        public void onPageTop(int l, int t, int oldl, int oldt);

        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }
    public interface PlayFinish{
        void After();
    }
    PlayFinish df;
    public void setDf(PlayFinish playFinish) {
        this.df = playFinish;
    }
    //onDraw表示显示完毕
    private boolean isRendered = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isRendered && null!=df && getContentHeight()>50)
        {
            Log.d("MyWebView", "getContentHeight():"+getContentHeight());
            df.After();
            isRendered = true;
        }
    }
}
