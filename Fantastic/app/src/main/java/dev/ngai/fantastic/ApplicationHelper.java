package dev.ngai.fantastic;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.statistics.AppStat;
import cn.bmob.v3.update.BmobUpdateAgent;
import dev.ngai.fantastic.data.source.local.DaoHelper;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des:
 */
public class ApplicationHelper {
    static private String TAG = "ApplicationHelper";
    private static Context mContext;
    private static ApplicationHelper mHelper;

    private ApplicationHelper() {
    }

    public static ApplicationHelper getInstance() {
        if (mHelper == null) {
            synchronized (ApplicationHelper.class) {
                mHelper = new ApplicationHelper();
            }
        }
        return mHelper;
    }

    public ApplicationHelper init(Context context) {
        mContext = context;
        Logc.d(TAG, "Context.initialize !");
        return mHelper;
    }

    ApplicationHelper initDao() {
        DaoHelper.initContext(mContext);
        Logc.d(TAG, "Dao.initialize !");
        return mHelper;
    }

    // test : 3636a6c3dc61eecaf0782393bc053e45
    // replase 24ae9f662773549c4a4ca33622c59eaa
    private final String AppKey = "24ae9f662773549c4a4ca33622c59eaa";

    ApplicationHelper initBmob() {
        Bmob.initialize(mContext, AppKey, "bmob");
        boolean appStatRs = AppStat.i(AppKey, "bmob");
//        //初始化建表操作
//        BmobUpdateAgent.initAppVersion();
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateCheckConfig(false);
        Logc.d(TAG, "Bmob.initialize , appStatRs = " + appStatRs
                + " ; UpdateOnlyWifi = false ; UpdateCheckConfig = false !");
        return mHelper;
    }

    ApplicationHelper initUmeng() {
        // 友盟统计
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.enableEncrypt(false);// 日志加密设置
        Logc.d(TAG, "Umeng.initialize !");
        return mHelper;
    }
}
