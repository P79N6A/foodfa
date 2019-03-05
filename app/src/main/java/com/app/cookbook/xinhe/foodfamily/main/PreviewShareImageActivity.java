package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ShareImageItemAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.progress.ProgressHUD;
import com.app.cookbook.xinhe.foodfamily.util.FileUtils;
import com.app.cookbook.xinhe.foodfamily.util.ImageUtils;
import com.app.cookbook.xinhe.foodfamily.util.ShareUtil;
import com.app.cookbook.xinhe.foodfamily.util.layout.MyWebView;
import com.tencent.stat.StatService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.webkit.WebView.enableSlowWholeDocumentDraw;


public class PreviewShareImageActivity extends Activity implements DialogInterface.OnCancelListener {

    @BindView(R.id.relat_layout)
    RelativeLayout relat_layout;
    @BindView(R.id.webView)
    MyWebView webView;

    private int[] imageids = {R.drawable.icon_save_image, R.drawable.icon_anew_image, R.drawable.icon_wechat, R.drawable.icon_wechat_moments, R.drawable.icon_weibo, R.drawable.icon_qq, R.drawable.icon_qqzone};
    private String[] name = {"保存本地", "重新生成", "微信", "朋友圈", "新浪微博", "QQ", "QQ空间"};
    private List<ShareImageItem> listem = new ArrayList<>();

