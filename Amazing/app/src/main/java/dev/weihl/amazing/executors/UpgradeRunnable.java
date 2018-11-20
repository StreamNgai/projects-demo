package dev.weihl.amazing.executors;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import dev.weihl.amazing.Logc;
import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.data.bean.UpgradeApp;
import dev.weihl.amazing.data.source.AmazingDataSource;
import dev.weihl.amazing.data.source.AmazingRepository;
import dev.weihl.amazing.sharedpres.PrefsKey;
import dev.weihl.amazing.sharedpres.SharedPres;
import dev.weihl.amazing.util.AppUtil;
import dev.weihl.amazing.util.InstallUtil;

/**
 * @author Ngai
 * @since 2018/5/29
 * Des:
 */
public class UpgradeRunnable implements Runnable {

    private UpgradeApp mUpgradeApp;
    private Context mContext;
    private File mFile;

    public UpgradeRunnable() {
        this.mContext = MainApplication.getContext();
    }

    @Override
    public void run() {
        if (Logc.allowPrints()) {
            Logc.d(Tags.AppUpdate, "onRun !");
        }

        long tipTime = SharedPres.getLong(PrefsKey.NextRemindUpgrade, 0);
        if (tipTime <= System.currentTimeMillis()) {
            AmazingRepository.getInstance().syncAppUpdate(new AmazingDataSource.AppUpdateCallBack() {
                @Override
                public void onResult(UpgradeApp upgradeApp) {
                    mUpgradeApp = upgradeApp;
                    if (mUpgradeApp != null
                            && (mUpgradeApp.versionCode > AppUtil.getVersionCode() || mUpgradeApp.force)
                            && mUpgradeApp.file != null
                            && !TextUtils.isEmpty(mUpgradeApp.file.getFileUrl())) {
                        mFile = new File(mContext.getFilesDir() + "/download/", mUpgradeApp.file.getFilename());
                        if (mFile.exists()) {
                            // showUpdateDialog
                            showUpdateDialog(mFile);
                        } else {
                            // downloadFile
                            downloadFile();
                        }
                    }
                }
            });
        }
    }

    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private void downloadFile() {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        if (!mFile.exists()) {
            DownloadWorker.execute(mFile.getPath(), mUpgradeApp.file.getFileUrl(), new DownloadWorker.CallBack() {
                @Override
                public void onStart() {
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.AppUpdate, "开始下载...");
                    }
                }

                @Override
                public void onDone(File file, Exception e) {
                    if (e == null) {
                        if (Logc.allowPrints()) {
                            Logc.d(Tags.AppUpdate, "下载成功,保存路径:" + file.getPath());
                        }
                        showUpdateDialog(file);
                    } else {
                        if (Logc.allowPrints()) {
                            Logc.d(Tags.AppUpdate, "下载失败：" + e.getMessage());
                        }
                    }
                }

                @Override
                public void onProgress(int value) {
                    if (Logc.allowPrints()) {
                        Logc.d(Tags.AppUpdate, "下载进度：" + value);
                    }
                }
            });
        }
    }

    private void showUpdateDialog(final File file) {
        AppExecutors.executeMainThread(new Runnable() {
            @Override
            public void run() {
                String[] updateLog = mUpgradeApp.updateLog.split(";");
                String mLog = "";
                for (String log : updateLog) {
                    mLog = mLog + log + "\n";
                }
                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(MainApplication.getTopActivity())
                        .title("版本升级 !")
                        .content(mLog);
                if (mUpgradeApp.force) {
                    dialogBuilder.positiveText("点击升级 ！");
                    dialogBuilder.dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InstallUtil.startInstall(file);
                            MainApplication.exit();
                        }
                    });
                } else {
                    dialogBuilder.positiveText("点击升级 ！")
                            .negativeText("下次提醒 ！")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    SharedPres.putLong(PrefsKey.NextRemindUpgrade, System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    InstallUtil.startInstall(file);
                                    MainApplication.exit();
                                }
                            });
                }
                dialogBuilder.show();
            }
        });
    }
}
