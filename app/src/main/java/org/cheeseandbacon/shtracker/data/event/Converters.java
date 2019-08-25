/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.event;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Action actionFromString (String value) {
        return new Gson().fromJson(value, new TypeToken<Action>(){}.getType());
    }

    @TypeConverter
    public static String stringFromAction (Action action) {
        return new Gson().toJson(action);
    }

    @TypeConverter
    public static Reason reasonFromString (String value) {
        return new Gson().fromJson(value, new TypeToken<Reason>(){}.getType());
    }

    @TypeConverter
    public static String stringFromReason (Reason reason) {
        return new Gson().toJson(reason);
    }
}
