package com.lxs.calendar.view;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/4/25.
 */

public class CalendarDate {

    private int year;
    private int month;
    private int day;
    private int week;
    private int state;

    public CalendarDate() {
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public CalendarDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static CalendarDate modifyCalendarDate(CalendarDate date, int day) {
        CalendarDate modifiDate = new CalendarDate(date.year, date.month, day);
        return modifiDate;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuffer dateString = new StringBuffer(year);
        dateString.append("-");
        dateString.append(month > 9 ? String.valueOf(month) : (String.valueOf(0) + month));
        dateString.append("-");
        dateString.append(day > 9 ? String.valueOf(day) : (String.valueOf(0) + day));
        return dateString.toString();
    }
}
