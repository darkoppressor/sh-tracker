/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.actionTemplate;

import android.content.Context;
import android.support.annotation.NonNull;

import org.cheeseandbacon.shtracker.data.AppDatabase;

import java.util.ArrayList;

public class ActionTemplateLoader {
    public interface OnLoaded {
        void execute(@NonNull final ActionTemplateDao dao);
    }

    public static void load (@NonNull final Context context, @NonNull final OnLoaded onLoaded) {
        AppDatabase database = AppDatabase.getInstance(context);

        if (database.actionTemplateDao().isInitialized()) {
            onLoaded.execute(database.actionTemplateDao());
        } else {
            database.actionTemplateDao().insert(ActionTemplate.class, new ArrayList<>(), () -> load(context, onLoaded));
        }
    }
}
