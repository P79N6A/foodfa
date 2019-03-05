package com.app.cookbook.xinhe.foodfamily.update;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;

public class LogoutDialog extends Dialog implements
        View.OnClickListener {

    private OnLogoutClickListener mOnLogoutClickListener;
    private ImageView mCancel;
    private LinearLayout mConfirm;
    private TextView updata_content;
    private TextView mTitle;
    private String msg;
    private String title;
    private Context context;
    //判断是否强制更新
    private String is_force;


    public LogoutDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public interface OnLogoutClickListener {
        /**
         * 退出的onclick事件
         *
         * @param v
         */
        void logoutOnClick(View v);
    }

    public void setOnLogoutClickListener(OnLogoutClickListener l, String msg,
                                         String title) {
        this.mOnLogoutClickListener = l;
        this.msg = msg;
        this.title = title;
        if (!TextUtils.isEmpty(msg)) {
            updata_content.setText(msg.replace("\\n", "\n"));
        }
        if (TextUtils.isEmpty(title)) {
            mTitle.setVisibility(View.GONE);
//            mConfirm.setText("确定");
            updata_content.setTextSize(16);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(title);
//            mConfirm.setText("升级");
            updata_content.setTextSize(16);
        }
    }

    public LogoutDialog(Context context, String is_force) {
        super(context, R.style.location_theme_dialog);
        steepStatusBar();
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        getWindow().setAttributes(lp);
        this.is_force = is_force;
        setContentView(
                LayoutInflater.from(context).inflate(R.layout.dialog_logout,
                        null), new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

        mCancel = (ImageView) findViewById(R.id.btn_logout_cancle);
        mConfirm = (LinearLayout) findViewById(R.id.btn_logout_confirm);
        updata_content = (TextView) findViewById(R.id.updata_content);
        mTitle = (TextView) findViewById(R.id.tv_mydialog_title);

        if ("1".equals(is_force)) {
            mCancel.setVisibility(View.GONE);
        } else if ("2".equals(is_force)) {
            mCancel.setVisibility(View.VISIBLE);
        }

        updata_content.setMovementMethod(ScrollingMovementMethod.getInstance());

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        // 设置点击外围解散
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (mOnLogoutClickListener != null) {
            mOnLogoutClickListener.logoutOnClick(v);
//            if (isShowing()) {
//                this.dismiss();
//            }
        }
    }

    private void steepStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
    }

}