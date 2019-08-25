/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.base;

import android.support.v7.widget.Toolbar;

public class Menu {
    public interface ResIdGetter {
        int getMenuResId();
    }

    private final ResIdGetter resIdGetter;
    private final Toolbar.OnMenuItemClickListener onMenuItemClickListener;

    public Menu (ResIdGetter resIdGetter, Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        this.resIdGetter = resIdGetter;
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    ResIdGetter getResIdGetter() {
        return resIdGetter;
    }

    Toolbar.OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }
}