    //传送数据
    private String type;
    private String title;
    private String ImagePath;
    private String id;
    //图片保存相册
    private String galleryPath = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            + File.separator + "Camera" + File.separator;
    private String imageFile = "/sdcard/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_preview_share_image);
        //初始化黄牛刀
        ButterKnife.bind(this);
        mProgressHUD = ProgressHUD.show(this, "正在加载", false, this);
        initView();

    }


    private void initView() {
        title = getIntent().getStringExtra("title");
        ImagePath = getIntent().getStringExtra("imagePath");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        dialog = new Dialog(this, R.style.shareDialog);
        webViewLoad();

    }

    private void webViewLoad() {
        // 设置Web视图
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true); //设置可以支持缩放
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //測試服
//        if ("1".equals(type)) {
//            webView.loadUrl("https://app1.shiyujia.com/screenShot/dist/index.html?id=" + id + "#/question");
//        } else if ("2".equals(type)) {
//            webView.loadUrl("https://app1.shiyujia.com/screenShot/dist/index.html?id=" + id + "#/answer");
//        }
        //正式服
        if ("1".equals(type)) {
            webView.loadUrl(Network.main_url+"screenShot/product/index.html?id=" + id + "#/question");
        } else if ("2".equals(type)) {
            webView.loadUrl(Network.main_url+"screenShot/product/index.html?id=" + id + "#/answer");
        }
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setDf(new MyWebView.PlayFinish() {
                    @Override
                    public void After() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //超时处理
                                dismissProgressDialog();
                                shareImagePopwindow();
                            }
                        }, 1500);//1秒后取消dialog和弹出分享框
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //超时处理
                                saveImage("0", imageFile);
                            }
                        }, 2000);//2秒后生成图片
                    }
                });
            }
        });

        webView.setOnTouchScreenListener(new MyWebView.OnTouchScreenListener() {
            @Override
            public void onReleaseScreen() {
                if (dialog.isShowing() == false) {
                    shareImagePopwindow();
                }
            }
        });
        webView.setOnScrollChangeListener(new MyWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    //分享全图图片
    private Dialog dialog;

    private void shareImagePopwindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.share_image, null);

        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);

        RecyclerView share_recyclerView = view.findViewById(R.id.share_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        share_recyclerView.setLayoutManager(layoutManager);
        share_recyclerView.setHasFixedSize(true);


        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        if (listem.size() == 0) {
            for (int i = 0; i < name.length; i++) {
                ShareImageItem item = new ShareImageItem();
                item.setShareImage(imageids[i]);
                item.setShareNamr(name[i]);
                listem.add(item);
            }
        }

        ShareImageItemAdapter shareImageItemAdapter = new ShareImageItemAdapter(this);
        shareImageItemAdapter.setShareImageItems(listem);
        share_recyclerView.setAdapter(shareImageItemAdapter);
        shareImageItemAdapter.setOnItemClickListener(new ShareImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                shareImage(listem.get(position).getShareNamr());
            }
        });

        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePaths != null) {
                    FileUtils.deleteFolderFile(PreviewShareImageActivity.this, ImagePath, true);
                }
                if (null != b && !b.isRecycled()) {
                    b.recycle();
                    b = null;
                }
                dialog.dismiss();
                finish();
            }
        });
    }

    private void shareImage(String Name) {
        Properties prop = new Properties();
        switch (Name) {
            case "保存本地":
                showProgressDialog("正在保存请稍后...", "1");
                break;
            case "重新生成":
                webView.reload();
                break;
            case "微信":
                prop.setProperty("name", "wechat_layout");
                StatService.trackCustomKVEvent(PreviewShareImageActivity.this, "Details_answer_more_share_wechat", prop);
                ShareUtil.SharePathWechat(ImagePath, type);
                break;
            case "朋友圈":
                prop.setProperty("name", "wechat_moments_layout");
                StatService.trackCustomKVEvent(PreviewShareImageActivity.this, "Details_answer_more_share_wechat_circle", prop);
                ShareUtil.SharePathWechatMoments(ImagePath, type);
                break;
            case "新浪微博":
                prop.setProperty("name", "sinaWeibo_layout");
                StatService.trackCustomKVEvent(PreviewShareImageActivity.this, "Details_answer_more_share_sina", prop);
                ShareUtil.SharePathWeiBo(title, ImagePath, type);
                break;
            case "QQ":
                prop.setProperty("name", "QQ_layout");
                StatService.trackCustomKVEvent(PreviewShareImageActivity.this, "Details_answer_more_share_qq", prop);
                ShareUtil.SharePathQQ(ImagePath, type);
                break;
            case "QQ空间":
                prop.setProperty("name", "QQzone_layout");
                StatService.trackCustomKVEvent(PreviewShareImageActivity.this, "Details_answer_more_share_qqzone", prop);
                ShareUtil.SharePathQZone(ImagePath, type);
                break;
        }
    }


    /**
     * 长图生成加载框
     **/
    private ProgressHUD mProgressHUD;

    public void showProgressDialog(String text, final String state) {
        mProgressHUD = ProgressHUD.show(this, text, false, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveImage(state, galleryPath);
            }
        }, 1000);
    }

    public Boolean dismissProgressDialog() {
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            return true;//取消成功
        }
        return false;//已经取消过了，不需要取消
    }

    /**
     * 保存图片
     */
    private Bitmap b;
    private File filePaths;
    private Thread thread;

    public void saveImage(final String state, final String imageFile) {
        //系统相册目录
        Picture picture = webView.capturePicture();
        if (picture.getHeight() > 2000) {
            if (b == null) {
                b = Bitmap.createBitmap(
                        picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                picture.draw(c);
            }
            final File file = new File(imageFile, System.currentTimeMillis() + ".png");
            if (file.exists()) {
                file.delete();
            }
            // 获得文件相对路径
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file.getAbsoluteFile());
                if (fos != null) {
                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    if ("1".equals(state)) {
                        Toast.makeText(PreviewShareImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else if ("2".equals(state)) {
                        Toast.makeText(PreviewShareImageActivity.this, "生成成功，请重新点击分享平台", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    sendBroadcast(intent);
                    if (null != b && !b.isRecycled()) {
                        b.recycle();
                        b = null;
                    }
                    filePaths = file.getAbsoluteFile();
                    dismissProgressDialog();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //需要在子线程中处理的逻辑
                            galleryAddPic2(filePaths);
                        }
                    });
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("123", "  生成失败异常  " + e.getMessage());
                dismissProgressDialog();
                Toast.makeText(PreviewShareImageActivity.this, "生成失败请重新生成", Toast.LENGTH_SHORT).show();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //超时处理
                    saveImage("0", imageFile);
                }
            }, 3000);
        }
    }

    private void galleryAddPic2(File filePath) {
        // 设置图片的大小
        Bitmap bitMap = ImageUtils.getSmallBitmap(filePath.getPath());
        //将新文件回写到本地
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(filePath.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitMap != null) {
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(filePath);
        intent.setData(uri);
        sendBroadcast(intent);
        if (null != bitMap && !bitMap.isRecycled()) {
            bitMap.recycle();
            bitMap = null;
        }
        ImagePath = filePath.getPath();
        filePaths = filePath;
        dismissProgressDialog();
        thread.interrupt();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (filePaths != null) {
                FileUtils.deleteFolderFile(this, ImagePath, true);
            }
            finish();
        }
        return false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
