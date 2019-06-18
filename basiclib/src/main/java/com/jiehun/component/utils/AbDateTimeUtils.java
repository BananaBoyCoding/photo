package com.jiehun.component.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by zhouyao on 16-9-25.
 */
public final class AbDateTimeUtils {
    public static final String DEFAUL_PARSE    = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD_HHmmss = "yyyy-MM-dd HH:00:00";
    public static final String YYYYMMDDHHMMSS  = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD      = "yyyy-MM-dd";
    public static final String MM_DD           = "MM-dd";
    public static final String YYYYMMDD        = "yyyyMMdd";
    public static final String YYYYMMDD_CN     = "yyyy年MM月dd日";
    public static final String HH_MM_SS        = "HH:mm:ss";
    public static final String HHMMSS          = "HHmmss";
    public static final String HH_MM           = "HH:mm";
    public static final String HH              = "HH";
    public static final String MINUTE          = "mm";
    public static final String YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";
    public static final String YYYYMMDD_HHMM   = "yyyyMMdd_HHmm";
    public static final String YYYYMMDDHH      = "yyyyMMddHH";
    public static final String YYYYMMDDHH_CN   = "yyyy年MM月dd日HH时";
    public static final String YYYYMMDDHHMM    = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMM1   = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDDHHDZ    = "yyyyMMddHH0000";
    public static final String MMDD_HHMM       = "MM-dd HH:mm";
    public static final String YYYYMM          = "yyyy年MM月";
    public static final String MMDD            = "MM月dd日";
    public static       Locale locale          = Locale.CHINA;

    /**
     * @param date
     * @return 统一的时间解析器。即对未知格式的解析 正常的网络时间格式是yyyy-MM-dd HH:mm:ss 这里有对非正常时间格式的支持。
     * <p>
     * 这里增加了空保护，如果为空了，则直接返回当前时间对象
     */
    public static Date parseStringToDate(String date) {
        Date result = null;
        String parse = date;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");

        SimpleDateFormat format = new SimpleDateFormat(parse);
        try {
            result = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = new Date();
        }
        // 增加空的保护
        return result == null ? new Date() : result;
    }


    public static int getMillSec() {
        return getCalendar(getCurrentDate()).get(Calendar.MILLISECOND);
    }

    public static int getMillSec(Date date) {
        return getCalendar(date).get(Calendar.MILLISECOND);
    }

    public static int getSec() {
        return getCalendar(getCurrentDate()).get(Calendar.SECOND);
    }

    public static int getSec(Date date) {
        return getCalendar(date).get(Calendar.SECOND);
    }

    public static int getMin() {
        return getCalendar(getCurrentDate()).get(Calendar.MINUTE);
    }

    public static int getMin(Date date) {
        return getCalendar(date).get(Calendar.MINUTE);
    }

