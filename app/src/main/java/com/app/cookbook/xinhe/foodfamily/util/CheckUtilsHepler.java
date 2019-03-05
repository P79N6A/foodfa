package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joyce on 2017/5/31.
 */

public class CheckUtilsHepler {
    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);

    }
    /**
     * 手机号码验证:
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9]
     * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     * 联通号段: 130,131,132,155,156,185,186,145,176,1709
     * 电信号段: 133,153,180,181,189,177,1700
     */

    public static boolean matchPhone(String text) {
//      if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
//          return true;
//      }
        return Pattern.compile("^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$").matcher(text).matches();
    }

    // 判断手机号码是否有效
    public static boolean isValidTelNumber(String telNumber) {
        if (!TextUtils.isEmpty(telNumber)) {
            String regex = "(\\+\\d+)?1[34578]\\d{9}$";
            return Pattern.matches(regex, telNumber);
        }

        return false;
    }
    /** 正则手机号格式是否正确 */
    public static boolean isTelphoneRight(Context mContext, String telphone) {

//        telphone = phone.getText().toString();

        if (telphone.length() == 0) {
            Toast.makeText(mContext,"请输入手机号", Toast.LENGTH_LONG).show();
            return false;
        }

        if (telphone.length() != 11) {
            Toast.makeText(mContext,"请输入11位长度的手机号", Toast.LENGTH_LONG).show();
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(telphone);

        if (m.matches()) {
            return true;
        } else {
            Toast.makeText(mContext,"手机号有误,请重新输入", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    // 判断邮箱地址是否有效
    public static boolean isValidEmailAddress(String emailAddress) {

        if (!TextUtils.isEmpty(emailAddress)) {
            String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
            return Pattern.matches(regex, emailAddress);
        }

        return false;
    }

    // 判断内容是否由字母，数字，下划线组成
    public static boolean isValidContent(String content) {

        if (!TextUtils.isEmpty(content)) {
            String regex = "^[\\w\\u4e00-\\u9fa5]+$";
            return Pattern.matches(regex, content);
        }

        return false;
    }

    // 判断身份证号码是否有效
    public static boolean isValidIdCard(String idCard) {
        return IdcardValidator.isValidateIdcard(idCard);
    }

    // 校验身份证的基本组成
    public boolean isIdcard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            String regex = "(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)";
            return Pattern.matches(regex, idCard);
        }

        return false;
    }

    // 校验15身份证的基本组成
    public boolean is15Idcard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            String regex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
            return Pattern.matches(regex, idCard);
        }

        return false;
    }

    // 校验18身份证的基本组成
    public boolean is18Idcard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$";
            return Pattern.matches(regex, idCard);
        }

        return false;
    }
    /**
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     *
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     *
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     *
     * 第十八位数字(校验码)的计算方法为：
     *      1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     *      2.将这17位数字和系数相乘的结果相加。
     *      3.用加出来和除以11，看余数是多少？
     *      4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为: 1 0 X 9 8 7 6 5 4 3 2;
     *      5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     */
    private static class IdcardValidator {

        // 省,直辖市代码表
        private static final String codeAndCity[][] = { { "11", "北京" }, { "12", "天津" },
                { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, { "21", "辽宁" },
                { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" },
                { "33", "浙江" }, { "34", "安徽" }, { "35", "福建" }, { "36", "江西" },
                { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" },
                { "44", "广东" }, { "45", "广西" }, { "46", "海南" }, { "50", "重庆" },
                { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" },
                { "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" },
                { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, { "82", "澳门" },
                { "91", "国外" } };

        // 每位加权因子
        private static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

        // 判断18位身份证号码是否有效
        private static boolean isValidate18Idcard(String idcard) {
            if (idcard.length() != 18) {
                return false;
            }
            String idcard17 = idcard.substring(0, 17);
            String idcard18Code = idcard.substring(17, 18);
            char c[];
            String checkCode;
            if (isDigital(idcard17)) {
                c = idcard17.toCharArray();
            } else {
                return false;
            }

            if (null != c) {
                int bit[] = converCharToInt(c);
                int sum17 = getPowerSum(bit);

                // 将和值与11取模得到余数进行校验码判断
                checkCode = getCheckCodeBySum(sum17);
                if (null == checkCode) {
                    return false;
                }
                // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
                if (!idcard18Code.equalsIgnoreCase(checkCode)) {
                    return false;
                }
            }

            return true;
        }

        // 将15位的身份证转成18位身份证
        public static String convertIdcarBy15bit(String idcard) {
            String idcard18 = null;
            if (idcard.length() != 15) {
                return null;
            }

            if (isDigital(idcard)) {
                // 获取出生年月日
                String birthday = idcard.substring(6, 12);
                Date birthdate = null;
                try {
                    birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cday = Calendar.getInstance();
                cday.setTime(birthdate);
                String year = String.valueOf(cday.get(Calendar.YEAR));

                idcard18 = idcard.substring(0, 6) + year + idcard.substring(8);

                char c[] = idcard18.toCharArray();
                String checkCode = "";

                if (null != c) {
                    int bit[] = converCharToInt(c);
                    int sum17;
                    sum17 = getPowerSum(bit);
                    // 获取和值与11取模得到余数进行校验码
                    checkCode = getCheckCodeBySum(sum17);
                    // 获取不到校验位
                    if (null == checkCode) {
                        return null;
                    }

                    // 将前17位与第18位校验码拼接
                    idcard18 += checkCode;
                }
            } else {
                return null;
            }

            return idcard18;
        }

        // 是否全部由数字组成
        public static boolean isDigital(String str) {
            return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
        }

        // 将身份证的每位和对应位的加权因子相乘之后，再得到和值
        public static int getPowerSum(int[] bit) {
            int sum = 0;
            if (power.length != bit.length) {
                return sum;
            }

            for (int i = 0; i < bit.length; i++) {
                for (int j = 0; j < power.length; j++) {
                    if (i == j) {
                        sum = sum + bit[i] * power[j];
                    }
                }
            }

            return sum;
        }

        // 将和值与11取模得到余数进行校验码判断
        private static String getCheckCodeBySum(int sum17) {
            String checkCode = null;
            switch (sum17 % 11) {
                case 10:
                    checkCode = "2";
                    break;
                case 9:
                    checkCode = "3";
                    break;
                case 8:
                    checkCode = "4";
                    break;
                case 7:
                    checkCode = "5";
                    break;
                case 6:
                    checkCode = "6";
                    break;
                case 5:
                    checkCode = "7";
                    break;
                case 4:
                    checkCode = "8";
                    break;
                case 3:
                    checkCode = "9";
                    break;
                case 2:
                    checkCode = "x";
                    break;
                case 1:
                    checkCode = "0";
                    break;
                case 0:
                    checkCode = "1";
                    break;
            }

            return checkCode;
        }

        // 将字符数组转为整型数组
        private static int[] converCharToInt(char[] c) throws NumberFormatException {
            int[] a = new int[c.length];
            int k = 0;
            for (char temp : c) {
                a[k++] = Integer.parseInt(String.valueOf(temp));
            }

            return a;
        }

        // 验证身份证号码是否有效
        public static boolean isValidateIdcard(String idcard) {
            if (!TextUtils.isEmpty(idcard)) {
                if (idcard.length() == 15) {
                    return isValidate18Idcard(convertIdcarBy15bit(idcard));
                } else if (idcard.length() == 18) {
                    return isValidate18Idcard(idcard);
                }
            }

            return false;
        }
    }
    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断银行卡有效期格式
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        }
        return convertSuccess;
    }
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
}
