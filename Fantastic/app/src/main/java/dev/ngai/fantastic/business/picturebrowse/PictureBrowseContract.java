package dev.ngai.fantastic.business.picturebrowse;

import android.content.Intent;

import java.util.ArrayList;

import dev.ngai.fantastic.BasePresenter;
import dev.ngai.fantastic.BaseView;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;

/**
 * Created by Ngai on 2017/2/8.
 */

public interface PictureBrowseContract {


    interface View extends BaseView<Presenter>{

        void showPayAlbumDialog(Discover discover);

        void showAccessGoldCoin();

        void onPayAlbumResult(boolean hasPaySuccess);

        void showScoreDeficiency();

        void showBalanceDeficiency();
    }

    interface Presenter extends BasePresenter{

        ArrayList<Imginfo> findPictureList(Intent intent);

        int findPictureIndex(Intent intent);

        void payAlbumByGoldCoin(int goldCoin);

        void payAlbumByRMB(int balance);

        boolean hasPayAlbum();

        int payGoldCoin();

        void onRefreshDiscoverTab();

        int payBalance();

        int payScore();

        void payAlbumByScore(int mScore);
    }

}
