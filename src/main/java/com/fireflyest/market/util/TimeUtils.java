package com.fireflyest.market.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Fireflyest
 */
public class TimeUtils {

    private TimeUtils(){

    }

    /**
     * 获取当前年份
     * @return int
     */
    public static int getYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return int
     */
    public static int getMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    /**
     * 获取当天是当月第几天
     * @return int
     */
    public static int getDay(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     * @return int
     */
    public static int getHour(){
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    /**
     * 获取当前分钟
     * @return int
     */
    public static int getMinute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒钟
     * @return int
     */
    public static int getSecond(){
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /**
     * 获取当前日期字符串
     * @return String
     */
    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期字符串
     * @return String
     */
    public static String getTimeToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取指定日期字符串
     * @return String
     */
    public static String getTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sdf.format(time);
    }

    /**
     * 获取当前时间数据
     * @return long
     */
    public static long getDate(){
        return Calendar.getInstance().getTime().getTime();
    }


}
