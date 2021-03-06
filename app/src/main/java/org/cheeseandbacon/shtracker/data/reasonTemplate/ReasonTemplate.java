/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.reasonTemplate;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class ReasonTemplate {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private Long creationTimestamp;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private boolean deleted;

    public ReasonTemplate(@NonNull String id, @NonNull Long creationTimestamp, @NonNull String name, @NonNull String description, boolean deleted) {
        this.id = id;
        this.creationTimestamp = creationTimestamp;
        this.name = name;
        this.description = description;
        this.deleted = deleted;
    }

    @NonNull
    public String getId () {
        return id;
    }

    public void setId (@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public Long getCreationTimestamp () {
        return creationTimestamp;
    }

    public void setCreationTimestamp (@NonNull Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @NonNull
    public String getName () {
        return name;
    }

    public void setName (@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription () {
        return description;
    }

    public void setDescription (@NonNull String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
