package dev.ngai.fantasticservice.task;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import dev.ngai.fantasticservice.ServiceApp;

/**
 * @author Ngai
 * @since 2018/1/2
 * Des:
 */
public class MyDownloadTask implements Runnable {

    @Override
    public void run() {

        String urlN = "http://p.gdown.baidu.com/769e53d0a569292bdc8e41305f9ab60842a1281b62d51eb071a788a08bb4393e6704e201c996830982b1234afe5ae3f442871971a5d7be91505a3e58f638cadee1695e54610ead1dea78cdd0fa00e60b415564049d54f9538283bd7581b1f86208542e45301ad7cfebda7319e521c88ffdaee5ad759b76a25aec2c0de49899e981c74f19c9461658c58f29b5df2eeb117eb1bf794d6fe04b0f1d0ac9d1a65c9b8b2661e4abaca7626584e86d222609fba671386298383d4293ca7b9f56155440d6f0070bd297b31ad83bfa0bddd2a88655490c67ec57b232498dd072932d232d";
        String filePath = "/mnt/sdcard/mydownload/fantastic.apk";

        try {
            URL url = new URL(urlN);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                Log.d("MyDownloadTask", "开始下载 !");
                InputStream mInputStream = conn.getInputStream();
                byte[] data = new byte[1024];
                File file = checkFile(filePath);
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                int len = -1;
                int contentLength = conn.getContentLength();
                Log.d("MyDownloadTask", "contentLength = " + contentLength);
                int addLen = 0;
                while (-1 != (len = mInputStream.read(data))) {
                    accessFile.write(data, 0, len);
//                    Log.d("MyDownloadTask", "len = " + (len));
                    addLen += len;
                    Log.d("MyDownloadTask", "addLen = " + addLen);
                    float unitPro = addLen / contentLength;
                    Log.d("MyDownloadTask", "unit = " + (unitPro));
                }
                mInputStream.close();
                Log.d("MyDownloadTask", "下载完成 !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File checkFile(String filePath) {
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
