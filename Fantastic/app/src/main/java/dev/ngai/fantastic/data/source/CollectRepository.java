package dev.ngai.fantastic.data.source;

import java.util.ArrayList;

import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.local.CollectLocalDataSource;
import dev.ngai.fantastic.data.source.remote.CollectRemoteDataSource;

/**
 * Des:
 * Created by weihl
 * 2017/6/29
 */

public class CollectRepository implements CollectDataSource {

    private static CollectRepository INSTANCE;
    private CollectDataSource mLocalDataSource;
    private CollectDataSource mRemoteDataSource; // 未扩展

    private CollectRepository() {
        mLocalDataSource = CollectLocalDataSource.getInstance();
        mRemoteDataSource = CollectRemoteDataSource.getInstance();
    }

    public static CollectRepository getInstance() {
        synchronized (CollectRepository.class) {
            if (INSTANCE == null)
                INSTANCE = new CollectRepository();
        }
        return INSTANCE;
    }

    @Override
    public boolean hasCollect(Discover discover) {
        return mLocalDataSource.hasCollect(discover);
    }

    @Override
    public void deleteCollectByIds(ArrayList<Long> deleteCollectIds) {
        mLocalDataSource.deleteCollectByIds(deleteCollectIds);
    }

    @Override
    public void addCollect(Discover discover) {

        mLocalDataSource.addCollect(discover);

    }

    @Override
    public void getCollectList(LoadCollectListCallback callback) {
        mLocalDataSource.getCollectList(callback);
    }

    @Override
    public void syncAccountCollectList() {
    }
}
