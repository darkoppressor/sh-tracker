/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.month;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.timessquare.CalendarPickerView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.day.DayActivity;
import org.cheeseandbacon.shtracker.util.DateAndTime;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MonthActivity extends BaseActivity {
    // years
    private static final int DATE_RANGE = 1;
    public static final String EXTRA_INITIAL_DATE = "org.cheeseandbacon.shtracker.month.initialDate";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_month, getString(R.string.month_title), null);

        CalendarPickerView calendarPickerView = findViewById(R.id.calendar);

        String initialDate = getIntent().getStringExtra(EXTRA_INITIAL_DATE);

        Calendar current = Calendar.getInstance();

        if (initialDate != null) {
            Date date = DateAndTime.dateStringToDate(initialDate);

            if (date != null) {
                current.setTime(date);
            }
        }

        Calendar minimum = Calendar.getInstance();
        minimum.setTime(current.getTime());
        minimum.add(Calendar.YEAR, -DATE_RANGE);

        Calendar maximum = Calendar.getInstance();
        maximum.setTime(current.getTime());
        maximum.add(Calendar.YEAR, DATE_RANGE);

        calendarPickerView.setCustomDayView(new MonthAdapter());
        calendarPickerView.setDecorators(Collections.singletonList(new MonthDecorator(this)));

        calendarPickerView.init(minimum.getTime(), maximum.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(current.getTime());

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                startActivity(new Intent(MonthActivity.this, DayActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(DayActivity.EXTRA_INITIAL_DATE, DateAndTime.dateToDateString(date))
                );
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
    }
}
