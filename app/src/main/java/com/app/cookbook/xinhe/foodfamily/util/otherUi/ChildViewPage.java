package com.app.cookbook.xinhe.foodfamily.util.otherUi;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shiyujia02 on 2018/2/26.
 */

public class ChildViewPage extends ViewPager{
    public ChildViewPage(Context context) {
        super(context);
    }

    public ChildViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void removeView(View view) {
        //super.removeView(view);
    }
}
