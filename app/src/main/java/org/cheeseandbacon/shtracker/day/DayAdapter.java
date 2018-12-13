/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.day;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateLoader;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateLoader;

import java.util.ArrayList;

public class DayAdapter extends BaseAdapter {
    @NonNull
    private final BaseActivity activity;
    @NonNull
    private final ArrayList<Event> items;

    public DayAdapter (@NonNull BaseActivity activity, @NonNull ArrayList<Event> items) {
        this.activity = activity;
        this.items = items;
    }

    public int getCount (){
        return items.size();
    }

    public Event getItem (int position) {
        return items.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.day_row, null);
        }

        final Event item = getItem(position);

        TextView textName = convertView.findViewById(R.id.name);
        TextView textSeverity = convertView.findViewById(R.id.severity);
        TextView textTime = convertView.findViewById(R.id.time);

        if (item.getAction() != null) {
            textName.setVisibility(View.VISIBLE);
            textSeverity.setVisibility(View.VISIBLE);

            textSeverity.setText(activity.getString(R.string.day_list_row_severity, item.getAction().getSeverity()));

            ActionTemplateLoader.load(activity, dao ->
                    dao.getById(item.getAction().getTemplateId()).observe(activity, actionTemplate -> {
                        if (actionTemplate != null) {
                            textName.setText(actionTemplate.getName());
                        }
                    }));
        } else if (item.getReason() != null) {
            textName.setVisibility(View.VISIBLE);
            textSeverity.setVisibility(View.VISIBLE);

            textSeverity.setText(activity.getString(R.string.day_list_row_severity, item.getReason().getSeverity()));

            ReasonTemplateLoader.load(activity, dao ->
                    dao.getById(item.getReason().getTemplateId()).observe(activity, reasonTemplate -> {
                        if (reasonTemplate != null) {
                            textName.setText(reasonTemplate.getName());
                        }
                    }));
        } else {
            textName.setVisibility(View.INVISIBLE);
            textSeverity.setVisibility(View.INVISIBLE);
        }

        textTime.setText(item.getTime());

        return convertView;
    }
}
