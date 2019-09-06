package cn.bmob.javasdkdemo.test.bean;


import cn.bmob.data.bean.table.BmobObject;
import cn.bmob.data.bean.table.BmobUser;
import cn.bmob.data.bean.type.BmobDate;
import cn.bmob.data.bean.type.BmobFile;
import cn.bmob.data.bean.type.BmobGeoPoint;
import cn.bmob.data.bean.type.BmobRelation;
import lombok.Data;

import java.util.List;

@Data
public class TestObject extends BmobObject {
    /**
     * String类型
     */
    private String str;
    /**
     * Boolean类型
     */
    private Boolean boo;
    /**
     * Integer类型
     */
    private Integer integer;
    /**
     * Long类型
     */
    private Long lon;
    /**
     * Double类型
     */
    private Double dou;
    /**
     * Float类型
     */
    private Float flt;
    /**
     * Short类型
     */
    private Short sht;
    /**
     * Byte类型
     */
    private Byte byt;
    /**
     * Character
     */
    private Character cht;
    /**
     * 地理位置类型
     */
    private BmobGeoPoint geo;
    /**
     * 文件类型
     */
    private BmobFile file;
    /**
     * 时间类型
     */
    private BmobDate date;
    /**
     * 数组类型
     */
    private List<String> array;
    /**
     * Pointer 一对多关系
     */
    private BmobUser user;
    /**
     * Relation 多对多关系类型
     */
    private BmobRelation relation;


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Boolean getBoo() {
        return boo;
    }

    public void setBoo(Boolean boo) {
        this.boo = boo;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }

    public Double getDou() {
        return dou;
    }

    public void setDou(Double dou) {
        this.dou = dou;
    }

    public Float getFlt() {
        return flt;
    }

    public void setFlt(Float flt) {
        this.flt = flt;
    }

    public Short getSht() {
        return sht;
    }

    public void setSht(Short sht) {
        this.sht = sht;
    }

    public Byte getByt() {
        return byt;
    }

    public void setByt(Byte byt) {
        this.byt = byt;
    }

    public Character getCht() {
        return cht;
    }

    public void setCht(Character cht) {
        this.cht = cht;
    }

    public BmobGeoPoint getGeo() {
        return geo;
    }

    public void setGeo(BmobGeoPoint geo) {
        this.geo = geo;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public List<String> getArray() {
        return array;
    }

    public void setArray(List<String> array) {
        this.array = array;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public BmobRelation getRelation() {
        return relation;
    }

    public void setRelation(BmobRelation relation) {
        this.relation = relation;
    }
}
