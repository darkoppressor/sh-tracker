/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.event;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Event {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String date;
    @NonNull
    private String time;
    @Nullable
    private Reason reason;
    @Nullable
    private Action action;
    private boolean isUrge;

    public Event(@NonNull String id, @NonNull String date, @NonNull String time, @Nullable Reason reason, @Nullable Action action, boolean isUrge) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.action = action;
        this.isUrge = isUrge;
    }

    @NonNull
    public String getId () {
        return id;
    }

    public void setId (@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getDate () {
        return date;
    }

    public void setDate (@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getTime () {
        return time;
    }

    public void setTime (@NonNull String time) {
        this.time = time;
    }

    @Nullable
    public Reason getReason () {
        return reason;
    }

    public void setReason (@Nullable Reason reason) {
        this.reason = reason;
    }

    @Nullable
    public Action getAction () {
        return action;
    }

    public void setAction (@Nullable Action action) {
        this.action = action;
    }

    public boolean isUrge() {
        return isUrge;
    }

    public void setUrge(boolean urge) {
        isUrge = urge;
    }
}
