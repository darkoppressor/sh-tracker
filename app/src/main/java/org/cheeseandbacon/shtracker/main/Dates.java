/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.main;

import java.util.Calendar;
import java.util.Date;

public class Dates {
    private Date before;
    private Date current;
    private Date after;

    public Dates (Calendar current) {
        Calendar before = Calendar.getInstance();
        before.setTime(current.getTime());
        before.add(Calendar.DATE, -1);

        Calendar after = Calendar.getInstance();
        after.setTime(current.getTime());
        after.add(Calendar.DATE, 1);

        this.before = before.getTime();
        this.current = current.getTime();
        this.after = after.getTime();
    }

    public Date getBefore () {
        return before;
    }

    public Date getCurrent () {
        return current;
    }

    public Date getAfter () {
        return after;
    }
}
