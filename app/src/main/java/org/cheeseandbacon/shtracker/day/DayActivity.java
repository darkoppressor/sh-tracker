/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.event.EventActivity;
import org.cheeseandbacon.shtracker.util.DateAndTime;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayActivity extends BaseActivity {
    public static final String EXTRA_INITIAL_DATE = "org.cheeseandbacon.shtracker.day.initialDate";
    public static final int REQUEST_CODE_EVENT_ADDITION = 0;
    public static final int REQUEST_CODE_EDIT_EVENT = 1;

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
        onCreate(R.layout.activity_day, getString(R.string.day_title), new Menu(() -> R.menu.day, (item) -> {
            if (item.getItemId() == R.id.actionJumpToToday) {
                Vibration.buttonPress(this);

                loadDate(Calendar.getInstance());

                return true;
            }

            return false;
        }));

        textDateBefore = findViewById(R.id.mainDateBefore);
        textDayOfWeekBefore = findViewById(R.id.mainDayOfWeekBefore);
        textDateCurrent = findViewById(R.id.mainDateCurrent);
        textDayOfWeekCurrent = findViewById(R.id.mainDayOfWeekCurrent);
        textDateAfter = findViewById(R.id.mainDateAfter);
        textDayOfWeekAfter = findViewById(R.id.mainDayOfWeekAfter);
        listView = findViewById(android.R.id.list);

        String initialDate = getIntent().getStringExtra(EXTRA_INITIAL_DATE);

        Calendar calendar = Calendar.getInstance();

        if (initialDate != null) {
            Date date = DateAndTime.dateStringToDate(initialDate);

            if (date != null) {
                calendar.setTime(date);
            }
        }

        loadDate(calendar);
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
            case REQUEST_CODE_EDIT_EVENT:
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
        EventLoader.load(this, dao -> {
            liveData = dao.getByDate(DateAndTime.dateToDateString(dates.getCurrent()));

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
            listView.setOnItemClickListener((parent, view, position, id) -> {
                final Event item = adapter.getItem(position);

                startActivityForResult(new Intent(this, EventActivity.class)
                        .putExtra(EventActivity.EXTRA_EVENT_ID, item.getId())
                        , REQUEST_CODE_EDIT_EVENT
                );
            });
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
            textDate.setText(getString(R.string.day_date_yesterday));
        } else if (DateAndTime.isToday(date)) {
            textDate.setText(getString(R.string.day_date_today));
        } else if (DateAndTime.isTomorrow(date)) {
            textDate.setText(getString(R.string.day_date_tomorrow));
        } else {
            textDate.setText(DateAndTime.dateToDateString(date));
        }

        textDayOfWeek.setText(DateAndTime.dateToDayOfWeekString(date));
    }

    private void loadDate (final Calendar calendar) {
        dates = new Dates(calendar);

        setDateTexts();

        liveData = null;
        events = null;
        adapter = null;

        listView.setAdapter(null);

        loadUi();
    }

    public void scrollBack (View view) {
        Vibration.buttonPress(this);

        Calendar newCurrent = Calendar.getInstance();
        newCurrent.setTime(dates.getBefore());

        loadDate(newCurrent);
    }

    public void scrollForward (View view) {
        Vibration.buttonPress(this);

        Calendar newCurrent = Calendar.getInstance();
        newCurrent.setTime(dates.getAfter());

        loadDate(newCurrent);
    }

    public void addEvent (View view) {
        Vibration.buttonPress(this);

        Calendar calendar = Calendar.getInstance();

        String time = DateAndTime.dateToTimeString(calendar.getTime());

        if (DateAndTime.dayIsBefore(dates.getCurrent(), calendar.getTime())) {
            time = DateAndTime.LAST_MINUTE_OF_DAY;
        }

        startActivityForResult(new Intent(this, EventActivity.class)
                .putExtra(EventActivity.EXTRA_DATE, DateAndTime.dateToDateString(dates.getCurrent()))
                .putExtra(EventActivity.EXTRA_TIME, time), REQUEST_CODE_EVENT_ADDITION
        );
    }
}
