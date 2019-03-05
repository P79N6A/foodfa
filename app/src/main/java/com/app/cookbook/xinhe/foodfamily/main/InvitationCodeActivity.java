package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class InvitationCodeActivity extends Activity implements View.OnClickListener {

    //验证码输入框
    @BindView(R.id.edit_code)
    PhoneCode edit_code;
    //错误验证码提示语
    @BindView(R.id.invitation_log)
    TextView invitation_log;
    @BindView(R.id.cancel_btn)
    TextView cancel_btn;
    @BindView(R.id.confirm_btn)
    TextView confirm_btn;

    private String phoneCode;
    /**
     * Rxjava
     */
    protected Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.invitation_code_activity);
        //初始化黄牛刀
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        //注册事件回调（根据实际需要，可写，可不写）
        edit_code.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                //例如底部【下一步】按钮可点击
                //最后一步验证码的输入可以获取验证码
                phoneCode = edit_code.getPhoneCode();
//                initDate(false, phoneCode);
            }

            @Override
            public void onInput() {
                //例如底部【下一步】按钮不可点击
                //最后输入的验证码不可点击
            }
        });

        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.confirm_btn:
                initDate(false, phoneCode);
                break;
        }
    }

    private void initDate(boolean is_show, final String invitation_code) {
        Map<String, String> params = new HashMap<>();
        params.put("invitation_code", invitation_code);
        subscription = Network.getInstance("验证邀请码", InvitationCodeActivity.this)
                .invitation_code_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List location) {
                                Log.e("123", "      验证邀请码     ");
                                //隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                Contacts.invitationCode = phoneCode;
//                                Intent intent = new Intent(InvitationCodeActivity.this, RegisterActivity.class);
//                                intent.putExtra("code", invitation_code);
//                                startActivity(intent);
                                edit_code.clearCode();
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","验证邀请码报错：" + error);
                                invitation_log.setVisibility(View.VISIBLE);
                                invitation_log.setText("请输入正确的邀请码");
                                edit_code.clearCode();
                            }
                        }, InvitationCodeActivity.this, is_show));
    }


}
