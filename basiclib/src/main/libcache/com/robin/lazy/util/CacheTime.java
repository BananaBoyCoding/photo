package com.robin.lazy.util;

import java.util.Calendar;

/**
 * Created by zhouyao on 16-9-18.
 * 缓存的时间管理
 */
public class CacheTime {

    private int TIME_OF_ONEMINUTE = 1 * getTimeUnit();//单位分钟
    private int TIME_OF_ONEHOURS = 60 * TIME_OF_ONEMINUTE;//单位分钟
    private int TIME_OF_ONEDAY = 24 * TIME_OF_ONEHOURS;
    private int TIME_OF_ONEWEEKEND = 7 * TIME_OF_ONEDAY;
    private int TIME_OF_TENDAY = 10 * TIME_OF_ONEDAY;
    private int TIME_OF_ONEMONTH = 30 * TIME_OF_ONEDAY;

    private static CacheTime mCacheTime;

    public static CacheTime getInstance(){
        if (mCacheTime == null){
            return new CacheTime();
        }
        return mCacheTime;
    }


    public int getTIME_OF_ONEMINUTE() {
        return TIME_OF_ONEMINUTE;
    }

    public int getTIME_OF_ONEHOURS() {
        return TIME_OF_ONEHOURS;
    }

    public int getTIME_OF_ONEDAY() {
        return TIME_OF_ONEDAY;
    }

    public int getTIME_OF_ONEWEEKEND() {
        return TIME_OF_ONEWEEKEND;
    }

    public int getTIME_OF_TENDAY() {
        return TIME_OF_TENDAY;
    }

    public int getTIME_OF_ONEMONTH() {
        return TIME_OF_ONEMONTH;
    }

    /**
     * 距离晚上多长时间
     * @return
     */
    public int getTimesLeftNightToTime() {
        return (int)(((getTimesnightInMillis() - System.currentTimeMillis()) / (1000 * 60)) * getTimeUnit()) ;
    }

    /**
     * 距离周日晚上24点多长时间
     * @return
     */
    public int getTimesLeftWeekNightToTime() {
        return (int)(((getTimesWeeknightInMillis() - System.currentTimeMillis()) / (1000 * 60)) * getTimeUnit());
    }


    /**
     * 距离月末晚上24点多长时间
     * @return
     */
    public int getTimesLeftMONTHNightToTime() {
        return (int)(((getTimesMonthnightInMillis() - System.currentTimeMillis()) / (1000 * 60))* getTimeUnit());
    }




    //获得当天24点时间
    public long getTimesnightInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获得本周日24点时间
    public long getTimesWeeknightInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (cal.getTime().getTime() + (7 * 24 * 60 * 60 * 1000));
    }

    //获得本月最后一天24点时间
    public long getTimesMonthnightInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTimeInMillis();
    }

    public int getTimeUnit(){
        return 1;
    }

}
