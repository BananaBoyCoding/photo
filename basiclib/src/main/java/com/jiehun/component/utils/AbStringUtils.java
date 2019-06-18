package com.jiehun.component.utils;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串操作工具包<br>
 * <b>创建时间</b> 2014-8-14
 *
 * @version 1.1
 */
public class AbStringUtils {
    private final static Pattern emailPattern   = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phonePattern   = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     *
     * @param needMatchOrgStr　匹配个数
     * @return
     */
    public static int getCharCountInStr(String needMatchOrgStr, String tagMatchStr){
        if(TextUtils.isEmpty(needMatchOrgStr)){
            return 0;
        }
        int orgCount = needMatchOrgStr.length();
        String tmpStr = needMatchOrgStr.replace(tagMatchStr, "");
        return orgCount - tmpStr.length();
    }


    /**
     * 如果如果内容为空的话，返回
     *
     * @param content
     * @return
     */
    public static String nullOrString(String content) {
        if (isNull(content)) {
            return "";
        }
        return content;
    }

    /**
     * 如果如果内容为空的话，返回true
     *
     * @param content
     * @return
     */
    public static boolean isNull(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else {
            if ("null".equals(content.trim().toLowerCase())) {
                return true;
            } else {
                return false;
            }

        }

    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }


    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input)) return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email)) return false;
        return emailPattern.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum)) return false;
        return phonePattern.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null) return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static float toFloat(String obj) {
        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }
        return 0f;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * 获取默认的字符窜
     *
     * @param orgValut
     * @param defValue
     * @return
     */
    public static String getLegitimateData(String orgValut, String defValue) {
        if (TextUtils.isEmpty(orgValut)) {
            return defValue;
        }
        return orgValut;
    }

    /**
     * @param str
     * @param strLength
     * @param addLeft   true左补0, false 右补0
     * @return
     */
    public static String addZeroForNum(String str, int strLength, boolean addLeft) {
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
            if (addLeft) {
                sb.append("0").append(str);// 左补0
            } else {
                sb.append(str).append("0");//右补0
            }
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    /**
     * 获取默认的长度,中文是两个字符,英文啥的一个字符
     *
     * @param source
     * @param limitCount 多少个字符
     * @param ellipsize
     * @return
     */
    public static CharSequence getLimitSubStr(CharSequence source, int limitCount, String ellipsize) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }
        if (limitCount < 2) {
            return source;
        }


        String tmpStr = String.valueOf(source);
        if (limitCount >= getWordLength(tmpStr)) {
            return source;
        }

        for (int i = 0; i < tmpStr.length(); i++) {
            int length = getWordLength(tmpStr.substring(0, i + 1));
            if (length > limitCount) {

                return tmpStr.substring(0, i) + ellipsize;
            }

        }

        return source;
    }


    //截取字符串太长的字符串
    public static String subMyString(String myStr, int length) {
        if (TextUtils.isEmpty(myStr)) {
            return "";
        }
        if (length <= 0) {
            return "";
        }
        String str = "";
        if (myStr.length() > length) {
            str = myStr.substring(0, length) + "...";
        } else {
            str = myStr;
        }

        return str;
    }

    /**
     * 相比上面的方法，包括点的长度。例如如果四个字则显示四个字，如果五个字，则显示三个字加省略号。
     *
     * @param myStr
     * @param length 最多显示的字数
     * @return
     */
    public static String subMyStringWithDot(String myStr, int length) {
        if (TextUtils.isEmpty(myStr)) {
            return "";
        }
        if (length <= 0) {
            return "";
        }
        String str = "";
        if (myStr.length() > length) {
            str = myStr.substring(0, length - 1) + "...";
        } else {
            str = myStr;
        }

        return str;
    }


    /**
     * 获取字符长度（半角算1、全角算2）
     * 中文两个字符，　英文一个字符
     *
     * @param str 字符串
     * @return 字符串长度
     */
    public static int getWordLength(final String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int countSize = 0;
        int i = 0;
        char[] array = str.toCharArray();
        for (i = 0; i < array.length; i++) {
            if ((char) (byte) array[i] != array[i])// 判断是否为中文
            {
                countSize += 2; // 如果为中文或者中文特殊符号则占两个字节
            } else {
                countSize += 1; // 英文则占一个字节
            }
        }
        return countSize;
    }

    /**
     * 判断单个字符的长度
     */
    public static int getACharLength(char oneChar) {
        if ((char) (byte) oneChar != oneChar)// 判断是否为中文
        {
            return 2; // 如果为中文或者中文特殊符号则占两个字节
        } else {
            return 1; // 英文则占一个字节
        }
    }

    /**
     * @param str 　字符串是否都是　Asic码,没有中文等字符
     * @return
     */
    public static boolean isAllWordAsicCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int i = 0;
        char[] array = str.toCharArray();
        for (i = 0; i < array.length; i++) {
            if ((char) (byte) array[i] != array[i])// 判断是否为中文
            {
                return false;
            }
        }

        return true;
    }

    /**
     * 字符过滤
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {

        String regEx = "[`~@#$%^&*+=|{}''\\[\\]<>/~#￥﹫&*（）——+|{}【】]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

    }

    public static boolean hasDigit(String content) {
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);

        return m.matches();
    }

    public static boolean isPwdValid(String pwd) {
        String expr = "^([a-zA-Z]|[0-9]|[.!@#^*-+_%&',;=?$\\x22]){8,20}$";
        return pwd.matches(expr);
    }

    /**
     * 密码规则判断
     * 0-9数字至少出现一次，a-z小写字母至少出现一次，不允许有空格
     * @param pwd
     * @return
     */
    public static boolean isPwdComplexityValid(String pwd) {

        String exprDigit = "[\\p{Digit}]+";
        String exprUpper = "[\\p{Upper}]+";
        String exprLower = "[\\p{Lower}]+";
        String exprPunct = "[\\p{Punct}]+";
//        String expr = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        return !pwd.matches(exprDigit) && !pwd.matches(exprUpper) && !pwd.matches(exprLower) && !pwd.matches(exprPunct);

    }

    public static boolean isPhoneNumberValid(String phoneNumber) {

        String expr = "^([+])?(86)?(1[34578])\\d{9}$";
        return phoneNumber.matches(expr);
    }

    public static boolean isAccountValid(String account) {

        String expr = "^([\\u4e00-\\u9fa5]|[a-zA-Z]|[0-9]|[.!@#^*-+_%&',;=?$\\x22]){2,30}$";
        return account.matches(expr);
    }

    public static boolean isCompanyNameValild(String companyName) {

        String expr = "^([\\u4e00-\\u9fa5]|[a-zA-Z]|[0-9]|[.!@#^*-+_%&',;=?$\\x22]){2,50}$";
        return companyName.matches(expr);
    }

    public static boolean isCreditCodeValid(String creditCode) {
        String expr = "^([a-zA-Z]|[0-9]){18}$";
        return creditCode.matches(expr);
    }

    public static boolean isTelephoneValid(String tel) {
        String expr = "^(\\d{3,4}-)\\d{7,8}(|([-\\u8f6c]\\d{1,5}))$";
        return tel.matches(expr);
    }

    public static boolean isCommonStrValid(String str) {
        String expr = "^([\\u4e00-\\u9fa5]|[a-zA-Z]|[0-9]|[.!@#^*-+_%&',;=?$\\x22]){0,500}$";
        return str.matches(expr);
    }

    //DM版面尺寸校验
    public static boolean isMediaSizeValid(String str) {
        String expr = "^(\\d{1,4})(\\*)(\\d{1,4})$";
        return str.matches(expr);
    }
}
