package dev.weihl.amazing;

import android.util.Log;

import dev.weihl.amazing.sharedpres.PrefsKey;
import dev.weihl.amazing.sharedpres.SharedPres;


/**
 * @author Ngai
 * @since 2017/12/21
 * Des:
 */
public class Logc {


    public static void d(String tag, String content) {
        Log.d("Amazing", "[" + tag + "] " + content);
    }

    public static boolean allowPrints() {
        long allowTime = SharedPres.getLong(PrefsKey.OpenLog, 0);
        return !BuildConfig.VERSION_NAME.endsWith(".00") || (System.currentTimeMillis() < allowTime);
    }
}
