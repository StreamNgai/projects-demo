package dev.ngai.fantastic.business.main.account;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.event.NetworkEvent;
import dev.ngai.fantastic.data.event.NotifyUpdateAppEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.RefreshCollectEvent;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;
import dev.ngai.fantastic.data.event.SynUserPrivateEvent;
import dev.ngai.fantastic.data.event.TickEvent;
import dev.ngai.fantastic.data.source.local.CollectLocalDataSource;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.UserPrivateLocalDataSource;
import dev.ngai.fantastic.data.source.remote.UserPrivateRemoteDataSource;
import dev.ngai.fantastic.utils.AppUtil;
import dev.ngai.fantastic.utils.NetworkUtil;
import dev.ngai.fantastic.utils.UpdateAgentUtil;

import static com.google.common.base.Preconditions.checkNotNull;


public class AccountPresenter implements MainContract.AccountPresenter {

    private String TAG = "AccountPresenter";
    private MainContract.AccountView mAccountView;

    public AccountPresenter(MainContract.AccountView accountView) {
        this.mAccountView = checkNotNull(accountView);
        mAccountView.setPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshAccountEvent(RefreshAccountEvent event) {
        Logc.d(TAG, "onRefreshAccountEvent ");
        mAccountView.showUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSynUserPrivateEvent(SynUserPrivateEvent event) {
        Logc.d(TAG, "onSynUserPrivateEvent " + Session.User.toString());
        UserPrivateLocalDataSource.getInstance().syncUserPrivate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {
        if (NetworkUtil.isNetworkConnected(Appfantastic.getContext())) {
            Logc.d(TAG, "onNetworkEvent ！");
            if (!Session.hasLogin()
                    || !Session.hasActivate()) {
                Session.doAccountLogin();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickEvent(TickEvent event) {
        Logc.d(TAG, "onTickEvent ！Session.User != null / " + (Session.User != null));
        if (Session.User != null) {
            if (event.mType == TickEvent.Type.M) {
                Logc.d(TAG, "onTickEvent 收益！");
                Session.User.setGoldCoin(Session.User.getGoldCoin() + 100);
            }
            DaoHelper.getDaoSession().getUserDao().insertOrReplace(Session.User);
            updateGoldCoin(event);
        }
    }

    private void updateGoldCoin(final TickEvent event) {
        User newUser = Session.getUpdateUser();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Logc.d(TAG, "更新用户信息成功");
                    if ((event.mType == TickEvent.Type.M)) {
                        mAccountView.onTickIncreaseCoin(Session.User.getGoldCoin());
                    }
                } else {
                    Logc.d(TAG, "更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyUpdateAppEvent(NotifyUpdateAppEvent event) {
        Logc.d(TAG, "NotifyUpdateAppEvent ！");
        if (needUpdateApp()) {
            mAccountView.showTipVersionUpdate();
        }
    }

    @Override
    public void onAccountExit() {
        Session.User = null;
        DaoHelper.getDaoSession().getCollectDao().deleteAll();
        DaoHelper.getDaoSession().getUserPrivateDao().deleteAll();
        DaoHelper.getDaoSession().getUserDao().deleteAll();

        // 这里可以优化请求次数!!S
        UserPrivateLocalDataSource.getInstance().refreshPayAlumIds();
        CollectLocalDataSource.getInstance().refreshLocalHasCollectIds();
        mAccountView.showUserInfo();
        EventBus.getDefault().post(new RefreshCollectEvent());
        EventBus.getDefault().post(new RefreshDiscoverEvent(-1));
    }

    @Override
    public void updateApp() {
        if (needUpdateApp()) {
            UpdateAgentUtil.startInstall(Appfantastic.getContext()
                    , UpdateAgentUtil.getFileByUpdateInfo(Session.UpdateResponse));
        } else {
            String content = "当前版本[" + AppUtil.getVersionName() + "]不需要更新!";
            mAccountView.nothingAppUpdate(content);
        }
    }

    @Override
    public boolean needUpdateApp() {
        return Session.UpdateResponse != null
                && AppUtil.getVersionCode() < Session.UpdateResponse.version_i;
    }

    @Override
    public void requestEmailVerify() {
        BmobUser.requestEmailVerify(Session.User.getEmail(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mAccountView.showTipRequestEmailVerify(e == null);

                if(e != null){
                    Logc.d(TAG,"requestEmailVerify : "+e.getMessage());
                }
            }
        });
    }

    @Override
    public void requestResetPassword() {
        if(Session.User != null && !TextUtils.isEmpty(Session.User.getEmail())){
            BmobUser.resetPasswordByEmail(Session.User.getEmail(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    mAccountView.showTipResetPassword(e == null);

                    if(e != null){
                        Logc.d(TAG,"requestResetPassword : "+e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onSyncAccCollects() {
        UserPrivateRemoteDataSource.getInstance().syncUserPrivate();
    }
}

