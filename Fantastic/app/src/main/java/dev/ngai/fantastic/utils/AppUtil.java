package dev.ngai.fantastic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import dev.ngai.fantastic.Appfantastic;

/**
 * Des:
 * Created by Weihl
 * 2017/7/19
 */
public class AppUtil {


    public static String getVersionName() {
        Context context = Appfantastic.getContext();

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode() {
        Context context = Appfantastic.getContext();

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
