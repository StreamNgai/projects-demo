package dev.ngai.fantasticservice.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author Ngai
 * @since 2017/12/14
 * Des:
 */
public class GitDirContent extends BmobObject {

    public int index;
    public String dirName;
    public String content;

    @Override
    public String toString() {
        return "GitDirContent{" +
                "content='" + content + '\'' +
                ", index=" + index +
                ", dirName='" + dirName + '\'' +
                '}';
    }
}
