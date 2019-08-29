package dev.ngai.fantasticservice.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * @author Ngai
 * @since 2017/12/14
 * Des:
 */
public class GitDir extends BmobObject {

    public int index;
    public String name;
    public String href;
    public ArrayList<GitDir> chapter;

    @Override
    public String toString() {
        return "GitDir{" +
                "chapter=" + chapter +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
