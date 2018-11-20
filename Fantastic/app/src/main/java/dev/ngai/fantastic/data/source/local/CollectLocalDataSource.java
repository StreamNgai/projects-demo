package dev.ngai.fantastic.data.source.local;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.Collect;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.data.source.CollectDataSource;
import dev.ngai.fantastic.data.source.UserPrivateDataSource;
import dev.ngai.fantastic.data.source.UserPrivateRepository;
import dev.ngai.fantastic.data.source.local.dao.CollectDao;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Des:
 * Created by weihl
 * 2017/6/29
 */

public class CollectLocalDataSource implements CollectDataSource {

    private final String TAG = "CollectLocalDataSource";
    private static CollectLocalDataSource INSTANCE;
    private List<Integer> mLocalHasCollectIds;
    private UserPrivateDataSource userPrivateDS;

    private CollectLocalDataSource() {
        userPrivateDS = UserPrivateRepository.getInstance();
        mLocalHasCollectIds = userPrivateDS.getLocalHasCollectIds();
    }

    public static CollectLocalDataSource getInstance() {
        synchronized (CollectLocalDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new CollectLocalDataSource();
        }
        return INSTANCE;
    }


    @Override
    public boolean hasCollect(Discover discover) {
        if (!mLocalHasCollectIds.isEmpty()) {
            for (Integer id : mLocalHasCollectIds) {
                if (id == discover.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteCollectByIds(ArrayList<Long> deleteCollectIds) {
        DaoHelper.getDaoSession().getCollectDao().deleteByKeyInTx(deleteCollectIds);

        SharedPres.putBoolean(PrefsKey.UserPrivateNeedSync,true);
    }

    @Override
    public void addCollect(Discover discover) {
        checkNotNull(discover);

        String data = new Gson().toJson(discover);
        String type = discover.getClass().getName();

        Collect collect = new Collect();
        collect.setData(data);
        collect.setType(type);
        collect.setId(null);

        String loginUnique = "";
        if (Session.User != null) {
            loginUnique = Session.User.getLoginUnique();
        }
        collect.setLoginUnique(loginUnique);

        DaoHelper.getDaoSession().getCollectDao().insertOrReplaceInTx(collect);

        saveObjectCollectId(discover);

        SharedPres.putBoolean(PrefsKey.UserPrivateNeedSync,true);
        Logc.d(TAG, "addCollect success ! loginUnique = " + loginUnique);
    }

    private void saveObjectCollectId(Discover discover) {
        checkNotNull(discover);
        mLocalHasCollectIds.add(discover.getId());
        String localCollectIdsJson = new Gson().toJson(mLocalHasCollectIds);
        userPrivateDS.saveLocalHasCollectIds(localCollectIdsJson);
    }

    @Override
    public void getCollectList(LoadCollectListCallback callback) {
        checkNotNull(callback);

        QueryBuilder<Collect> query = DaoHelper.getDaoSession().getCollectDao().queryBuilder();

        String loginUnique = "";
        if (Session.hasLogin()) {
            loginUnique = Session.User.getLoginUnique();
        }
        List<Collect> collects = query
                .where(CollectDao.Properties.LoginUnique.eq(loginUnique))
                .where(CollectDao.Properties.Id.isNotNull())
                .orderDesc(CollectDao.Properties.Id).list();

        List<Discover> tCollectResultList = new ArrayList<>();
        if (collects != null && !collects.isEmpty()) {
            Gson gson = new Gson();
            for (Collect collect : collects) {
                Discover discover = gson.fromJson(collect.getData(), Discover.class);
                discover.setTag(String.valueOf(collect.getId()));
                tCollectResultList.add(discover);
            }

            List<Discover> tHasPayList = new ArrayList<>();
            List<Discover> tNotPayList = new ArrayList<>();
            for (Discover tDiscover : tCollectResultList) {
                if (UserPrivateRepository.getInstance().hasPayAlbum(tDiscover.getId())) {
                    tHasPayList.add(tDiscover);
                } else {
                    tNotPayList.add(tDiscover);
                }
            }

            callback.onCollectListLoaded(tNotPayList, tHasPayList);
        } else {
            callback.onDataNotAvailable();
        }
    }


    @Override
    public void syncAccountCollectList() {
    }

    public void setLocalHasCollectIds(List<Integer> localHasCollectIds) {
        mLocalHasCollectIds.clear();
        mLocalHasCollectIds.addAll(localHasCollectIds);
    }

    public void refreshLocalHasCollectIds() {
        mLocalHasCollectIds.clear();
        mLocalHasCollectIds = userPrivateDS.getLocalHasCollectIds();
    }
}
