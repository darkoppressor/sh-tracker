package org.cheeseandbacon.shtracker.data.event;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date dateFromTimestamp (Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long timestampFromDate (Date date) {
        return date == null ? null : date.getTime();
    }
}
