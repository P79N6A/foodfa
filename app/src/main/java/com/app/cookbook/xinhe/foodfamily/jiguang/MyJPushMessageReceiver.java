package com.app.cookbook.xinhe.foodfamily.jiguang;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 * */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    /**
     * tag增删查改的操作会在此方法中回调结果
     */
    @Override
    public void onTagOperatorResult(Context context,JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
        Log.e("MyJPushMessageReceiver", "onTagOperatorResult查询得到的别名: " + jPushMessage.getAlias());
        Log.e("MyJPushMessageReceiver", "onTagOperatorResult查询得到的标签: " + jPushMessage.getTags());
        Log.e("MyJPushMessageReceiver", "onTagOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());
        Log.e("MyJPushMessageReceiver", "onTagOperatorResult传入的标示: " + jPushMessage.getSequence());
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
        Log.e("MyJPushMessageReceiver", "onCheckTagOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());

    }
    /**
     * 查询某个tag与当前用户的绑定状态的操作会在此方法中回调结果
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    /**
     * alias相关的操作会在此方法中回调结果
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
        Log.e("MyJPushMessageReceiver", "onAliasOperatorResult错误码0为成功: " + jPushMessage.getErrorCode());

    }
}
