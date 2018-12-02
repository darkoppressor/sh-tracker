/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.base;

class NavigationItem {
    public interface OnClick {
        void onClick();
    }

    private int id;

	private final String name;
	private final int drawableId;
	private final OnClick onClick;

    public NavigationItem (String name, int drawableId, OnClick onClick) {
        this.name = name;
        this.drawableId = drawableId;
        this.onClick = onClick;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public int getDrawableId () {
        return drawableId;
    }

    public OnClick getOnClick () {
        return onClick;
    }
}
