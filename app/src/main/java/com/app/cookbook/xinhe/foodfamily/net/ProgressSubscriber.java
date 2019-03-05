package com.app.cookbook.xinhe.foodfamily.net;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.progress.ProgressHUD;
import com.app.cookbook.xinhe.foodfamily.update.LoginErrorDialog;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public class ProgressSubscriber<T> extends Subscriber<Bean<T>> implements DialogInterface.OnCancelListener {

    private SubscriberOnNextListener<Bean<T>> mListener;
    private Context mContext;
    private ProgressHUD mProgressHUD;
    private String message;
    private boolean mIsProgress;

    /**
     * 没有加载框 用于列表刷新加载
     *
     * @param listener 请求成功 逻辑处理
     * @param context  上下文
     */
    public ProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = false;
    }

    /**
     * 可设置有加载框 内容显示为 加载中...
     *
     * @param listener   请求成功 逻辑处理
     * @param context    上下文
     * @param isProgress 是否显示进度条
     */
    public ProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context, boolean isProgress) {
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = isProgress;
    }


    /**
     * 可设置有加载框 设置加载内容显示
     *
     * @param listener   请求成功 逻辑处理
     * @param context    上下文
     * @param isProgress 是否显示进度条
     * @param message    进度条内容显示
     */
    public ProgressSubscriber(SubscriberOnNextListener<Bean<T>> listener, Context context, boolean isProgress, String message) {
        this.mListener = listener;
        this.mContext = context;
        this.mIsProgress = isProgress;
        this.message = message;
    }

    private void showProgressDialog() {
        if (mProgressHUD != null) {
            mProgressHUD = ProgressHUD.show(mContext, "加载中", false, this);
        }
    }

    private void showProgressDialog(String message) {
        if (mProgressHUD != null) {
            mProgressHUD = ProgressHUD.show(mContext, message, false, this);
        }
    }

    private void dismissProgressDialog() {
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
        if (mIsProgress) {
            if (message != null && !message.equals("")) {
                showProgressDialog(message);
                return;
            }
            showProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        //      Log.e("网络请求","进来了onCompleted");
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        //     Log.e("网络请求","进来了onError");
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(MyApplication.getApplication(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("接口请求异常", e.getMessage());
            mListener.onError(e.getMessage());

            if (e.getMessage().contains("AppClient has not been setup")) {
                if (mListener != null) {
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
                }
            } else if (e.getMessage().contains("异地登录")) {
//                MyApplication.destoryActivity("SplashActivity");
                if ("1".equals(Contacts.isPage)) {
                    Log.e("123", "         ------------> 异地登录     ");
                    Contacts.isPage = "2";
                    Intent intent = new Intent(mContext, LoginErrorDialog.class);
                    if (!TextUtils.isEmpty(msg)) {
                        intent.putExtra("errorMes", msg);
                    }
                    mContext.startActivity(intent);
                }
            }
//            else {
//                mListener.onError(e.getMessage());
//            }
        }

        dismissProgressDialog();
    }

    private String msg;

    @Override
    public void onNext(Bean<T> t) {
        if (t != null) {
//            Log.e("网络请求", "进来了onNext");
            if (t.getCode().equals("1")) {
                if (mListener != null) {
                    mListener.onNext(t);
                }
            } else if (t.getCode().equals("3")) {
                if (MyApplication.is_first = true) {
                    if (mListener != null) {
                        if ("1".equals(Contacts.isPage)) {
                            Gson gson = new GsonBuilder().
                                    registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                                        @Override
                                        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                                            if (src == src.longValue())
                                                return new JsonPrimitive(src.longValue());
                                            return new JsonPrimitive(src);
                                        }
                                    }).create();
                            String myJson = gson.toJson(t.getData());//将gson转化为json
                            JSONObject obj = JSONObject.parseObject(myJson);
                            msg = obj.getString("msg");
                            if ("1".equals(Contacts.isPage)) {
                                Contacts.isPage = "2";
                                Log.e("123", "         ------------> 异地登录 1    ");
                                Intent intent = new Intent(mContext, LoginErrorDialog.class);
                                intent.putExtra("errorMes", msg);
                                mContext.startActivity(intent);
                            }
                        }
                    }
                    MyApplication.is_first = false;
                }
            } else if (t.getCode().equals("0")) {
                mListener.onError(t.getMsg());
            }
        }
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
