/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.actionTemplate;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.cheeseandbacon.shtracker.data.BaseDao;

@Dao
public abstract class ActionTemplateDao extends BaseDao<ActionTemplate> {
    @Query("SELECT * FROM actiontemplate WHERE id IS :id")
    public abstract LiveData<ActionTemplate> getById(String id);

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
