package dev.ngai.fantastic.data.source.local;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.data.source.local.dao.DaoMaster;
import dev.ngai.fantastic.data.source.local.dao.DaoSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Des:
 * Created by Ngai
 * 2017/6/27
 */

public class DaoHelper {

    static private final String TAG = "DaoHelper";
    static private DaoSession daoSession;

    static private Context mContext;

    static public void initContext(Context context){
        checkNotNull(context);
        mContext = context;
    }

    static public DaoSession getDaoSession() {
        checkNotNull(mContext);
        if (daoSession == null) {
            Logc.d(TAG, "new DaoSession !");
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "fantastic-db");
            Database db = helper.getWritableDb();
            daoSession = new DaoMaster(db).newSession();
        }
        return daoSession;
    }

}
