/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.reasonTemplates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.BaseEditText;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;
import org.cheeseandbacon.shtracker.event.CustomizeReasonActivity;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddReasonTemplateActivity extends BaseActivity {
    public static final String EXTRA_TEMPLATE_ID = "org.cheeseandbacon.shtracker.reasonTemplates.templateId";
    public static final String EXTRA_CUSTOMIZE_AFTER = "org.cheeseandbacon.shtracker.reasonTemplates.customizeAfter";
    public static final int REQUEST_CODE_CUSTOMIZE_SELECTION = 0;

    private BaseEditText name;
    private BaseEditText description;

    private String templateId;
    private boolean customizeAfter;

    private ReasonTemplate reasonTemplate;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_add_reason_template,
                getString(R.string.add_reason_template_title),
                new Menu(() -> R.menu.add_reason_template, (item) -> {
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

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);

        templateId = getIntent().getStringExtra(EXTRA_TEMPLATE_ID);
        customizeAfter = getIntent().getBooleanExtra(EXTRA_CUSTOMIZE_AFTER, false);

        reasonTemplate = null;

        if (templateId != null) {
            setToolbarTitle(getString(R.string.add_reason_template_edit_title));

            setMenu(new Menu(() -> R.menu.edit_reason_template, (item) -> {
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

                        ArrayList<ReasonTemplate> data = new ArrayList<>();

                        reasonTemplate.setName(name.getString());
                        reasonTemplate.setDescription(description.getString());

                        data.add(reasonTemplate);

                        ReasonTemplateLoader.load(this, dao -> dao.delete(ReasonTemplate.class, data, () -> {
                            setResult(RESULT_OK);

                            finish();
                        }));

                        return true;
                    default:
                        return false;
                }
            }));

            ReasonTemplateLoader.load(this, dao -> dao.getById(templateId)
                    .observe(this, reasonTemplate -> {
                        if (reasonTemplate != null) {
                            this.reasonTemplate = reasonTemplate;

                            name.setText(reasonTemplate.getName());
                            description.setText(reasonTemplate.getDescription());
                        }
                    }));
        }
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CUSTOMIZE_SELECTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        setResult(RESULT_OK, data);

                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void done () {
        if (name.getString().length() == 0) {
            Toast.makeText(this, getString(R.string.add_reason_template_name_needed),
                    Toast.LENGTH_LONG).show();

            return;
        }

        if (templateId == null) {
            String id = UUID.randomUUID().toString();

            ArrayList<ReasonTemplate> data = new ArrayList<>();
            data.add(new ReasonTemplate(
                    id,
                    Calendar.getInstance().getTime().getTime(),
                    name.getString(),
                    description.getString()
            ));

            if (customizeAfter) {
                ReasonTemplateLoader.load(this, (dao) -> dao.insert(ReasonTemplate.class, data, () -> {
                    startActivityForResult(new Intent(this, CustomizeReasonActivity.class)
                                    .putExtra(CustomizeReasonActivity.EXTRA_TEMPLATE_ID, id),
                            REQUEST_CODE_CUSTOMIZE_SELECTION
                    );
                }));
            } else {
                ReasonTemplateLoader.load(this, (dao) -> dao.insert(ReasonTemplate.class, data, () -> {
                    setResult(RESULT_OK);

                    finish();
                }));
            }
        } else {
            setResult(RESULT_OK);

            finish();
        }
    }
}
