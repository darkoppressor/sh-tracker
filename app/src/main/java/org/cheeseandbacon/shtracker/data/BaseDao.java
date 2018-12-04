/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data;

import android.arch.persistence.room.Dao;
import android.support.annotation.Nullable;

import java.util.ArrayList;

@Dao
public abstract class BaseDao<T> {
    @SuppressWarnings("unchecked")
    protected abstract void insertTs(T... ts);

    @SuppressWarnings("unchecked")
    protected abstract void updateTs(T... ts);

    @SuppressWarnings("unchecked")
    protected abstract void deleteTs(T... ts);

    protected abstract void clearTable();

    @SuppressWarnings("unchecked")
    public void insert (Class<T> tClass, ArrayList<T> ts, @Nullable InsertTask.OnTaskComplete onTaskComplete) {
        new InsertTask<T>(onTaskComplete).execute(new BaseAction<>(tClass, this, ts));
    }

    @SuppressWarnings("unchecked")
    public void update (Class<T> tClass, ArrayList<T> ts, @Nullable UpdateTask.OnTaskComplete onTaskComplete) {
        new UpdateTask<T>(onTaskComplete).execute(new BaseAction<>(tClass, this, ts));
    }

    @SuppressWarnings("unchecked")
    public void delete (Class<T> tClass, ArrayList<T> ts, @Nullable DeleteTask.OnTaskComplete onTaskComplete) {
        new DeleteTask<T>(onTaskComplete).execute(new BaseAction<>(tClass, this, ts));
    }

    public void clear () {
        new ClearTask().execute(this);
    }
}
