package dev.ngai.fantastic.business.main;

import android.widget.Chronometer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.BasePresenter;
import dev.ngai.fantastic.BaseView;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.data.Imginfo;

public interface MainContract {


    interface DiscoverView extends BaseView<DiscoverPresenter> {
    }

    interface DiscoverTabView extends BaseView<DiscoverPresenter> {
        void displayDiscoverDatas(ArrayList<Discover> datas);

        void onDataNotAvailable();

        void onRefresh(int itemPosition);
    }

    interface DiscoverPresenter extends BasePresenter {

        void onCollect(Discover discover);

        void bindDiscoverViewByTab(DiscoverTab tab, DiscoverTabView view);

        void updateDiscover(Discover discover);

        void loadDiscoverData(DiscoverTab tab, Constant.Load load, long lastId);

        boolean hasPayAlbum(int discoverId);

        boolean hasCollect(Discover discover);

        List<Imginfo> parsingDetails(String details);
    }

    // ******* Collect
    interface CollectView extends BaseView<CollectPresenter> {
        void displayCollectDatas(List<Discover> collectsNotPay, List<Discover> collectsHasPay);

        void onDataNotAvailable();

        void showSkipPictureDialog();
    }

    interface CollectPresenter extends BasePresenter {
        void loadCollectData();


        void doDeleteCollects(boolean hasPay ,List<Discover> stayDiscover,List<Discover> deleteDiscover);

        void doDownloadCollects(List<Discover> downloadDiscovers);

        List<Imginfo> parsingDetails(String details);
    }

    interface AccountPresenter extends BasePresenter {
        void onAccountExit();

        void updateApp();

        void onSyncAccCollects();

        boolean needUpdateApp();

        void requestEmailVerify();

        void requestResetPassword();
    }

    // Account
    interface AccountView extends BaseView<AccountPresenter> {
        void showUserInfo();

        void nothingAppUpdate(String content);

        void onTickIncreaseCoin(int goldCoin);

        void showTipVersionUpdate();

        void showTipRequestEmailVerify(boolean b);

        void showTipResetPassword(boolean b);
    }

    interface MainView extends BaseView<MainPresenter>{

        void notWifiNetworkTip();

        void showLoadingDialog(boolean hasShow,String title ,String content);

        void onSwitchMainPage(int position);

        void showEmailNotVerifiedBadge();
    }

    interface MainPresenter extends BasePresenter{

        void onChronometerTick(Chronometer chronometer);
    }
}
