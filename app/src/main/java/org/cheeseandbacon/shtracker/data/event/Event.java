package org.cheeseandbacon.shtracker.data.event;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Event {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String date;
    @NonNull
    private String time;

    public Event (@NonNull String id, @NonNull String date, @NonNull String time) {
        this.id = id;
        this.date = date;
        this.time = time;
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
}
