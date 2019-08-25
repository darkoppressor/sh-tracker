/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class TimePickerFragment extends DialogFragment {
    private static final String FRAGMENT_TAG = "timePicker";
    private static final String ARGUMENT_INITIAL_TIME = "initialTime";

    @Nullable
    private final Date initialTime;

    public TimePickerFragment () {
        initialTime = getArguments() != null ? (Date) getArguments().getSerializable(ARGUMENT_INITIAL_TIME) : null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        if (initialTime != null) {
            calendar.setTime(initialTime);
        }

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    public static void create (FragmentManager fragmentManager, String initialTime) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT_INITIAL_TIME, DateAndTime.timeStringToDate(initialTime));

        timePickerFragment.setArguments(bundle);

        timePickerFragment.show(fragmentManager, FRAGMENT_TAG);
    }
}
