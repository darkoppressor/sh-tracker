/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.actionTemplates.AddActionTemplateActivity;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;

public class SelectActionActivity extends BaseActivity {
    public static final int REQUEST_CODE_CUSTOMIZE_SELECTION = 0;
    public static final int REQUEST_CODE_ADD_ACTION_TEMPLATE = 1;

    private ListView listView;

    private ArrayList<ActionTemplate> actionTemplates;
    private ActionAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_select_action, getString(R.string.select_action_title),
                new Menu(() -> R.menu.select_action, (item) -> {
                    switch (item.getItemId()) {
                        case R.id.actionCancel:
                            Vibration.buttonPress(this);

                            setResult(RESULT_CANCELED);

                            finish();

                            return true;
                        default:
                            return false;
                    }
                }));

        hideNavigationMenu();

        listView = findViewById(android.R.id.list);

        actionTemplates = null;

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

            case REQUEST_CODE_ADD_ACTION_TEMPLATE:
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

    private void loadUi () {
        ActionTemplateLoader.load(this, dao -> dao.getAll()
                .observe(this, actionTemplates -> {
                    if (actionTemplates != null) {
                        this.actionTemplates = (ArrayList<ActionTemplate>) actionTemplates;

                        buildUi();
                    }
                }));
    }

    private void buildUi () {
        if (actionTemplates != null) {
            ArrayList<ActionTemplate> actionTemplatesClean = new ArrayList<>();

            for (ActionTemplate actionTemplate : actionTemplates) {
                if (!actionTemplate.isDeleted()) {
                    actionTemplatesClean.add(actionTemplate);
                }
            }

            adapter = new ActionAdapter(this, actionTemplatesClean);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (position == adapter.getCount()) {
                    Vibration.buttonPress(this);

                    startActivityForResult(new Intent(this, AddActionTemplateActivity.class)
                                    .putExtra(AddActionTemplateActivity.EXTRA_CUSTOMIZE_AFTER,
                                            true), REQUEST_CODE_ADD_ACTION_TEMPLATE);
                } else {
                    final ActionTemplate item = adapter.getItem(position);

                    Vibration.buttonPress(this);

                    startActivityForResult(new Intent(this, CustomizeActionActivity.class)
                                    .putExtra(CustomizeActionActivity.EXTRA_TEMPLATE_ID,
                                            item.getId()), REQUEST_CODE_CUSTOMIZE_SELECTION
                    );
                }
            });

            if (listView.getFooterViewsCount() == 0) {
                View view = getLayoutInflater().inflate(R.layout.action_row, null);
                TextView textAction = view.findViewById(R.id.action);
                textAction.setText(getString(R.string.select_action_new_action));
                listView.addFooterView(view);
            }
        }
    }
}
