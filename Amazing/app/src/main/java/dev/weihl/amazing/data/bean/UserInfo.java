package dev.weihl.amazing.data.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import cn.bmob.v3.BmobObject;

/**
 * @author Weihl
 * @since 2017/8/31
 * Des:
 */
@Entity
public class UserInfo extends BmobObject {

    String userId;
    String userEmail;
    String collectAlbumIds;// json 收藏专辑ID discover
    String payAlbumIds;// json 兑换专辑ID  discover
    @Generated(hash = 425755659)
    public UserInfo(String userId, String userEmail, String collectAlbumIds,
            String payAlbumIds) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.collectAlbumIds = collectAlbumIds;
        this.payAlbumIds = payAlbumIds;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserEmail() {
        return this.userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getCollectAlbumIds() {
        return this.collectAlbumIds;
    }
    public void setCollectAlbumIds(String collectAlbumIds) {
        this.collectAlbumIds = collectAlbumIds;
    }
    public String getPayAlbumIds() {
        return this.payAlbumIds;
    }
    public void setPayAlbumIds(String payAlbumIds) {
        this.payAlbumIds = payAlbumIds;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", collectAlbumIds='" + collectAlbumIds + '\'' +
                ", payAlbumIds='" + payAlbumIds + '\'' +
                '}';
    }
}
