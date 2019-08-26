package com.vsoontech.game.geniussch;

/**
 * 一些apk信息
 *
 * @author Ngai
 */
public class GsConfig {

    public static int appVersion = 0;
    public static String appVersionName = "";

    public static boolean nonReleaseVersion() {
        return !appVersionName.endsWith(".00");
    }
}
