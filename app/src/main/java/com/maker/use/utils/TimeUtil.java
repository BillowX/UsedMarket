package com.maker.use.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * 获取绝对时间(系统当前时间),时间格式为"yyMMddHHmmss"
     *
     * @return
     */
    public static String getAbsoluteTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 获取相对时间(将给点的时间变换成相对于系统当前时间的差值)，格式为“XX分钟前”
     *
     * @return
     */
    public static String getRelativeTime(String date) {
        Log.i("TimeUtil", "date=" + date);
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            Date dt1 = sdf.parse(date);

            Calendar cl = Calendar.getInstance();
            int year2 = cl.get(Calendar.YEAR);
            int month2 = cl.get(Calendar.MONTH);
            int day2 = cl.get(Calendar.DAY_OF_MONTH);
            int hour2 = cl.get(Calendar.HOUR_OF_DAY);
            int minute2 = cl.get(Calendar.MINUTE);
            int second2 = cl.get(Calendar.SECOND);

            cl.setTime(dt1);
            int year1 = cl.get(Calendar.YEAR);
            int month1 = cl.get(Calendar.MONTH);
            int day1 = cl.get(Calendar.DAY_OF_MONTH);
            int hour1 = cl.get(Calendar.HOUR_OF_DAY);
            int minute1 = cl.get(Calendar.MINUTE);
            int second1 = cl.get(Calendar.SECOND);

            if (year1 == year2) {
                if (month1 == month2) {
                    if (day1 == day2) {
                        if (hour1 == hour2) {
                            if (minute1 == minute2) {
                                time = "刚才";
                            } else {
                                time = (minute2 - minute1) + "分钟前";
                            }
                        } else if (hour2 - hour1 > 3) {
                            time = formatTime(hour1, minute1);
                        } else if (hour2 - hour1 == 1) {
                            if (minute2 - minute1 > 0) {
                                time = "1小时前";
                            } else {
                                time = (60 + minute2 - minute1) + "分钟前";
                            }
                        } else {
                            time = (hour2 - hour1) + "小时前";
                        }
                    } else if (day2 - day1 == 1) {  //昨天
                        if (hour1 > 12) {
                            time = (month1 + 1) + "月" + day1 + "日  下午";
                        } else {
                            time = (month1 + 1) + "月" + day1 + "日  上午";
                        }
                    } else {
                        time = (month1 + 1) + "月" + day1 + "日";
                    }
                } else {
                    time = (month1 + 1) + "月" + day1 + "日";
                }
            } else {
                time = year1 + "年" + month1 + "月" + day1 + "日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getFormatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy年MM月dd日   HH:mm:ss");
        return sdf2.format(date);
    }

    private static String formatTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time += "0" + hour + ":";
        } else {
            time += hour + ":";
        }

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }
        System.out.println("format(hour, minute)=" + time);
        return time;
    }







    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;


    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static String format(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse("2016-10-26 18:35:35");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delta = new Date().getTime() - Long.parseLong(str);
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }
    private static long toSeconds(long date) {
        return date / 1000L;
    }


    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }


    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }


    private static long toDays(long date) {
        return toHours(date) / 24L;
    }


    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }


    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
