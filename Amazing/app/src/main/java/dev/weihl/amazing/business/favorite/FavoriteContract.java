package dev.weihl.amazing.business.favorite;

import java.util.List;

import dev.weihl.amazing.business.BasePresenter;
import dev.weihl.amazing.business.BaseView;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.Imginfo;

/**
 * @author Ngai
 * @since 2018/7/31
 * Des:
 */
public interface FavoriteContract {

    interface View extends BaseView<FavoriteContract.Presenter> {

        void onRefreshFavorites(List<Favorite> favoriteList);
    }

    interface Presenter extends BasePresenter {

    }

}
