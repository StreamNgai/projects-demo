package com.res.batchexe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class FileAction {


    FileAction() {
    }

    void changeFileNameToFirstLowerCaseLetter(@NonNull String dir, @NonNull String suffix) {
        try {
            if (!isEmpty(suffix)) {
                File dirFire = new File(dir);
                File[] srcFiles = dirFire.listFiles();
                for (File file : srcFiles) {
                    if (file.isDirectory()) {
                        changeFileNameToFirstLowerCaseLetter(file.getAbsolutePath(), suffix);
                    } else if (file.isFile() && file.getName().contains(suffix)) {
                        String filePath = file.getAbsolutePath();
                        int fileNameFirstLetterIndex = filePath.lastIndexOf("\\") + 1;
                        String newFileName = Character.toString(
                            filePath.charAt(fileNameFirstLetterIndex)).toLowerCase() + suffix;
                        String newFile = filePath.substring(0, fileNameFirstLetterIndex) + newFileName;
                        file.renameTo(new File(newFile));
                    }
                }
            }
            System.out.print("修改当前目录--目标后缀文件名为首字母小写--完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public void changeFileNameToFirstLowerCaseLetterAddIndex(@NonNull String dir, @NonNull String suffix) {
        try {
            if (!isEmpty(suffix)) {
                File dirFire = new File(dir);
                File[] srcFiles = dirFire.listFiles();
                int fileIndex = 0;
                for (File file : srcFiles) {
                    if (file.isDirectory()) {
                        changeFileNameToFirstLowerCaseLetterAddIndex(file.getAbsolutePath(), suffix);
                    } else if (file.isFile() && file.getName().contains(suffix)) {
                        String filePath = file.getAbsolutePath();
                        int fileNameFirstLetterIndex = filePath.lastIndexOf("\\") + 1;
                        String newFileName = Character.toString(
                            filePath.charAt(fileNameFirstLetterIndex)).toLowerCase() + "_" + (++fileIndex) + suffix;
                        String newFile = filePath.substring(0, fileNameFirstLetterIndex) + newFileName;
                        file.renameTo(new File(newFile));
                    }
                }
            }
            System.out.print("目录:" + dir + " ; 目标后缀文件:" + suffix + "  重命名完成 \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyAndPasteNewDir(String resDir, String targetDir, @NonNull String suffix) {
        try {
            if (!isEmpty(suffix)) {
                File resFile = new File(resDir);
                File targetFile = new File(targetDir);
                File[] srcFiles = resFile.listFiles();

                // targetFileDir
                if (!targetFile.exists()) {
                    boolean mknew = targetFile.mkdirs();
                    System.out.print(targetFile.getAbsolutePath() + "  目标目录创建" + mknew + "\n");
                }

                // 文件操作
                for (File file : srcFiles) {
                    File newFile = new File(targetFile.getAbsolutePath() + "/" + file.getName());

                    // targetFile
                    if (!targetFile.exists()) {
                        boolean mknew = targetFile.mkdirs();
                        System.out.print(targetFile.getAbsolutePath() + "  目标文件创建" + mknew + "\n");
                    }

                    if (file.isDirectory()) {
                        copyAndPasteNewDir(file.getAbsolutePath(), newFile.getAbsolutePath(), suffix);
                    } else if (file.isFile() && file.getName().contains(suffix)) {
                        if (!newFile.exists()) {
                            newFile.createNewFile();
                            copyFile(file, newFile);
                        }
                    }
                }
                System.out.print(
                    "源目录:" + resFile.getAbsolutePath() + "  目标目录：" + targetFile.getAbsolutePath() + " ; 目标后缀文件:"
                        + suffix + "  复制完成 \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
        throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
}
