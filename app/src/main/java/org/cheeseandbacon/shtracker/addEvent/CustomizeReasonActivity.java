/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.addEvent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.BaseEditText;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;
import org.cheeseandbacon.shtracker.util.Vibration;

public class CustomizeReasonActivity extends BaseActivity {
    public static final String EXTRA_TEMPLATE_ID = "org.cheeseandbacon.shtracker.addEvent.templateId";

    public static final int SEVERITY_MINIMUM = 1;
    public static final int SEVERITY_MAXIMUM = 10;
    public static final int DEFAULT_SEVERITY = 1;

    private TextView reason;
    private NumberPicker severity;
    private BaseEditText comment;

    private String templateId;

    private ReasonTemplate reasonTemplate;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_customize_reason, getString(R.string.customize_reason_title),
                new Menu(() -> R.menu.customize_reason, (item) -> {
                    switch (item.getItemId()) {
                        case R.id.actionCancel:
                            Vibration.buttonPress(this);

                            setResult(RESULT_CANCELED);

                            finish();

                            return true;
                        case R.id.actionDone:
                            Vibration.buttonPress(this);

                            Intent intent = new Intent();
                            intent.putExtra(AddEventActivity.EXTRA_REASON_TEMPLATE_ID, templateId);
                            intent.putExtra(AddEventActivity.EXTRA_REASON_COMMENT, comment.getString());
                            intent.putExtra(AddEventActivity.EXTRA_REASON_SEVERITY, severity.getValue());

                            setResult(RESULT_OK, intent);

                            finish();

                            return true;
                        default:
                            return false;
                    }
                }));

        hideNavigationMenu();

        reason = findViewById(R.id.reason);
        severity = findViewById(R.id.severity);
        comment = findViewById(R.id.comment);

        severity.setMinValue(SEVERITY_MINIMUM);
        severity.setMaxValue(SEVERITY_MAXIMUM);
        severity.setWrapSelectorWheel(false);
        severity.setValue(DEFAULT_SEVERITY);

        templateId = getIntent().getStringExtra(EXTRA_TEMPLATE_ID);

        reasonTemplate = null;

        loadUi();
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    private void loadUi () {
        ReasonTemplateLoader.load(this, dao -> dao.getById(templateId)
                .observe(this, reasonTemplate -> {
                    if (reasonTemplate != null) {
                        this.reasonTemplate = reasonTemplate;

                        buildUi();
                    }
                }));
    }

    private void buildUi () {
        if (reasonTemplate != null) {
            reason.setText(reasonTemplate.getName());
        }
    }
}
