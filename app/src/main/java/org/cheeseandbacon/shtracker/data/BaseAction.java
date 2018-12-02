package org.cheeseandbacon.shtracker.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

class BaseAction<T> {
    private final Class<T> tClass;
    private final BaseDao<T> baseDao;
    private final ArrayList<T> ts;

    BaseAction(Class<T> tClass, BaseDao<T> baseDao, ArrayList<T> ts) {
        this.tClass = tClass;
        this.baseDao = baseDao;
        this.ts = ts;
    }

    BaseDao<T> getBaseDao() {
        return baseDao;
    }

    T[] getTs() {
        @SuppressWarnings("unchecked")
        final T[] tArray = (T[]) Array.newInstance(tClass, ts.size());

        for (int i = 0; i < ts.size(); i++) {
            tArray[i] = ts.get(i);
        }

        return tArray;
    }
}
