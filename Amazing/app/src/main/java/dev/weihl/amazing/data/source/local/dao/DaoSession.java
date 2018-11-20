package dev.weihl.amazing.data.source.local.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.UserFeedback;
import dev.weihl.amazing.data.bean.UserInfo;

import dev.weihl.amazing.data.source.local.dao.DiscoverDao;
import dev.weihl.amazing.data.source.local.dao.DiscoverTabDao;
import dev.weihl.amazing.data.source.local.dao.FavoriteDao;
import dev.weihl.amazing.data.source.local.dao.UserFeedbackDao;
import dev.weihl.amazing.data.source.local.dao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig discoverDaoConfig;
    private final DaoConfig discoverTabDaoConfig;
    private final DaoConfig favoriteDaoConfig;
    private final DaoConfig userFeedbackDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final DiscoverDao discoverDao;
    private final DiscoverTabDao discoverTabDao;
    private final FavoriteDao favoriteDao;
    private final UserFeedbackDao userFeedbackDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        discoverDaoConfig = daoConfigMap.get(DiscoverDao.class).clone();
        discoverDaoConfig.initIdentityScope(type);

        discoverTabDaoConfig = daoConfigMap.get(DiscoverTabDao.class).clone();
        discoverTabDaoConfig.initIdentityScope(type);

        favoriteDaoConfig = daoConfigMap.get(FavoriteDao.class).clone();
        favoriteDaoConfig.initIdentityScope(type);

        userFeedbackDaoConfig = daoConfigMap.get(UserFeedbackDao.class).clone();
        userFeedbackDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        discoverDao = new DiscoverDao(discoverDaoConfig, this);
        discoverTabDao = new DiscoverTabDao(discoverTabDaoConfig, this);
        favoriteDao = new FavoriteDao(favoriteDaoConfig, this);
        userFeedbackDao = new UserFeedbackDao(userFeedbackDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(Discover.class, discoverDao);
        registerDao(DiscoverTab.class, discoverTabDao);
        registerDao(Favorite.class, favoriteDao);
        registerDao(UserFeedback.class, userFeedbackDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        discoverDaoConfig.clearIdentityScope();
        discoverTabDaoConfig.clearIdentityScope();
        favoriteDaoConfig.clearIdentityScope();
        userFeedbackDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public DiscoverDao getDiscoverDao() {
        return discoverDao;
    }

    public DiscoverTabDao getDiscoverTabDao() {
        return discoverTabDao;
    }

    public FavoriteDao getFavoriteDao() {
        return favoriteDao;
    }

    public UserFeedbackDao getUserFeedbackDao() {
        return userFeedbackDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
