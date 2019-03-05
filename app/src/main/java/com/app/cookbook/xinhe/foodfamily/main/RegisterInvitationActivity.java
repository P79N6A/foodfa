package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.layout.PhoneCode;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterInvitationActivity extends BaseActivity {

    //验证码输入框
    @BindView(R.id.edit_code)
    PhoneCode edit_code;
    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //错误验证码提示语
    @BindView(R.id.invitation_log)
    TextView invitation_log;

    //定时器
    private Timer timer;
    private TimerTask task;

    private int i = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                i++;
                if (i == 2) {
                    invitation_log.setVisibility(View.GONE);
                    timer.cancel();
                } else {
                    invitation_log.setVisibility(View.GONE);
                }
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register_invitation);
        //初始化黄牛刀
        ButterKnife.bind(this);
        Contacts.addActivity(this);
        iamge_back.setOnClickListener(this);
        //注册事件回调（根据实际需要，可写，可不写）
        edit_code.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                //例如底部【下一步】按钮可点击
                //最后一步验证码的输入可以获取验证码
                String phoneCode = edit_code.getPhoneCode();
                initDate(false, phoneCode);
            }

            @Override
            public void onInput() {
                //例如底部【下一步】按钮不可点击
                //最后输入的验证码不可点击
            }
        });
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
        switch (v.getId()) {
            case R.id.iamge_back:
                finish();
                break;
        }
    }

    private void initDate(boolean is_show, final String invitation_code) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", invitation_code);
//        Log.e("123", "      phoneCode    " + invitation_code);
        subscription = Network.getInstance("验证邀请码", RegisterInvitationActivity.this)
                .invitation_code_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List location) {
                                Log.e("123", "      验证邀请码     ");
                                //隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                Intent intent = new Intent(RegisterInvitationActivity.this, RegisterActivity.class);
                                intent.putExtra("code", invitation_code);
                                startActivity(intent);
                                edit_code.clearCode();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("验证邀请码报错：" + error);

                                if (error.equals("邀请码不存在")) {
                                    invitation_log.setVisibility(View.VISIBLE);
                                    invitation_log.setText("请输入正确的邀请码");
                                    edit_code.clearCode();
                                } else {
                                    invitation_log.setVisibility(View.VISIBLE);
                                    invitation_log.setText(error);
//                                    Textutil.ToastUtil(RegisterInvitationActivity.this, error);
                                    edit_code.clearCode();
                                }
                                getTime();
                                timer.schedule(task, 3000, 3000);
                            }
                        }, RegisterInvitationActivity.this, is_show));
    }

    private void getTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // 需要做的事:发送消息
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
    }

}
