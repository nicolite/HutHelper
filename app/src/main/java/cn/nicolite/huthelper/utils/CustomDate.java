package cn.nicolite.huthelper.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 高沛 on 2016/7/10.
 */
public class CustomDate implements Serializable {
    private static final long serialVersionUID = 1L;
    public int year;
    public int month;
    public int day;
    public int week;
    public int style;

    public CustomDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.day = day;
        this.month = month;
    }

    public CustomDate() {
        this.year = DateUtils.getYear();
        this.month = DateUtils.getMonth();
        this.day = DateUtils.getCurrentMonthDay();
    }

    public CustomDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        this.day = c.get((Calendar.DATE));
        this.month = c.get(Calendar.MONTH) + 1;
        this.year = c.get(Calendar.YEAR);
    }

    @Override
    public String toString() {
        String month, day;

        if (this.month < 10) {
            month = "0" + this.month;
        } else {
            month = "" + this.month;
        }

        if (this.day < 10) {
            day = "0" + this.day;
        } else {
            day = "" + this.day;
        }
        return year + "-" + month + "-" + day;
    }


    public String toDateString() {
        String month, day;

        if (this.month < 10) {
            month = "0" + this.month;
        } else {
            month = "" + this.month;
        }

        if (this.day < 10) {
            day = "0" + this.day;
        } else {
            day = "" + this.day;
        }
        return year + "年" + month + "月" + day + "日";
    }


    /**
     * 下一天
     *
     * @return
     */
    public String nextDay() {
        int days = DateUtils.getMonthDays(this.year, this.month);
        String year, month, day;

        if (this.day + 1 > days) {
            if (this.month == 12) {
                year = String.valueOf(this.year + 1);
                month = "01";
            } else {
                year = String.valueOf(this.year);
                if (this.month + 1 < 10) {
                    month = "0" + this.month + 1;
                } else {
                    month = "" + this.month + 1;
                }
            }
            day = "01";
        } else {
            year = String.valueOf(this.year);
            month = String.valueOf(this.month);
            day = String.valueOf(this.day + 1);
        }
        return year + "-" + month + "-" + day;
    }

    public static CustomDate modifiDayForObject(CustomDate date, int day) {
        return new CustomDate(date.year, date.month, day);
    }

    public static CustomDate modifiDayForObject(int year, int month, int day) {
        return new CustomDate(year, month, day);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getStyle() {
        return style;
    }
}
