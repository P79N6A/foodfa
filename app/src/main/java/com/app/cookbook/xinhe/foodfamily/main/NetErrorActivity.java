package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetErrorActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.image_top)
    ImageView image_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }
    String is_splash;
    @Override
    public void initParms(Bundle parms) {
        is_splash = parms.getString("is_splash");
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_net_error);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                is_splash();
            }
        });

        //重新连接网络
        image_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void is_splash(){
        if(is_splash.equals("true")){
            PopWindowHelper.public_tishi_pop(NetErrorActivity.this, "食与家提示", "是否退回桌面？", "取消", "确定", new DialogCallBack() {
                @Override
                public void save() {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                }

                @Override
                public void cancel() {

                }
            });
        }else{
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        is_splash();

    }

    @Override
    public void widgetClick(View v) {

    }
}
