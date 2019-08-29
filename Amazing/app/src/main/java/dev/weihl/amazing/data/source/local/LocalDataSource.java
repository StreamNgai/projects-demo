package dev.weihl.amazing.data.source.local;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.browse.AlbumContract;
import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.UpgradeApp;
import dev.weihl.amazing.data.bean.UserFeedback;
import dev.weihl.amazing.data.bean.UserInfo;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.local.dao.DaoHelper;
import dev.weihl.amazing.data.source.local.dao.DiscoverDao;
import dev.weihl.amazing.data.source.local.dao.DiscoverTabDao;
import dev.weihl.amazing.data.source.local.dao.FavoriteDao;
import dev.weihl.amazing.data.source.local.dao.UserFeedbackDao;
import dev.weihl.amazing.sharedpres.PrefsKey;
import dev.weihl.amazing.sharedpres.SharedPres;

/**
 * @author Ngai
 * @since 2018/5/25
 * Des:
 */
public class LocalDataSource implements AmazingDataSource {
    private static LocalDataSource INSTANCE;
    private Context mContext;

    private LocalDataSource() {
        mContext = MainApplication.getContext();
    }

    public static LocalDataSource getInstance() {
        synchronized (LocalDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new LocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void syncAppUpdate(AppUpdateCallBack callBack) {
//      FileCache.get(mContext).put("AppUpdateObj",appUpdate);
        if (callBack != null) {
            UpgradeApp appUpdate = (UpgradeApp) FileCache.get(mContext).getAsObject(PrefsKey.AppUpdateObj);
            callBack.onResult(appUpdate);
            String tabResult = "Null !";
            if (appUpdate != null) {
                tabResult = appUpdate.toString();
            }
            if (Logc.allowPrints()) {
                Logc.d(Tags.AppUpdate, "Sync Local Result = " + tabResult);
            }
        }
    }

    @Override
    public void syncDiscoverTab(DiscoverTabCallBack callBack) {
        if (callBack != null) {
            DiscoverTabDao tabDao = DaoHelper.getDaoSession().getDiscoverTabDao();
            if (tabDao != null) {
                List<DiscoverTab> tabList = tabDao.loadAll();
                String tabResult = "Null !";
                if (tabList != null && !tabList.isEmpty()) {
                    tabResult = tabList.toString();
                }
                if (Logc.allowPrints()) {
                    Logc.d(Tags.DiscoverTab, "Sync Local Result = " + tabResult);
                }
                callBack.onResult(tabList);
            }
        }
    }

    @Override
    public void syncDiscover(DiscoverTab tab, int discoverId, boolean remote, int limit, DiscoverCallBack callBack) {
        if (!remote && tab != null && discoverId > 0 && limit > 0 && callBack != null) {
            DiscoverDao discoverDao = DaoHelper.getDaoSession().getDiscoverDao();
            if (discoverDao != null) {
                QueryBuilder<Discover> queryBuilder = discoverDao.queryBuilder();
                queryBuilder.where(DiscoverDao.Properties.Tab.eq(tab.tab))
                        .where(DiscoverDao.Properties.Id.le(discoverId))
                        .limit(limit)
                        .orderDesc(DiscoverDao.Properties.Id);
                List<Discover> discoverList = queryBuilder.list();
                if (Logc.allowPrints()) {
                    Logc.d(Tags.Discover, "Sync Local Result = "
                            + (discoverList != null && !discoverList.isEmpty() ? discoverList.toString() : "is null !"));
                }
                callBack.onResult(discoverList);
            }
        }
    }

    @Override
    public void login(String email, String pwd, LoginCallBack callBack) {
        if (callBack != null) {

        }
    }

    @Override
    public void loadUserInfo(BmobUser user, UserInfoCallBack callBack) {
        if (user != null && callBack != null) {
            List<UserInfo> userInfoList = DaoHelper.getDaoSession().getUserInfoDao().loadAll();
            UserInfo userInfo = null;
            if (userInfoList != null && !userInfoList.isEmpty()) {
                userInfo = userInfoList.get(0);
                userInfo.setObjectId(SharedPres.getString(PrefsKey.UserInfoObjectId, ""));
            }
            if (Logc.allowPrints()) {
                Logc.d(Tags.UserInfo, "UserInfo Local Load "
                        + (userInfo != null ? "Result = Success ! ObjectId = " + userInfo.getObjectId() : "UserInfo Is NUll !"));
            }
            callBack.onResult(userInfo);
        }
    }

    @Override
    public void syncUserInfo(UserInfo userInfo, UserInfoCallBack callBack) {
        if (userInfo != null) {
            DaoHelper.getDaoSession().getUserInfoDao().deleteAll();
            DaoHelper.getDaoSession().getUserInfoDao().insert(userInfo);
            if (Logc.allowPrints()) {
                Logc.d(Tags.UserInfo, "UserInfo Sync Dao ! ");
            }
        } else {
            if (Logc.allowPrints()) {
                Logc.d(Tags.UserInfo, "UserInfo Sync Dao Failure ,UserInfo is Null ! ");
            }
        }
    }

    @Override
    public void loadUserFeedback(UserFeedbackCallBack callBack) {
        if (callBack != null) {
            UserFeedbackDao feedbackDao = DaoHelper.getDaoSession().getUserFeedbackDao();
            if (feedbackDao != null) {
                List<UserFeedback> list = feedbackDao.loadAll();
                if (Logc.allowPrints()) {
                    Logc.d(Tags.UserFeedback, "UserFeedback Local Load " + ((list != null && !list.isEmpty()) ? "Success !" : "Failure"));
                }
                callBack.onResult(list);
            }
        }
    }

    @Override
    public void uploadUserFeedback(UserFeedback userFeedback, UserFeedbackCallBack callBack) {
        if (userFeedback != null && callBack != null) {
            UserFeedbackDao feedbackDao = DaoHelper.getDaoSession().getUserFeedbackDao();
            List<UserFeedback> list = null;
            if (feedbackDao != null && feedbackDao.insert(userFeedback) > 0) {
                list = new ArrayList<>();
                list.add(userFeedback);
            }
            if (Logc.allowPrints()) {
                Logc.d(Tags.UserFeedback, "UserFeedback Local upload " + ((list != null && !list.isEmpty()) ? "Success !" : "Failure"));
            }
            callBack.onResult(list);
        }
    }

    @Override
    public void saveFavorite(Favorite favorite, AlbumContract.FavoriteCallBack callBack) {
        if (favorite != null) {
            if (Logc.allowPrints()) {
                Logc.d(Tags.Favorite, "SaveFavorite ! ");
            }
            FavoriteDao favoriteDao = DaoHelper.getDaoSession().getFavoriteDao();
            Favorite tFavorite = findFavorite(favorite.discoverId);
            if (tFavorite != null) {
                favoriteDao.deleteInTx(tFavorite);
            } else {
                favoriteDao.insertOrReplaceInTx(favorite);
            }
            if (callBack != null) {
                callBack.onResult(true);
            }
        }
    }

    @Override
    public void loadFavoriteList(FavoriteCallBack callBack) {
        if (callBack != null) {
            FavoriteDao favoriteDao = DaoHelper.getDaoSession().getFavoriteDao();
            List<Favorite> list = favoriteDao.loadAll();
            callBack.onResult(list);
        }
    }

    @Override
    public Favorite findFavorite(int discoverId) {
        if (discoverId > 0) {
            FavoriteDao favoriteDao = DaoHelper.getDaoSession().getFavoriteDao();
            QueryBuilder<Favorite> queryBuilder = favoriteDao.queryBuilder();
            queryBuilder.where(FavoriteDao.Properties.DiscoverId.eq(discoverId));
            List<Favorite> list = queryBuilder.list();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    public boolean hasLocalDiscoverTab() {
        DiscoverTabDao tabDao = DaoHelper.getDaoSession().getDiscoverTabDao();
        return tabDao != null && !tabDao.loadAll().isEmpty();
    }
}
