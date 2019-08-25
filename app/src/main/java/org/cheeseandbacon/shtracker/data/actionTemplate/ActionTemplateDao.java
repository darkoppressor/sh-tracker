/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.actionTemplate;

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
public abstract class ActionTemplateDao extends BaseDao<ActionTemplate> {
    @Query("SELECT * FROM actiontemplate WHERE id IS :id")
    public abstract LiveData<ActionTemplate> getById(String id);

    @Query("SELECT * FROM actiontemplate ORDER BY creationTimestamp DESC")
    public abstract LiveData<List<ActionTemplate>> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insertTs(ActionTemplate... ts);

    @Override
    @Update
    protected abstract void updateTs(ActionTemplate... ts);

    @Override
    @Delete
    protected abstract void deleteTs(ActionTemplate... ts);

    @Override
    @Query("DELETE FROM actiontemplate")
    protected abstract void clearTable();
}
