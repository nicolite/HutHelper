package cn.nicolite.huthelper.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.model.bean.Configure;

/**
 * 日期相关工具类
 * Created by nicolite on 17-11-5.
 */

public class DateUtils {

    private static String[] weekName = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    /**
     * 计算当前周数
     */
    public static int getNowWeek(String date) {

        if (TextUtils.isEmpty(date)) {
            return 1;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long be = 0;
        try {
            Date begin = df.parse(date);
            Date end = new Date();
            be = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (be / 7 + 1);
    }

    /**
     * 计算星期几
     */
    public static int getWeekOfToday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek--;
        if (dayOfWeek == 0)
            return 7;
        else
            return dayOfWeek;
    }

    /**
     * 获取每月天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }
        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }


    public static CustomDate getNextSunday() {

        Calendar c = Calendar.getInstance();

        int dayWeek = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天

        c.add(Calendar.DATE, 7 - getWeekDay() + 2);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        month += 1;
        if (dayWeek == 1) {
            day -= 7;
        }
        return new CustomDate(year, month, day);
    }

    public static CustomDate getNextSunday(Date d) {

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayWeek = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        c.add(Calendar.DATE, 7 - getWeekDay() + 2);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        month += 1;
        if (dayWeek == 1) {
            day -= 7;
        }
        return new CustomDate(year, month, day);
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index == 0) {
            week_index = 7;
        } else if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public static boolean isToday(CustomDate date) {
        return (date.year == DateUtils.getYear() &&
                date.month == DateUtils.getMonth()
                && date.day == DateUtils.getCurrentMonthDay());
    }

    public static boolean isCurrentMonth(CustomDate date) {
        return (date.year == DateUtils.getYear() &&
                date.month == DateUtils.getMonth());
    }


    public static Date toDate(int... value) {
        int year = value[0];
        int month = value[1];
        int day = value[2];
        String str = year + "-" + month + "-" + day + " 00:00";
        Date dt = null;
        try {
            dt = sdf.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 根据日期 获取星期几
     *
     * @param sDate
     * @return -1 日期格式错误
     */
    public static int getWeekOfDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// 定义日期格式
        Date date = null;
        try {
            date = format.parse(sDate);// 将字符串转换为日期
        } catch (ParseException e) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 根据日期 获取星期几
     *
     * @param date
     * @return -1 日期格式错误
     */
    public static String getWeekOfDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekName[w];
    }

    /**
     * 获取时间
     *
     * @param time 格式08:00
     * @return
     */
    public static int[] getTime(String time) {
        String[] sParams = time.split("\\:");
        int[] times = new int[2];
        try {
            times[0] = Integer.parseInt(sParams[0]);
            times[1] = Integer.parseInt(sParams[1]);
        } catch (Exception e) {
            return null;
        }
        return times;
    }


    /**
     * 解析日期
     *
     * @param date
     * @param splitStr
     * @return 数字数组
     */
    public static int[] getDate(String date, String splitStr) {
        String[] sParams = date.split(splitStr);
        int[] date1 = new int[3];
        date1[0] = Integer.parseInt(sParams[0]);
        date1[1] = Integer.parseInt(sParams[1]);
        date1[2] = Integer.parseInt(sParams[2]);
        return date1;
    }

    /**
     * 解析日期 获取时间格式是08的字符串
     *
     * @param date
     * @param splitStr
     * @return 字符串数组
     */
    public static String[] getDateStr(String date, String splitStr) {
        if (date == null) {
            return null;
        }
        String[] sParams;
        try {
            sParams = date.split(splitStr);
        } catch (Exception e) {
            return null;
        }

        String[] date1 = new String[3];
        date1[0] = sParams[0];
        date1[1] = sParams[1];
        date1[2] = sParams[2];
        return date1;
    }


    /**
     * 根据日期 获取星期几
     *
     * @param date
     * @return -1 日期格式错误
     */
    public static int getWeekOfDateNum(Date date) {

        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 计算两个时间之间相隔天数
     *
     * @return
     */
    public static int getIntervalDays(Date begin, Date end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long be = 0;
        be = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
        return (int) be;
    }

    /**
     * 计算当前周数
     *
     * @return
     */
    public static int getNowWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long be = 0;
        try {
            //TODO 开学时间
            List<Configure> configureList = DaoUtils.getConfigureList();
            Configure configure = configureList.get(0);
            String newTerm = TextUtils.isEmpty(configure.getNewTermDate()) ? "2018-03-05" : configure.getNewTermDate();
            Date begin = df.parse(newTerm);
            Date end = new Date();
            be = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (be / 7 + 1);
    }

    /**
     * 计算一个日期加上天数后的日期
     *
     * @param date
     * @param day
     * @return
     * @throws ParseException
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }
}
