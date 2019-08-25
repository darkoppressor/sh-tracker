/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.ArrayList;

// This extended TextInputEditText adds the following functionality:
// The ability to clear all associated TextWatchers
// A method for getting a nonnull string representing the text
public class BaseEditText extends TextInputEditText {
    private ArrayList<TextWatcher> textWatchers = null;

    public BaseEditText (Context context) {
        super(context);
    }

    public BaseEditText (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseEditText (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener (TextWatcher watcher) {
        if (textWatchers == null) {
            textWatchers = new ArrayList<>();
        }

        textWatchers.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        if (textWatchers != null) {
            int textWatcherIndex = textWatchers.indexOf(watcher);

            if (textWatcherIndex >= 0) {
                textWatchers.remove(textWatcherIndex);
            }
        }

        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners () {
        if (textWatchers != null) {
            for (TextWatcher textWatcher : textWatchers) {
                super.removeTextChangedListener(textWatcher);
            }

            textWatchers.clear();
            textWatchers = null;
        }
    }

    @NonNull
    public String getString () {
        return getText() != null ? getText().toString() : "";
    }
}
