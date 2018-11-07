package dev.weihl.amazing.business.favorite;

import java.util.List;

import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;

/**
 * @author Ngai
 * @since 2018/8/9
 * Des:
 */
public class FavoritePresenter implements FavoriteContract.Presenter {

    FavoriteContract.View mView;
    AmazingRepository mRepository;

    FavoritePresenter(FavoriteContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {
        mRepository.loadFavoriteList(new AmazingDataSource.FavoriteCallBack() {
            @Override
            public void onResult(List<Favorite> favoriteList) {
                mView.onRefreshFavorites(favoriteList);
            }
        });
    }

    @Override
    public void stop() {

    }
}
