package dev.weihl.amazing.util;

import android.content.Context;

/**
 * @author Ngai
 * @since 2018/6/14
 * Des:
 */
public class DensityUtil {

    public static int dpToPx(Context context, float dps) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
        // 官网方法，参考 TabLayout dpToPx
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    public static int pxToDp(Context context, float px) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue / scale + 0.5f);
        return Math.round(context.getResources().getDisplayMetrics().density / px);
    }

}
