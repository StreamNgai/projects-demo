package dev.ngai.fantasticservice;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


public class ServiceApp extends Application {

    static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    // 微色影 test : 3636a6c3dc61eecaf0782393bc053e45   ;
    //    replase : 24ae9f662773549c4a4ca33622c59eaa
    // Git学习手册 e9b3696169c3ffb0beaf9ad82806e125
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        Bmob.initialize(this, "3636a6c3dc61eecaf0782393bc053e45","demo");

//        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config = new BmobConfig.Builder(this)
//                //设置appkey
//                .setApplicationId("3636a6c3dc61eecaf0782393bc053e45")
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                //文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(2500)
//                .build();
//        Bmob.initialize(config);
    }


}
