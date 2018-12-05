/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.event;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Action implements Serializable {
    @NonNull
    private String templateId;
    @NonNull
    private String comment;
    private int severity;

    public Action (@NonNull String templateId, @NonNull String comment, int severity) {
        this.templateId = templateId;
        this.comment = comment;
        this.severity = severity;
    }

    @NonNull
    public String getTemplateId () {
        return templateId;
    }

    @NonNull
    public String getComment () {
        return comment;
    }

    public int getSeverity () {
        return severity;
    }
}
