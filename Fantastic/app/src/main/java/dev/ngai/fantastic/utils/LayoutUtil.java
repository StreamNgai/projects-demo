package dev.ngai.fantastic.utils;

import android.support.v4.graphics.ColorUtils;

/**
 * Created by Ngai on 2017/2/3.
 */

public class LayoutUtil {


    // 根据bywidth , 重新调整宽高；
    public static int[] adjustLayoutParamsByWidth(int byWidth, int resWidth, int resHeight) {

        int byHeight = (byWidth * resHeight)/resWidth;

        return new int[]{byWidth,byHeight};
    }

    public static int[] adjustLayoutParamsByHeight(int byHeight, int resWidth, int resHeight) {

        int byWidth = (byHeight * resWidth) / resHeight;

        return new int[]{byWidth,byHeight};
    }

    public static int randomColor() {
        float[] TEMP_HSL = new float[]{0, 0, 0};
        float[] hsl = TEMP_HSL;
        hsl[0] = (float) (Math.random() * 360);
        hsl[1] = (float) (40 + (Math.random() * 60));
        hsl[2] = (float) (40 + (Math.random() * 60));
        return ColorUtils.HSLToColor(hsl);
    }
}
