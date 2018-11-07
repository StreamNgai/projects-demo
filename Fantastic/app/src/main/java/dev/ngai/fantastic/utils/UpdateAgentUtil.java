package dev.ngai.fantastic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.BuildConfig;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.event.NotifyUpdateAppEvent;
import dev.ngai.fantastic.data.event.RefreshDiscoverEvent;

/**
 * @author Ngai
 * @since 2017/7/18
 * Des:
 */
public class UpdateAgentUtil {
    static File file;

    public static void update(final Activity mainActivity) {
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                //根据updateStatus来判断更新是否成功
                if (updateStatus == UpdateStatus.Yes) {
                    Session.UpdateResponse = updateInfo;
                    file = getFileByUpdateInfo(updateInfo);
                    EventBus.getDefault().post(new NotifyUpdateAppEvent());
                    Logc.d("UpdateAgentUtil", file.exists() + " / " + file.getPath());
                } else if (updateStatus == UpdateStatus.IGNORED) {//新增忽略版本更新
                    Logc.d("UpdateAgentUtil", "该版本已经被忽略更新");
                }
            }
        });

        //设置对对话框按钮的点击事件的监听
        BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.Update:
                        startInstall(mainActivity, file);
                        if (Session.UpdateResponse.isforce) {
                            Appfantastic.exit();
                        }
                        Logc.d("UpdateAgentUtil", "点击了立即更新按钮");
                        break;
                    case UpdateStatus.NotNow:
                        Logc.d("UpdateAgentUtil", "点击了以后再说按钮");
                        break;
                    case UpdateStatus.Close:
                        Logc.d("UpdateAgentUtil", "点击了对话框关闭按钮");
                        //只有在强制更新状态下才会在更新对话框的右上方出现close按钮,
                        // 如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
                        break;
                }
            }
        });
        Logc.d("UpdateAgentUtil", "update ！");
        BmobUpdateAgent.update(mainActivity);
    }

    public static void startInstall(Context context, File file) {

        chmod777(file.getParentFile());
        chmod777(file);


        Intent intent = new Intent(Intent.ACTION_VIEW); //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Logc.d("AAA", "uild.VERSION_CODES.N !");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Logc.d("AAA", "nor !" + file.getPath() + "; " + file.getAbsolutePath());
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private static void chmod777(File file) {
        //通过linux命令修改apk更新文件读写权限
        try {

            String command = "chmod 777 " + file.getAbsolutePath();

            Logc.d("chmod777", "command = " + command);

            Runtime runtime = Runtime.getRuntime();

            Process proc = runtime.exec(command);

        } catch (IOException e) {

            Logc.d("chmod777", "chmod fail!!!!");

            e.printStackTrace();

        }
    }

    public static File getFileByUpdateInfo(UpdateResponse updateInfo) {
        return new File(Environment.getExternalStorageDirectory(), updateInfo.path_md5 + ".apk");
    }


}
