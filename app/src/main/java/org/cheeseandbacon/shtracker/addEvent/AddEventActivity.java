/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.addEvent;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.data.event.Action;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.data.event.Reason;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;
import org.cheeseandbacon.shtracker.day.DayActivity;
import org.cheeseandbacon.shtracker.util.DateAndTime;
import org.cheeseandbacon.shtracker.util.TimePickerFragment;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.UUID;

public class AddEventActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_DATE = "org.cheeseandbacon.shtracker.addEvent.date";
    public static final String EXTRA_TIME = "org.cheeseandbacon.shtracker.addEvent.time";
    public static final String EXTRA_REASON_TEMPLATE_ID = "org.cheeseandbacon.shtracker.addEvent.reasonTemplateId";
    public static final String EXTRA_REASON_COMMENT = "org.cheeseandbacon.shtracker.addEvent.reasonComment";
    public static final String EXTRA_REASON_SEVERITY = "org.cheeseandbacon.shtracker.addEvent.reasonSeverity";
    public static final String EXTRA_ACTION_TEMPLATE_ID = "org.cheeseandbacon.shtracker.addEvent.actionTemplateId";
    public static final String EXTRA_ACTION_COMMENT = "org.cheeseandbacon.shtracker.addEvent.actionComment";
    public static final String EXTRA_ACTION_SEVERITY = "org.cheeseandbacon.shtracker.addEvent.actionSeverity";
    public static final int REQUEST_CODE_REASON_SELECTION = 0;
    public static final int REQUEST_CODE_ACTION_SELECTION = 1;

    private TextView textTime;
    private TextView textReason;
    private ImageView imageReason;
    private TextView textAction;
    private ImageView imageAction;

    private String date;
    private String time;
    private Reason reason;
    private Action action;

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
                                    time,
                                    reason,
                                    action
                            ));

                            EventLoader.load(this, dao -> dao.insert(Event.class, data, () -> {
                                Intent intent = new Intent();
                                intent.putExtra(DayActivity.EXTRA_INITIAL_DATE, date);

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
        textReason = findViewById(R.id.reason);
        imageReason = findViewById(R.id.reasonImage);
        textAction = findViewById(R.id.action);
        imageAction = findViewById(R.id.actionImage);

        date = getIntent().getStringExtra(EXTRA_DATE);
        time = getIntent().getStringExtra(EXTRA_TIME);

        reason = null;
        action = null;

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

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_REASON_SELECTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String templateId = data.getStringExtra(EXTRA_REASON_TEMPLATE_ID);
                        String comment = data.getStringExtra(EXTRA_REASON_COMMENT);
                        int severity = data.getIntExtra(EXTRA_REASON_SEVERITY,
                                CustomizeReasonActivity.DEFAULT_SEVERITY);

                        reason = new Reason(templateId, comment, severity);

                        ReasonTemplateLoader.load(this, dao -> dao.getById(templateId)
                                .observe(this, reasonTemplate -> {
                                    if (reasonTemplate != null) {
                                        textReason.setText(reasonTemplate.getName());
                                        imageReason.setImageResource(R.drawable.ic_baseline_close_24px);
                                    }
                                }));
                    }
                }
                break;
            case REQUEST_CODE_ACTION_SELECTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String templateId = data.getStringExtra(EXTRA_ACTION_TEMPLATE_ID);
                        String comment = data.getStringExtra(EXTRA_ACTION_COMMENT);
                        int severity = data.getIntExtra(EXTRA_ACTION_SEVERITY,
                                CustomizeActionActivity.DEFAULT_SEVERITY);

                        action = new Action(templateId, comment, severity);

                        ActionTemplateLoader.load(this, dao -> dao.getById(templateId)
                                .observe(this, actionTemplate -> {
                                    if (actionTemplate != null) {
                                        textAction.setText(actionTemplate.getName());
                                        imageAction.setImageResource(R.drawable.ic_baseline_close_24px);
                                    }
                                }));
                    }
                }
                break;
            default:
                break;
        }
    }

    public void time (View view) {
        Vibration.buttonPress(this);

        TimePickerFragment.create(getSupportFragmentManager(), time);
    }

    public void reason (View view) {
        Vibration.buttonPress(this);

        if (reason == null) {
            startActivityForResult(new Intent(this, SelectReasonActivity.class), REQUEST_CODE_REASON_SELECTION);
        } else {
            reason = null;

            textReason.setText(R.string.add_event_reason);
            imageReason.setImageResource(R.drawable.ic_baseline_add_24px);
        }
    }

    public void action (View view) {
        Vibration.buttonPress(this);

        if (action == null) {
            startActivityForResult(new Intent(this, SelectActionActivity.class), REQUEST_CODE_ACTION_SELECTION);
        } else {
            action = null;

            textAction.setText(R.string.add_event_action);
            imageAction.setImageResource(R.drawable.ic_baseline_add_24px);
        }
    }
}