    public static int getHour() {
        return getCalendar(getCurrentDate()).get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour(Date date) {
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    public static int getDay() {
        return getCalendar(getCurrentDate()).get(Calendar.DAY_OF_MONTH);
    }

    public static String getDayWithZero(Date date) {
        int day = getCalendar(date).get(Calendar.DAY_OF_MONTH);
        return (day < 10 ? "0" : "") + day;
    }

    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeek() {
        return getCalendar(getCurrentDate()).get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeek(Date date) {
        return getCalendar(date).get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekDay() {
        return getCalendar(getCurrentDate()).get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeekDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    public static String getWeekDayCN() {
        return getWeekDayCN(getCurrentDate());
    }

    public static String getWeekDayCN(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int weekday = getWeekDay(date) - 1;
        if (weekday < 0)
            weekday = 0;
        return weekDays[weekday];
    }

    public static String getWeekDayCN(Calendar calendar) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekday < 0)
            weekday = 0;
        return weekDays[weekday];
    }

    public static String getWeekDayEN() {
        return getWeekDayCN(getCurrentDate());
    }

    public static String getWeekDayEN(Date date) {
        String[] weekDays = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THRUSDAY", "FRIDAY", "SATURDAY"};
        int weekday = getWeekDay(date) - 1;
        if (weekday < 0)
            weekday = 0;
        return weekDays[weekday];
    }

    public static int getMonth() {
        return (getCalendar(getCurrentDate()).get(Calendar.MONTH) + 1);
    }

    public static int getMonth(Date date) {
        return (getCalendar(date).get(Calendar.MONTH) + 1);
    }

    public static int getYear() {
        return getCalendar(getCurrentDate()).get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * @param date
     * @param withZeroFill
     * @return desc 取day的操作
     */
    public static String getDayOfMonth(Date date, boolean withZeroFill) {
        Calendar calendar = getCalendar(date);
        String day = (calendar.get(Calendar.DAY_OF_MONTH)) + "";
        return (day.length() == 1 && withZeroFill) ? ("0" + day) : day;
    }

    /**
     * @param date
     * @param withZeroFill 是否需要0进行补位操作
     * @return
     */
    public static String getMonth(Date date, boolean withZeroFill) {
        Calendar calendar = getCalendar(date);
        String month = (calendar.get(Calendar.MONTH) + 1) + "";
        return (month.length() == 1 && withZeroFill) ? ("0" + month) : month;
    }


    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static long getCurrentDateToLong() {
        return System.currentTimeMillis();
    }

    public static long getDateToLong(Date date) {
        return (date.getTime());
    }

    public static Date getDateByLongTime(long time) {
        return (new Date(time));
    }

    public static String getCurrentDateStr() {
        return getParseDateToStr(getCurrentDate(), DEFAUL_PARSE);
    }

    public static String getParseDateToStr(Date date) {
        return getParseDateToStr(date, DEFAUL_PARSE);
    }

    public static String getParseDateToStr(Date date, String parsePattern) {
        SimpleDateFormat dataformat = new SimpleDateFormat(parsePattern, locale);
        return dataformat.format(date);
    }

    public static String getParseDateToStrEn(Date date, String parsePattern) {
        SimpleDateFormat dataformat = new SimpleDateFormat(parsePattern, Locale.ENGLISH);
        return dataformat.format(date);
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }


    public static Date getDate(String dateStr) throws ParseException {
        return getDate(dateStr, DEFAUL_PARSE);
    }

    public static Date getDate(String dateStr, String parsePattern)
            throws ParseException {
        Locale locale = Locale.CHINA;
        SimpleDateFormat dataformat = new SimpleDateFormat(parsePattern, locale);
        Date date = dataformat.parse(dateStr);
        return date;
    }

    public static Date getDate(int year, int month, int day, int hour, int min,
                               int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, (month - 1), day, hour, min, sec);
        return calendar.getTime();
    }

    public static String getSpecialTime(String dateStr) throws ParseException {
        Date date = getDate(dateStr, DEFAUL_PARSE);
        return getSpecialTime(date);
    }

    public static String getSpecialTime(String dateStr, String parsePattern) throws ParseException {
        Date date = getDate(dateStr, parsePattern);
        return getSpecialTime(date);
    }

    public static String getSpecialTime(Date date) {
        Date beforeDate = date;
        Date currentDate = getCurrentDate();

        long between = (currentDate.getTime() - beforeDate.getTime()) / 1000;
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        if (between < 0) {
            return getParseDateToStr(date, DEFAUL_PARSE);
        } else if (day > 365 || day == 365) {
            return getParseDateToStr(date, YYYY_MM_DD);
        } else if (day > 1 || day == 1) {
            return getParseDateToStr(date, MM_DD);
        } else if (hour > 1 || hour == 1) {
            return hour + "小时前";
        } else if (minute > 1 || minute == 1) {
            return minute + "分钟前";
        } else {
            return "刚刚";
        }
    }

    public static int getGapDay(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24));
    }

    // 将指定格式的时间 转化为 另一个指定格式 比如  将 yyyy-MM-dd HH:mm:ss 转化为yyyy年MM月dd日HH时
    //默认的原时间格式是yyyy-MM-dd HH:mm:ss转化为dateFormat
    public static String getTimeFormat(String time, String dateFormat) {
        return getTimeFormat(time, DEFAUL_PARSE, dateFormat);
    }

    public static String getTimeFormat(String time, String dateFormat1, String dateFormat2) {
        SimpleDateFormat format1 = new SimpleDateFormat(dateFormat1, locale);
        SimpleDateFormat format2 = new SimpleDateFormat(dateFormat2, locale);
        return getTimeFormat(time, format1, format2);
    }

    public static String getTimeFormat(String time, SimpleDateFormat dateFormat1, SimpleDateFormat dateFormat2) {
        String formatDate = "";
        try {
            Date date = dateFormat1.parse(time);
            formatDate = dateFormat2.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formatDate;
    }

    /**
     * 比较两个时间是否相同
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        boolean isSameYear = getYear(date1) == getYear(date2);
        boolean isSameMonth = isSameYear
                && getMonth(date1) == getMonth(date2);
        boolean isSameDate = isSameMonth
                && getDay(date1) == getDay(date2);

        return isSameDate;
    }

    /**
     * 获取今天之后n天的日期
     * @param n
     * @return
     */
    public static String getDayAfterNDay(int n) {
        Calendar calendar = getCalendar(getCurrentDate());
        DateFormat dateFormat = new SimpleDateFormat(MMDD);
        calendar.add(Calendar.DATE, n);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static String getDayAfterNDay(Calendar calendar, String format, int n) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        calendar.add(Calendar.DATE, n);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    //获取  计时  时间
    public static String GetRecTime(int length) {
        if (length < 0) {
            return "00:00";
        }
        String str;
        int second = length % 60;
        int minute = (length / 60) % 60;
        str = (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        return str;
    }

    /**
     * 描述：判断是否是闰年()
     * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 描述：根据时间返回格式化后的时间的描述.
     * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     *
     * @param strDate   the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String formatDateStr2Desc(String strDate, String outFormat) {

        DateFormat df = new SimpleDateFormat(DEFAUL_PARSE);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    return "今天 " + getStringByFormat(strDate, HH_MM);
                    //return h + "小时前";
                } else if (h < 0) {
                    //return Math.abs(h) + "小时后";
                } else if (h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    } else if (m < 0) {
                        //return Math.abs(m) + "分钟后";
                    } else {
                        return "刚刚";
                    }
                }

            } else if (d > 0) {
                if (d == 1) {
                    return "昨天 " + getStringByFormat(strDate, HH_MM);
                } else if (d == 2) {
                    return "前天 " + getStringByFormat(strDate, HH_MM);
                }
            } else if (d < 0) {
                if (d == -1) {
//                    return "明天"+getStringByFormat(strDate,outFormat);
                } else if (d == -2) {
//                    return "后天"+getStringByFormat(strDate,outFormat);
                } else {
//                    return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
                }
            }

            String out = getStringByFormat(strDate, outFormat);
            if (!android.text.TextUtils.isEmpty(out)) {
                return out;
            }
        } catch (Exception e) {
        }

        return strDate;
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format       格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(DEFAUL_PARSE);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }
}

