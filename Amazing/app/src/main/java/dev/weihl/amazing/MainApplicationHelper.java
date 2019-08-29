package dev.weihl.amazing;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.statistics.AppStat;
import cn.bmob.v3.update.BmobUpdateAgent;
import dev.weihl.amazing.data.source.local.dao.DaoHelper;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des:
 */
public class MainApplicationHelper {
    static private String TAG = "MainApplicationHelper";
    private static Context mContext;
    private static MainApplicationHelper mHelper;

    private MainApplicationHelper() {
    }

    public static MainApplicationHelper getInstance() {
        if (mHelper == null) {
            synchronized (MainApplicationHelper.class) {
                mHelper = new MainApplicationHelper();
            }
        }
        return mHelper;
    }

    public MainApplicationHelper init(Context context) {
        mContext = context;
        Logc.d(TAG, "Context.initialize !");
        return mHelper;
    }

    MainApplicationHelper initDao() {
        DaoHelper.initContext(mContext);
        Logc.d(TAG, "Dao.initialize !");
        return mHelper;
    }

    //     test : 3636a6c3dc61eecaf0782393bc053e45
//     replase 24ae9f662773549c4a4ca33622c59eaa
    private final String AppKey = "24ae9f662773549c4a4ca33622c59eaa";

    MainApplicationHelper initBmob() {
        Bmob.initialize(mContext, AppKey, "bmob");
        boolean appStatRs = AppStat.i(AppKey, "bmob");
//        //初始化建表操作
//        BmobUpdateAgent.initAppVersion();
//        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateCheckConfig(false);
        Logc.d(TAG, "Bmob.initialize , appStatRs = " + appStatRs
                + " ; UpdateOnlyWifi = false ; UpdateCheckConfig = false !");
        return mHelper;
    }

}
