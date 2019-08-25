/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.event;

import org.cheeseandbacon.shtracker.data.BaseDao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class EventDao extends BaseDao<Event> {
    @Query("SELECT * FROM event WHERE id IS :id")
    public abstract LiveData<Event> getById(String id);

    @Query("SELECT * FROM event WHERE date IS :date ORDER BY date ASC")
    public abstract LiveData<List<Event>> getByDate(String date);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insertTs(Event... ts);

    @Override
    @Update
    protected abstract void updateTs(Event... ts);

    @Override
    @Delete
    protected abstract void deleteTs(Event... ts);

    @Override
    @Query("DELETE FROM event")
    protected abstract void clearTable();
}
