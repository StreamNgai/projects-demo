package dev.ngai.fantastic.data.source;

import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.data.Discover;

/**
 * Des:
 * Created by weihl
 * 2017/6/29
 */

public interface CollectDataSource {

    boolean hasCollect(Discover discover);

    void deleteCollectByIds(ArrayList<Long> deleteCollectIds);

    interface LoadCollectListCallback {

        void onCollectListLoaded(List<Discover> collectsNotPay,List<Discover> collectsHasPay);

        void onDataNotAvailable();
    }

    void addCollect(Discover discover);

    void getCollectList(LoadCollectListCallback callback);

    void syncAccountCollectList();

}
