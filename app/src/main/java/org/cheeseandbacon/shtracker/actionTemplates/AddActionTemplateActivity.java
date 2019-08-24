/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.actionTemplates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.BaseEditText;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.event.CustomizeActionActivity;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddActionTemplateActivity extends BaseActivity {
    public static final String EXTRA_TEMPLATE_ID =
            "org.cheeseandbacon.shtracker.actionTemplates.templateId";
    public static final String EXTRA_CUSTOMIZE_AFTER =
            "org.cheeseandbacon.shtracker.actionTemplates.customizeAfter";
    public static final int REQUEST_CODE_CUSTOMIZE_SELECTION = 0;

    private BaseEditText name;
    private BaseEditText description;

    private String templateId;
    private boolean customizeAfter;

    private ActionTemplate actionTemplate;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_add_action_template,
                getString(R.string.add_action_template_title),
                new Menu(() -> R.menu.add_action_template, (item) -> {
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

        actionTemplate = null;

        if (templateId != null) {
            setToolbarTitle(getString(R.string.add_action_template_edit_title));

            setMenu(new Menu(() -> R.menu.edit_action_template, (item) -> {
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

                        ArrayList<ActionTemplate> data = new ArrayList<>();

                        actionTemplate.setDeleted(true);

                        data.add(actionTemplate);

                        ActionTemplateLoader.load(this, dao ->
                                dao.update(ActionTemplate.class, data, () -> {
                                    setResult(RESULT_OK);

                                    finish();
                                }));

                        return true;
                    default:
                        return false;
                }
            }));

            ActionTemplateLoader.load(this, dao -> dao.getById(templateId)
                    .observe(this, actionTemplate -> {
                        if (actionTemplate != null) {
                            this.actionTemplate = actionTemplate;

                            name.setText(actionTemplate.getName());
                            description.setText(actionTemplate.getDescription());
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
            Toast.makeText(this, getString(R.string.add_action_template_name_needed),
                    Toast.LENGTH_LONG).show();

            return;
        }

        if (templateId == null) {
            String id = UUID.randomUUID().toString();

            ArrayList<ActionTemplate> data = new ArrayList<>();
            data.add(new ActionTemplate(
                    id,
                    Calendar.getInstance().getTime().getTime(),
                    name.getString(),
                    description.getString(),
                    false
            ));

            if (customizeAfter) {
                ActionTemplateLoader.load(this, (dao) ->
                        dao.insert(ActionTemplate.class, data, () -> {
                            startActivityForResult(new Intent(this, CustomizeActionActivity.class)
                                            .putExtra(CustomizeActionActivity.EXTRA_TEMPLATE_ID, id),
                                    REQUEST_CODE_CUSTOMIZE_SELECTION
                            );
                        }));
            } else {
                ActionTemplateLoader.load(this, (dao) ->
                        dao.insert(ActionTemplate.class, data, () -> {
                            setResult(RESULT_OK);

                            finish();
                        }));
            }
        } else {
            ArrayList<ActionTemplate> data = new ArrayList<>();

            actionTemplate.setName(name.getString());
            actionTemplate.setDescription(description.getString());

            data.add(actionTemplate);

            ActionTemplateLoader.load(this, (dao) -> dao.update(ActionTemplate.class, data, () -> {
                setResult(RESULT_OK);

                finish();
            }));
        }
    }
}
