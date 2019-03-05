package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JiGuangNetActivity extends BaseActivity {

    @BindView(R.id.linear_left)
    LinearLayout linear_left;
    WebView webView;
    String net_url;
    private View mErrorView; //加载错误的视图
    private RelativeLayout webParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        net_url =  parms.getString("net_url");
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_ji_guang_net);
        //初始化黄牛刀
        ButterKnife.bind(this);
        setTopTitleContent("推送消息");
        isShowTitle(true);

        webView = (WebView) findViewById(R.id.webview);

    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    private void initWebView() {
        //加载需要显示的网页
        webView.loadUrl(net_url);
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    //允许加载javascript
        mWebSettings.setSupportZoom(true);          //允许缩放
        mWebSettings.setBuiltInZoomControls(true);  //原网页基础上缩放
        mWebSettings.setUseWideViewPort(true);      //任意比例缩放
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                Log.i(TAG, "onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //网络未连接
                showErrorPage();
            }

            //处理网页加载失败时
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //6.0以上执行
                Log.i(TAG, "onReceivedError: ");
                showErrorPage();//显示错误页面
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "onProgressChanged:----------->" + newProgress);
                if (newProgress == 100) {
                    //loadingLayout.setVisibility(View.GONE);
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "onReceivedTitle:title ------>" + title);
                if (title.contains("404")){
                    showErrorPage();
                }else if(title.contains("about:blank")){
                    showErrorPage();
                }
            }
        });

        webParentView = (RelativeLayout) webView.getParent(); //获取父容器
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    private void showErrorPage() {
        webParentView.removeAllViews(); //移除加载网页错误时，默认的提示信息
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webParentView.addView(mErrorView, 0, layoutParams); //添加自定义的错误提示的View
    }
    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.layout_load_error, null);
        }
    }
    @Override
    public void doBusiness(Context mContext) {
        initErrorPage();

        initWebView();
    }

    @Override
    public void widgetClick(View v) {

    }
}
