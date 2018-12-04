/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.reasonTemplate;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = "id", unique = true)})
public class ReasonTemplate {
    @PrimaryKey
    @NonNull
    private String id;
    @Nullable
    private String description;

    public ReasonTemplate (@NonNull String id, @Nullable String description) {
        this.id = id;
        this.description = description;
    }

    @NonNull
    public String getId () {
        return id;
    }

    public void setId (@NonNull String id) {
        this.id = id;
    }

    @Nullable
    public String getDescription () {
        return description;
    }

    public void setDescription (@Nullable String description) {
        this.description = description;
    }
}
