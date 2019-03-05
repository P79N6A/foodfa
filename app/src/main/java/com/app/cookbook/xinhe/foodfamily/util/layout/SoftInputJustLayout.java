package com.app.cookbook.xinhe.foodfamily.util.layout;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 * Created by 18030150 on 2018/4/3.
 */

public class SoftInputJustLayout extends RelativeLayout {
    private int width;
    private int height;
    private int screenHeight;
    private boolean sizeChanged = false;
    private OnSizeChangedListener onSizeChangedListener;
    public SoftInputJustLayout(Context context) {
        super(context);
        init(context);
    }
    public SoftInputJustLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public SoftInputJustLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm=new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = widthMeasureSpec;
        this.height = heightMeasureSpec;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //监听不为空、宽度不变、当前高度与历史高度不为0
        if (this.onSizeChangedListener != null && w == oldw && h != 0 && oldh != 0)
        {
            if (h >= oldh )
            //有的此处是获取了屏幕的高度，当高度变化1/4屏高时才认为发生变化，我认为这是没必要的。现在的高度小于原来的高度不就是软件盘弹出所致么。这里可能有人考虑到有的手机下面会有虚拟的手机按键（home mune back）我在这里就忽略这个问题
            {
                sizeChanged = false;
            } else  {
                sizeChanged = true;
            }
            this.onSizeChangedListener.onSizeChange(sizeChanged);
            measure(this.width - w + getWidth(), this.height - h + getHeight());
        }
    }

    //设置监听
    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }
    //定义回调接口
    public abstract interface OnSizeChangedListener {
        public abstract void onSizeChange(boolean isChanged);
    }
}