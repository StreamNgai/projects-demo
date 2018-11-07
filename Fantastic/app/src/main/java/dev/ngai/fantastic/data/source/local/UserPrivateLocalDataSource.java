package dev.ngai.fantastic.data.source.local;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.Collect;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.UserPrivate;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;
import dev.ngai.fantastic.data.event.RefreshCollectEvent;
import dev.ngai.fantastic.data.source.UserPrivateDataSource;
import dev.ngai.fantastic.data.source.local.dao.UserPrivateDao;
import dev.ngai.fantastic.utils.DiscoverUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ngai
 * @since 2017/7/26
 * Des:
 */
public class UserPrivateLocalDataSource implements UserPrivateDataSource {

    private static UserPrivateLocalDataSource INSTANCE = new UserPrivateLocalDataSource();
    private ArrayList<Integer> mPayAlbumIds;
    private final String TAG = "UserPrivateLocalDataSource";

    private UserPrivateLocalDataSource() {
        mPayAlbumIds = new ArrayList<>();
    }

    public static UserPrivateLocalDataSource getInstance() {
        synchronized (UserPrivateLocalDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new UserPrivateLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public boolean hasPayAlbum(int id) {
        return getPayAlbumIds().contains(id);
    }

    @Override
    public List<Integer> getPayAlbumIds() {
        if (mPayAlbumIds.isEmpty()) {
            UserPrivate userPrivate = getUserPrivate();
            if (userPrivate != null && !TextUtils.isEmpty(userPrivate.getPayAlbumIds())) {
                List<Integer> idList = new Gson().fromJson(userPrivate.getPayAlbumIds(), new TypeToken<List<Integer>>() {
                }.getType());
                mPayAlbumIds.addAll(idList);
            }
        }
        return mPayAlbumIds;
    }

    public UserPrivate getUserPrivate() {
        if (Session.User != null) {
            UserPrivateDao userPrivateDao = DaoHelper.getDaoSession().getUserPrivateDao();
            QueryBuilder<UserPrivate> query = userPrivateDao.queryBuilder();
            query.where(UserPrivateDao.Properties.Id.gt(0));
            List<UserPrivate> privates = query.list();
            if (privates != null && !privates.isEmpty()) {
                return privates.get(0);
            }
        }
        return new UserPrivate(1L, "[]", "[]");
    }

    @Override
    public void saveHasPayAlbumId(int id) {
        mPayAlbumIds.add(id);
        saveHasPayAlbumId();
    }

    private void saveHasPayAlbumId() {
        String jsonIds = new Gson().toJson(mPayAlbumIds);
        UserPrivate userPrivate = getUserPrivate();
        userPrivate.setPayAlbumIds(jsonIds);
        DaoHelper.getDaoSession().getUserPrivateDao().insertOrReplace(userPrivate);
    }

    @Override
    public void saveHasPayAlbumId(List<Integer> ids) {
        mPayAlbumIds.addAll(ids);
        saveHasPayAlbumId();
    }

    @Override
    public void syncUserPrivate() {
        if("[]".equals(getUserPrivate().getCollectIds())){
            // 将服务器的用户私有资源同步到本地；
            DaoHelper.getDaoSession().getCollectDao().deleteAll();
            if (Session.User != null && !TextUtils.isEmpty(Session.User.getPrivateId())) {
                BmobQuery<UserPrivate> query = new BmobQuery<UserPrivate>();
                query.getObject(Session.User.getPrivateId(), new QueryListener<UserPrivate>() {

                    @Override
                    public void done(UserPrivate object, BmobException e) {
                        if (e == null) {
                            DaoHelper.getDaoSession().getUserPrivateDao().deleteAll();
                            DaoHelper.getDaoSession().getUserPrivateDao().insertOrReplace(object);
                            syncHasCollects(object);
                        } else {
                            Logc.d(TAG, "syncUserPrivate 失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }

                });
            }
        }
    }

    private void syncHasCollects(UserPrivate userPrivate) {
        // 根据收藏ID，批量获取并缓存在本地服务器
        checkNotNull(userPrivate);
        if (!TextUtils.isEmpty(userPrivate.getCollectIds())) {
            List<Integer> collectIds = new Gson().fromJson(userPrivate.getCollectIds(), new TypeToken<List<Integer>>() {
            }.getType());

            BmobQuery<Discover> query = new BmobQuery<Discover>();
            query.addWhereContainedIn("id", collectIds);
            query.findObjects(new FindListener<Discover>() {
                @Override
                public void done(List<Discover> object, BmobException e) {
                    if (e == null) {
                        Logc.d(TAG, "查询成功：共" + object.size() + "条数据。");
                        List<Collect> collects = new ArrayList<Collect>();
                        Gson gson = new Gson();
                        for (Discover discover : object) {
                            DiscoverUtil.displayWh(discover);
                            Collect tCollect = new Collect();
                            tCollect.setType(discover.getClass().getName());
                            tCollect.setData(gson.toJson(discover));
                            tCollect.setLoginUnique(Session.User.getLoginUnique());
                            collects.add(tCollect);
                        }
                        if (!collects.isEmpty()) {
                            DaoHelper.getDaoSession().getCollectDao().insertOrReplaceInTx(collects);
                            CollectLocalDataSource.getInstance().setLocalHasCollectIds(getLocalHasCollectIds());
                            EventBus.getDefault().post(new RefreshCollectEvent());
                            EventBus.getDefault().post(new RefreshDiscoverEvent(-1));
                        }
                    } else {
                        Logc.d(TAG, "syncHasCollects 失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    @Override
    public void saveLocalHasCollectIds(String localCollectIdsJson) {
        UserPrivate userPrivate = getUserPrivate();
        userPrivate.setCollectIds(localCollectIdsJson);
        DaoHelper.getDaoSession().getUserPrivateDao().insertOrReplace(userPrivate);
    }

    @Override
    public List<Integer> getLocalHasCollectIds() {
        UserPrivate userPrivate = getUserPrivate();
        if (!TextUtils.isEmpty(userPrivate.getCollectIds())) {
            return new Gson().fromJson(userPrivate.getCollectIds(), new TypeToken<List<Integer>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    public void refreshPayAlumIds() {
        mPayAlbumIds.clear();
        getPayAlbumIds();
    }
}
