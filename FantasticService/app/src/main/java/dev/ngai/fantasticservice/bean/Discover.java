package dev.ngai.fantasticservice.bean;

import cn.bmob.v3.BmobObject;

/**
 * Des:
 * Created by Weihl
 * 2017/7/03
 */
public class Discover extends BmobObject {

    public int id;
    public String title;
    public String desc;
    public String thumb;
    public String thumbWh;
    public String tab;// qingchun,xinggan,xiaoyuan
    public String tag;
    public String details;// json
    public String date;


    @Override
    public String toString() {
        return "Discover{" +
                "date='" + date + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", thumb='" + thumb + '\'' +
                ", thumbWh='" + thumbWh + '\'' +
                ", tab='" + tab + '\'' +
                ", tag='" + tag + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
