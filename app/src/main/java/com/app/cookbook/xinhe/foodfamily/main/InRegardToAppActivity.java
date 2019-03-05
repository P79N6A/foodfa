package com.app.cookbook.xinhe.foodfamily.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.TelDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InRegardToAppActivity extends BaseActivity {

    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.app_logo)
    ImageView app_logo;
    @BindView(R.id.app_name)
    TextView app_name;
    @BindView(R.id.yonghuxieyi_layout)
    RelativeLayout yonghuxieyi_layout;
    @BindView(R.id.service_tel)
    RelativeLayout service_tel;
    @BindView(R.id.service_mailbox)
    RelativeLayout service_mailbox;
    @BindView(R.id.versions_Name)
    TextView versions_Name;
    private String VersionName = "";

    /**
     * 请求打电话的权限码
     */
    private int REQUEST_CODE_ASK_CALL_PHONE = 100;

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_in_regard_to_app);
        ButterKnife.bind(this);
        isShowTitle(false);

        try {
            VersionName = Contacts.getLocalVersionName(this);
            if (Network.main_url.equals("https://app.shiyujia.com/")) {
                versions_Name.setText("V " + VersionName + "_20190219");
            } else {
                versions_Name.setText("V " + VersionName + "_20190219" + "B");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        iamge_back.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        yonghuxieyi_layout.setOnClickListener(this);
        service_tel.setOnClickListener(this);
        service_mailbox.setOnClickListener(this);
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iamge_back:
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
            case R.id.yonghuxieyi_layout:
                intent = new Intent(InRegardToAppActivity.this, ServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.service_tel:
                callButtonClickAction();
                break;
            case R.id.service_mailbox:

                break;
        }

    }

    /**
     * 点击打电话的按钮响应事件
     */
    public void callButtonClickAction() {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        requestPermission();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        break;
                }
            }
        };

        //弹窗让用户选择，是否允许申请权限
        TelDialogUtil.showConfirm(InRegardToAppActivity.this, "申请权限", "是否允许获取打电话权限？", dialogOnclicListener, dialogOnclicListener);
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(InRegardToAppActivity.this, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }
}
