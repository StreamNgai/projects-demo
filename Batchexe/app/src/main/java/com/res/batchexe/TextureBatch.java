package com.res.batchexe;

public class TextureBatch {


    public static void main(String[] args) {

//        FileAction fileAction = new FileAction();
//
//        String resDir = "./funnyabc/letter/audio";
//        String targetDir = "./app/src/main/assets/letter/audio";
//        fileAction.changeFileNameToFirstLowerCaseLetter(resDir, ".mp3");
//        fileAction.copyAndPasteNewDir(resDir, targetDir, ".mp3");
//
//        resDir = "./funnyabc/letter/blank";
//        targetDir = "./app/src/main/assets/letter/blank";
//        fileAction.changeFileNameToFirstLowerCaseLetter(resDir, ".png");
//        fileAction.copyAndPasteNewDir(resDir, targetDir, ".png");
//
//        resDir = "./funnyabc/letter/fill";
//        targetDir = "./app/src/main/assets/letter/fill";
//        fileAction.changeFileNameToFirstLowerCaseLetter(resDir, ".png");
//        fileAction.copyAndPasteNewDir(resDir, targetDir, ".png");
//
//        RuntimeAction runtimeAction = new RuntimeAction();
//        String inputPath = "./funnyabc/letter/dynamic";
//        String outPath = "./app/src/main/assets/letter/dynamic";
//        fileAction.changeFileNameToFirstLowerCaseLetterAddIndex(inputPath, ".png");
//        runtimeAction.runnableTexturePackerSubDir(inputPath, outPath);

//        ResourceAction resourceAction = new ResourceAction();
//        String constantPath = "./app/src/main/assets/letter/blank";
//        resourceAction.createConstantFile(constantPath);
//        resourceAction.createSubConstantFile(constantPath,"letter/");

        geniussch();
    }

    private static void geniussch() {
        // load
        FileAction fileAction = new FileAction();
//        RuntimeAction runtimeAction = new RuntimeAction();
//        String inputPath = "./geniussch/load";
//        String outPath = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\load";
//        runtimeAction.runnableTexturePacker(inputPath, outPath, "bar");

        // res
        ResourceAction resourceAction = new ResourceAction();
        String constantPath = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\letter";
        resourceAction.createConstantField(constantPath,"assets",".png");

//        constantPath = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\letter\\audio";
//        resourceAction.createConstantField(constantPath);
//
//        constantPath = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\letter\\blank";
//        resourceAction.createConstantField(constantPath);
//
//        constantPath = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\letter\\fill";
//        resourceAction.createConstantField(constantPath);

//        String resDir = "./app/src/main/assets/letter";
//        String targetDir = "D:\\Android\\workspace\\libgdx\\GeniusSchool\\android\\assets\\letter";
//        fileAction.copyAndPasteNewDir(resDir, targetDir, ".");
    }


}
