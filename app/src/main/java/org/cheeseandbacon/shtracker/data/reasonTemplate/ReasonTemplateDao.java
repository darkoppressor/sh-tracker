/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data.reasonTemplate;

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
public abstract class ReasonTemplateDao extends BaseDao<ReasonTemplate> {
    @Query("SELECT * FROM reasontemplate WHERE id IS :id")
    public abstract LiveData<ReasonTemplate> getById(String id);

    @Query("SELECT * FROM reasontemplate ORDER BY creationTimestamp DESC")
    public abstract LiveData<List<ReasonTemplate>> getAll();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insertTs(ReasonTemplate... ts);

    @Override
    @Update
    protected abstract void updateTs(ReasonTemplate... ts);

    @Override
    @Delete
    protected abstract void deleteTs(ReasonTemplate... ts);

    @Override
    @Query("DELETE FROM reasontemplate")
    protected abstract void clearTable();
}
