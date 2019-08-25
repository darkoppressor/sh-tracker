/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.month;

import android.view.LayoutInflater;
import android.view.View;

import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.DayViewAdapter;

import org.cheeseandbacon.shtracker.R;

public class MonthAdapter implements DayViewAdapter {
    @Override
    public void makeCellView (CalendarCellView parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day, parent, false);

        parent.addView(view);
        parent.setDayOfMonthTextView(view.findViewById(R.id.day));
    }
}
