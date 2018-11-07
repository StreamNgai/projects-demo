package dev.ngai.fantastic.business.picturebrowse;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.data.event.PayAlbumEvent;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.RefreshCollectEvent;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;
import dev.ngai.fantastic.data.source.CollectDataSource;
import dev.ngai.fantastic.data.source.CollectRepository;
import dev.ngai.fantastic.data.source.DiscoverDataSource;
import dev.ngai.fantastic.data.source.DiscoverRepository;
import dev.ngai.fantastic.data.source.UserPrivateDataSource;
import dev.ngai.fantastic.data.source.UserPrivateRepository;
import dev.ngai.fantastic.utils.DiscoverUtil;

public class PictureBrowsePresenter implements PictureBrowseContract.Presenter {


    public static final String PICTURE_DATA = "picture_data_list";
    public static final String PICTURE_INDEX = "picture_index";
    public static final String PICTURE_OBJ_ID = "picture_obj_id";
    public static final String PICTURE_ITEM_POSITION = "picture_item_position";// listView position
    private final String TAG = "PictureBrowsePresenter";
    private PictureBrowseContract.View mView;
    private int mDiscoverId;
    private DiscoverDataSource mDiscoverLocalDS;
    private UserPrivateDataSource mUserPrivateDS;
    private CollectDataSource mCollectLocalDS;
    private ArrayList<Imginfo> mImgList;
    private int mGoldCoin = -1;
    private int mIndex;
    private int mItemPosition;

    public PictureBrowsePresenter(PictureBrowseContract.View view, Intent intent) {
        this.mView = view;
        mDiscoverLocalDS = DiscoverRepository.getInstance();
        mUserPrivateDS = UserPrivateRepository.getInstance();
        mCollectLocalDS = CollectRepository.getInstance();
        pramIntent(intent);
        mView.setPresenter(this);
    }

