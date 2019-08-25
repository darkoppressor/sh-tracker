/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.event;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

public class EventActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_DATE = "org.cheeseandbacon.shtracker.event.date";
    public static final String EXTRA_TIME = "org.cheeseandbacon.shtracker.event.time";
    public static final String EXTRA_EVENT_ID = "org.cheeseandbacon.shtracker.event.eventId";
    public static final String EXTRA_REASON_TEMPLATE_ID = "org.cheeseandbacon.shtracker.event.reasonTemplateId";
    public static final String EXTRA_REASON_COMMENT = "org.cheeseandbacon.shtracker.event.reasonComment";
    public static final String EXTRA_REASON_SEVERITY = "org.cheeseandbacon.shtracker.event.reasonSeverity";
    public static final String EXTRA_ACTION_TEMPLATE_ID = "org.cheeseandbacon.shtracker.event.actionTemplateId";
    public static final String EXTRA_ACTION_COMMENT = "org.cheeseandbacon.shtracker.event.actionComment";
    public static final String EXTRA_ACTION_SEVERITY = "org.cheeseandbacon.shtracker.event.actionSeverity";
    public static final int REQUEST_CODE_REASON_SELECTION = 0;
    public static final int REQUEST_CODE_ACTION_SELECTION = 1;
    public static final int REQUEST_CODE_EDIT_REASON = 2;
    public static final int REQUEST_CODE_EDIT_ACTION = 3;

    private TextView textTime;
    private TextView textReason;
    private ImageView imageReason;
    private TextView textAction;
    private ImageView imageAction;
    private AppCompatCheckBox checkBoxUrge;

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    private String date;
    private String time;

    private String eventId;
    private Event event;

    private Reason reason;
    private Action action;

    private boolean urge;

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

                            done();

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
        checkBoxUrge = findViewById(R.id.urge);

        reason = null;
        action = null;
        urge = false;

        date = getIntent().getStringExtra(EXTRA_DATE);
        time = getIntent().getStringExtra(EXTRA_TIME);
        eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);

        onCheckedChangeListener = (compoundButton, b) -> urge = b;

        checkBoxUrge.setOnCheckedChangeListener(onCheckedChangeListener);

        if (eventId != null) {
            setToolbarTitle(getString(R.string.add_event_edit_title));

            setMenu(new Menu(() -> R.menu.edit_event, (item) -> {
                switch (item.getItemId()) {
                    case R.id.actionCancel:
                        Vibration.buttonPress(this);

                        setResult(RESULT_CANCELED);

                        finish();

                        return true;

                    case R.id.actionDone:
                        Vibration.buttonPress(this);

                        done();

                        return true;

                    case R.id.actionDelete:
                        Vibration.buttonPress(this);

                        ArrayList<Event> data = new ArrayList<>();

                        event.setTime(time);
                        event.setReason(reason);
                        event.setAction(action);
                        event.setUrge(urge);

                        data.add(event);

                        EventLoader.load(this, dao -> dao.delete(Event.class, data, () -> {
                            Intent intent = new Intent();
                            intent.putExtra(DayActivity.EXTRA_INITIAL_DATE, event.getDate());

                            setResult(RESULT_OK, intent);

                            finish();
                        }));

                        return true;

                    default:
                        return false;
                }
            }));

            EventLoader.load(this, dao -> dao.getById(eventId).observe(this, event -> {
                if (event != null) {
                    this.event = event;

                    date = event.getDate();
                    time = event.getTime();
                    reason = event.getReason();
                    action = event.getAction();
                    urge = event.isUrge();

                    textTime.setText(time);

                    checkBoxUrge.setOnCheckedChangeListener(null);
                    checkBoxUrge.setChecked(urge);
                    checkBoxUrge.setOnCheckedChangeListener(onCheckedChangeListener);

                    if (reason != null) {
                        ReasonTemplateLoader.load(this, reasonTemplateDao ->
                                reasonTemplateDao.getById(reason.getTemplateId()).observe(this, reasonTemplate -> {
                                    if (reasonTemplate != null) {
                                        textReason.setText(reasonTemplate.getName());
                                        textReason.setTextColor(getColor(R.color.gray));
                                        imageReason.setImageResource(R.drawable.ic_baseline_close_24px);
                                    }
                                }));
                    }

                    if (action != null) {
                        ActionTemplateLoader.load(this, actionTemplateDao ->
                                actionTemplateDao.getById(action.getTemplateId()).observe(this, actionTemplate -> {
                                    if (actionTemplate != null) {
                                        textAction.setText(actionTemplate.getName());
                                        textAction.setTextColor(getColor(R.color.gray));
                                        imageAction.setImageResource(R.drawable.ic_baseline_close_24px);
                                    }
                                }));
                    }
                }
            }));
        }

        textTime.setText(time);

        checkBoxUrge.setOnCheckedChangeListener(null);
        checkBoxUrge.setChecked(urge);
        checkBoxUrge.setOnCheckedChangeListener(onCheckedChangeListener);
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
            case REQUEST_CODE_EDIT_REASON:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String templateId = data.getStringExtra(EXTRA_REASON_TEMPLATE_ID);
                        String comment = data.getStringExtra(EXTRA_REASON_COMMENT);
                        int severity = data.getIntExtra(EXTRA_REASON_SEVERITY,
                                CustomizeReasonActivity.DEFAULT_SEVERITY);

                        if (templateId != null && comment != null) {
                            reason = new Reason(templateId, comment, severity);

                            ReasonTemplateLoader.load(this, dao ->
                                    dao.getById(templateId).observe(this, reasonTemplate -> {
                                        if (reasonTemplate != null) {
                                            textReason.setText(reasonTemplate.getName());
                                            textReason.setTextColor(getColor(R.color.gray));
                                            imageReason.setImageResource(R.drawable.ic_baseline_close_24px);
                                        }
                                    }));
                        }
                    }
                }
                break;

            case REQUEST_CODE_ACTION_SELECTION:
            case REQUEST_CODE_EDIT_ACTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String templateId = data.getStringExtra(EXTRA_ACTION_TEMPLATE_ID);
                        String comment = data.getStringExtra(EXTRA_ACTION_COMMENT);
                        int severity = data.getIntExtra(EXTRA_ACTION_SEVERITY,
                                CustomizeActionActivity.DEFAULT_SEVERITY);

                        if (templateId != null && comment != null) {
                            action = new Action(templateId, comment, severity);

                            ActionTemplateLoader.load(this, dao ->
                                    dao.getById(templateId).observe(this, actionTemplate -> {
                                        if (actionTemplate != null) {
                                            textAction.setText(actionTemplate.getName());
                                            textAction.setTextColor(getColor(R.color.gray));
                                            imageAction.setImageResource(R.drawable.ic_baseline_close_24px);
                                        }
                                    }));
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    private void done () {
        if (eventId == null) {
            ArrayList<Event> data = new ArrayList<>();

            data.add(new Event(
                    UUID.randomUUID().toString(),
                    date,
                    time,
                    reason,
                    action,
                    urge
            ));

            EventLoader.load(this, dao -> dao.insert(Event.class, data, () -> {
                Intent intent = new Intent();
                intent.putExtra(DayActivity.EXTRA_INITIAL_DATE, date);

                setResult(RESULT_OK, intent);

                finish();
            }));
        } else {
            ArrayList<Event> data = new ArrayList<>();

            event.setTime(time);
            event.setReason(reason);
            event.setAction(action);
            event.setUrge(urge);

            data.add(event);

            EventLoader.load(this, dao -> dao.update(Event.class, data, () -> {
                Intent intent = new Intent();
                intent.putExtra(DayActivity.EXTRA_INITIAL_DATE, event.getDate());

                setResult(RESULT_OK, intent);

                finish();
            }));
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
            startActivityForResult(new Intent(this, CustomizeReasonActivity.class)
                            .putExtra(CustomizeReasonActivity.EXTRA_TEMPLATE_ID, reason.getTemplateId())
                            .putExtra(CustomizeReasonActivity.EXTRA_SEVERITY, reason.getSeverity())
                            .putExtra(CustomizeReasonActivity.EXTRA_COMMENT, reason.getComment()),
                    REQUEST_CODE_EDIT_REASON);
        }
    }

    public void reasonButton (View view) {
        Vibration.buttonPress(this);

        if (reason == null) {
            startActivityForResult(new Intent(this, SelectReasonActivity.class), REQUEST_CODE_REASON_SELECTION);
        } else {
            reason = null;

            textReason.setText(R.string.add_event_reason);
            textReason.setTextColor(getColor(android.R.color.darker_gray));
            imageReason.setImageResource(R.drawable.ic_baseline_add_24px);
        }
    }

    public void action (View view) {
        Vibration.buttonPress(this);

        if (action == null) {
            startActivityForResult(new Intent(this, SelectActionActivity.class), REQUEST_CODE_ACTION_SELECTION);
        } else {
            startActivityForResult(new Intent(this, CustomizeActionActivity.class)
                            .putExtra(CustomizeActionActivity.EXTRA_TEMPLATE_ID, action.getTemplateId())
                            .putExtra(CustomizeActionActivity.EXTRA_SEVERITY, action.getSeverity())
                            .putExtra(CustomizeActionActivity.EXTRA_COMMENT, action.getComment()),
                    REQUEST_CODE_EDIT_ACTION);
        }
    }

    public void actionButton (View view) {
        Vibration.buttonPress(this);

        if (action == null) {
            startActivityForResult(new Intent(this, SelectActionActivity.class), REQUEST_CODE_ACTION_SELECTION);
        } else {
            action = null;

            textAction.setText(R.string.add_event_action);
            textAction.setTextColor(getColor(android.R.color.darker_gray));
            imageAction.setImageResource(R.drawable.ic_baseline_add_24px);
        }
    }

    public void urge (View view) {
        Vibration.buttonPress(this);

        checkBoxUrge.toggle();
    }
}
