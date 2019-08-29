package dev.ngai.fantasticservice.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Ngai
 * @since 2017/12/14
 * Des:
 */
public class FileUtil {

    public static boolean writeFile(String filePath, String content, boolean append) {
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            if (file.exists() || file.createNewFile()) {
                if (file.isFile() && file.canWrite()) {
                    fileWriter = new FileWriter(filePath, append);
                    fileWriter.write(content);
                }
                return true;
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }
        }
        return false;
    }
}
