/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.reasonTemplate;

import android.content.Context;
import android.support.annotation.NonNull;

import org.cheeseandbacon.shtracker.data.AppDatabase;

public class ReasonTemplateLoader {
    public interface OnLoaded {
        void execute(@NonNull final ReasonTemplateDao dao);
    }

    public static void load (@NonNull final Context context, @NonNull final OnLoaded onLoaded) {
        AppDatabase database = AppDatabase.getInstance(context);

        onLoaded.execute(database.reasonTemplateDao());
    }
}
