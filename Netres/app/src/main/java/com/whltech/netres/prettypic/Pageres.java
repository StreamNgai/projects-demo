package com.whltech.netres.prettypic;

import java.util.ArrayList;

public class Pageres {

    // 当前页 地址
    public String href;
    // 对应 Pic 组标题
    public String alt;
    // 对应 Pic 缩略图
    public String src;
    // 对应 Pic 个数
    public int count;
    // 高清图组
    public ArrayList<Hd> hds;

    public static class Hd {

        // 对应 Pic 高清Href
        public String hdHref;
        // 对应 Pic 缩略图
        public String hdSrc;

        @Override
        public String toString() {
            return "Hd{" +
                "hdHref='" + hdHref + '\'' +
                ", hdSrc='" + hdSrc + '\'' +
                '}';
        }
    }

    @Override
    public String toString() {
        return "Pageres{" +
            "href='" + href + '\'' +
            ", alt='" + alt + '\'' +
            ", src='" + src + '\'' +
            ", \ncount=" + hds.size() +
            ", \nhds=" + hds +
            '}';
    }
}
