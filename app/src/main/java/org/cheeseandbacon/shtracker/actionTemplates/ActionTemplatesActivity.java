/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.actionTemplates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.event.ActionAdapter;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;

public class ActionTemplatesActivity extends BaseActivity {
    public static final int REQUEST_CODE_ADD_ACTION_TEMPLATE = 0;
    public static final int REQUEST_CODE_EDIT_ACTION_TEMPLATE = 1;

    private ListView listView;

    private ArrayList<ActionTemplate> actionTemplates;
    private ActionAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_action_templates, getString(R.string.action_templates_title), null);

        hideNavigationMenu();

        ///QQQ Add back button in place of nav menu

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
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ADD_ACTION_TEMPLATE:
                if (resultCode == RESULT_OK) {
                    recreate();
                }
                break;

            case REQUEST_CODE_EDIT_ACTION_TEMPLATE:
                if (resultCode == RESULT_OK) {
                    recreate();
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
            adapter = new ActionAdapter(this, actionTemplates);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                final ActionTemplate item = adapter.getItem(position);

                startActivityForResult(new Intent(this, AddActionTemplateActivity.class)
                                .putExtra(AddActionTemplateActivity.EXTRA_TEMPLATE_ID, item.getId()),
                        REQUEST_CODE_EDIT_ACTION_TEMPLATE
                );
            });
        }
    }

    public void addActionTemplate (View view) {
        Vibration.buttonPress(this);

        startActivityForResult(new Intent(this, AddActionTemplateActivity.class),
                REQUEST_CODE_ADD_ACTION_TEMPLATE);
    }
}
