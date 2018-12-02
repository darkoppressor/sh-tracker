package org.cheeseandbacon.shtracker.data.event;

import android.content.Context;
import android.support.annotation.NonNull;

import org.cheeseandbacon.shtracker.data.AppDatabase;

import java.util.ArrayList;

public class EventLoader {
    public interface OnLoaded {
        void execute(@NonNull final EventDao eventDao);
    }

    public static void load (@NonNull final Context context, @NonNull final OnLoaded onLoaded) {
        AppDatabase database = AppDatabase.getInstance(context);

        if (database.eventDao().isInitialized()) {
            onLoaded.execute(database.eventDao());
        } else {
            database.eventDao().insert(Event.class, new ArrayList<>(), () -> load(context, onLoaded));
        }
    }
}