    private void pramIntent(Intent intent) {
        mDiscoverId = intent.getExtras().getInt(PICTURE_OBJ_ID);
        mImgList = intent.getExtras().getParcelableArrayList(PICTURE_DATA);
        mIndex = intent.getExtras().getInt(PICTURE_INDEX);
        mItemPosition = intent.getExtras().getInt(PICTURE_ITEM_POSITION);
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public ArrayList<Imginfo> findPictureList(Intent intent) {
        return mImgList;
    }

    @Override
    public int findPictureIndex(Intent intent) {
        return mIndex;
    }

    @Override
    public void payAlbumByGoldCoin(int goldCoin) {
        if (goldCoin > Session.User.getGoldCoin()) {
            mView.showAccessGoldCoin();
        } else {
            Logc.d(TAG, "doPayAlbum !");
            doPayAlbumAction(goldCoin);
        }
    }

    private void doPayAlbumAction(final int goldCoin) {
        Session.User.setGoldCoin(Session.User.getGoldCoin() - goldCoin);
        User newUser = Session.getUpdateUser();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Logc.d(TAG, "更新用户信息成功");

                    mUserPrivateDS.saveHasPayAlbumId(mDiscoverId);
                    final Discover tDiscover = mDiscoverLocalDS.getDiscover(mDiscoverId);
                    if (!mCollectLocalDS.hasCollect(tDiscover)) {
                        DiscoverUtil.displayWh(tDiscover);
                        mDiscoverLocalDS.updateDiscover(tDiscover);
                        mCollectLocalDS.addCollect(tDiscover);
                        Logc.d(TAG, "将 id = " + tDiscover.getId() + " 加入收藏列表!");
                    } else {
                        Logc.d(TAG, "已在收藏列表");
                    }
                    mView.onPayAlbumResult(true);
                    EventBus.getDefault().post(new RefreshCollectEvent());
                    EventBus.getDefault().post(new RefreshDiscoverEvent(mItemPosition));
                } else {
                    Logc.d(TAG, "更新用户信息失败:" + e.getMessage());
                    Session.User.setGoldCoin(Session.User.getGoldCoin() + goldCoin);
                    mView.onPayAlbumResult(false);
                }

            }
        });
    }

    @Override
    public void payAlbumByRMB(int balance) {// 1元
        if (Session.User != null) {
            if (Session.User.getBalance() < balance) {
                mView.showBalanceDeficiency();
                EventBus.getDefault().post(new LoadingEvent(false, "", ""));
            } else {
                doPayAlbumByRMB(balance);
            }
        }
    }

    private void doPayAlbumByRMB(final int balance) {
        if(Session.User.getBalance() - balance > 0){
            Session.User.setBalance(Session.User.getBalance() - balance);
            User newUser = Session.getUpdateUser();
            BmobUser bmobUser = BmobUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Logc.d(TAG, "更新用户信息成功");

                        mUserPrivateDS.saveHasPayAlbumId(mDiscoverId);
                        final Discover tDiscover = mDiscoverLocalDS.getDiscover(mDiscoverId);
                        if (!mCollectLocalDS.hasCollect(tDiscover)) {
                            DiscoverUtil.displayWh(tDiscover);
                            mDiscoverLocalDS.updateDiscover(tDiscover);
                            mCollectLocalDS.addCollect(tDiscover);
                            Logc.d(TAG, "将 id = " + tDiscover.getId() + " 加入收藏列表!");
                        } else {
                            Logc.d(TAG, "已在收藏列表");
                        }
                        mView.onPayAlbumResult(true);
                        EventBus.getDefault().post(new RefreshCollectEvent());
                        EventBus.getDefault().post(new RefreshDiscoverEvent(mItemPosition));
                    } else {
                        Logc.d(TAG, "更新用户信息失败:" + e.getMessage());
                        Session.User.setBalance(Session.User.getBalance() + balance);
                        mView.onPayAlbumResult(false);
                    }

                }
            });
        }else {
            mView.onPayAlbumResult(false);
        }
    }

    @Override
    public boolean hasPayAlbum() {
        return mUserPrivateDS.hasPayAlbum(mDiscoverId);
    }

    @Override
    public int payGoldCoin() {
        if (mGoldCoin == -1) {
            if (mImgList != null && mImgList.size() > 4) {
                int lockCount = mImgList.size() - 4;
                mGoldCoin = lockCount * Constant.ALBUM_UNIT_PRICE;
                Logc.d(TAG, mGoldCoin + "");
            }
        }
        return mGoldCoin;
    }

    @Override
    public void onRefreshDiscoverTab() {
    }

    @Override
    public int payBalance() {
        int goldCoin = payGoldCoin();
        return Math.round(goldCoin / 500);
    }

    @Override
    public int payScore() {
        int goldCoin = payGoldCoin();
        return Math.round(goldCoin / 50);
    }

    @Override
    public void payAlbumByScore(final int score) {
        if(Session.User.getScore() - score >0){
            Session.User.setScore(Session.User.getScore() - score);
            User newUser = Session.getUpdateUser();
            BmobUser bmobUser = BmobUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Logc.d(TAG, "更新用户信息成功");

                        mUserPrivateDS.saveHasPayAlbumId(mDiscoverId);
                        final Discover tDiscover = mDiscoverLocalDS.getDiscover(mDiscoverId);
                        if (!mCollectLocalDS.hasCollect(tDiscover)) {
                            DiscoverUtil.displayWh(tDiscover);
                            mDiscoverLocalDS.updateDiscover(tDiscover);
                            mCollectLocalDS.addCollect(tDiscover);
                            Logc.d(TAG, "将 id = " + tDiscover.getId() + " 加入收藏列表!");
                        } else {
                            Logc.d(TAG, "已在收藏列表");
                        }
                        mView.onPayAlbumResult(true);
                        EventBus.getDefault().post(new RefreshAccountEvent());
                        EventBus.getDefault().post(new RefreshCollectEvent());
                        EventBus.getDefault().post(new RefreshDiscoverEvent(mItemPosition));
                    } else {
                        Logc.d(TAG, "更新用户信息失败:" + e.getMessage());
                        Session.User.setScore(Session.User.getScore() + score);
                        mView.onPayAlbumResult(false);
                    }

                }
            });
        }else {
            mView.showScoreDeficiency();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayAlbumEvent(PayAlbumEvent event) {
        Logc.d(TAG, "onPayAlbumEvent !");

        if (event.mType == PayAlbumEvent.Type.Show) {
            Discover mDiscover = mDiscoverLocalDS.getDiscover(mDiscoverId);
            if (mDiscover != null) {
                Logc.d(TAG, "showPayAlbumDialog !");
                mView.showPayAlbumDialog(mDiscover);
            }
        }

    }
}
