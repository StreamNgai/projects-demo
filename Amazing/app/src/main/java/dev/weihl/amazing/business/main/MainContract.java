package dev.weihl.amazing.business.main;

import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.List;

import dev.weihl.amazing.business.BasePresenter;
import dev.weihl.amazing.business.BaseView;
import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;


public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
    }


    interface DiscoverView<T> {

        void setPresenter(T presenter);

        void refreshDiscoverList(DiscoverTab tab, boolean remote, List<Discover> discoverList);
    }

    interface DiscoverPresenter extends BasePresenter {
        void loadDiscoverList(DiscoverTab tab, int discoverId, boolean remote);
    }
}
