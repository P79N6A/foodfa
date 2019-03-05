package com.app.cookbook.xinhe.foodfamily.net.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.app.cookbook.xinhe.foodfamily.util.OnHomeKeyPressListener;

/**
 * Created by 18030150 on 2018/4/9.
 */

public class HomeKeyInterceptor extends BroadcastReceiver {
    private static final boolean LOGD = true;
    private static final String TAG = "HomeKeyInterceptor";

    private Context mContext;
    private static HomeKeyInterceptor sInstance;
    private OnHomeKeyPressListener mListener;

    private static final String EXTRA_KEY_REASON = "reason";
    private static final String REASON_GLOBAL_ACTIONS = "globalactions";
    private static final String REASON_RECENT_APPS = "recentapps";
    private static final String REASON_HOME_KEY = "homekey";
    private static final String REASON_LOCK = "lock";
    private static final String REASON_ASSIST = "assist";

    public static HomeKeyInterceptor getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HomeKeyInterceptor(context);
        }
        return sInstance;
    }

    private HomeKeyInterceptor(Context context) {
        final Context appContext = context.getApplicationContext();
        mContext = appContext;
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        appContext.registerReceiver(this, intentFilter);
    }

    public void setListener(OnHomeKeyPressListener listener) {
        mListener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (LOGD) {
            Log.w(TAG, "onReceive: " + action);
        }
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
            final String reason = intent.getStringExtra(EXTRA_KEY_REASON);
            if (LOGD) {
                Log.w(TAG, "reason: " + reason);
            }
            if (REASON_HOME_KEY.equals(reason)) {
                onHomeKeyPress();
            } else if (REASON_RECENT_APPS.equals(reason)) {
                onRecentApps();
            }
        }
    }

    private void onHomeKeyPress() {
        if (mListener != null) {
            mListener.onHomeKeyPress();
        }
    }

    private void onRecentApps() {
        if (mListener != null) {
            mListener.onRecentApps();
        }
    }
}