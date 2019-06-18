package com.jiehun.component.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by zhouyao on 16-9-25.
 */
public class AbNumberUtils {

    /**
     * 返回带小数点的货币单位
     * 如 12，345.78;
     * 1、始终保留两们小数
     * 2、小数点前面至少一位，至多10位
     * 3、千位加一个逗号
     *
     * @param number
     * @return
     */
    public static String getCurrencyString(double number) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);//setMinimumFractionDigits(int) 设置数值的小数部分允许的最小位数。
        format.setMaximumFractionDigits(2);//setMaximumFractionDigits(int) 设置数值的小数部分允许的最大位数。
        format.setMaximumIntegerDigits(10);//setMaximumIntegerDigits(int)  设置数值的整数部分允许的最大位数。
        format.setMinimumIntegerDigits(1);//setMinimumIntegerDigits(int)  设置数值的整数部分允许的最小位数.

        return format.format(number);
    }

    public static int getIntByFloat(double source) {
        int dest = (int) source;
        return dest;
    }

    /**
     * 货币格式化。
     * 给两个小数点，并且隔三们加个逗号
     */
    public static String formatMoney(double money) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(money);
    }

    public static String formatMoney(double money, int digit) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(digit);
        nf.setMinimumFractionDigits(digit);
        return nf.format(money);
    }

    /**
     * 货币格式化。
     * 给两个小数点，并且隔三们加个逗号
     */
    public static String formatMoney(String money) {
        Double dMoney = 0.0d;
        try {
            dMoney = Double.parseDouble(money);
        } catch (Exception e) {
            dMoney = 0.0d;
        }
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(dMoney);
    }

    public static String formatMoney(String money, int digit) {
        Double dMoney = 0.0d;
        try {
            dMoney = Double.parseDouble(money);
        } catch (Exception e) {
            dMoney = 0.0d;
        }
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(digit);
        nf.setMinimumFractionDigits(digit);
        return nf.format(dMoney);
    }

    public static String formatDecimal(String dMoney, int digit) {
        Double money = 0.0d;
        try {
            money = Double.parseDouble(dMoney);
        } catch (Exception e) {
            money = 0.0d;
        }
        StringBuilder sb = new StringBuilder("0.");

        for (int i = 0; i < digit; i++) {
            sb.append("0");
        }
        if (money == 0) {
            return "0";
        }
        DecimalFormat df1 = new DecimalFormat(sb.toString());
        return df1.format(money);

    }

    public static String formatDecimal(Double money, int digit) {
        return formatDecimal(money + "", digit);
    }

    /**
     * 将一个double类型的数转换为百分数 保留1位小数
     *
     * @param number
     * @return
     */
    public static String doubletoString(Double number) {
        int num = (int) (number * 1000);
        double dNum = (double) num / 10;
        return dNum + "%";
    }

    /**
     * 将string转换成float
     * @param str
     * @return
     */
    public static Float getFloatByString(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * 去掉String中小数点后多余的0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 将double转换为string并去掉小数点后多余的0
     * @param value
     * @return
     */
    public static String getFormatFloat(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return subZeroAndDot(df.format(value));
    }
}
