package com.app.cookbook.xinhe.foodfamily.net;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;


import com.app.cookbook.xinhe.foodfamily.net.progress.ProgressHUD;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by shiyujia02 on 2018/1/6.
 */

public class ProgressSubscriberM <T> extends Subscriber<T> implements DialogInterface.OnCancelListener{
    private SubscriberOnNextListener<T> mListener;
    private Context mContext;
    private ProgressHUD mProgressHUD;
    private String message;
    private boolean mIsProgress;

    /**
     *  没有加载框 用于列表刷新加载
     * @param listener 请求成功 逻辑处理
     * @param context 上下文
     */
    public ProgressSubscriberM(SubscriberOnNextListener<T> listener, Context context){
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = false;
    }

    /**
     *  可设置有加载框 内容显示为 加载中...
     * @param listener 请求成功 逻辑处理
     * @param context 上下文
     * @param isProgress 是否显示进度条
     */
    public ProgressSubscriberM(SubscriberOnNextListener<T> listener, Context context, boolean isProgress){
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = isProgress;
    }

    /**
     * 可设置有加载框 设置加载内容显示
     * @param listener 请求成功 逻辑处理
     * @param context 上下文
     * @param isProgress 是否显示进度条
     * @param message 进度条内容显示
     */
    public ProgressSubscriberM(SubscriberOnNextListener<T> listener, Context context, boolean isProgress, String message){
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = isProgress;
        this.message = message;
    }

    private void showProgressDialog(){
        mProgressHUD = ProgressHUD.show(mContext, "加载中", false, this);
    }
    private void showProgressDialog(String message){
        mProgressHUD = ProgressHUD.show(mContext, message, false, this);
    }

    private void dismissProgressDialog(){
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        if(mIsProgress) {
            if(message != null && !message.equals("")) {
                showProgressDialog(message);
                return;
            }
            showProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        Log.e("金粉世家打开了福建省的","进来了onCompleted");

        dismissProgressDialog();
//        Toast.makeText(App.getAppContext(),"获取数据完成！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("金粉世家打开了福建省的","进来了onError");

        if (e instanceof SocketTimeoutException) {
//            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
//            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("惊世毒妃含税1111",e.getMessage());
            mListener.onError(e.getMessage());
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if(t != null) {
            Log.e("金粉世家打开了福建省的","进来了onNext");
            if (mListener != null){
                mListener.onNext(t);
            }
        }
    }



    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }
}
