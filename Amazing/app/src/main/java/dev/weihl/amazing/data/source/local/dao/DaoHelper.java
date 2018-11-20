package dev.weihl.amazing.data.source.local.dao;

import android.content.Context;

import dev.weihl.amazing.Logc;

/**
 * Des:
 * Created by Ngai
 * 2017/6/27
 */

public class DaoHelper {

    static private final String TAG = "DaoHelper";
    static private DaoSession daoSession;

    static private Context mContext;

    static public void initContext(Context context) {
        checkNotNull(context);
        mContext = context;
    }

    private static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException("DaoHelper Check Is Null !");
        }
    }

    static public DaoSession getDaoSession() {
        checkNotNull(mContext);
        if (daoSession == null) {
            if (Logc.allowPrints()) {
                Logc.d(TAG, "new DaoSession !");
            }
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "amazing-db");
            org.greenrobot.greendao.database.Database db = helper.getWritableDb();
            daoSession = new DaoMaster(db).newSession();
        }
        return daoSession;
    }

}
