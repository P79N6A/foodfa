package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18030150 on 2018/4/19.
 */

public class TextChanged {

    private static String str = "";
    private static String str2 = "";

    public static void setTextChanged(final Context context, final EditText editText, final String type) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法

                if ("1".equals(type)) {
                    str = s.toString();
                    if (s.length() == 14) {
                    }
                } else if ("2".equals(type)) {
                    str2 = s.toString();
                    if (s.length() == 15) {
                    }
                }
                if ((str.length() + str2.length()) == 50) {
                    if ("1".equals(type)) {
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(str.length())});
                    } else if ("2".equals(type)) {
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(str2.length())});
                    }
                    Toast.makeText(context, "到达限制50子", Toast.LENGTH_SHORT).show();
                }
                Log.e("123", "        " + (str.length() + str2.length()));
            }
        });
    }

}
