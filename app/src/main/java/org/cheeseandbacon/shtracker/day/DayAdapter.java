/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.day;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.data.event.Event;

import java.util.ArrayList;

public class DayAdapter extends BaseAdapter {
    @NonNull
    private final Context context;
    @NonNull
    private final ArrayList<Event> items;

    public DayAdapter (@NonNull Context context, @NonNull ArrayList<Event> items) {
        this.context = context;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.day_row, null);
            }

            final Event item = getItem(position);

            TextView textTime = convertView.findViewById(R.id.time);

            textTime.setText(item.getTime());
        }

        return convertView;
    }
}