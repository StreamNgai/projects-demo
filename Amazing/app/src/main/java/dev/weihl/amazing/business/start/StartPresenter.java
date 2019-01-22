package dev.weihl.amazing.business.start;

import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.Session;
import dev.weihl.amazing.business.account.LoginActivity;
import dev.weihl.amazing.business.main.MainActivity;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.UserInfo;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;
import dev.weihl.amazing.manage.User;

/**
 * @author Ngai
 * @since 2018/6/4
 * Des:
 */
public class StartPresenter implements StartContract.Presenter {

    StartContract.View mStartView;
    AmazingDataSource mRepository;

    StartPresenter(StartContract.View startView) {
        mStartView = startView;
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {
        User.getInstance().doLogin();
        syncDiscoverTab();
    }

    private void syncDiscoverTab() {
        mRepository.syncDiscoverTab(null);
    }

    @Override
    public void stop() {

    }
 
}
