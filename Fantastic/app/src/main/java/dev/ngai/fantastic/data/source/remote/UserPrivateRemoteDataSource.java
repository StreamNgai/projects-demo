package dev.ngai.fantastic.data.source.remote;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.UserPrivate;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.source.UserPrivateDataSource;
import dev.ngai.fantastic.data.source.local.UserPrivateLocalDataSource;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;

/**
 * @author Ngai
 * @since 2017/7/26
 * Des:
 */
public class UserPrivateRemoteDataSource implements UserPrivateDataSource {

    private final String TAG = "UserPrivateRemoteDataSource";
    private static UserPrivateRemoteDataSource INSTANCE = new UserPrivateRemoteDataSource();

    private UserPrivateLocalDataSource localDataSource;

    private UserPrivateRemoteDataSource() {
        localDataSource = UserPrivateLocalDataSource.getInstance();
    }

    public static UserPrivateRemoteDataSource getInstance() {
        synchronized (UserPrivateRemoteDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new UserPrivateRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public boolean hasPayAlbum(int id) {
        return false;
    }

    @Override
    public List<Integer> getPayAlbumIds() {
        return null;
    }


    @Override
    public void saveHasPayAlbumId(int id) {

    }

    @Override
    public void saveHasPayAlbumId(List<Integer> ids) {

    }

    @Override
    public void syncUserPrivate() {
        // 将本地收藏信息同步到服务器
        if (Session.User != null) {
            if (!TextUtils.isEmpty(Session.User.getPrivateId())) {
                // update
                updateUserPrivate();
            } else {
                // save
                saveUserPrivate();
            }
        }
    }

    private void updateUserPrivate() {
        UserPrivate userPrivate = localDataSource.getUserPrivate();
        userPrivate.update(Session.User.getPrivateId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Logc.d(TAG, "UserPrivate 更新成功");
                    SharedPres.putBoolean(PrefsKey.UserPrivateNeedSync,false);
                } else {
                    Logc.d(TAG, "UserPrivate 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                EventBus.getDefault().post(new RefreshAccountEvent());
                EventBus.getDefault().post(new LoadingEvent(false, "", ""));
            }
        });
    }

    private void saveUserPrivate() {
        UserPrivate dbUserPrivate = localDataSource.getUserPrivate();
        UserPrivate userPrivate = new UserPrivate();
        userPrivate.setCollectIds(dbUserPrivate.getCollectIds());
        userPrivate.setPayAlbumIds(dbUserPrivate.getPayAlbumIds());
        userPrivate.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Logc.d(TAG, "UserPrivate : 创建数据成功：" + objectId);
                    updateUserPrivateId(objectId);
                    SharedPres.putBoolean(PrefsKey.UserPrivateNeedSync,false);
                } else {
                    Logc.d(TAG, "UserPrivate : 创建数据失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                EventBus.getDefault().post(new RefreshAccountEvent());
                EventBus.getDefault().post(new LoadingEvent(false, "", ""));
            }
        });
    }


    private void updateUserPrivateId(final String objectId) {
        Session.User.setPrivateId(objectId);
        User newUser = Session.getUpdateUser();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Logc.d(TAG, "更新用户信息成功");
                    Session.User.setPrivateId(objectId);
                } else {
                    Session.User.setPrivateId("-1");
                    updateUserPrivateId(objectId);
                    Logc.d(TAG, "更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void saveLocalHasCollectIds(String localCollectIdsJson) {

    }

    @Override
    public List<Integer> getLocalHasCollectIds() {
        return null;
    }

}
