/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data;

import android.content.Context;

import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplate;
import org.cheeseandbacon.shtracker.data.actionTemplate.ActionTemplateDao;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventDao;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplate;
import org.cheeseandbacon.shtracker.data.reasonTemplate.ReasonTemplateDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        Event.class,
        ActionTemplate.class,
        ReasonTemplate.class
        }, version = 1)
@TypeConverters({
        org.cheeseandbacon.shtracker.data.event.Converters.class
})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "db";

    private static AppDatabase instance;

    public abstract EventDao eventDao();
    public abstract ActionTemplateDao actionTemplateDao();
    public abstract ReasonTemplateDao reasonTemplateDao();

    public void clearAll () {
        eventDao().clear();
        actionTemplateDao().clear();
        reasonTemplateDao().clear();
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
