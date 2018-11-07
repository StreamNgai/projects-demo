package dev.ngai.fantasticservice;

import android.util.Log;

/**
 * @author Ngai
 * @since 2018/2/6
 * Des:
 */
public class TagFragment {


    public static void onCreate(String tag) {
        log(tag+" onCreate !");
    }

    private static void log(String tag) {
        Log.d("TagFragment",tag);
    }

    public static void onStart(String tag) {
        log(tag+" onStart !");
    }

    public static void onStop(String tag) {
        log(tag+" onStop !");
    }

    public static void onDestroy(String tag) {
        log(tag+" onDestroy !");
    }

    public static void onPause(String tag) {
        log(tag+" onPause !");
    }

    public static void onResume(String tag) {
        log(tag+" onResume !");
    }

    public static void onAttach(String tag) {
        log(tag+" onAttach !");
    }

    public static void onDestroyView(String tag) {
        log(tag+" onDestroyView !");
    }

    public static void onViewCreated(String tag) {
        log(tag+" onViewCreated !");
    }

    public static void onActivityCreated(String tag) {
        log(tag+" onActivityCreated !");
    }

    public static void onSaveInstanceState(String tag) {
        log(tag+" onSaveInstanceState !");
    }

    public static void onCreateView(String tag) {
        log(tag+" onCreateView !");
    }
}
