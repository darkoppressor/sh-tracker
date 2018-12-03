/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.addEvent;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.main.MainActivity;
import org.cheeseandbacon.shtracker.util.DateAndTime;
import org.cheeseandbacon.shtracker.util.TimePickerFragment;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.UUID;

public class AddEventActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_DATE = "org.cheeseandbacon.shtracker.addEvent.date";
    public static final String EXTRA_TIME = "org.cheeseandbacon.shtracker.addEvent.time";

    private TextView textTime;

    private String date;
    private String time;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_add_event, getString(R.string.add_event_title),
                new Menu(() -> R.menu.add_event, (item) -> {
                    switch (item.getItemId()) {
                        case R.id.actionCancel:
                            Vibration.buttonPress(this);

                            setResult(RESULT_CANCELED);

                            finish();

                            return true;
                        case R.id.actionDone:
                            Vibration.buttonPress(this);

                            ArrayList<Event> data = new ArrayList<>();

                            data.add(new Event(
                                    UUID.randomUUID().toString(),
                                    date,
                                    time
                            ));

                            EventLoader.load(this, eventDao -> eventDao.insert(Event.class, data, () -> {
                                Intent intent = new Intent();
                                intent.putExtra(MainActivity.EXTRA_INITIAL_DATE, date);

                                setResult(RESULT_OK, intent);

                                finish();
                            }));

                            return true;
                        default:
                            return false;
                    }
                }));

        hideNavigationMenu();

        textTime = findViewById(R.id.time);

        date = getIntent().getStringExtra(EXTRA_DATE);
        time = getIntent().getStringExtra(EXTRA_TIME);

        textTime.setText(time);
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    @Override
    public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
        time = DateAndTime.getTimeString(hourOfDay, minute);

        textTime.setText(time);
    }

    public void time (View view) {
        Vibration.buttonPress(this);

        TimePickerFragment.create(getSupportFragmentManager(), time);
    }
}
