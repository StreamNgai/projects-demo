package dev.weihl.amazing.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

import dev.weihl.amazing.BuildConfig;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.BaseActivity;

/**
 * @author Ngai
 * @since 2018/5/29
 * Des:
 */
public class InstallUtil {
    public static void startInstall(File file) {
        if (file != null) {
            Activity activity = MainApplication.getTopActivity();
            chmod777(file.getParentFile());
            chmod777(file);
            Intent intent = new Intent(Intent.ACTION_VIEW); //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (Logc.allowPrints()) {
                    Logc.d(Tags.AppUpdate, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.N");
                }
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri contentUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                if (Logc.allowPrints()) {
                    Logc.d(Tags.AppUpdate, "nor !" + file.getPath() + "; " + file.getAbsolutePath());
                }
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            activity.startActivity(intent);
        }
    }

    private static void chmod777(File file) {
        //通过linux命令修改apk更新文件读写权限
        try {
            String command = "chmod 777 " + file.getAbsolutePath();
            if (Logc.allowPrints()) {
                Logc.d(Tags.AppUpdate, "chmod777 ! command = " + command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (IOException e) {
            if (Logc.allowPrints()) {
                Logc.d(Tags.AppUpdate, "chmod777 ! chmod fail!!!!");
            }
            e.printStackTrace();

        }
    }
}
