/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.data;

import android.os.AsyncTask;

public class ClearTask extends AsyncTask<BaseDao, Void, Void> {
    @Override
    protected Void doInBackground (BaseDao... baseDaos) {
        for (BaseDao baseDao : baseDaos) {
            baseDao.clearTable();
        }

        return null;
    }
}
