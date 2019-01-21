package dev.weihl.amazing.manage;

/**
 * 用户的账户信息
 */
public class User implements IUser {

    private final static User INSTANCE = new User();

    private User() {
    }

    public static User getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void doLogin() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getCollectAlbumIds() {
        return null;
    }

    @Override
    public String getPayAlbumIds() {
        return null;
    }

}
