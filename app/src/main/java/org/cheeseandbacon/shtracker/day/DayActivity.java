/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.day;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class DayActivity extends BaseActivity {
    public static final String EXTRA_INITIAL_DATE = "org.cheeseandbacon.shtracker.main.initialDate";
    public static final int REQUEST_CODE_EVENT_ADDITION = 0;

    private TextView textDateBefore;
    private TextView textDayOfWeekBefore;
    private TextView textDateCurrent;
    private TextView textDayOfWeekCurrent;
    private TextView textDateAfter;
    private TextView textDayOfWeekAfter;
    private ListView listView;

    private Dates dates;
    private LiveData<List<Event>> liveData;
    private ArrayList<Event> events;
    private DayAdapter adapter;

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
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnLongClickListener(v -> {
            Toast.makeText(this, getString(R.string.add_event_title), Toast.LENGTH_SHORT).show();

            return true;
        });

        String initialDate = getIntent().getStringExtra(EXTRA_INITIAL_DATE);

        Calendar calendar = Calendar.getInstance();

        if (initialDate != null) {
            Date date = DateAndTime.dateStringToDate(initialDate);

            if (date != null) {
                calendar.setTime(date);
            }
        }

        dates = new Dates(calendar);

        setDateTexts();

        liveData = null;
        events = null;
        adapter = null;

        loadUi();
    }

    @Override
    public void onResume () {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetInvalidated();
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_EVENT_ADDITION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String date = data.getStringExtra(EXTRA_INITIAL_DATE);

                        getIntent().removeExtra(EXTRA_INITIAL_DATE);
                        getIntent().putExtra(EXTRA_INITIAL_DATE, date);
                    }

                    recreate();
                }
                break;
            default:
                break;
        }
    }

    private void loadUi () {
        EventLoader.load(this, eventDao -> {
            liveData = eventDao.getByDate(DateAndTime.dateToDateString(dates.getCurrent()));

            liveData.observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged (@Nullable List<Event> events) {
                    if (events != null) {
                        liveData.removeObserver(this);

                        liveData = null;

                        DayActivity.this.events = (ArrayList<Event>) events;

                        buildUi();
                    }
                }
            });
        });
    }

    private void buildUi () {
        if (events != null) {
            adapter = new DayAdapter(this, events);
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
            textDate.setText(DateAndTime.dateToDateString(date));
        }

        textDayOfWeek.setText(DateAndTime.dateToDayOfWeekString(date));
    }

    public void scrollBack (View view) {
        Vibration.buttonPress(this);

        Calendar newCurrent = Calendar.getInstance();
        newCurrent.setTime(dates.getBefore());

        dates = new Dates(newCurrent);

        setDateTexts();

        events = null;
        adapter = null;

        listView.setAdapter(null);

        loadUi();
    }

    public void scrollForward (View view) {
        Vibration.buttonPress(this);

        Calendar newCurrent = Calendar.getInstance();
        newCurrent.setTime(dates.getAfter());

        dates = new Dates(newCurrent);

        setDateTexts();

        events = null;
        adapter = null;

        listView.setAdapter(null);

        loadUi();
    }

    public void addEvent (View view) {
        Vibration.buttonPress(this);

        Calendar calendar = Calendar.getInstance();

        String time = DateAndTime.dateToTimeString(calendar.getTime());

        if (DateAndTime.dayIsBefore(dates.getCurrent(), calendar.getTime())) {
            time = DateAndTime.LAST_MINUTE_OF_DAY;
        }

        startActivityForResult(new Intent(this, AddEventActivity.class)
                .putExtra(AddEventActivity.EXTRA_DATE, DateAndTime.dateToDateString(dates.getCurrent()))
                .putExtra(AddEventActivity.EXTRA_TIME, time), REQUEST_CODE_EVENT_ADDITION
        );
    }
}
