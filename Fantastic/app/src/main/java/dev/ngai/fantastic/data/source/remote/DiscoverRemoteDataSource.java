package dev.ngai.fantastic.data.source.remote;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.source.DiscoverDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by weihl on 2017/4/4.
 * 使用 Bmob 移动数据库解决方案；
 */

public class DiscoverRemoteDataSource implements DiscoverDataSource {

    static private final String TAG = "DiscoverRemoteDataSource";

    static private DiscoverRemoteDataSource INSTANCE;

    private DiscoverRemoteDataSource() {
    }

    public static DiscoverRemoteDataSource getInstance() {
        synchronized (DiscoverRemoteDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new DiscoverRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public boolean onDiscoverDaoNotNull(String tab) {
        return false;
    }

    /**
     * // 分数 < 50
     * query.addWhereLessThan("score", 50);
     * //分数 <= 50
     * query.addWhereLessThanOrEqualTo("score", 50);
     * //分数 > 50
     * query.addWhereGreaterThan("score", 50);
     * //分数 >= 50
     * query.addWhereGreaterThanOrEqualTo("score", 50);
     *
     * @param lastId
     * @param mPageSize
     * @return
     */
    @Override
    public void getDiscoverList(String tab, Constant.Load loadMode, long lastId, int mPageSize, final LoadDiscoverListCallback callback) {
        Logc.d("bmobManager", "-> getDiscoverList ,LastId = " + lastId + " , PageSize = " + mPageSize);
        checkNotNull(callback);
        BmobQuery<Discover> query = new BmobQuery<>();
        query.addWhereEqualTo("tab", tab);
        query.order("id");
        query.setLimit(mPageSize);
        query.addWhereGreaterThan("id", lastId);

        query.findObjects(new FindListener<Discover>() {

            @Override
            public void done(List<Discover> list, BmobException e) {
                if (e != null) {
                    callback.onDataNotAvailable();
                    Logc.d("bmobManager", "-> getDiscoverList ,BmobException = " + e.getMessage());
                } else {
                    if (list != null && !list.isEmpty()) {
                        callback.onDiscoverListLoaded(list);
                        Logc.d("bmobManager", "-> getDiscoverList ,list = " + list.toString());
                    } else {
                        callback.onDataNotAvailable();
                        Logc.d("bmobManager", "-> getDiscoverList ,list is null ");
                    }
                }
            }
        });
    }

    @Override
    public void updateDiscover(Discover discover) {

    }

    @Override
    public Discover getDiscover(int id) {
        return null;
    }
}
