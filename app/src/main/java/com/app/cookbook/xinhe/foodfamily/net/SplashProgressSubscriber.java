package com.app.cookbook.xinhe.foodfamily.net;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.progress.ProgressHUD;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by shiyujia02 on 2018/2/10.
 */

public class SplashProgressSubscriber <T> extends Subscriber<Bean<T>> implements DialogInterface.OnCancelListener{
    private SubscriberOnNextListener<Bean<T>> mListener;
    private Context mContext;
    private ProgressHUD mProgressHUD;
    private String message;
    private boolean mIsProgress;

    /**
     *  没有加载框 用于列表刷新加载
     * @param listener 请求成功 逻辑处理
     * @param context 上下文
     */
    public SplashProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context){
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
    public SplashProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context, boolean isProgress){
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
    public SplashProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context, boolean isProgress, String message){
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
        Log.e("网络请求","进来了onCompleted");
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("网络请求","进来了onError");

        if (e instanceof SocketTimeoutException) {
//            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
//            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("网络请求异常",e.getMessage());
            mListener.onError(e.getMessage());
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(Bean<T> t) {
        if(t != null) {
            Log.e("网络请求","进来了onNext");
            if(t.getCode() .equals("1") ) {
                if (mListener != null){
                    mListener.onNext(t);
                }
            } else {
                if (mListener != null){
                    Log.e("网络请求异常打印",t.getMsg());
//                    if(t.getMsg().equals("令牌无效")){
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        mContext.startActivity(intent);
//                    }else{
//                        mListener.onError(t.getMsg());
//                    }
                    mListener.onError(t.getMsg());

                }
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
