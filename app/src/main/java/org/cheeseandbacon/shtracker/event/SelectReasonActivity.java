/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.base.Menu;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;
import org.cheeseandbacon.shtracker.reasonTemplates.AddReasonTemplateActivity;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class SelectReasonActivity extends BaseActivity {
    public static final int REQUEST_CODE_CUSTOMIZE_SELECTION = 0;
    public static final int REQUEST_CODE_ADD_REASON_TEMPLATE = 1;

    private ListView listView;

    private ArrayList<ReasonTemplate> reasonTemplates;
    private ReasonAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_select_reason, getString(R.string.select_reason_title),
                new Menu(() -> R.menu.select_reason, (item) -> {
                    if (item.getItemId() == R.id.actionCancel) {
                        Vibration.buttonPress(this);

                        setResult(RESULT_CANCELED);

                        finish();

                        return true;
                    }

                    return false;
                }));

        hideNavigationMenu();

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
    public void onBackPressed () {
        setResult(RESULT_CANCELED);

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CUSTOMIZE_SELECTION:
            case REQUEST_CODE_ADD_REASON_TEMPLATE:
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
        ReasonTemplateLoader.load(this, dao -> dao.getAll().observe(this, reasonTemplates -> {
            if (reasonTemplates != null) {
                this.reasonTemplates = (ArrayList<ReasonTemplate>) reasonTemplates;

                buildUi();
            }
        }));
    }

    private void buildUi () {
        if (reasonTemplates != null) {
            ArrayList<ReasonTemplate> reasonTemplatesClean = new ArrayList<>();

            for (ReasonTemplate reasonTemplate : reasonTemplates) {
                if (!reasonTemplate.isDeleted()) {
                    reasonTemplatesClean.add(reasonTemplate);
                }
            }

            adapter = new ReasonAdapter(this, reasonTemplatesClean);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (position == adapter.getCount()) {
                    Vibration.buttonPress(this);

                    startActivityForResult(new Intent(this, AddReasonTemplateActivity.class)
                                    .putExtra(AddReasonTemplateActivity.EXTRA_CUSTOMIZE_AFTER, true),
                            REQUEST_CODE_ADD_REASON_TEMPLATE);
                } else {
                    final ReasonTemplate item = adapter.getItem(position);

                    Vibration.buttonPress(this);

                    startActivityForResult(new Intent(this, CustomizeReasonActivity.class)
                                    .putExtra(CustomizeReasonActivity.EXTRA_TEMPLATE_ID, item.getId()),
                            REQUEST_CODE_CUSTOMIZE_SELECTION
                    );
                }
            });

            if (listView.getFooterViewsCount() == 0) {
                View view = getLayoutInflater().inflate(R.layout.reason_row, null);
                TextView textReason = view.findViewById(R.id.reason);
                textReason.setText(getString(R.string.select_reason_new_reason));
                listView.addFooterView(view);
            }
        }
    }
}
