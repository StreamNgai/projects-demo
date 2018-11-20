package dev.ngai.fantastic.business.splash;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import dev.ngai.fantastic.BasePresenter;
import dev.ngai.fantastic.BaseView;
import dev.ngai.fantastic.data.DiscoverTab;

/**
 * @author Ngai
 * @since 2017/7/25
 * Des:
 */
public interface SplashContract {

    interface Presenter extends BasePresenter{
        void loginRequestSuccess();
    }


    interface View extends BaseView<Presenter>{

        void finish();

        void notNetworkConnected();

        void startMainActivity(List<DiscoverTab> list);

        void startLoginActivity();
    }
}
