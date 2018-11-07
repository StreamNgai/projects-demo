package dev.ngai.fantastic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.update.UpdateResponse;
import dev.ngai.fantastic.business.login.LoginActivity;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.event.LoginEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.dao.UserDao;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.NetworkUtil;

/**
 * @author Ngai
 * @since 2017/7/19
 * Des:
 */
public class Session {
    public static User User = null;
    public static UpdateResponse UpdateResponse;
    private static int displayWidth;


    public static int getDisplayWidth() {
        return displayWidth;
    }

    public static void initDefaultDisplay(Activity activity) {
        displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static User getUpdateUser() {
        User updateUser = new User();
        updateUser.setGoldCoin(Session.User.getGoldCoin());
        updateUser.setBalance(Session.User.getBalance());
        updateUser.setPrivateId(Session.User.getPrivateId());
        updateUser.setScore(Session.User.getScore());
        return updateUser;
    }

    public static boolean hasLogin() {
        // 若无网络情况下，本地已有用户信息，则直接本地登录。
        if (User != null
                && !TextUtils.isEmpty(User.getLoginUnique())
                && !TextUtils.isEmpty(User.getOauthPwd())) {
            return true;
        }
        return false;
    }

    public static boolean hasActivate() {
        if (hasLogin()) {
            if (User.getEmailVerified() != null
                    && User.getEmailVerified()) {
                return true;
            }
        }
        return false;
    }

    public static void doAccountLogin() {
        List<User> mUsers = DaoHelper.getDaoSession().getUserDao().loadAll();
        if (mUsers != null && !mUsers.isEmpty()) {
            final User user = mUsers.get(0);
            if (user != null
                    && !TextUtils.isEmpty(user.getLoginUnique())
                    && !TextUtils.isEmpty(user.getOauthPwd())) {
                if (NetworkUtil.isNetworkConnected(Appfantastic.getContext())) {
                    Logc.d("Login", "net loginUnique / oauthPwd =  " + user.getLoginUnique() + " / " + user.getOauthPwd());
                    BmobUser.loginByAccount(user.getLoginUnique(),
                            user.getOauthPwd(), new LogInListener<User>() {
                                @Override
                                public void done(User userL, BmobException e) {
                                    Logc.d("Login", "User.Cache = 用户登陆 ? " + (userL != null));
                                    if (userL != null) {
                                        userL.setOauthPwd(user.getOauthPwd());
                                        userL.setLoginUnique(user.getLoginUnique());
                                        User = userL;
                                        saveUserInfo();
                                        EventBus.getDefault().post(new LoginEvent(true));
                                        EventBus.getDefault().post(new RefreshAccountEvent());
                                    } else {
                                        Intent loginIntent = new Intent(Appfantastic.getContext(), LoginActivity.class);
                                        Bundle mBundle = new Bundle();
                                        mBundle.putString(Constant.USER_NAME, user.getLoginUnique());
                                        mBundle.putBoolean(Constant.TOOLBAR_NEEDCALLBACK, false);
                                        loginIntent.putExtras(mBundle);
                                        Appfantastic.getContext().startActivity(loginIntent);
                                        EventBus.getDefault().post(new LoginEvent(false));
                                    }
                                }
                            });
                    return;
                }
                Logc.d("Login", "local loginUnique / oauthPwd =  " + user.getLoginUnique() + " / " + user.getOauthPwd());
                String userName = SharedPres.getString(PrefsKey.UserName,"Fantatic");
                user.setUsername(userName);
                User = user;
            }
        }
        EventBus.getDefault().post(new LoginEvent(false));
    }

    private static void saveUserInfo() {
        UserDao userDao = DaoHelper.getDaoSession().getUserDao();
        userDao.deleteAll();
        userDao.insertOrReplace(User);
    }
}
