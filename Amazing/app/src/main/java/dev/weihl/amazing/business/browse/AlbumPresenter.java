package dev.weihl.amazing.business.browse;

import android.content.Context;

import java.util.List;

import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.Imginfo;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;
import dev.weihl.amazing.util.ImginfoUtil;

/**
 * @author Ngai
 * @since 2018/7/31
 * Des:
 */
public class AlbumPresenter implements AlbumContract.Presenter {

    AlbumContract.View mView;
    Context mContext;
    AmazingDataSource mRepository;

    public AlbumPresenter(AlbumContract.View view) {
        this.mView = view;
        this.mContext = MainApplication.getContext();
        mView.setPresenter(this);
        mRepository = AmazingRepository.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onParsingImgs(String imgsJson) {
        List<Imginfo> imginfos = ImginfoUtil.parsingDetails(imgsJson);
        mView.onDisplayImgs(imginfos);
    }

    @Override
    public void onSaveFavorite(Favorite favorite, AlbumContract.FavoriteCallBack callBack) {
        mRepository.saveFavorite(favorite,callBack);
    }

    @Override
    public boolean isFavorite(int discoverId) {
        return mRepository.findFavorite(discoverId) != null;
    }

}
