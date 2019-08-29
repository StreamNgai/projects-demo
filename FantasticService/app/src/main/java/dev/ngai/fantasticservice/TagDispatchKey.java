package dev.ngai.fantasticservice;

import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import static android.view.View.FOCUS_BACKWARD;
import static android.view.View.FOCUS_DOWN;
import static android.view.View.FOCUS_FORWARD;
import static android.view.View.FOCUS_LEFT;
import static android.view.View.FOCUS_RIGHT;
import static android.view.View.FOCUS_UP;

/**
 * @author Ngai
 * @since 2018/1/4
 * Des:
 */
public class TagDispatchKey {
    public static String Dispatch = "Dispatch";
    public static String TouchDispatch = "TouchDispatch";
    public static String KeyDispatch = "KeyDispatch";
    public static String FocusDispatch = "FocusDispatch";

    public static void dispatchTouchEvent(View view, MotionEvent ev) {
        Log.d(TagDispatchKey.TouchDispatch, "Touch >> View(" + view.getTag()
                + ") ->dispatchTouchEvent MotionEvent = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onInterceptTouchEvent(View view, MotionEvent ev) {
        Log.d(TagDispatchKey.TouchDispatch, "Touch >> View(" + view.getTag()
                + ") ->onInterceptTouchEvent KeyEvent = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onTouchEvent(View view, MotionEvent event) {
        Log.d(TagDispatchKey.TouchDispatch, "Touch >> View(" + view.getTag()
                + ") ->onTouchEvent KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void dispatchKeyEvent(View view, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->dispatchKeyEvent  KeyEvent = " + (event.getAction() == KeyEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
                + " ; KeyCode = " + event.getKeyCode()
        );
    }

    public static void onKeyDown(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyDown KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onKeyUp(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyUp KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onKeyLongPress(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyLongPress KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onKeyMultiple(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyMultiple KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onKeyPreIme(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyPreIme KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onKeyShortcut(View view, int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.KeyDispatch, "Key >> View(" + view.getTag()
                + ") ->onKeyShortcut KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onFocusChanged(View view, boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->onFocusChanged gainFocus = " + gainFocus + " ; direction = " + direction
                + " ; previouslyFocusedRect = " + (previouslyFocusedRect == null ? " null " : previouslyFocusedRect.toString()));
    }

    public static void focusSearch(View view, int direction) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->focusSearch direction = " + direction
                + " ; " + getStringDirection(direction));
    }

    private static String getStringDirection(int direction) {
        switch (direction) {
            case FOCUS_BACKWARD:
                return "FOCUS_BACKWARD";
            case FOCUS_FORWARD:
                return "FOCUS_FORWARD";
            case FOCUS_LEFT:
                return "FOCUS_LEFT";
            case FOCUS_UP:
                return "FOCUS_UP";
            case FOCUS_RIGHT:
                return "FOCUS_RIGHT";
            case FOCUS_DOWN:
                return "FOCUS_DOWN";
        }
        return "";
    }

    public static void focusSearch(View view, View focused, int direction) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->focusSearch_2 direction = " + direction
                + " ; " + getStringDirection(direction)
                + " ; focused = " + focused.getTag()
        );
    }

    public static void requestFocus(View view, int direction, Rect previouslyFocusedRect) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->requestFocus direction = " + direction
                + " ; " + getStringDirection(direction)
        );
    }

    public static void findFocus(View view) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->findFocus "
        );
    }

    public static void clearChildFocus(View view, View child) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->clearChildFocus "
                + "child = " + child.getTag()
        );
    }

    public static void clearFocus(View view) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->clearFocus "
        );
    }

    public static void hasWindowFocus(View view) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->hasWindowFocus "
        );
    }

    public static void focusableViewAvailable(View view, View v) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->focusableViewAvailable "
                + "v = " + v.getTag()
        );
    }

    public static void requestChildFocus(View view, View child, View focused) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->requestChildFocus "
                + "child = " + child.getTag()
                + " ; focused = " + focused.getTag()
        );
    }

    public static void dispatchGenericFocusedEvent(View view, MotionEvent event) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->dispatchGenericFocusedEvent "
                + "event = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
    }

    public static void onRequestFocusInDescendants(View view, int direction, Rect previouslyFocusedRect) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->onRequestFocusInDescendants "
                + "direction = " + getStringDirection(direction)
        );
    }

    public static void onWindowFocusChanged(View view, boolean hasWindowFocus) {
        Log.d(TagDispatchKey.FocusDispatch, "Key >> View(" + view.getTag()
                + ") ->onWindowFocusChanged "
                + "hasWindowFocus = " + hasWindowFocus
        );
    }
}
