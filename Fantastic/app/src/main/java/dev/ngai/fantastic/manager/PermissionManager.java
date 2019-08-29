package dev.ngai.fantastic.manager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.SyncStateContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.business.splash.SplashActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ngai
 * @since 2017/9/20
 * Des:
 */
public class PermissionManager {

    static public void checkAndRequestPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> lackedPermission = new ArrayList<String>();
            if (!(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (!(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            // 权限都已经有了，那么直接调用SDK
            if (lackedPermission.size() == 0) {
                Logc.d("PermissionManager", "所有权限已具备!");
            } else {
                // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
                String[] requestPermissions = new String[lackedPermission.size()];
                lackedPermission.toArray(requestPermissions);
                activity.requestPermissions(requestPermissions, 1024);
            }
        }
    }
}
