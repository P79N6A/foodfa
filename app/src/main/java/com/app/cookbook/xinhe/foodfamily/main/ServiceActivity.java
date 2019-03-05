package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.XieYiEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends BaseActivity {

    @BindView(R.id.linear_left)
    LinearLayout linear_left;
    @BindView(R.id.back_image)
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    WebView webView;
    String linkCss;

    @Override
    public void doBusiness(Context mContext) {
        setContentLayout(R.layout.activity_service2);
        //初始化黄牛刀
        ButterKnife.bind(this);
        setTopTitleContent("服务协议");
        isShowTitle(true);

        webView = (WebView) findViewById(R.id.webview);
        //自适应屏幕
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linear_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        linkCss = "<style type=\"text/css\"> " +
                "p {" +
                "font-size:40px;" +
                "}"
                +
                "body {" +
                "margin-right:15px;" +
                "margin-left:15px;" +
                "margin-top:5px;" +
                "font-size:30px;" +
                "}" +
                "</style>";

        initWebViewUrl();
    }

    private void initWebViewUrl() {
        subscription = Network.getInstance("消息页面获取通知界面", getApplicationContext())
                .get_user_xieyi(
                        new ProgressSubscriberNew<>(XieYiEntity.class, new GsonSubscriberOnNextListener<XieYiEntity>() {
                            @Override
                            public void on_post_entity(XieYiEntity result) {
                                Logger.e("获取用户协议url成功：" + result.getContent());
                                String html = "<html><header>" + linkCss + "</header>" + result.getContent() + "</body></html>";
                                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("获取用户协议url失败：" + error);
                            }
                        }, this, true));
    }

    @Override
    public void widgetClick(View v) {

    }
}
