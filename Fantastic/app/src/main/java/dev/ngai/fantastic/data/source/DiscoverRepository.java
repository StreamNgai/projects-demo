package dev.ngai.fantastic.data.source;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.DiscoverLocalDataSource;
import dev.ngai.fantastic.data.source.remote.DiscoverRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Des:
 * Created by Weihl
 * 2017/6/27
 */

public class DiscoverRepository implements DiscoverDataSource {

    private static final String TAG = "DiscoverRepository";
    static private DiscoverRepository INSTANCE = null;

    private final DiscoverDataSource mBmobDataSource;
    private final DiscoverDataSource mLocalDataSource;

    private DiscoverRepository() {
        mBmobDataSource = DiscoverRemoteDataSource.getInstance();
        mLocalDataSource = DiscoverLocalDataSource.getInstance();
    }

    static public DiscoverRepository getInstance() {
        synchronized (DiscoverRepository.class) {
            if (INSTANCE == null) {
                INSTANCE = new DiscoverRepository();
            }
        }
        return INSTANCE;
    }


    @Override
    public boolean onDiscoverDaoNotNull(String tab) {
        return mLocalDataSource.onDiscoverDaoNotNull(tab);
    }

    @Override
    public void getDiscoverList(String tab, Constant.Load loadMode, long lastId, int pageSize, final LoadDiscoverListCallback callback) {
        checkNotNull(callback);

        if (loadMode == Constant.Load.More ||
                (loadMode == Constant.Load.Init && mLocalDataSource.onDiscoverDaoNotNull(tab))) {
            Log.d(TAG,"doLocalDataSource.lastId = "+lastId+" ; LoadMode = "+loadMode);
            mLocalDataSource.getDiscoverList(tab,loadMode, lastId, pageSize, callback);
        } else {
            Log.d(TAG,"doBmobDataSource.lastId = "+lastId+" ; LoadMode = "+loadMode);
            mBmobDataSource.getDiscoverList(tab,loadMode, lastId, pageSize, new LoadDiscoverListCallback() {

                @Override
                public void onDiscoverListLoaded(List<Discover> discoverList) {
                    DaoHelper.getDaoSession().getDiscoverDao().insertOrReplaceInTx(discoverList);
                    Collections.reverse(discoverList);
                    callback.onDiscoverListLoaded(discoverList);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void updateDiscover(Discover discover) {
        mLocalDataSource.updateDiscover(discover);
    }

    @Override
    public Discover getDiscover(int id) {
        return mLocalDataSource.getDiscover(id);
    }

}
