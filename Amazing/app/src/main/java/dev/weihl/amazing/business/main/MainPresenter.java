package dev.weihl.amazing.business.main;

import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.business.account.LoginActivity;
import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.UserFeedback;
import dev.weihl.amazing.data.event.NetworkEvent;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;
import dev.weihl.amazing.executors.AppExecutors;
import dev.weihl.amazing.executors.UpgradeRunnable;
import dev.weihl.amazing.receiver.NetworkReceiver;


/**
 * Des:
 * Created by Weihl
 * 2017/7/19
 */
public class MainPresenter implements MainContract.Presenter {

    MainContract.View mMainView;
    AmazingDataSource mRepository;

    MainPresenter(MainContract.View mainView) {
        this.mMainView = mainView;
        mMainView.setPresenter(this);
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        NetworkReceiver.registerN(mMainView.getActivity());// 网络状态权限
        AppExecutors.executeDiskIO(new UpgradeRunnable());// 升级

        loadDiscoverTabs();

    }

    private void loadDiscoverTabs() {
        mRepository.syncDiscoverTab(new AmazingDataSource.DiscoverTabCallBack() {
            @Override
            public void onResult(List<DiscoverTab> tabList) {
                mMainView.displayDiscoverTabs(tabList);
            }
        });
    }

    private void login() {
        BmobUser user = BmobUser.getCurrentUser();
        if (user == null) {
            mMainView.getActivity().startActivity(new Intent(mMainView.getActivity(), LoginActivity.class));
        }
    }

    private void feedBack() {
        mRepository.loadUserFeedback(new AmazingDataSource.UserFeedbackCallBack() {
            @Override
            public void onResult(List<UserFeedback> userFeedbackList) {

            }
        });
        BmobUser bmobUser = BmobUser.getCurrentUser();
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(bmobUser.getObjectId());
        userFeedback.setUserMail(bmobUser.getEmail());
        userFeedback.setQuestion(String.valueOf(System.currentTimeMillis()));
        mRepository.uploadUserFeedback(userFeedback, new AmazingDataSource.UserFeedbackCallBack() {
            @Override
            public void onResult(List<UserFeedback> userFeedbackList) {

            }
        });
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {

    }

}
