/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.month;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.util.DateAndTime;

import java.util.Date;

public class MonthDecorator implements CalendarCellDecorator {
    @NonNull
    private final BaseActivity activity;

    MonthDecorator (@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void decorate (CalendarCellView cellView, Date date) {
        EventLoader.load(cellView.getContext(), dao ->
                dao.getByDate(DateAndTime.dateToDateString(date)).observe(activity, events -> {
                    LinearLayout layoutDots = cellView.findViewById(R.id.dots);

                    if (events != null && events.size() > 0) {
                        layoutDots.setVisibility(View.VISIBLE);

                        ImageView dot1 = cellView.findViewById(R.id.dot1);
                        ImageView dot2 = cellView.findViewById(R.id.dot2);
                        ImageView dot3 = cellView.findViewById(R.id.dot3);
                        ImageView dot4 = cellView.findViewById(R.id.dot4);

                        dot1.setVisibility(View.GONE);
                        dot2.setVisibility(View.GONE);
                        dot3.setVisibility(View.GONE);
                        dot4.setVisibility(View.GONE);

                        int nonUrgeEvents = 0;

                        for (Event event : events) {
                            if (!event.isUrge()) {
                                nonUrgeEvents++;
                            }
                        }

                        int color = R.color.dateEventYellow;

                        if (nonUrgeEvents > 3) {
                            dot1.setVisibility(View.VISIBLE);
                            dot2.setVisibility(View.VISIBLE);
                            dot3.setVisibility(View.VISIBLE);
                            dot4.setVisibility(View.VISIBLE);

                            color = R.color.dateEventRed;
                        } else if (nonUrgeEvents > 2) {
                            dot1.setVisibility(View.VISIBLE);
                            dot2.setVisibility(View.VISIBLE);
                            dot3.setVisibility(View.VISIBLE);

                            color = R.color.dateEventRed;
                        } else if (nonUrgeEvents > 1) {
                            dot1.setVisibility(View.VISIBLE);
                            dot2.setVisibility(View.VISIBLE);

                            color = R.color.dateEventOrange;
                        } else {
                            dot1.setVisibility(View.VISIBLE);
                        }

                        if (nonUrgeEvents == 0) {
                            color = R.color.dateUrge;
                        }

                        cellView.getDayOfMonthTextView().setTextColor(cellView.getContext().getColor(color));
                    } else {
                        cellView.getDayOfMonthTextView()
                                .setTextColor(cellView.getContext().getColor(R.color.calendar_text_selector));

                        layoutDots.setVisibility(View.GONE);
                    }
                }));
    }
}
