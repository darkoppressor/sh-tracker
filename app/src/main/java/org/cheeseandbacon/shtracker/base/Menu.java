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

    public ResIdGetter getResIdGetter () {
        return resIdGetter;
    }

    public Toolbar.OnMenuItemClickListener getOnMenuItemClickListener () {
        return onMenuItemClickListener;
    }
}
