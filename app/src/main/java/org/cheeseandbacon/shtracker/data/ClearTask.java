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
