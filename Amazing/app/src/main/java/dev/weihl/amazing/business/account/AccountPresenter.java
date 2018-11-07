package dev.weihl.amazing.business.account;

import android.text.TextUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dev.weihl.amazing.data.bean.UserInfo;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;

/**
 * @author Ngai
 * @since 2018/6/5
 * Des:
 */
public class AccountPresenter implements AccountContract.Presenter {

    AccountContract.View mAccountView;
    AmazingRepository mRepository;

    public AccountPresenter(AccountContract.View accountView) {
        mAccountView = accountView;
        mRepository = AmazingRepository.getInstance();
        mAccountView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void login(String email, String password) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mRepository.login(email, password, new AmazingDataSource.LoginCallBack() {
                @Override
                public void onResult(final BmobUser user) {
                    if (user != null) {
                        mRepository.loadUserInfo(user, new AmazingDataSource.UserInfoCallBack() {
                            @Override
                            public void onResult(UserInfo userInfo) {
                                mAccountView.loginResult(user);
                            }
                        });
                    } else {
                        mAccountView.loginResult(null);
                    }
                }
            });
        }

    }

    @Override
    public void register(String accName, final String email, final String password) {
        BmobUser bu = new BmobUser();
        bu.setUsername(accName);
        bu.setPassword(password);
        bu.setEmail(email); //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<BmobUser>() {//MyUser
            @Override
            public void done(BmobUser user, BmobException e) {
                String errorMessage = null;
                if (e != null) {
                    errorMessage = e.getMessage();
                    if (e.getErrorCode() == 203) {
                        errorMessage = "邮箱[" + email + "]已被注册,请换个邮箱试试!";
                    }
                }
                mAccountView.registerResult(false, errorMessage);
            }
        });
    }

    @Override
    public void retrievePassword(final String email) {
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mAccountView.retrievePasswordResult(e == null);
            }
        });
    }
}
