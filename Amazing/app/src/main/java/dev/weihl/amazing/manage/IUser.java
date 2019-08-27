package dev.weihl.amazing.manage;

public interface IUser {

    boolean isLogin();
    void doLogin();
    String getName();
    String getId();
    String getEmail();
    String getCollectAlbumIds();
    String getPayAlbumIds();


}
