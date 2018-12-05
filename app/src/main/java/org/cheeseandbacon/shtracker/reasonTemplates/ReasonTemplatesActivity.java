/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.reasonTemplates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;
import org.cheeseandbacon.shtracker.event.ReasonAdapter;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;

public class ReasonTemplatesActivity extends BaseActivity {
    public static final int REQUEST_CODE_ADD_REASON_TEMPLATE = 0;
    public static final int REQUEST_CODE_EDIT_REASON_TEMPLATE = 1;

    private ListView listView;

    private ArrayList<ReasonTemplate> reasonTemplates;
    private ReasonAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_reason_templates, getString(R.string.reason_templates_title), null);

        listView = findViewById(android.R.id.list);

        reasonTemplates = null;

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
            case REQUEST_CODE_ADD_REASON_TEMPLATE:
                if (resultCode == RESULT_OK) {
                    recreate();
                }
                break;

            case REQUEST_CODE_EDIT_REASON_TEMPLATE:
                if (resultCode == RESULT_OK) {
                    recreate();
                }
                break;
            default:
                break;
        }
    }

    private void loadUi () {
        ReasonTemplateLoader.load(this, dao -> dao.getAll()
                .observe(this, reasonTemplates -> {
                    if (reasonTemplates != null) {
                        this.reasonTemplates = (ArrayList<ReasonTemplate>) reasonTemplates;

                        buildUi();
                    }
                }));
    }

    private void buildUi () {
        if (reasonTemplates != null) {
            adapter = new ReasonAdapter(this, reasonTemplates);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                final ReasonTemplate item = adapter.getItem(position);

                startActivityForResult(new Intent(this, AddReasonTemplateActivity.class)
                                .putExtra(AddReasonTemplateActivity.EXTRA_TEMPLATE_ID, item.getId()),
                        REQUEST_CODE_EDIT_REASON_TEMPLATE
                );
            });
        }
    }

    public void addReasonTemplate (View view) {
        Vibration.buttonPress(this);

        startActivityForResult(new Intent(this, AddReasonTemplateActivity.class),
                REQUEST_CODE_ADD_REASON_TEMPLATE);
    }
}
