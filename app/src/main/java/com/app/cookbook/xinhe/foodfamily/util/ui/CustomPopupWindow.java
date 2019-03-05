package com.app.cookbook.xinhe.foodfamily.util.ui;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.callback.XingBieCallBack;


/**
 * Created by shiyujia02 on 2017/8/8.
 */

public class CustomPopupWindow {



    public static View getView(Context activity, int resource) {
        LayoutInflater lay = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return lay.inflate(resource, null);
    }

    /**
     *  背景透明的popwindow
     *@param activity
     * @param view
     * @param resource
     * @return
     */
    public static PopupWindow PopwindowType(Activity activity, View view, int resource) {
        final PopupWindow windowtax = new PopupWindow(view, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        windowtax.setContentView(view);

        windowtax.setFocusable(true);
        windowtax.update();
        windowtax.showAtLocation(view, Gravity.CENTER, 0, 0);
        return windowtax;
    }
    /**
     *  背景透明的popwindow
     * @param activity
     * @param view
     * @param resource
     * @return
     */
    public static PopupWindow PopwindowType2(Activity activity, View view, int resource) {
        setBackgroundAlpha(0.5f,activity);
        final PopupWindow windowtax = new PopupWindow(view, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        windowtax.setContentView(view);

        windowtax.setFocusable(true);
        windowtax.update();
        windowtax.showAtLocation(view, Gravity.CENTER, 0, 0);
        return windowtax;
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(float bgAlpha,Activity mContext) {
        WindowManager.LayoutParams lp = mContext.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }

    public static void public_tishi_pop(final Activity activity, String title_str,
                                        String content_str, String sure_str, String cancel_str,
                                        final DialogCallBack dialogCallBack) {
        View view = getView(activity, R.layout.yl_dialog_no_title);
        final PopupWindow wenxin_tishi = PopwindowType2(activity, view, R.layout.yl_dialog);
       // wenxin_tishi.setAnimationStyle(R.style.popwin_anim_style);
        wenxin_tishi.setContentView(view);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(title_str);
        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(content_str);
        TextView folder_cancel = (TextView) view.findViewById(R.id.folder_cancel);
        folder_cancel.setText(sure_str);
        folder_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wenxin_tishi.dismiss();
                setBackgroundAlpha(1.0f, activity);
                dialogCallBack.cancel();

            }
        });
        TextView folder_ok = (TextView) view.findViewById(R.id.folder_ok);
        folder_ok.setText(cancel_str);
        folder_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wenxin_tishi.dismiss();
                setBackgroundAlpha(1.0f, activity);
                dialogCallBack.save();
            }
        });
    }




    /**
     * 性别的popwindow
     * @param activity
     * @param xingBieCallBack
     */
    public static void xingbie_pop(final Activity activity, final XingBieCallBack xingBieCallBack) {
        View view = getView(activity, R.layout.xingbie);
        final PopupWindow xingbie_pop = PopwindowType(activity, view, R.layout.xingbie);
        final LinearLayout man_layout = view.findViewById(R.id.man_layout);
        final LinearLayout man_layout_check = view.findViewById(R.id.man_layout_check);
        final LinearLayout woman_layout = view.findViewById(R.id.woman_layout);
        final LinearLayout woman_layout_check = view.findViewById(R.id.woman_layout_check);

        man_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xingBieCallBack.onLeft("男");
                man_layout_check.setVisibility(View.VISIBLE);
                man_layout.setVisibility(View.GONE);
                woman_layout_check.setVisibility(View.GONE);
                woman_layout.setVisibility(View.VISIBLE);
            }
        });

        woman_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xingBieCallBack.onRight("女");
                woman_layout_check.setVisibility(View.VISIBLE);
                woman_layout.setVisibility(View.GONE);
                man_layout_check.setVisibility(View.GONE);
                man_layout.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout queding = view.findViewById(R.id.queding);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xingbie_pop.dismiss();
            }
        });




    }




}
