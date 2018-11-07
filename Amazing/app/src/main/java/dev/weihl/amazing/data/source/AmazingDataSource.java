package dev.weihl.amazing.data.source;

import java.util.List;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.business.browse.AlbumContract;
import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.UpgradeApp;
import dev.weihl.amazing.data.bean.UserFeedback;
import dev.weihl.amazing.data.bean.UserInfo;

/**
 * @author Ngai
 * @since 2018/5/25
 * Des:
 */
public interface AmazingDataSource {



    interface AppUpdateCallBack {
        void onResult(UpgradeApp appUpdate);
    }

    interface DiscoverTabCallBack {
        void onResult(List<DiscoverTab> tabList);
    }

    interface DiscoverCallBack {
        void onResult(List<Discover> discoverList);
    }

    interface LoginCallBack {
        void onResult(BmobUser user);
    }

    interface UserInfoCallBack {
        void onResult(UserInfo userInfo);
    }

    interface UserFeedbackCallBack {
        void onResult(List<UserFeedback> userFeedbackList);
    }

    interface FavoriteCallBack {
        void onResult(List<Favorite> favoriteList);
    }

    void syncAppUpdate(AppUpdateCallBack callBack);

    void syncDiscoverTab(DiscoverTabCallBack callBack);

    void syncDiscover(DiscoverTab tab, int discoverId, boolean remote, int limit, DiscoverCallBack callBack);

    void login(String email, String pwd, LoginCallBack callBack);

    // 根据用户ID，加载用户信息（若本地为空，则同步远程服务器）
    void loadUserInfo(BmobUser user, UserInfoCallBack callBack);

    // 同步用户信息（若本地用户信息变化，同步远程服务器）
    void syncUserInfo(UserInfo userInfo, UserInfoCallBack callBack);

    void loadUserFeedback(UserFeedbackCallBack callBack);

    void uploadUserFeedback(UserFeedback userFeedback, UserFeedbackCallBack callBack);

    void saveFavorite(Favorite favorite, AlbumContract.FavoriteCallBack callBack);

    void loadFavoriteList(FavoriteCallBack callBack);

    Favorite findFavorite(int discoverId);
}
