package dev.ngai.fantastic.business.login;

import android.app.Activity;
import android.content.Context;

import dev.ngai.fantastic.BasePresenter;
import dev.ngai.fantastic.BaseView;
import dev.ngai.fantastic.business.login.fragment.RegisteredFragment;

/**
 * @author Weihl
 * @since 2017/7/4
 * Des:
 */
public interface LoginContract {

    interface LoginView extends BaseView<LoginPresenter> {

        void loginResult(boolean result);

        void resetPasswordByEmailResult(boolean b);
    }

    interface RegisteredView extends BaseView<LoginPresenter> {
        void showVerifyEmailCodeLayout();

        void registerResult(boolean result,String error);

    }

    interface LoginPresenter extends BasePresenter {

        boolean legalEmail(String email);// 检验合法

        void sendVerifyEmail();// 发送邮件; 验证码

        String getEmailVerifyCode();

        boolean verifyEmailCode();

        void login(String email, String password);

        void registerAccount(String email, boolean verifyEmail, String name, String password);

        void bindRegisteredView(RegisteredView registeredView);

        void resetPasswordByEmail(String emailInput);
    }

}
