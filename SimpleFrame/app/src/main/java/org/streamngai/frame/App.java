package org.streamngai.frame;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ngai on 2016/3/24.
 */
public class App extends Application {

    static private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    static public Context getAppContext(){
        return mContext;
    }
}
