package dev.weihl.amazing.business.account;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.business.BasePresenter;
import dev.weihl.amazing.business.BaseView;

/**
 * @author Ngai
 * @since 2018/6/5
 * Des:
 */
public interface AccountContract {

    interface View extends BaseView<Presenter>{

        void loginResult(BmobUser user);

        void registerResult(boolean b, String errorMessage);

        void retrievePasswordResult(boolean b);
    }

    interface Presenter extends BasePresenter{

        void login(String email, String password);

        void register(String accName, String email, String password);

        void retrievePassword(String email);
    }

}
