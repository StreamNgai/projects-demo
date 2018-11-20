package dev.ngai.fantastic.data.source.remote;

import java.util.ArrayList;

import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.CollectDataSource;

/**
 * @author weihl
 * @since 2017/6/29
 * Des: Bmob
 */
public class CollectRemoteDataSource implements CollectDataSource {

    private static CollectRemoteDataSource INSTANCE = new CollectRemoteDataSource();

    private CollectRemoteDataSource() {
    }

    public static CollectRemoteDataSource getInstance() {
        synchronized (CollectRemoteDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new CollectRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public boolean hasCollect(Discover discover) {
        return false;
    }

    @Override
    public void deleteCollectByIds(ArrayList<Long> deleteCollectIds) {

    }

    @Override
    public void addCollect(Discover discover) {

    }


    @Override
    public void getCollectList(LoadCollectListCallback callback) {

    }

    @Override
    public void syncAccountCollectList() {

    }


}
