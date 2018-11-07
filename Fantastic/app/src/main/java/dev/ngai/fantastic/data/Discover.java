package dev.ngai.fantastic.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import cn.bmob.v3.BmobObject;

/**
 * Des:
 * Created by Weihl
 * 2017/7/03
 */
@Entity
public class Discover extends BmobObject {
    @Unique
    public int id;
    public String title;
    public String desc;
    public String thumb;
    public String thumbWh;
    public String tab;// qingchun,xinggan,xiaoyuan
    public String tag;
    public String details;// json
    public String date;
    @Generated(hash = 990100962)
    public Discover(int id, String title, String desc, String thumb, String thumbWh,
            String tab, String tag, String details, String date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.thumb = thumb;
        this.thumbWh = thumbWh;
        this.tab = tab;
        this.tag = tag;
        this.details = details;
        this.date = date;
    }
    @Generated(hash = 156017668)
    public Discover() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getThumb() {
        return this.thumb;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public String getThumbWh() {
        return this.thumbWh;
    }
    public void setThumbWh(String thumbWh) {
        this.thumbWh = thumbWh;
    }
    public String getTab() {
        return this.tab;
    }
    public void setTab(String tab) {
        this.tab = tab;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getDetails() {
        return this.details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Discover{" +
                "id=" + id +
                '}';
    }
}
