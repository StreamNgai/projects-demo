package dev.weihl.amazing.data.source.remote;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
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
import dev.weihl.amazing.data.source.local.FileCache;
import dev.weihl.amazing.data.source.local.dao.DaoHelper;
import dev.weihl.amazing.executors.AppExecutors;
import dev.weihl.amazing.sharedpres.PrefsKey;
import dev.weihl.amazing.sharedpres.SharedPres;
import dev.weihl.amazing.util.DateUtil;

/**
 * @author Ngai
 * @since 2018/5/25
 * Des:
 */
public class RemoteDataSource implements AmazingDataSource {
    private static RemoteDataSource INSTANCE;
    private static Context mContext;

    private RemoteDataSource() {
        mContext = MainApplication.getContext();
    }

    public static RemoteDataSource getInstance() {
        synchronized (RemoteDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Favorite findFavorite(int discoverId) {
        return null;
    }

    @Override
    public void syncAppUpdate(final AppUpdateCallBack callBack) {
        BmobQuery<UpgradeApp> query = new BmobQuery<>();
        query.addWhereEqualTo("pkgName", mContext.getPackageName());
        query.findObjects(new FindListener<UpgradeApp>() {

            @Override
            public void done(List<UpgradeApp> list, BmobException e) {
                UpgradeApp mAppUpdate = null;
                if (e == null && list != null && !list.isEmpty()) {
                    mAppUpdate = list.get(0);
                    if (mAppUpdate != null) {
                        SharedPres.putInt(PrefsKey.AppUpdateDate, getTodayDate());
                        FileCache.get(mContext).put(PrefsKey.AppUpdateObj, mAppUpdate);
                        if (Logc.allowPrints()) {
                            Logc.d(Tags.AppUpdate, "查询成功：" + mAppUpdate.toString());
                        }
                    }
                } else {
                    if (Logc.allowPrints()) {
                        String msg = e != null ? e.getMessage() : "list is empty !";
                        Logc.d(Tags.AppUpdate, "查询失败：" + msg);
                    }
                }
                final UpgradeApp finalMAppUpdate = mAppUpdate;
                AppExecutors.executeDiskIO(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onResult(finalMAppUpdate);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void syncDiscoverTab(final DiscoverTabCallBack callBack) {
        BmobQuery<DiscoverTab> query = new BmobQuery<>();
        query.addWhereEqualTo("effective", true);
        query.findObjects(new FindListener<DiscoverTab>() {

            @Override
            public void done(final List<DiscoverTab> tabList, BmobException e) {
                if (e == null && tabList != null && !tabList.isEmpty()) {
                    SharedPres.putInt(PrefsKey.DiscoverTabDate, getTodayDate());
                    DaoHelper.getDaoSession().getDiscoverTabDao().deleteAll();
                    DaoHelper.getDaoSession().getDiscoverTabDao().insertInTx(tabList);
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.DiscoverTab, "Sync Remote Result = " + tabList.toString());
                    }
                } else {
                    if (Logc.allowPrints()) {
                        String msg = e != null ? e.getMessage() : "list is empty !";
                        Logc.d(Tags.DiscoverTab, "Sync Remote Result = " + msg);
                    }
                }
                AppExecutors.executeDiskIO(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onResult(tabList);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void syncDiscover(DiscoverTab tab, int discoverId, boolean remote, int limit, final DiscoverCallBack callBack) {
        if (remote && tab != null && discoverId > 0 && limit > 0 && callBack != null) {
            BmobQuery<Discover> query = new BmobQuery<>();
            query.addWhereEqualTo("tab", tab.tab);
            query.addWhereGreaterThan("id", discoverId);
            query.setLimit(limit);
            query.order("id");// 升序
            query.findObjects(new FindListener<Discover>() {

                @Override
                public void done(List<Discover> discoverList, BmobException e) {
                    if (discoverList != null && !discoverList.isEmpty()) {
                        Collections.reverse(discoverList);
                        DaoHelper.getDaoSession().getDiscoverDao().insertOrReplaceInTx(discoverList);
                    }
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.Discover, "Sync Remote Result = "
                                + (discoverList != null && !discoverList.isEmpty() ? discoverList.toString() : "is null !")
                                + " ; errorMsg = " + (e != null ? e.getMessage() : " Null !"));
                    }
                    callBack.onResult(discoverList);
                }
            });
        }
    }

    @Override
    public void login(final String email, final String pwd, final LoginCallBack callBack) {
        if (callBack != null) {
            BmobUser.loginByAccount(email, pwd, new LogInListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.Login, "IUser Remote Account "
                                + (bmobUser == null ? "Result = Failure ! ErrorMsg = "
                                + (e == null ? " Null" : e.getMessage()) : "Result ! ObjectId = " + bmobUser.getObjectId()));
                    }
                    callBack.onResult(bmobUser);
                }
            });
        }
    }

    @Override
    public void loadUserInfo(BmobUser user, final UserInfoCallBack callBack) {
        if (user != null && !TextUtils.isEmpty(user.getEmail()) && callBack != null) {
            BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userEmail", user.getEmail());
            bmobQuery.findObjects(new FindListener<UserInfo>() {
                @Override
                public void done(List<UserInfo> list, BmobException e) {
                    UserInfo userInfo = null;
                    if (list != null && !list.isEmpty()) {
                        userInfo = list.get(0);
                    }
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.UserInfo, "UserInfo Remote Load By IUser ! "
                                + (userInfo == null ? "Result = Failure ! ErrorMsg = "
                                + (e == null ? " Null" : e.getMessage()) : "Result = " + userInfo.toString()));
                    }
                    callBack.onResult(userInfo);
                }
            });
        }
    }

    @Override
    public void syncUserInfo(final UserInfo userInfo, final UserInfoCallBack callBack) {
        if (userInfo != null && callBack != null) {
            if (!TextUtils.isEmpty(userInfo.getObjectId())) {
                userInfo.update(userInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (Logc.allowPrints()) {
                            Logc.d(Tags.UserInfo, "UserInfo Remote " + (e == null ? "Result = Success !" : "Sync Failure !" + e.getMessage()));
                        }
                        callBack.onResult(e == null ? userInfo : null);
                    }
                });
            } else {
                userInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (Logc.allowPrints()) {
                            Logc.d(Tags.UserInfo, "UserInfo Create " + (e == null ? "Success ! objectId = " + objectId
                                    : "Error ! Msg = " + e.getMessage()));
                        }
                        if (e == null && !TextUtils.isEmpty(objectId)) {
                            SharedPres.putString(PrefsKey.UserInfoObjectId, objectId);
                            userInfo.setObjectId(objectId);
                        }
                        callBack.onResult(userInfo);
                    }
                });
            }
        }
    }

    @Override
    public void loadUserFeedback(final UserFeedbackCallBack callBack) {
        if (callBack != null) {
            BmobUser bmobUser = BmobUser.getCurrentUser();
            BmobQuery<UserFeedback> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userId", bmobUser.getObjectId());
            bmobQuery.findObjects(new FindListener<UserFeedback>() {
                @Override
                public void done(List<UserFeedback> list, BmobException e) {
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.UserFeedback, "UserFeedback Remote Load " + ((list != null && !list.isEmpty()) ? "Success !" : "Failure"));
                    }
                    callBack.onResult(list);
                }
            });
        }
    }

    @Override
    public void uploadUserFeedback(final UserFeedback userFeedback, final UserFeedbackCallBack callBack) {
        if (userFeedback != null && callBack != null) {
            userFeedback.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    List<UserFeedback> list = null;
                    if (!TextUtils.isEmpty(objectId)) {
                        userFeedback.setObjectId(objectId);
                        list = new ArrayList<>();
                        list.add(userFeedback);
                    }
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.UserFeedback, "UserFeedback Remote upload " + ((list != null && !list.isEmpty()) ? "Success !" : "Failure"));
                    }
                    callBack.onResult(list);
                }
            });
        }
    }

    @Override
    public void saveFavorite(Favorite favorite, AlbumContract.FavoriteCallBack callBack) {

    }

    @Override
    public void loadFavoriteList(FavoriteCallBack callBack) {

    }

    public boolean needSyncAppUpdate() {
        int updateDate = SharedPres.getInt(PrefsKey.AppUpdateDate, 0);
        if (updateDate > 0) {
            int curDate = getTodayDate();
            return updateDate < curDate;
        }
        return true;
    }

    private int getTodayDate() {
        return Integer.valueOf(DateUtil.formatDate(new Date(), "yyyyMMdd"));
    }

    // 一天同步一次
    public boolean needSyncDiscoverTab() {
        int updateDate = SharedPres.getInt(PrefsKey.DiscoverTabDate, 0);
        if (updateDate > 0) {
            int curDate = getTodayDate();
            return updateDate < curDate;
        }
        return true;
    }
}
