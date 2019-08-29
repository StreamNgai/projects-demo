package dev.weihl.amazing.data.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Ngai
 * @since 2018/8/9
 * Des:
 */
@Entity
public class Favorite {

    @Unique
    @Id
    public Long id;
    public int discoverId;
    public String title;
    public String desc;
    public String tab;// qingchun,xinggan,xiaoyuan
    public String imgs;// json
    @Generated(hash = 1273636979)
    public Favorite(Long id, int discoverId, String title, String desc, String tab,
            String imgs) {
        this.id = id;
        this.discoverId = discoverId;
        this.title = title;
        this.desc = desc;
        this.tab = tab;
        this.imgs = imgs;
    }
    @Generated(hash = 459811785)
    public Favorite() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getDiscoverId() {
        return this.discoverId;
    }
    public void setDiscoverId(int discoverId) {
        this.discoverId = discoverId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTab() {
        return this.tab;
    }
    public void setTab(String tab) {
        this.tab = tab;
    }
    public String getImgs() {
        return this.imgs;
    }
    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
     
}
