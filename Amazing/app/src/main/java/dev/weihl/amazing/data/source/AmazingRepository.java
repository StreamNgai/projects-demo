package dev.weihl.amazing.data.source;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.browse.AlbumContract;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.UserFeedback;
import dev.weihl.amazing.data.bean.UserInfo;
import dev.weihl.amazing.data.source.local.LocalDataSource;
import dev.weihl.amazing.data.source.remote.RemoteDataSource;

/**
 * @author Ngai
 * @since 2018/5/25
 * Des:
 */
public class AmazingRepository implements AmazingDataSource {
    private static AmazingRepository INSTANCE;
    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;
    private Context mContext;
    private HashMap<String, Integer> mIdMaxSet;


    private AmazingRepository() {
        mLocalDataSource = LocalDataSource.getInstance();
        mRemoteDataSource = RemoteDataSource.getInstance();
        mContext = MainApplication.getContext();
    }

    public static AmazingRepository getInstance() {
        synchronized (AmazingRepository.class) {
            if (INSTANCE == null)
                INSTANCE = new AmazingRepository();
        }
        return INSTANCE;
    }

    @Override
    public Favorite findFavorite(int discoverId) {
        return mLocalDataSource.findFavorite(discoverId);
    }

    @Override
    public void syncAppUpdate(AppUpdateCallBack callBack) {
        // 每天一次获取版本升级信息
        if (mRemoteDataSource.needSyncAppUpdate()) {
            if (Logc.allowPrints()) {
                Logc.d(Tags.AppUpdate, "Sync Remote !");
            }
            mRemoteDataSource.syncAppUpdate(callBack);
        } else {
            if (Logc.allowPrints()) {
                Logc.d(Tags.AppUpdate, "Sync Local !");
            }
            mLocalDataSource.syncAppUpdate(callBack);
        }
    }

    @Override
    public void syncDiscoverTab(DiscoverTabCallBack callBack) {

        if (mLocalDataSource.hasLocalDiscoverTab()) {
            if (Logc.allowPrints()) {
                Logc.d(Tags.DiscoverTab, "Sync Local !");
            }
            mLocalDataSource.syncDiscoverTab(callBack);
        }

        if (mRemoteDataSource.needSyncDiscoverTab()) {
            if (Logc.allowPrints()) {
                Logc.d(Tags.DiscoverTab, "Sync Remote !");
            }
            mRemoteDataSource.syncDiscoverTab(mLocalDataSource.hasLocalDiscoverTab() ? null : callBack);
        }
    }

    @Override
    public void syncDiscover(DiscoverTab tab, int discoverId, boolean remote, int limit, DiscoverCallBack callBack) {
        if (remote) {
            mRemoteDataSource.syncDiscover(tab, discoverId, true, limit, callBack);
        } else {
            // load local
            mLocalDataSource.syncDiscover(tab, discoverId, false, limit, callBack);
        }
    }

    @Override
    public void login(final String email, final String pwd, final LoginCallBack callBack) {
        if (callBack != null) {
            BmobUser bmobUser = BmobUser.getCurrentUser();
            if (bmobUser != null) {
                if (Logc.allowPrints()) {
                    Logc.d(Tags.Login, "User Local Account Result ! ObjectId = " + bmobUser.getObjectId());
                }
                callBack.onResult(bmobUser);
            } else {
                mRemoteDataSource.login(email, pwd, callBack);
            }
        }
    }

    @Override
    public void loadUserInfo(final BmobUser user, final UserInfoCallBack callBack) {
        if (user != null && callBack != null) {
            mLocalDataSource.loadUserInfo(user, new UserInfoCallBack() {
                @Override
                public void onResult(UserInfo userInfo) {
                    if (userInfo == null
                            && !TextUtils.isEmpty(user.getEmail()) && user.getEmailVerified()) {
                        mRemoteDataSource.loadUserInfo(user, callBack);
                    } else {
                        callBack.onResult(userInfo);
                    }
                }
            });
        }
    }

    @Override
    public void syncUserInfo(UserInfo userInfo, final UserInfoCallBack callBack) {
        if (userInfo != null && callBack != null) {
            mRemoteDataSource.syncUserInfo(userInfo, new UserInfoCallBack() {
                @Override
                public void onResult(UserInfo userInfo) {
                    if (userInfo != null && !TextUtils.isEmpty(userInfo.getObjectId())) {
                        mLocalDataSource.syncUserInfo(userInfo, callBack);
                    } else {
                        callBack.onResult(null);
                    }
                }
            });
        }
    }

    @Override
    public void loadUserFeedback(final UserFeedbackCallBack callBack) {
        if (callBack != null) {
            mLocalDataSource.loadUserFeedback(new UserFeedbackCallBack() {
                @Override
                public void onResult(List<UserFeedback> userFeedbackList) {
                    if (userFeedbackList != null && !userFeedbackList.isEmpty()) {
                        callBack.onResult(userFeedbackList);
                    } else {
                        mRemoteDataSource.loadUserFeedback(new UserFeedbackCallBack() {
                            @Override
                            public void onResult(List<UserFeedback> userFeedbackList) {
                                callBack.onResult(userFeedbackList);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void uploadUserFeedback(final UserFeedback userFeedback, final UserFeedbackCallBack callBack) {
        if (userFeedback != null && callBack != null) {
            mRemoteDataSource.uploadUserFeedback(userFeedback, new UserFeedbackCallBack() {
                @Override
                public void onResult(List<UserFeedback> userFeedbackList) {
                    if (userFeedbackList != null && !userFeedbackList.isEmpty()) {
                        mLocalDataSource.uploadUserFeedback(userFeedback, callBack);
                    } else {
                        callBack.onResult(null);
                    }
                }
            });
        }
    }

    @Override
    public void saveFavorite(Favorite favorite, AlbumContract.FavoriteCallBack callBack) {
        if(favorite != null
                && !TextUtils.isEmpty(favorite.getImgs())){
            mLocalDataSource.saveFavorite(favorite, callBack);
        }
    }

    @Override
    public void loadFavoriteList(FavoriteCallBack callBack) {
        mLocalDataSource.loadFavoriteList(callBack);
    }


}
