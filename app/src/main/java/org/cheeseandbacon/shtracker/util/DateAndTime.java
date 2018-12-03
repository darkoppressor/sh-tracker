/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.util;

import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTime {
    private static final SimpleDateFormat dateFormatInputMdy = new SimpleDateFormat("M/d/yy", Locale.US);
    private static final SimpleDateFormat dateFormatInputDayOfWeek = new SimpleDateFormat("EEEE", Locale.US);
    private static final SimpleDateFormat timeFormatInputHm = new SimpleDateFormat("HH:mm", Locale.US);

    public static final String LAST_MINUTE_OF_DAY = "23:59";

    public static boolean isToday (Date date) {
        return DateUtils.isToday(date.getTime());
    }

    public static boolean isYesterday (Date date) {
        return DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isTomorrow (Date date) {
        return DateUtils.isToday(date.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public static boolean dayIsBefore (Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar1.before(calendar2);
    }

    public static String dateToDateString (Date date) {
        return dateFormatInputMdy.format(date);
    }

    @Nullable
    public static Date dateStringToDate (String dateString) {
        try {
            return dateFormatInputMdy.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToDayOfWeekString (Date date) {
        return dateFormatInputDayOfWeek.format(date);
    }

    public static String dateToTimeString (Date date) {
        return timeFormatInputHm.format(date);
    }

    @Nullable
    public static Date timeStringToDate (String timeString) {
        try {
            return timeFormatInputHm.parse(timeString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getTimeString (int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        return dateToTimeString(calendar.getTime());
    }
}
