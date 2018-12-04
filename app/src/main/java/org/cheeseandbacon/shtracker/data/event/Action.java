/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.event;

public class Action {
    private String templateId;
    private String comment;
    private int severity;

    public String getTemplateId () {
        return templateId;
    }

    public void setTemplateId (String templateId) {
        this.templateId = templateId;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }

    public int getSeverity () {
        return severity;
    }

    public void setSeverity (int severity) {
        this.severity = severity;
    }
}
