package com.app.cookbook.xinhe.foodfamily.update;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;

/**
 * Created by 18030150 on 2018/3/26.
 */


public class InvitationDialog extends Dialog implements
        View.OnClickListener {

    private OnLogoutClickListener mOnLogoutClickListener;
    private ImageView mCancel;
    private LinearLayout mConfirm;
    private TextView updata_content;
    private TextView invitation_num;
    private String msg;
    private String title;
    private Context context;

    public InvitationDialog(Context context, int theme) {
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
            String str = "恭喜您成功注册食与家账号，您是食与家，" + "<font color='#0EB39B'>第"+title+"</font>" + "位受邀者。感谢您对食与家的支持。";
            updata_content.setText(Html.fromHtml(str));

        }
        if (TextUtils.isEmpty(title)) {
            invitation_num.setVisibility(View.GONE);
            updata_content.setTextSize(16);
        } else {
            invitation_num.setVisibility(View.VISIBLE);
            invitation_num.setText(title);
            updata_content.setTextSize(16);
        }
    }

    public InvitationDialog(Context context) {
        super(context, R.style.invitation_theme_dialog);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        setContentView(
                LayoutInflater.from(context).inflate(R.layout.invitation_dialog,
                        null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        mCancel = (ImageView) findViewById(R.id.btn_logout_cancle);
        mConfirm = (LinearLayout) findViewById(R.id.btn_logout_consent);
        updata_content = (TextView) findViewById(R.id.updata_content);
        invitation_num = (TextView) findViewById(R.id.invitation_num);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        // 设置点击外围解散
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (mOnLogoutClickListener != null) {
            mOnLogoutClickListener.logoutOnClick(v);
            if (isShowing()) {
                this.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }


}
