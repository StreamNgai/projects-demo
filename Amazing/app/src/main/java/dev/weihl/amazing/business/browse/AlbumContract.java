package dev.weihl.amazing.business.browse;

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
public interface AlbumContract {

    interface View extends BaseView<AlbumContract.Presenter> {

        void onDisplayImgs(List<Imginfo> imginfos);
    }

    interface Presenter extends BasePresenter {

        void onParsingImgs(String imgsJson);

        void onSaveFavorite(Favorite favorite,FavoriteCallBack callBack);

        boolean isFavorite(int discoverId);
    }

    interface FavoriteCallBack {
        void onResult(Boolean rs);
    }

}
