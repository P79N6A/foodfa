package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.Poster;
import com.app.cookbook.xinhe.foodfamily.update.LoginErrorDialog;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.models.puzzle.Line;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerDetailWebActivity extends AppCompatActivity {
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    public String token = "";
    String url = "";
    String detail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸状态栏
        setchenjing();
        setContentView(R.layout.activity_banner_detail_web);
        //初始化黄牛刀
        ButterKnife.bind(this);
        /**动态设置距离状态栏高度*/
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(0, 140, 0, 0);
            } else {
                lp.setMargins(0, 70, 0, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(0, 30, 0, 0);
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        }
        lin_layout.setLayoutParams(lp);
        //影藏虚拟键
        hideBottomUIMenu();

        //加载webview
        initWebView();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        Bundle bundle = getIntent().getExtras();
        detail_id = bundle.getString("detail_id");


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //显示进度条
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    //加载完毕隐藏进度条
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        //启用javaScrip
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            /*对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,
            如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，
            因而对于这样的系统我们就先直接加载。*/
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        settings.setJavaScriptEnabled(true);//支持javaScrip
//        //webView加载页面使用优先缓存加载
//        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不缓存
//        //开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
//        settings.setDomStorageEnabled(true);
//        //设置编码
//        settings.setDefaultTextEncodingName("utf-8");
//        //将图片调整到适合webview的大小
//        settings.setUseWideViewPort(false);
//        //支持缩放
//        settings.setSupportZoom(true);
//        //支持内容重新布局
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        //支持通过JS打开新窗口
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        //缩放至屏幕的大小
//        settings.setLoadWithOverviewMode(true);
        //这个方法用于让H5调用android方法
        webView.addJavascriptInterface(this, "androidinfo");


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 网页加载完成时处理  如：让 加载对话框 消失
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 加载网页失败时处理 如：提示失败，或显示新的界面

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//重新传入url
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//处理https请求
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
            }


            @Override  //当没网时给用户的提示信息  这里只用一个TExtView显示提示信息就行
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                //view.loadUrl("file:///android_asset/error.html");  //加载事先写好asset包下的error.html 的网页提示错误

//                mTextView_error.setText("404 error! 网络连接失败"); //设置网络错误提示
//                text_title.setVisibility(View.GONE); //让显示网页的显示框隐藏
            }
        });

        webView.loadUrl("javascript:getDataFromNative()");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (is_yidi_login) {
//            is_yidi_login = false;//防止取消的时候重复出现弹窗
//            Log.e("方法1","方法1");
//        } else {
//            Log.e("方法2","方法2");
//        }
        //获取token和登录状态
        if (!TextUtils.isEmpty(SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString())) {
            if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString())) {//未登录
                token = "";
            } else {
                token = SharedPreferencesHelper.get(getApplicationContext(), "login_token", "").toString();//已登录
            }
        }
        //地址区分正式服和测试服
        if (Network.main_url.contains("app1")) {//测试服
            url =Network.main_url+ "answerPhone2/#/recipesDetail?type=Android&id=" + detail_id + "&token=" + token;

        } else {//正式服
            url = Network.main_url+"answerPhone/#/recipesDetail?type=Android&id=" + detail_id + "&token=" + token;
        }
        Log.e("webview地址: ", url);
        webView.loadUrl(url);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            title_tv.setText(title);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }

    }

    boolean is_yidi_login = false;
    String title = "";

    @JavascriptInterface
    public void getDataFormH5(String message) {
        Log.e("返回字符串：", message);
        Gson gson = new Gson();
        Poster poster1 = gson.fromJson(message, Poster.class);
        Log.e("111异地登录：", "哈哈" + poster1.getStatus());
        if (null != poster1.getStatus() && poster1.getStatus().equals("1")) {//页面初始化的时候
            Log.e("方法3", "方法3");
            if (null != poster1.getTitle()) {
                title = poster1.getTitle();
                Log.e("反馈代理商", poster1.getTitle());
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        if (handler != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                };
                thread.start();
            }
        } else {//点击收藏按钮的时候
            if (null != poster1.getMsg()) {//有值的话是异地登录
                //is_yidi_login = true;
                Log.e("方法4", "方法4");
                Intent intent = new Intent(BannerDetailWebActivity.this, LoginErrorDialog.class);
                if (!TextUtils.isEmpty(poster1.getMsg().getMsg())) {
                    intent.putExtra("errorMes", poster1.getMsg().getMsg());
                }
                startActivity(intent);
            } else {//没值的话是重新登录
                Intent intent = new Intent(BannerDetailWebActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
    }

    private void setchenjing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(BannerDetailWebActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(BannerDetailWebActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
