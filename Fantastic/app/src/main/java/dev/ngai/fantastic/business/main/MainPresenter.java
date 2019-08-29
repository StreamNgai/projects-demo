package dev.ngai.fantastic.business.main;

import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.widget.Chronometer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.data.event.NetworkEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.SwitchMainPageEvent;
import dev.ngai.fantastic.data.event.TickEvent;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.tasks.AppUpdateTask;
import dev.ngai.fantastic.tasks.TaskScheduler;
import dev.ngai.fantastic.utils.NetworkUtil;

/**
 * Des:
 * Created by Weihl
 * 2017/7/19
 */
public class MainPresenter implements MainContract.MainPresenter {

    MainContract.MainView mMainView;


    public MainPresenter(MainContract.MainView mainView) {
        this.mMainView = mainView;
        mMainView.setPresenter(this);
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        onRefreshAccountEvent(null);
        TaskScheduler.execute(new AppUpdateTask());
    }

    @Override
    public void stop() {

        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {

        if (event.mNetworkType != ConnectivityManager.TYPE_WIFI
                && !SharedPres.getBoolean(PrefsKey.NotWifiNetworkTip, false)) {
            mMainView.notWifiNetworkTip();
        }

//        if(NetworkUtil.isNetworkConnected(Appfantastic.getContext())
//                && !Session.hasLogin()){
//            Session.doAccountLogin();
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadingEvent(LoadingEvent event) {
        mMainView.showLoadingDialog(event.hasShow,event.title,event.content);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshAccountEvent(RefreshAccountEvent event) {
        mMainView.showEmailNotVerifiedBadge();

    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        long diff = SystemClock.elapsedRealtime() - chronometer.getBase();

        if (diff >= 60 * 1000 * 10) {
            Logc.d("Chronometer", "diff = " + diff
                    + " ；elapsedRealtime = " + SystemClock.elapsedRealtime() + " ; Base : " + chronometer.getBase());
            chronometer.setBase(SystemClock.elapsedRealtime());
            EventBus.getDefault().post(new TickEvent(TickEvent.Type.M));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchMainPageEvent(SwitchMainPageEvent event) {
        mMainView.onSwitchMainPage(event.mPosition);
    }

}
