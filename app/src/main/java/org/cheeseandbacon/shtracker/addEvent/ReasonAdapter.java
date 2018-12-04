/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.addEvent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;

import java.util.ArrayList;

public class ReasonAdapter extends BaseAdapter {
    @NonNull
    private final Context context;
    @NonNull
    private final ArrayList<ReasonTemplate> items;

    public ReasonAdapter (@NonNull Context context, @NonNull ArrayList<ReasonTemplate> items) {
        this.context = context;
        this.items = items;
    }

    public int getCount (){
        return items.size();
    }

    public ReasonTemplate getItem (int position) {
        return items.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.reason_row, null);
            }

            final ReasonTemplate item = getItem(position);

            TextView textReason = convertView.findViewById(R.id.reason);

            textReason.setText(item.getName());
        }

        return convertView;
    }
}
