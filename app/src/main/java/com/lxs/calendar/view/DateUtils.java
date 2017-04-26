package com.lxs.calendar.view;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/25.
 */

public class DateUtils {

    /**
     * 返回当前月份的天数
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

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    /**
     * 转换日期字符串
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getDateFromString(int year, int month) {
        return getDateFromString(year, month, 1);
    }

    /**
     * 转换日期字符串
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDateFromString(int year, int month, int day) {
        StringBuffer dateString = new StringBuffer();
        dateString.append(year);
        dateString.append("-");
        dateString.append(month > 9 ? String.valueOf(month) : (String.valueOf(0) + month));
        dateString.append("-");
        dateString.append((day > 9 ? String.valueOf(day) : (String.valueOf(0) + day)));
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString.toString());
        } catch (ParseException e) {
            Log.d("Data", "日期转换异常");
        }
        return date;
    }

    /**
     * 返回天数差
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }
}
