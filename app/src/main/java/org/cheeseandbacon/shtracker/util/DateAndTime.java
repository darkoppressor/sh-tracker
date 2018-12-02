/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.util;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTime {
    private static final SimpleDateFormat dateFormatInputMdy = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private static final SimpleDateFormat dateFormatInputDayOfWeek = new SimpleDateFormat("EEEE", Locale.US);
    private static final SimpleDateFormat timeFormatInputHms = new SimpleDateFormat("HH:mm:ss", Locale.US);

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

    public static String formatDateMdy (Date date) {
        return dateFormatInputMdy.format(date);
    }

    public static String formatDateDayOfWeek (Date date) {
        return dateFormatInputDayOfWeek.format(date);
    }

    public static String formatTimeHms (Date date) {
        return timeFormatInputHms.format(date);
    }
}
