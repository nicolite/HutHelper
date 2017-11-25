package cn.nicolite.huthelper.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期相关工具类
 * Created by nicolite on 17-11-5.
 */

public class DateUtiils {
    /**
     * 计算当前周数
     *
     * @return
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
}
