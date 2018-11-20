package dev.ngai.fantastic.business.main.discover;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dev.ngai.fantastic.BuildConfig;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.event.RefreshCollectEvent;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;
import dev.ngai.fantastic.data.source.CollectDataSource;
import dev.ngai.fantastic.data.source.CollectRepository;
import dev.ngai.fantastic.data.source.DiscoverDataSource;
import dev.ngai.fantastic.data.source.DiscoverRepository;
import dev.ngai.fantastic.data.source.UserPrivateRepository;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.utils.ImginfoUtil;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiscoverPresenter implements MainContract.DiscoverPresenter {

    private static final String TAG = "DiscoverPresenter";
    private int mPageSize = 5;
    private MainContract.DiscoverView mDiscoverView;
    private DiscoverDataSource mDiscoverRepository;
    private CollectDataSource mCollectRepository;
    private UserPrivateRepository mUserPrivateDS;
    private HashMap<String, MainContract.DiscoverTabView> mDiscoverTabViews = new HashMap<>();

    public DiscoverPresenter(MainContract.DiscoverView discoverView) {
        this.mDiscoverView = checkNotNull(discoverView);
        this.mDiscoverRepository = checkNotNull(DiscoverRepository.getInstance());
        this.mCollectRepository = checkNotNull(CollectRepository.getInstance());
        this.mUserPrivateDS = checkNotNull(UserPrivateRepository.getInstance());

        discoverView.setPresenter(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void bindDiscoverViewByTab(DiscoverTab tab, MainContract.DiscoverTabView view) {
        Log.d(TAG, "bindDiscoverViewByTab Tab = " + tab + " ; DiscoverView = " + view.toString());
        mDiscoverTabViews.put(tab.tab, view);
        view.setPresenter(this);
    }

    @Override
    public void updateDiscover(Discover discover) {
        mDiscoverRepository.updateDiscover(discover);
    }

    @Override
    public void loadDiscoverData(final DiscoverTab discoverTab, Constant.Load load, long lastId) {
        mDiscoverRepository.getDiscoverList(discoverTab.tab, load, lastId, mPageSize, new DiscoverDataSource.LoadDiscoverListCallback() {
            @Override
            public void onDiscoverListLoaded(List<Discover> discoverList) {
                Log.d(TAG, "loadDiscoverData.onDiscoverListLoaded" + discoverList.size());
                DaoHelper.getDaoSession().getDiscoverDao().insertOrReplaceInTx(discoverList);
                mDiscoverTabViews.get(discoverTab.tab).displayDiscoverDatas((ArrayList<Discover>) discoverList);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "loadDiscoverData.onDataNotAvailable");
                mDiscoverTabViews.get(discoverTab.tab).onDataNotAvailable();
            }
        });
    }

    @Override
    public boolean hasPayAlbum(int discoverId) {
        return mUserPrivateDS.hasPayAlbum(discoverId);
    }

    @Override
    public boolean hasCollect(Discover discover) {
        return mCollectRepository.hasCollect(discover);
    }

    @Override
    public List<Imginfo> parsingDetails(String details) {
        return ImginfoUtil.parsingDetails(details);
    }

    @Override
    public void onCollect(Discover discover) {
        mCollectRepository.addCollect(discover);

        EventBus.getDefault().post(new RefreshCollectEvent());
        EventBus.getDefault().post(new RefreshAccountEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDiscoverEvent(RefreshDiscoverEvent event) {
        Logc.d(TAG, "RefreshDiscoverEvent ！");
        Collection<MainContract.DiscoverTabView> tabViews = mDiscoverTabViews.values();
        for (MainContract.DiscoverTabView tabView : tabViews) {
            tabView.onRefresh(event.ItemPosition);
        }
    }
}
