package com.app.cookbook.xinhe.foodfamily.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UpdateManager implements View.OnClickListener {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    /**
     * 获取文件大小
     */
    public int length;
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private TextView mPercent;
    private Dialog mDownloadDialog;
    protected LoadingDialog mLoadingDialog;
    //是否强制更新
    private String is_force;

    public Version version = new Version();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    mPercent.setText(progress + "%");
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context, String is_force) {
        this.mContext = context;
        this.is_force = is_force;
        mLoadingDialog = new LoadingDialog(mContext, "请稍后");
        dialog = new Dialog(mContext, R.style.update_location_theme_dialog);
    }

    public UpdateManager(Context context, Version version) {
        this.mContext = context;
        this.version = version;
    }

    /**
     * 获取当前程序的版本号 .
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packInfo.versionName;
    }

    protected void showLoadingDialog(String text) {
        if (text != null) {
            mLoadingDialog.setText(text);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private Button mCancelUpdate;
    private Button mConfirmUpdate;
    private TextView mUpdateMessage;
    private TextView mUpdateTitle;
    private Dialog dialog;
    private TextView mCancel;

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_logout, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        mCancelUpdate = (Button) view.findViewById(R.id.btn_logout_cancle);
        mConfirmUpdate = (Button) view.findViewById(R.id.btn_logout_confirm);
        mConfirmUpdate.setText("升级");
        mUpdateMessage = (TextView) view.findViewById(R.id.message);
        mUpdateTitle = (TextView) view.findViewById(R.id.tv_mydialog_title);
        mUpdateTitle.setText("检测到新版本V" + version.getVersion());
        mUpdateTitle.setVisibility(View.VISIBLE);
        mUpdateMessage.setText(Html.fromHtml(version.getDiscrible().trim()));

        mConfirmUpdate.setOnClickListener(this);
        mCancelUpdate.setOnClickListener(this);
        dialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        if ("1".equals(is_force)) {
            //强制更新
            mDownloadDialog = new Dialog(mContext, R.style.UploadDialogTwo);
        } else if ("2".equals(is_force)) {
            //非强制更新
            mDownloadDialog = new Dialog(mContext, R.style.UploadDialog);
        }

        View v = LayoutInflater.from(mContext).inflate(
                R.layout.softupdate_progress, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mDownloadDialog.setContentView(v, params);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        mPercent = (TextView) v.findViewById(R.id.tv_percent);
        mCancel = (TextView) v.findViewById(R.id.btn_cancel_upload);
        if ("1".equals(is_force)) {
            mCancel.setVisibility(View.GONE);
        } else if ("2".equals(is_force)) {
            mCancel.setVisibility(View.VISIBLE);
        }
        mCancel.setOnClickListener(this);
        mDownloadDialog.show();
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * @author
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(Contacts.appAddress);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    length = conn.getContentLength();
                    Log.e("123", "        文件大小       " + length);
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "ShiYuJia.apk");
                    Log.e("123", "   +++++++++++++++++++    " + apkFile);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);

                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, "ShiYuJia.apk");
//        if (!apkfile.exists()) {
//            return;
//        }
//        // 通过Intent安装APK文件
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//                "application/vnd.android.package-archive");
//        mContext.startActivity(i);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkfile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        dismissLoadingDialog();
        switch (v.getId()) {
            case R.id.btn_logout_confirm:
                dialog.dismiss();
                if (TextUtils.isEmpty(version.getDiscrible())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Contacts.appAddress));
                    mContext.startActivity(intent);
                } else {
                    showDownloadDialog();
                }
                break;
            case R.id.btn_logout_cancle:
                dialog.dismiss();
                break;
            case R.id.btn_cancel_upload:
                mDownloadDialog.dismiss();
                cancelUpdate = true;
                break;

            default:
                break;
        }
    }

}
