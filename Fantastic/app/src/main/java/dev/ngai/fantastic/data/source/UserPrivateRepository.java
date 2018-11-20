package dev.ngai.fantastic.data.source;

import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.data.Collect;
import dev.ngai.fantastic.data.source.local.DaoHelper;
import dev.ngai.fantastic.data.source.local.UserPrivateLocalDataSource;
import dev.ngai.fantastic.data.source.remote.UserPrivateRemoteDataSource;

/**
 * @author Ngai
 * @since 2017/8/31
 * Des:
 */
public class UserPrivateRepository implements UserPrivateDataSource {

    private static UserPrivateRepository INSTANCE = new UserPrivateRepository();

    private UserPrivateDataSource mLocalDataSource;
    private UserPrivateDataSource mRemoteDataSource;

    private UserPrivateRepository() {
        mLocalDataSource = UserPrivateLocalDataSource.getInstance();
        mRemoteDataSource = UserPrivateRemoteDataSource.getInstance();
    }

    public static UserPrivateRepository getInstance() {
        synchronized (UserPrivateRepository.class) {
            if (INSTANCE == null)
                INSTANCE = new UserPrivateRepository();
        }
        return INSTANCE;
    }

    @Override
    public boolean hasPayAlbum(int id) {
        return mLocalDataSource.hasPayAlbum(id);
    }

    @Override
    public List<Integer> getPayAlbumIds() {
        return mLocalDataSource.getPayAlbumIds();
    }

    @Override
    public void saveHasPayAlbumId(int id) {
        mLocalDataSource.saveHasPayAlbumId(id);
    }

    @Override
    public void saveHasPayAlbumId(List<Integer> ids) {
        mLocalDataSource.saveHasPayAlbumId(ids);
    }

    @Override
    public void syncUserPrivate() {
    }

    @Override
    public void saveLocalHasCollectIds(String localCollectIdsJson) {
        mLocalDataSource.saveLocalHasCollectIds(localCollectIdsJson);
    }

    @Override
    public List<Integer> getLocalHasCollectIds() {
        return mLocalDataSource.getLocalHasCollectIds();
    }
}
