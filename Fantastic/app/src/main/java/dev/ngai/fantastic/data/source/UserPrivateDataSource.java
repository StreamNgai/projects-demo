package dev.ngai.fantastic.data.source;

import java.util.List;

import dev.ngai.fantastic.data.Discover;

/**
 * Des:
 * Created by weihl
 * 2017/8/31
 */

public interface UserPrivateDataSource {

    boolean hasPayAlbum(int id);

    List<Integer> getPayAlbumIds();

    void saveHasPayAlbumId(int id);

    void saveHasPayAlbumId(List<Integer> ids);

    void syncUserPrivate();

    void saveLocalHasCollectIds(String localCollectIdsJson);

    List<Integer> getLocalHasCollectIds();
}
