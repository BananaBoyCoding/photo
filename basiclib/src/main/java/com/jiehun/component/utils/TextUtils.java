package com.jiehun.component.utils;

import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * 文字处理类
 *
 * @author llj
 */
public class TextUtils {
    public static final String COMMA         = ",";
    public static final String FORWARD_SLASH = "/";
    public static final String BACK_SLASH    = "\\";
    public static final String SEPARATOR     = "|";

    /**
     * 判断TextView中的内容是否为空
     *
     * @param textView 文本框
     * @return true 文本框中没有文字
     */
    public static boolean isEmpty(TextView textView) {
        return textView == null || android.text.TextUtils.isEmpty(textView.getText().toString().trim());
    }

    /**
     * 1.buf.substring(0, buf.length()-1)
     * 2.buf.replace(buf.length() - 1, buf.length(), "")
     * 3.buf.deleteCharAt(buf.length()-1)
     *
     * @param str
     * @param match
     * @return
     */
    public static String replace(String str, String match) {
        if (str == null) {
            return "";
        }
        if (android.text.TextUtils.isEmpty(match)) {
            return str;
        }
        if (str.endsWith(match)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 获取textView中的字符串
     *
     * @param textView 文本框
     */
    public static String getText(TextView textView) {
        return getText(textView, true);
    }

    public static String getText(TextView textView, boolean shouldTrim) {
        if (textView == null) {
            return "";
        }
        if (shouldTrim) {
            return textView.getText().toString().trim();
        }
        return textView.getText().toString();
    }

    /**
     * @param textView
     * @param string
     * @return
     */
    public static boolean equals(TextView textView, CharSequence string) {
        return getText(textView).equals(string);
    }

    /**
     * 设置textView的文字,防止设置null的情况
     *
     * @param textView    文本框
     * @param destination 设置的文字
     */
    public static void setText(TextView textView, CharSequence destination) {
        setText(textView, destination, null, true);
    }

    public static void setText(TextView textView, CharSequence destination, boolean shouldTrim) {
        setText(textView, destination, null, shouldTrim);
    }


    public static void setText(TextView textView, CharSequence destination, CharSequence defaultStr) {
        setText(textView, destination, defaultStr, false);
    }

    /**
     * 设置textView的文字,防止设置null的情况
     *
     * @param textView    文本框
     * @param destination 正常设置的文字
     * @param defaultStr  Empty时候设置的文字
     */
    public static void setText(TextView textView, CharSequence destination, CharSequence defaultStr, boolean shouldTrim) {
        if (textView == null) {
            return;
        }
        if (android.text.TextUtils.isEmpty(destination)) {
            if (defaultStr == null) {
                textView.setText("");
            } else {
                textView.setText(defaultStr);
            }
        } else {
            if (shouldTrim) {
                textView.setText(destination.toString().trim());
            } else {
                textView.setText(destination);
            }
        }
        setSelection(textView);
    }

    /**
     * 设置文字信息，对应显示或者隐藏控件
     *
     * @param textView
     * @param destination
     */
    public static void setTextWithVisibility(TextView textView, CharSequence destination) {
        if (textView == null) {
            return;
        }
        if (!android.text.TextUtils.isEmpty(destination)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(destination);
            setSelection(textView);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置EditText指针到最后的位置
     *
     * @param textView 输入框
     */
    public static void setSelection(TextView textView) {
        if (textView instanceof EditText)
            ((EditText) textView).setSelection(textView.getText().toString().length());
    }


    /**
     * 1.计算出该TextView中文字的长度(像素)
     *
     * @param textView
     * @param text     文字
     * @return
     */
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        return paint.measureText(text);
    }

    /**
     * 根据宽度去截取字符串，在最后位置显示省略号
     *
     * @param charSequence       提供的字符串
     * @param textView           显示文字的textView
     * @param availableTextWidth 三行除去省略号的长度
     * @return 截取后的字符串
     */
    public static CharSequence getEndEllipsizeStr(CharSequence charSequence, TextView textView, int availableTextWidth) {
        return android.text.TextUtils.ellipsize(charSequence, textView.getPaint(), availableTextWidth, android.text.TextUtils.TruncateAt.END);
    }

    /**
     * MD5加密，大写
     *
     * @param s 需要加密的String
     * @return 加密后String
     */
    public static final String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str).toUpperCase(Locale.getDefault());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5加密，小写
     *
     * @param s 需要加密的String
     * @return 加密后String
     */
    public static final String md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 格式化名字，大于max个的名字中间用省略号显示
     *
     * @param max  可以显示的最大字数，超过则在中间显示星号
     * @param name
     * @return
     */
    public static String formatName(int max, String name) {
        String newName = "";
        if (name != null) {
            if (name.length() > max) {
                newName = name.charAt(0) + "*" + name.charAt(name.length() - 1);
                return newName;
            } else {
                return name;
            }
        } else {
            return newName;
        }
    }

    /**
     * 默认的转换的名字
     *
     * @param name
     * @return
     */
    public static String formatName(String name) {
        return formatName(3, name);
    }

}
