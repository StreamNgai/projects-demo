package dev.ngai.fantastic.utils;

import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.Discover;

/**
 * Des:
 * Created by Weihl
 * 2017/8/31
 */
public class DiscoverUtil {
    public static void displayWh(Discover discover) {
        String[] thumbWh = discover.getThumbWh().split(Constant.Separated);
        int width = Integer.valueOf(thumbWh[0]);
        int height = Integer.valueOf(thumbWh[1]);
        int displayWidth = Session.getDisplayWidth();
        int imgGridWidth = (displayWidth) / 3;
        int imgGridHeight = (imgGridWidth * height) / width;
        discover.setThumbWh(imgGridWidth + Constant.Separated + imgGridHeight);
    }
}
