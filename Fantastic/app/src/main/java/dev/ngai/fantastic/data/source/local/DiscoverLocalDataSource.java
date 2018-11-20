package dev.ngai.fantastic.data.source.local;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.DiscoverDataSource;
import dev.ngai.fantastic.data.source.local.dao.DiscoverDao;

/**
 * Des:
 * Created by Ngai
 * 2017/6/27
 */

public class DiscoverLocalDataSource implements DiscoverDataSource {

    private static DiscoverLocalDataSource INSTANCE;
    private final String TAG = "DiscoverLocalDataSource";
    private DiscoverDao mDiscoverDao;

    private DiscoverLocalDataSource() {
        mDiscoverDao = DaoHelper.getDaoSession().getDiscoverDao();
    }

    public static DiscoverLocalDataSource getInstance() {
        synchronized (DiscoverLocalDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new DiscoverLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public boolean onDiscoverDaoNotNull(String tab) {
        QueryBuilder<Discover> disQuery = mDiscoverDao.queryBuilder();
        disQuery.where(DiscoverDao.Properties.Tab.eq(tab));
        return disQuery.count() > 0;
    }

    @Override
    public void getDiscoverList(String tab, Constant.Load loadMode, long lastId, int pageSize, LoadDiscoverListCallback callback) {

        long start = lastId;
        long end = lastId - pageSize;
        if (end < 0) end = 0;
        Logc.d(TAG, "getDiscoverList start = " + lastId + " ; end = " + end + " ; mPageSize = " + pageSize);

        QueryBuilder<Discover> qb = mDiscoverDao.queryBuilder();
        qb.where(DiscoverDao.Properties.Tab.eq(tab));
        if (loadMode == Constant.Load.More) {
            qb.where(DiscoverDao.Properties.Id.lt(start));
        } else if (loadMode == Constant.Load.Init) {
            qb.where(DiscoverDao.Properties.Id.le(start));
        }
        qb.limit(pageSize);
//        qb.where(QingChunDao.Properties.Id.ge(end));
        qb.orderDesc(DiscoverDao.Properties.Id);

        List<Discover> datas = qb.list();
        if (datas != null && !datas.isEmpty()) {
            callback.onDiscoverListLoaded(datas);
            Logc.d(TAG, "datas == null ? " + datas.toString());
        } else {
            callback.onDataNotAvailable();
            Logc.d(TAG, "onDataNotAvailable ");
        }
    }


    @Override
    public void updateDiscover(Discover discover) {
        mDiscoverDao.insertOrReplaceInTx(discover);
    }

    @Override
    public Discover getDiscover(int mDiscoverId) {
        QueryBuilder<Discover> qb = mDiscoverDao.queryBuilder();
        qb.where(DiscoverDao.Properties.Id.eq(mDiscoverId));
        if (qb.list() != null) {
            return qb.list().get(0);
        }
        return null;
    }
}
