package org.cheeseandbacon.shtracker.data;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class InsertTask<T> extends AsyncTask<BaseAction<T>, Void, ArrayList<BaseAction<T>>> {
    public interface OnTaskComplete {
        void onTaskComplete ();
    }

    @Nullable
    private final OnTaskComplete onTaskCompleteListener;

    InsertTask(@Nullable OnTaskComplete onTaskCompleteListener) {
        this.onTaskCompleteListener = onTaskCompleteListener;
    }

    @SafeVarargs
    @Override
    protected final ArrayList<BaseAction<T>> doInBackground(BaseAction<T>... baseActions) {
        for (BaseAction<T> baseAction : baseActions) {
            baseAction.getBaseDao().insertTs(baseAction.getTs());
        }

        return new ArrayList<>(Arrays.asList(baseActions));
    }

    @Override
    protected void onPostExecute(ArrayList<BaseAction<T>> baseActions) {
        for (BaseAction<T> baseAction : baseActions) {
            baseAction.getBaseDao().setInitialized(true);
        }

        if (onTaskCompleteListener != null) {
            onTaskCompleteListener.onTaskComplete();
        }
    }
}
