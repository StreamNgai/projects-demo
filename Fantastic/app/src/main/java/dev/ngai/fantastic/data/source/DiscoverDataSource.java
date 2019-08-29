package dev.ngai.fantastic.data.source;

import java.util.List;

import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.data.Discover;

/**
 * Des:
 * Created by Ngai
 * 2017/6/27
 */

public interface DiscoverDataSource {

    boolean onDiscoverDaoNotNull(String tab);

    // Discover
    interface LoadDiscoverListCallback {

        void onDiscoverListLoaded(List<Discover> discover);

        void onDataNotAvailable();
    }
    void getDiscoverList(String tab,Constant.Load loadMode,long lastId, int pageSize, LoadDiscoverListCallback callback);
    void updateDiscover(Discover discover);
    Discover getDiscover(int id);
}
