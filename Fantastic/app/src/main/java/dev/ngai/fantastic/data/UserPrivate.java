package dev.ngai.fantastic.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Weihl
 * @since 2017/8/31
 * Des:
 */
@Entity
public class UserPrivate extends BmobObject {

    @Id(autoincrement = true)
    Long id ;
    String collectIds;// json 收藏专辑ID discover
    String payAlbumIds;// json 兑换专辑ID  discover
    @Generated(hash = 2115834243)
    public UserPrivate(Long id, String collectIds, String payAlbumIds) {
        this.id = id;
        this.collectIds = collectIds;
        this.payAlbumIds = payAlbumIds;
    }
    @Generated(hash = 873696593)
    public UserPrivate() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCollectIds() {
        return this.collectIds;
    }
    public void setCollectIds(String collectIds) {
        this.collectIds = collectIds;
    }
    public String getPayAlbumIds() {
        return this.payAlbumIds;
    }
    public void setPayAlbumIds(String payAlbumIds) {
        this.payAlbumIds = payAlbumIds;
    }

}
