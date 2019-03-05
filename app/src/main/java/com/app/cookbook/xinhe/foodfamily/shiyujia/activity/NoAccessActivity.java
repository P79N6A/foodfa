package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hei.permission.PermissionActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NoAccessActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;

    @BindView(R.id.cancel_btn)
    RelativeLayout cancel_btn;

    @BindView(R.id.setting_btn)
    RelativeLayout setting_btn;

    public final static String WRITE_PERMISSION_TIP = "请添加必要的权限！";
    public final static int WRITE_PERMISSION_CODE = 110;
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int faile_back_number = 0;
    String value="";

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }
    @Override
    public void initView() {
        steepStatusBar();

        setContentLayout(R.layout.activity_no_access);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);

        /**动态设置距离状态栏高度*/
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(Build.BRAND)) {
                lp.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, 0);
            }
        } else if ("Xiaomi".equals(Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(10, 140, 10, 0);
            } else {
                lp.setMargins(10, 70, 10, 0);
            }
        } else {
            if ("HUAWEI".equals(Build.BRAND)) {
                lp.setMargins(10, 70, 10, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(10, 70, 10, 0);
            }
        }

        if(null==getIntent().getExtras()){
            return;
        }else{
            Bundle bundle = getIntent().getExtras();
            value = bundle.get("put_value").toString();
        }


        //取消此页面
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "faile_back_number", ""))) {
                    faile_back_number = 0;
                } else {
                    faile_back_number = 110;
                }
                //检查权限
                if (faile_back_number == 110) {//返回失败过。直接跳设置
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                } else {
                    EasyPermissions.requestPermissions(NoAccessActivity.this, WRITE_PERMISSION_TIP, WRITE_PERMISSION_CODE, PERMISSIONS);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(getApplicationContext(), PERMISSIONS)) {
            if(value.equals("video")){
                Intent intent1 = new Intent(NoAccessActivity.this, PlayerActivity.class);
                startActivity(intent1);
                finish();
            }else{
                Intent intent1 = new Intent(NoAccessActivity.this, CamareActivity.class);
                startActivity(intent1);
                finish();
            }
        }else{
            EasyPermissions.requestPermissions(NoAccessActivity.this, WRITE_PERMISSION_TIP, WRITE_PERMISSION_CODE, PERMISSIONS);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesHelper.put(getApplicationContext(), "faile_back_number", faile_back_number + "");
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(NoAccessActivity.this, R.color.color_00000000);
        } else {
            StatusBarUtil.transparencyBar(NoAccessActivity.this);
        }
    }

    /**
     * 重写onRequestPermissionsResult，用于接受请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //用户授权成功
        Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        faile_back_number = requestCode;
        SharedPreferencesHelper.put(getApplicationContext(), "faile_back_number", faile_back_number + "");

        Log.e("司法局克鲁赛德", requestCode + "");
        Toast.makeText(getApplicationContext(), "用户授权失败", Toast.LENGTH_SHORT).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //当从软件设置界面，返回当前程序时候
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                if (EasyPermissions.hasPermissions(getApplicationContext(), PERMISSIONS)) {
                    String value = getIntent().getStringExtra("put_value");
                    Log.e("就是客服了解到",value);
                    if(value.equals("video")){
                        Intent intent1 = new Intent(NoAccessActivity.this, PlayerActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        Intent intent1 = new Intent(NoAccessActivity.this, CamareActivity.class);
                        startActivity(intent1);
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "用户授权失败", Toast.LENGTH_SHORT).show();
                }
                break;

        }
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

    }
}
