/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.addEvent.AddEventActivity;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.util.DateAndTime;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BaseActivity {
    private TextView textDateBefore;
    private TextView textDayOfWeekBefore;
    private TextView textDateCurrent;
    private TextView textDayOfWeekCurrent;
    private TextView textDateAfter;
    private TextView textDayOfWeekAfter;
    private ListView listView;

    private Dates dates;
    private ArrayList<Event> events;
    private MainAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_main, getString(R.string.main_title), null);

        textDateBefore = findViewById(R.id.mainDateBefore);
        textDayOfWeekBefore = findViewById(R.id.mainDayOfWeekBefore);
        textDateCurrent = findViewById(R.id.mainDateCurrent);
        textDayOfWeekCurrent = findViewById(R.id.mainDayOfWeekCurrent);
        textDateAfter = findViewById(R.id.mainDateAfter);
        textDayOfWeekAfter = findViewById(R.id.mainDayOfWeekAfter);
        listView = findViewById(android.R.id.list);

        dates = new Dates(Calendar.getInstance());

        setDateTexts();

        adapter = null;

        loadUi(dates.getCurrent());
    }

    @Override
    public void onResume () {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetInvalidated();
        }
    }

    private void loadUi (@NonNull final Date date) {
        EventLoader.load(this, eventDao -> eventDao.getByDate(DateAndTime.formatDateMdy(date)).observe(this, events -> {
            if (events != null) {
                this.events = (ArrayList<Event>) events;

                buildUi();
            }
        }));
    }

    private void buildUi () {
        if (events != null) {
            adapter = new MainAdapter(this, events);
            listView.setAdapter(adapter);
        }
    }

    private void setDateTexts () {
        setDateText(dates.getBefore(), textDateBefore, textDayOfWeekBefore);
        setDateText(dates.getCurrent(), textDateCurrent, textDayOfWeekCurrent);
        setDateText(dates.getAfter(), textDateAfter, textDayOfWeekAfter);
    }

    private void setDateText (@NonNull final Date date, @NonNull final TextView textDate,
                              @NonNull final TextView textDayOfWeek) {
        if (DateAndTime.isYesterday(date)) {
            textDate.setText(getString(R.string.main_date_yesterday));
        } else if (DateAndTime.isToday(date)) {
            textDate.setText(getString(R.string.main_date_today));
        } else if (DateAndTime.isTomorrow(date)) {
            textDate.setText(getString(R.string.main_date_tomorrow));
        } else {
            textDate.setText(DateAndTime.formatDateMdy(date));
        }

        textDayOfWeek.setText(DateAndTime.formatDateDayOfWeek(date));
    }

    public void addEvent (View view) {
        Vibration.buttonPress(this);

        Calendar calendar = Calendar.getInstance();

        String time = DateAndTime.formatTimeHms(calendar.getTime());

        if (DateAndTime.dayIsBefore(dates.getCurrent(), calendar.getTime())) {
            time = "23:59:59";
        }

        startActivity(new Intent(this, AddEventActivity.class)
                .putExtra(AddEventActivity.EXTRA_DATE, DateAndTime.formatDateMdy(dates.getCurrent()))
                .putExtra(AddEventActivity.EXTRA_TIME, time)
        );
    }
}
