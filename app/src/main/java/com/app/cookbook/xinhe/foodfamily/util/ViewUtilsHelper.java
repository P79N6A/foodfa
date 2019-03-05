package com.app.cookbook.xinhe.foodfamily.util;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by joyce on 2017/5/31.
 */

public class ViewUtilsHelper {
    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 动态设置EditText的高度
     *
     * @param view
     */
    public static void setViewHight(EditText view, int begin_hight) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        int lins = view.getLineCount();//获取edittext的行数
        int text_height = getFontHeight(view.getTextSize());//获取edittext字体的高度
        if (lins <= 1) {
            params.height = begin_hight;
            view.setLayoutParams(params);
        } else {
            params.height = begin_hight + text_height * (lins - 1);
            view.setLayoutParams(params);
        }
    }
    /**
     * 获取字体高度
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * 判断输入框是否为空
     * @param editText
     * @return
     */
    public static boolean edit_is_null(EditText editText){
        if(editText.getText().toString().equals("")){
            return true;
        }else{
            return false;
        }
    }
}
