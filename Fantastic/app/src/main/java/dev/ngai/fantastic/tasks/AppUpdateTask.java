package dev.ngai.fantastic.tasks;

import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.BuildConfig;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.data.AppUpdate;
import dev.ngai.fantastic.utils.UpdateAgentUtil;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des:
 */
public class AppUpdateTask extends TaskScheduler.BaseTask {

    final String TAG = "AppUpdateTask";
    AppUpdate mAppUpdate;

    @Override
    void onRun() {
        queryAppUpdateVersion();
    }

    @Override
    public String uniqueTag() {
        return "AppUpdateTask";
    }

    @Override
    void onCancel() {

    }

    private void queryAppUpdateVersion() {

        BmobQuery<AppUpdate> query = new BmobQuery<>();

        query.findObjects(new FindListener<AppUpdate>() {

            @Override
            public void done(List<AppUpdate> list, BmobException e) {
                if (e == null && list != null && !list.isEmpty()) {
                    mAppUpdate = list.get(0);
                    if (BuildConfig.VERSION_CODE < mAppUpdate.versionCode
                            && mAppUpdate.file != null) {
                        SharedPres.putInt(PrefsKey.UpdateVersionCode, mAppUpdate.versionCode);
                        downloadFile(mAppUpdate.file);
                    } else {
                        File saveFile = getFile(mAppUpdate.file);
                        if (saveFile.exists()) {
                            saveFile.delete();
                        }
                    }
                } else {
                    Logc.d(TAG, "查询失败：" + e.getMessage());
                }
            }
        });
    }

    private File getFile(BmobFile file) {
        return new File(Appfantastic.getContext().getFilesDir() + "/download/", file.getFilename());
    }

    private void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = getFile(file);
        if (!saveFile.exists()) {
            file.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {
                    Logc.d(TAG, "开始下载...");
                }

                @Override
                public void done(String savePath, BmobException e) {
                    if (e == null) {
                        Logc.d(TAG, "下载成功,保存路径:" + savePath);
                        SharedPres.putString(PrefsKey.DownloadFilePath, savePath);
                        showUpdateDialog(savePath);
                    } else {
                        Logc.d(TAG, "下载失败：" + e.getErrorCode() + "," + e.getMessage());
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    Logc.d(TAG, "下载进度：" + value + "," + newworkSpeed);
                }

            });
        } else {
            UpdateAgentUtil.startInstall(Appfantastic.getTopActivity(), saveFile);
            Appfantastic.exit();
        }

    }

    private void showUpdateDialog(final String savePath) {
        String[] updateLog = mAppUpdate.updateLog.split(";");
        String mLog = "";
        for (String log : updateLog) {
            mLog = mLog + log + "\n";
        }
        new MaterialDialog.Builder(Appfantastic.getTopActivity())
                .title("版本升级 !")
                .content(mLog)
                .positiveText("点击升级 ！")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        UpdateAgentUtil.startInstall(Appfantastic.getTopActivity(), new File(savePath));
                        Appfantastic.exit();
                    }
                })
                .show();
    }
}
