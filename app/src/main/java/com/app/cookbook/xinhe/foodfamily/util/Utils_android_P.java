package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by 18030150 on 2018/7/23.
 */

public class Utils_android_P {

    //判断是否有刘海
    public static boolean hasNotchResult(Context context) {
        boolean result = false;
        if ("HUAWEI".equals(android.os.Build.BRAND)) {
            if (Utils_android_P.hasNotchInScreen(context) == true) {
                result = true;
            } else {
                result = false;
            }
        }  else if ("OPPO".equals(android.os.Build.BRAND)) {
            if (Utils_android_P.hasNotchInOppo(context) == true) {
                result = true;
            } else {
                result = false;
            }
        } else if ("VIVO".equals(android.os.Build.BRAND)) {
            if (Utils_android_P.hasNotchInScreenAtVoio(context) == true) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 根据手机类型加不同的高度
     * @return
     */
    public static int set_add_height() {
        int set_height=0;
        if ("HUAWEI".equals(android.os.Build.BRAND)) {
            set_height = 50;
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {
            set_height = 50;
        } else if ("OPPO".equals(android.os.Build.BRAND)) {
            set_height = 50;
        } else if ("VIVO".equals(android.os.Build.BRAND)) {
            set_height = 50;
        }else{
            set_height = 50;
        }
        return set_height;
    }



    //判断华为手机是否有刘海
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    //获取华为手机刘海屏的参数
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "getNotchSize Exception");
        } finally {
            return ret;
        }
    }

    //获取华为手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //判断OPPO手机是否是刘海手机
    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    //判断vivo手机是否是刘海手机
    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;//是否有圆角

    public static boolean hasNotchInScreenAtVoio(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);

        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    //获取小米刘海高度
    public static int hasNotchInScreenAtXiaomi(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //获取小米状态栏高度
    public static int getStatusBarHeightXiaomi(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
