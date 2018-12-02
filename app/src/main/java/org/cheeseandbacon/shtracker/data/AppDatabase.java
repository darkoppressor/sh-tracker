package org.cheeseandbacon.shtracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventDao;

@Database(entities = {
        Event.class
        }, version = 1)
@TypeConverters({
        org.cheeseandbacon.shtracker.data.event.Converters.class
})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "db";

    private static AppDatabase instance;

    public abstract EventDao eventDao();

    public void clearAll () {
        eventDao().clear();
    }

    public static AppDatabase getInstance (@NonNull final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }

        return instance;
    }
}
