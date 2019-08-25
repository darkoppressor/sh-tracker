/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.event;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.BaseEditText;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.util.Vibration;

public class CustomizeActionActivity extends BaseActivity {
    public static final String EXTRA_TEMPLATE_ID = "org.cheeseandbacon.shtracker.event.templateId";
    public static final String EXTRA_SEVERITY = "org.cheeseandbacon.shtracker.event.severity";
    public static final String EXTRA_COMMENT = "org.cheeseandbacon.shtracker.event.comment";

    public static final int SEVERITY_MINIMUM = 1;
    public static final int SEVERITY_MAXIMUM = 10;
    public static final int DEFAULT_SEVERITY = 1;

    private TextView action;
    private TextView description;
    private NumberPicker severity;
    private BaseEditText comment;

    private String templateId;

    private ActionTemplate actionTemplate;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_customize_action, getString(R.string.customize_action_title),
                new Menu(() -> R.menu.customize_action, (item) -> {
                    switch (item.getItemId()) {
                        case R.id.actionCancel:
                            Vibration.buttonPress(this);

                            setResult(RESULT_CANCELED);

                            finish();

                            return true;

                        case R.id.actionDone:
                            Vibration.buttonPress(this);

                            Intent intent = new Intent();
                            intent.putExtra(EventActivity.EXTRA_ACTION_TEMPLATE_ID, templateId);
                            intent.putExtra(EventActivity.EXTRA_ACTION_COMMENT, comment.getString());
                            intent.putExtra(EventActivity.EXTRA_ACTION_SEVERITY, severity.getValue());

                            setResult(RESULT_OK, intent);

                            finish();

                            return true;

                        default:
                            return false;
                    }
                }));

        hideNavigationMenu();

        action = findViewById(R.id.action);
        description = findViewById(R.id.description);
        severity = findViewById(R.id.severity);
        comment = findViewById(R.id.comment);

        severity.setMinValue(SEVERITY_MINIMUM);
        severity.setMaxValue(SEVERITY_MAXIMUM);
        severity.setWrapSelectorWheel(false);

        templateId = getIntent().getStringExtra(EXTRA_TEMPLATE_ID);
        int initialSeverity = getIntent().getIntExtra(EXTRA_SEVERITY, DEFAULT_SEVERITY);
        String initialComment = getIntent().getStringExtra(EXTRA_COMMENT);

        severity.setValue(initialSeverity);
        if (initialComment != null && initialComment.length() > 0) {
            comment.setText(initialComment);
        }

        actionTemplate = null;

        loadUi();
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    private void loadUi () {
        ActionTemplateLoader.load(this, dao -> dao.getById(templateId).observe(this, actionTemplate -> {
            if (actionTemplate != null) {
                this.actionTemplate = actionTemplate;

                buildUi();
            }
        }));
    }

    private void buildUi () {
        if (actionTemplate != null) {
            action.setText(actionTemplate.getName());

            if (actionTemplate.getDescription().length() > 0) {
                description.setText(actionTemplate.getDescription());
            } else {
                description.setVisibility(View.GONE);
            }
        }
    }

    public void helpSeverity (View view) {
        Vibration.buttonPress(this);

        new AlertDialog.Builder(this)
                .setTitle(R.string.customize_action_help_severity_title)
                .setMessage(R.string.customize_action_help_severity_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    Vibration.buttonPress(this);

                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
}
