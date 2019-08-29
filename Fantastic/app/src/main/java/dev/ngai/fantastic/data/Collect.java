package dev.ngai.fantastic.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobObject;

/**
 * Des:
 * Created by Weihl
 * 2017/6/29
 */
@Entity
public class Collect {

    @Id(autoincrement = true)
    Long id ;
    String type;// qingchun
    String data;// json
    String tag;
    String loginUnique; // email

    @Generated(hash = 1229497607)
    public Collect(Long id, String type, String data, String tag,
            String loginUnique) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.tag = tag;
        this.loginUnique = loginUnique;
    }

    @Generated(hash = 1726975718)
    public Collect() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLoginUnique() {
        return this.loginUnique;
    }

    public void setLoginUnique(String loginUnique) {
        this.loginUnique = loginUnique;
    }


}
