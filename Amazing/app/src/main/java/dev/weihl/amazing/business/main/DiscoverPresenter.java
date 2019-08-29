package dev.weihl.amazing.business.main;

import java.util.List;

import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;

/**
 * @author Ngai
 * @since 2018/7/12
 * Des:
 */
public class DiscoverPresenter implements MainContract.DiscoverPresenter {

    MainContract.DiscoverView mDiscoverView;
    AmazingDataSource mRepository;

    public DiscoverPresenter(MainContract.DiscoverView mainView) {
        this.mDiscoverView = mainView;
        mDiscoverView.setPresenter(this);
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loadDiscoverList(final DiscoverTab tab, int discoverId, final boolean remote) {
        mRepository.syncDiscover(tab, discoverId, remote, 20, new AmazingDataSource.DiscoverCallBack() {
            @Override
            public void onResult(List<Discover> discoverList) {
                mDiscoverView.refreshDiscoverList(tab, remote, discoverList);
            }
        });
    }
}
