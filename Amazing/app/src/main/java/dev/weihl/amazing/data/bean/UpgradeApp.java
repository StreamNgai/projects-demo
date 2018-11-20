package dev.weihl.amazing.data.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class UpgradeApp extends BmobObject {
    public int versionCode;
    public String updateLog;
    public BmobFile file; // 这里编译不了SQLite
    public boolean force;
    public String pkgName;

    @Override
    public String toString() {
        return "UpgradeApp{" +
                "versionCode=" + versionCode +
                ", updateLog='" + updateLog + '\'' +
                ", file=" + file +
                ", force=" + force +
                ", pkgName='" + pkgName + '\'' +
                '}';
    }
}
