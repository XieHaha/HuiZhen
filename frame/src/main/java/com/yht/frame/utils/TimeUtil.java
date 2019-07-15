package com.yht.frame.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author 顿顿
 * @date 19/7/1 15:53
 * @des 时间间隔处理
 */
public class TimeUtil {
    /**
     * 刚刚  30秒以内
     */
    private static final long TIME_JUST = 30 * 1000;
    /**
     * 一分钟
     */
    private static final long TIME_ONE_MINUTE = TIME_JUST * 2;
    /**
     * 一小时
     */
    private static final long TIME_ONE_HOUR = 60 * TIME_ONE_MINUTE;
    /**
     * 一天
     */
    private static final long TIME_ONE_DAY = 24 * TIME_ONE_HOUR;
    /**
     * 两天
     */
    private static final long TIME_TWO_DAYS = 2 * TIME_ONE_DAY;
    /**
     * 一年
     */
    private static final long TIME_ONE_YEAR = 365 * TIME_ONE_DAY;

    public static String getTimeString(Long timestamp) {
        String result = "";
        String hourTimeFormat = "HH:mm";
        String monthTimeFormat = "MM-dd HH:mm";
        String yearTimeFormat = "yy-MM-dd HH:mm";
        try {
            //当前时间
            Calendar todayCalendar = Calendar.getInstance();
            //消息时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            //当年
            if (todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                //当月
                if (todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    //当天
                    if (todayCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        //小时
                        int hour = todayCalendar.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
                        if (hour < 1) {
                            int minute = todayCalendar.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE);
                            if (minute < 1) {
                                int milliSecond =
                                        todayCalendar.get(Calendar.MILLISECOND) - calendar.get(Calendar.MILLISECOND);
                                if (milliSecond < TIME_JUST) {
                                    result = "刚刚";
                                }
                                else {
                                    result = "1分钟前";
                                }
                            }
                            else {
                                result = minute + "分钟前";
                            }
                        }
                        else {
                            result = hour + "小时前";
                        }
                    }
                    else if (todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1) {
                        result = "昨天 " + getTime(timestamp, hourTimeFormat);
                    }
                    else if (todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 2) {
                        result = "前天 " + getTime(timestamp, hourTimeFormat);
                    }
                    else {
                        result = getTime(timestamp, monthTimeFormat);
                    }
                }
                else {
                    if (todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1) {
                        result = "昨天 " + getTime(timestamp, hourTimeFormat);
                    }
                    else if (todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 2) {
                        result = "前天 " + getTime(timestamp, hourTimeFormat);
                    }
                    else {
                        result = getTime(timestamp, monthTimeFormat);
                    }
                }
            }
            else {
                result = getTime(timestamp, yearTimeFormat);
            }
            return result;
        }
        catch (Exception e) {
            return "";
        }
    }

    private static String getTime(long time, String pattern) {
        Date date = new Date(time);
        return dateFormat(date, pattern);
    }

    private static String dateFormat(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(date);
    }
}
