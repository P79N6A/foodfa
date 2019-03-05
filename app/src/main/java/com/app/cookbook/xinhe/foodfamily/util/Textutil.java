package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.*;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.CommentActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.finalteam.toolsfinal.*;

/**
 * Created by 18030150 on 2018/3/21.
 */

public class Textutil {

    public static SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    /**
     * 多关键字查询表红,避免后面的关键字成为特殊的HTML语言代码
     *
     * @param str    检索结果
     * @param inputs 关键字集合
     * @param resStr 表红后的结果
     */
    public static StringBuffer addChild(String str, List<String> inputs, StringBuffer resStr) {
        int index = str.length();//用来做为标识,判断关键字的下标
        String next = "";//保存str中最先找到的关键字
        for (int i = inputs.size() - 1; i >= 0; i--) {
            String theNext = inputs.get(i);
            int theIndex = str.indexOf(theNext);
            if (theIndex == -1) {//过滤掉无效关键字
                inputs.remove(i);
            } else if (theIndex < index) {
                index = theIndex;//替换下标
                next = theNext;
            }
        }

        //如果条件成立,表示串中已经没有可以被替换的关键字,否则递归处理
        if (index == str.length()) {
            resStr.append(str);
        } else {
            resStr.append(str.substring(0, index));
            resStr.append("<font color='#FF0000'>" + str.substring(index, index + next.length()) + "</font>");
            String str1 = str.substring(index + next.length(), str.length());
            addChild(str1, inputs, resStr);//剩余的字符串继续替换
        }
        return resStr;
    }


    /**
     * @param content 传入整体内容，
     * @param list    传入替换内容集合，
     * @return
     */
    public static SpannableString foramtString(Context context,String content, List<String> list) {
        SpannableString result = new SpannableString(content);
        for (String item : list) {
            Pattern pattern = Pattern.compile(item);
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                result.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_51B55C)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return result;

    }

    public static String getString(String s, String s1)//s是需要删除某个子串的字符串s1是需要删除的子串
    {
        int postion = s.indexOf(s1);
        int length = s1.length();
        int Length = s.length();
        String newString = s.substring(0, postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串
    }


    public static void ToastUtil(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        layout.setBackgroundColor(Color.parseColor("#70000000"));
        layout.setBackgroundResource(R.drawable.toast_bg);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 过滤掉常见特殊字符,常见的表情
     */
    public static void setEtFilter(EditText et) {
        if (et == null) {
            return;
        }
        //表情过滤器
        InputFilter emojiFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                Pattern emoji = Pattern.compile(
                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
        //特殊字符过滤器
        InputFilter specialCharFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(regexStr);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.matches()) {
                    return "";
                } else {
                    return null;
                }

            }
        };

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };

        et.setFilters(new InputFilter[]{emojiFilter, specialCharFilter, filter});
    }

}
