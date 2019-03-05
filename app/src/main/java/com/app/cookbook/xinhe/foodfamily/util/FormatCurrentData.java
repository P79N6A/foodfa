package com.app.cookbook.xinhe.foodfamily.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FormatCurrentData {
    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_7days = seconds_of_1day * 7;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;


    /**
     * 时间日期格式化到月日.
     */
    public static String dateFormatMD_CN = "MM-dd";

    /**
     * 1522745623000           1522729829
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getTimeRange(long mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        String l = String.valueOf(mTime + "000");
        long time = Long.valueOf(l);
        startTime = new Date(time);
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return "1分钟内";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {

//            return "半小时前";
            return elapsedTime / seconds_of_1minute + " 分钟前";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }

        if (elapsedTime < seconds_of_7days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
        //几月几号
        if (elapsedTime > seconds_of_7days) {

            return getDateFromSeconds(l);
        }
        //yyyy年MM月dd日
        if (elapsedTime >= seconds_of_1year) {
            return getDateFromSeconds2(l);
        }
        return "";
    }


    public static String eFormatDate(Calendar calendar) {

        String monthStr = "";

        switch (calendar.get(Calendar.MONTH) + 1) {
            case 1:
                monthStr = "Jan.";
                break;
            case 2:
                monthStr = "Feb.";
                break;
            case 3:
                monthStr = "Mar.";
                break;
            case 4:
                monthStr = "Apr.";
                break;
            case 5:
                monthStr = "May.";
                break;
            case 6:
                monthStr = "Jun.";
                break;
            case 7:
                monthStr = "Jul.";
                break;
            case 8:
                monthStr = "Aug.";
                break;
            case 9:
                monthStr = "Sep.";
                break;
            case 10:
                monthStr = "Oct.";
                break;
            case 11:
                monthStr = "Nov.";
                break;
            case 12:
                monthStr = "Dec.";
                break;

        }


        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return monthStr + (day < 10 ? "0" + day : day);
    }


    /**
     * 秒数转化为日期
     */
    public static String getDateFromSeconds(String seconds) {
        if (seconds == null)
            return " ";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds));
            } catch (NumberFormatException nfe) {

            }
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatMD_CN);
            return sdf.format(date);
        }
    }

    /**
     * 秒数转化为日期
     */
    public static String getDateFromSeconds2(String seconds) {
        if (seconds == null)
            return " ";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds));
            } catch (NumberFormatException nfe) {

            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }

    public static String getFetureDate(long expire) {
        //PHP和Java时间戳存在三位位差，用000补齐
        if (String.valueOf(expire).length() == 10) {
            expire = expire * 1000;
        }
        Date date = new Date(expire);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = format.format(date);
        if (result.startsWith("0")) {
            result = result.substring(1);
        }
        return result;
    }
}
