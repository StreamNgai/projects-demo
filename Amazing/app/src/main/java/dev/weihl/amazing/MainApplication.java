package dev.weihl.amazing;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.SoftReference;

public class MainApplication extends Application implements Application.ActivityLifecycleCallbacks {

    static private Context mContext;
    static private SoftReference<Activity> mTopActivity;

    public static Activity getTopActivity() {
        return mTopActivity.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);

        mContext = getApplicationContext();
        MainApplicationHelper.getInstance()
                .init(mContext)
                .initDao()
                .initBmob();
    }

    static public Context getContext() {
        return mContext;
    }

    static public void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);//status是非零参数，那么表示是非正常退出
    }


    // 生命周期 ----------
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mTopActivity = new SoftReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
