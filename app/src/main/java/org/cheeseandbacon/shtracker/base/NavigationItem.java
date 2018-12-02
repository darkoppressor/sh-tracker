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
