package com.vsoontech.game.geniussch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * log 输出，不想说太多
 *
 * @author Ngai
 */
public class Logc {

    private static boolean mAllowPrint;

    static {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        mAllowPrint = GsConfig.nonReleaseVersion();
    }

    public static boolean allowPrint() {
        return mAllowPrint;
    }

    public static void d(String log) {
        Gdx.app.debug("GeniusSchool", log);
    }

}
