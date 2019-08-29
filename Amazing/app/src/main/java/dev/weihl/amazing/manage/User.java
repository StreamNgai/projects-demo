package dev.weihl.amazing.manage;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.data.bean.UserInfo;
import dev.weihl.amazing.data.event.LoginEvent;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;

/**
 * 用户的账户信息
 */
public class User implements IUser {

    private AmazingDataSource mRepository;
    private final static User INSTANCE = new User();
    private Info mInfo;

    private class Info {
        String name;
        String id;
        String email;
        String collectAlbumIds;// json 收藏专辑ID discover
        String payAlbumIds;// json 兑换专辑ID  discover
        boolean isLoaded;// 是否已经加载完毕
    }

    private User() {
        mRepository = AmazingRepository.getInstance();
    }

    public static User getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isLogin() {
        return mInfo != null && mInfo.isLoaded;
    }

    @Override
    public void doLogin() {
        // 建立在已授权登录过 本地登录 OR 远程登录
        BmobUser mUser = BmobUser.getCurrentUser();
        if (mUser != null && !TextUtils.isEmpty(mUser.getObjectId())) {
            mInfo = new Info();
            mInfo.name = mUser.getUsername();
            mInfo.id = mUser.getObjectId();
            mInfo.isLoaded = false;
            mRepository.loadUserInfo(mUser, new AmazingDataSource.UserInfoCallBack() {
                @Override
                public void onResult(UserInfo userInfo) {
                    if (userInfo != null) {
                        mInfo.email = userInfo.getUserEmail();
                        mInfo.payAlbumIds = userInfo.getPayAlbumIds();
                        mInfo.collectAlbumIds = userInfo.getCollectAlbumIds();
                        mInfo.isLoaded = true;
                    }
                    EventBus.getDefault().post(new LoginEvent(mInfo.isLoaded));
                }
            });
        }
    }

    @Override
    public String getName() {
        return  mInfo != null && !TextUtils.isEmpty(mInfo.name) ? mInfo.name : "";
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return mInfo != null && !TextUtils.isEmpty(mInfo.email) ? mInfo.email : "";
    }

    @Override
    public String getCollectAlbumIds() {
        return null;
    }

    @Override
    public String getPayAlbumIds() {
        return null;
    }
}
