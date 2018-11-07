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

/**
 * @author Ngai
 * @since 2018/6/4
 * Des:
 */
public class StartPresenter implements StartContract.Presenter {

    StartContract.View mStartView;
    AmazingDataSource mRepository;
    BmobUser mUser;
    boolean isReadyUserInfo;
    boolean isReadyDiscoverTab;

    public StartPresenter(StartContract.View startView) {
        mStartView = startView;
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {
//        login();// 可以省了
        mUser = BmobUser.getCurrentUser();
        loadUserInfo();
        syncDiscoverTab();
    }

    private void syncDiscoverTab() {
        isReadyDiscoverTab = false;
        mRepository.syncDiscoverTab(new AmazingDataSource.DiscoverTabCallBack() {
            @Override
            public void onResult(List<DiscoverTab> tabList) {
                Session.tabList = tabList;
                isReadyDiscoverTab = true;
                startMain();
            }
        });
    }

    private void loadUserInfo() {
        if (mUser != null && !TextUtils.isEmpty(mUser.getObjectId())) {
            isReadyUserInfo = false;
            mRepository.loadUserInfo(mUser, new AmazingDataSource.UserInfoCallBack() {
                @Override
                public void onResult(UserInfo userInfo) {
                    Session.userInfo = userInfo;
                    isReadyUserInfo = true;
                    startMain();
                }
            });
        } else {
            isReadyUserInfo = true;
        }
    }


    @Override
    public void stop() {

    }

    @Override
    public boolean isReady() {
        return isReadyUserInfo && isReadyDiscoverTab;
    }

    @Override
    public void startMain() {
        if (isReady()) {
            mStartView.getActivity().startActivity(new Intent(mStartView.getActivity(), MainActivity.class));
            mStartView.getActivity().finish();
        }
    }
}
