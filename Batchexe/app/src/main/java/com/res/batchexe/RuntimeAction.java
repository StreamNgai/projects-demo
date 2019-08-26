package com.res.batchexe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;

public class RuntimeAction {

    public static final String PATH_TEXTURE_PACKER_JAR = "./jar/texturepacker.jar";

    public void runnableTexturePackerSubDir(@NonNull String inputPath, @NonNull String outPath) {
        String jarPath = new File(PATH_TEXTURE_PACKER_JAR).getAbsolutePath();
        String cmdStr = "cmd.exe /c java -jar  " + jarPath;
        try {

            File srcDir = new File(inputPath);
            File targetDir = new File(outPath);
            File[] srcFiles = srcDir.listFiles();
            StringBuilder errorStr = new StringBuilder();
            for (File itemFile : srcFiles) {
                if (itemFile.isDirectory()) {
                    StringBuilder cmdBuilder = new StringBuilder(cmdStr);
                    String aPath = itemFile.getAbsolutePath();
                    int charIndex = aPath.lastIndexOf("\\");
                    String newDir = Character.toString(aPath.charAt(charIndex + 1)).toLowerCase();
                    cmdBuilder.append(" ").append(aPath)
                        .append(" ").append(targetDir.getAbsolutePath()).append("\\").append(newDir)
                        .append(" ").append(newDir);
                    Process process = Runtime.getRuntime().exec(cmdBuilder.toString());
                    int status = process.waitFor();
                    System.out.println("执行完毕：（" + status + ") --> " + cmdBuilder.toString());
                    if (status == 1) {
                        errorStr.append(itemFile.getName()).append(" ; ");
                    }
                }
            }
            System.out.println("失败操作：（" + errorStr.toString() + ")");

        } catch (Exception e) {

        }
    }

    public void runnableTexturePacker(@NonNull String inputPath, @NonNull String outPath) {
        runnableTexturePacker(inputPath, outPath, "pack");
    }

    public void runnableTexturePacker(@NonNull String inputPath, @NonNull String outPath, String atlasName) {
        String jarPath = new File(PATH_TEXTURE_PACKER_JAR).getAbsolutePath();
        String cmdStr = "cmd.exe /c java -jar  " + jarPath;
        try {
            File srcDir = new File(inputPath);
            File targetDir = new File(outPath);
            if (srcDir.isDirectory()) {
                StringBuilder cmdBuilder = new StringBuilder(cmdStr);
                cmdBuilder.append(" ").append(srcDir.getAbsolutePath())
                    .append(" ").append(targetDir.getAbsolutePath())
                    .append(" ").append(atlasName);
                Process process = Runtime.getRuntime().exec(cmdBuilder.toString());
                int status = process.waitFor();
                System.out.println("执行完毕：（" + status + ") --> " + cmdBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
}
