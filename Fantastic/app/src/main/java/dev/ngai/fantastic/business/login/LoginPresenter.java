package dev.ngai.fantastic.business.login;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.SynUserPrivateEvent;
import dev.ngai.fantastic.data.source.local.CollectLocalDataSource;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.dao.UserDao;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Weihl
 * @since 2017/7/4
 * Des:
 */
public class LoginPresenter implements LoginContract.LoginPresenter {

    private final String TAG = "LoginPresenter";
    private LoginContract.LoginView mLoginView;
    private LoginContract.RegisteredView mRegisView;
    private boolean hasDestroy = true;

    public LoginPresenter(LoginContract.LoginView loginView) {
        this.mLoginView = checkNotNull(loginView);
        mLoginView.setPresenter(this);
    }

    @Override
    public boolean legalEmail(String email) {
        return true;
    }

    @Override
    public void sendVerifyEmail() {

    }

    @Override
    public String getEmailVerifyCode() {

        return null;
    }

    @Override
    public boolean verifyEmailCode() {
        return false;
    }

    @Override
    public void login(final String email, final String password) {
        BmobUser.loginByAccount(email, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (hasDestroy) {
                    if (user != null) {
                        user.setOauthPwd(password);
                        user.setLoginUnique(email);
                        Session.User = user;
                        saveUserInfo(Session.User);
                        EventBus.getDefault().post(new SynUserPrivateEvent());
                        EventBus.getDefault().post(new RefreshAccountEvent());
                        Logc.d(TAG, "用户登陆成功");
                    }
                    mLoginView.loginResult(user != null);
                }
            }
        });
    }

    @Override
    public void registerAccount(final String email, boolean verifyEmail, String name, final String password) {
        User bu = new User();
        bu.setUsername(name);
        bu.setPassword(password);
        bu.setEmail(email); //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {//MyUser
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    user.setOauthPwd(password);
                    user.setLoginUnique(email);
                    Session.User = user;
                    saveUserInfo(Session.User);

                    EventBus.getDefault().post(new RefreshAccountEvent());
                    mRegisView.registerResult(true, null);
                } else {
                    String errorMessage = e.getMessage();
                    if (e.getErrorCode() == 203) {
                        errorMessage = "邮箱[" + email + "]已被注册,请换个邮箱试试!";
                    }
                    mRegisView.registerResult(false, errorMessage);
                }
            }
        });
    }

    private static void saveUserInfo(User user) {
        SharedPres.putString(PrefsKey.UserName,user.getUsername());
        UserDao userDao = DaoHelper.getDaoSession().getUserDao();
        userDao.deleteAll();
        userDao.insertOrReplace(user);
    }

    @Override
    public void bindRegisteredView(LoginContract.RegisteredView registeredView) {
        this.mRegisView = registeredView;
        mRegisView.setPresenter(this);
    }

    @Override
    public void resetPasswordByEmail(String emailInput) {
        final String email = emailInput;

        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mLoginView.resetPasswordByEmailResult(true);
                } else {
                    mLoginView.resetPasswordByEmailResult(false);
                }
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        this.hasDestroy = true;
    }
}
