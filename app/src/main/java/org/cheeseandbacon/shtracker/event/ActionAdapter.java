/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.event;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Space;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;

import java.util.ArrayList;

public class ActionAdapter extends BaseAdapter {
    @NonNull
    private final Activity activity;
    @NonNull
    private final ArrayList<ActionTemplate> items;

    public ActionAdapter (@NonNull Activity activity, @NonNull ArrayList<ActionTemplate> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount (){
        return items.size();
    }

    @Override
    public ActionTemplate getItem (int position) {
        return items.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (!isItemVisible(position)) {
            if (!(convertView instanceof Space)) {
                convertView = new Space(activity);
            }
        } else {
            if (convertView == null || convertView instanceof Space) {
                convertView = activity.getLayoutInflater().inflate(R.layout.action_row, null);
            }

            final ActionTemplate item = getItem(position);

            TextView textAction = convertView.findViewById(R.id.action);

            textAction.setText(item.getName());
        }

        return convertView;
    }

    public boolean isItemVisible (int position) {
        final ActionTemplate item = getItem(position);

        return !item.isDeleted();
    }
}
