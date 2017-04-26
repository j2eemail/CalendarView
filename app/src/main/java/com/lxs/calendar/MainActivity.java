package com.lxs.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lxs.calendar.view.CalendarDate;
import com.lxs.calendar.view.CalendarView;
import com.lxs.calendar.view.DateUtils;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarCard;
    private CalendarDate mCalendarDate;
    private HashMap<Integer, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVariable.getInstance().init(this);

        calendarCard = (CalendarView) findViewById(R.id.main_calendar);
        mCalendarDate = new CalendarDate();
        map = new HashMap<>();

        calendarCard.updateData(mCalendarDate, map);
        calendarCard.setOnCalendarCardItemListener(new com.lxs.calendar.view.OnCalendarCardItemListener() {
            @Override
            public void onItemClick(CalendarDate calendarDate) {
                Date nowdate = DateUtils.getDateFromString(mCalendarDate.getYear(), mCalendarDate.getMonth(), mCalendarDate.getDay());
                Date showData = DateUtils.getDateFromString(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDay());
                if (DateUtils.daysOfTwo(nowdate, showData) >= 0) {
                    if (map.containsKey(calendarDate.getDay())) {
                        if (map.get(calendarDate.getDay()) == 1) {
                            map.put(calendarDate.getDay(), 2);
                        } else {
                            map.put(calendarDate.getDay(), 1);
                        }
                    } else {
                        if (calendarDate.getState() == 1) {
                            map.put(calendarDate.getDay(), 2);
                        } else {
                            map.put(calendarDate.getDay(), 1);
                        }
                    }
                    calendarCard.updateData(calendarDate, map);
                }
            }
        });
    }
}
