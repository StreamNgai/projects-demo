package dev.weihl.amazing.executors;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import dev.weihl.amazing.Logc;

/**
 * @author Ngai
 * @since 2018/1/2
 * Des:
 */
public class DownloadWorker {

    public interface CallBack {
        void onStart();

        void onDone(File file, Exception e);

        void onProgress(int value);
    }

    public static void execute(String filePath, String fileUrl, CallBack callBack) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                if (callBack != null) {
                    callBack.onStart();
                    if (Logc.allowPrints()) {
                        Log.d("DownloadWorker", "start !");
                    }
                }
                InputStream mInputStream = conn.getInputStream();
                byte[] data = new byte[1024];
                File file = checkFile(filePath);
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                int len = -1;
                float contentLength = conn.getContentLength();
                float addLen = 0;
                int tempUnitPro = 0;
                while (-1 != (len = mInputStream.read(data))) {
                    accessFile.write(data, 0, len);
                    addLen += len;
                    int unitPro = (int) (addLen / contentLength * 100);
                    if (Logc.allowPrints()) {
                        Log.d("DownloadWorker", "contentLength = " + contentLength + " ; addLen = " + addLen + " ; unit = " + unitPro);
                    }
                    if (callBack != null && tempUnitPro < unitPro) {
                        tempUnitPro = unitPro;
                        callBack.onProgress(tempUnitPro);
                    }
                }
                mInputStream.close();
                if (Logc.allowPrints()) {
                    Log.d("DownloadWorker", "finish !");
                }
                if (callBack != null) {
                    callBack.onDone(file, null);
                }
            }

        } catch (Exception e) {
            if (callBack != null) {
                callBack.onDone(null, e);
            }
            e.printStackTrace();
        }
    }

    public void run() {
        String fileUrl = "http://p.gdown.baidu.com/769e53d0a569292bdc8e41305f9ab60842a1281b62d51eb071a788a08bb4393e6704e201c996830982b1234afe5ae3f442871971a5d7be91505a3e58f638cadee1695e54610ead1dea78cdd0fa00e60b415564049d54f9538283bd7581b1f86208542e45301ad7cfebda7319e521c88ffdaee5ad759b76a25aec2c0de49899e981c74f19c9461658c58f29b5df2eeb117eb1bf794d6fe04b0f1d0ac9d1a65c9b8b2661e4abaca7626584e86d222609fba671386298383d4293ca7b9f56155440d6f0070bd297b31ad83bfa0bddd2a88655490c67ec57b232498dd072932d232d";
        String filePath = "/mnt/sdcard/mydownload/fantastic.apk";
        execute(filePath, fileUrl, new CallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDone(File file, Exception e) {

            }

            @Override
            public void onProgress(int value) {

            }
        });
    }

    private static File checkFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file;
            }
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                file.createNewFile();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
