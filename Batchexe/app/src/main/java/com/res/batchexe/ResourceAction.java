package com.res.batchexe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;

/**
 * 生成 常量文件
 */
public class ResourceAction {

    static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public void createConstantField(String constantPath) {
        createConstantField(constantPath, "assets");
    }

    public void createConstantField(@NonNull String constantPath, @NonNull String segment) {
        createConstantField(constantPath, "assets", null);
    }

    public void createConstantField(@NonNull String constantPath, @NonNull String segment, String suffix) {
        File constantFile = new File(constantPath);
        if (constantFile.exists()
            && constantFile.isDirectory()) {
            System.out.print("segment = " + segment + "\n");
            recursiveFiles(constantFile, segment, suffix);
        }
    }

    private void recursiveFiles(File constantFile, String segment, String suffix) {
        File[] files = constantFile.listFiles();
        for (File file : files) {
            if (file.isFile() && (isEmpty(suffix) || checkSuffix(file, suffix))) {
                String path = file.getAbsolutePath();
                int segmentIndex = path.indexOf(segment) + segment.length() + 1;

                // 常量字段名
                String constantStr = path.substring(segmentIndex).replace("\\", "_")
                    .replace("/", "_")
                    .replace(".", "_").toUpperCase();

                // 去掉重复名
                String[] istrs = constantStr.split("_");
                String[] fields = new String[istrs.length];
                int fIndex = -1;
                for (int i = 0; i < istrs.length; i++) {
                    if (!isEmpty(istrs[i]) && !hasContains(fields, istrs[i])) {
                        fields[++fIndex] = istrs[i];
                    }
                }

                // 重组文件名
                StringBuilder constantField = new StringBuilder();
                for (int i = 0; i < fields.length; ) {
                    if (!isEmpty(fields[i])) {
                        constantField.append(fields[i]);
                    }
                    i++;
                    if (i < fields.length && !isEmpty(fields[i])) {
                        constantField.append("_");
                    }
                }

                // 常量路径
                String constantPath = path.substring(segmentIndex);
                constantPath = constantPath.replace("\\", "/");

                System.out.print("public static final String " + constantField + " = \"" + constantPath + "\";\n");
            } else if (file.isDirectory()) {
                recursiveFiles(file, segment, suffix);
            }
        }
    }

    private boolean checkSuffix(File file, String suffix) {
        if (file != null && !isEmpty(suffix)) {
            String fileName = file.getName();
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            return suffix.equals(fileSuffix);
        }
        return false;
    }

    private boolean hasContains(String[] fields, String istr) {
        if (fields != null && !isEmpty(istr)) {
            for (String itemS : fields) {
                if (istr.equals(itemS)) {
                    return true;
                }
            }
        }
        return false;
    }
}
