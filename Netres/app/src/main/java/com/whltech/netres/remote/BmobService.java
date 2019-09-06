package com.whltech.netres.remote;

import cn.bmob.data.Bmob;
import cn.bmob.data.bean.op.BmobQuery;
import cn.bmob.data.bean.table.BmobUser;
import cn.bmob.data.callback.object.GetListener;
import cn.bmob.data.callback.object.GetsListener;
import cn.bmob.data.config.BmobConfig;
import cn.bmob.data.exception.BmobException;
import java.util.List;

public class BmobService {

    public static String appId = "2154b7b7ece90beb81d742f9f4b03899";
    public static String apiKey = "3c189730bcfabb4a50a0c8e2a4069434";

    public static void main(final String[] args) {
        //TODO Application Entrance

        Bmob.getInstance().init(appId, apiKey);
        Bmob.getInstance().setMasterKey("af3c98d6edc3ade4827e723cfb61b36a");

        BmobQuery bmobQuery = new BmobQuery();
        bmobQuery.getObjects(new GetsListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> array) {
                System.out.println("get users " + array.size());
            }

            @Override
            public void onFailure(BmobException ex) {
                System.err.println(ex.getMessage());
            }
        });


    }
}
