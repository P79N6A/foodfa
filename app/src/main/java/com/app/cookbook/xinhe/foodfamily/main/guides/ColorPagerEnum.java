package com.app.cookbook.xinhe.foodfamily.main.guides;

import com.app.cookbook.xinhe.foodfamily.R;

/**
 * Created by Joker on 2015/11/12.
 */
public enum ColorPagerEnum {

    RED(R.layout.view_blue),
    green(R.layout.view_red),
    BLUE(R.layout.view_green);
//    ORANGE(R.layout.view_orange);

    private int layoutResId;

    ColorPagerEnum(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

}
