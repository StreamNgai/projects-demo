package dev.ngai.fantastic.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Des:
 * Created by Weihl
 * 2017/12/22
 */
public class AppUpdate extends BmobObject {

    public int versionCode;
    public String updateLog;
    public BmobFile file;

}
