package dev.ngai.fantasticservice.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import dev.ngai.fantasticservice.TagDispatchKey;

/**
 * @author Ngai
 * @since 2018/1/4
 * Des: 事件传递过程 ！
 */
public class MainView extends RelativeLayout {
    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



        getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        TagDispatchKey.dispatchKeyEvent(this, event);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TagDispatchKey.onKeyDown(this, keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TagDispatchKey.onKeyUp(this, keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        TagDispatchKey.onKeyLongPress(this, keyCode, event);
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        TagDispatchKey.onKeyMultiple(this, keyCode, event);
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        TagDispatchKey.onKeyPreIme(this, keyCode, event);
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        TagDispatchKey.onKeyShortcut(this, keyCode, event);
        return super.onKeyShortcut(keyCode, event);
    }

    // --------- TouchEvent ----------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TagDispatchKey.dispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        TagDispatchKey.onInterceptTouchEvent(this, ev);
//        if("mainView".equals(getTag())){
//            return true;
//        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TagDispatchKey.onTouchEvent(this, event);
//        if("mainView3".equals(getTag())){
//            return true;
//        }
        return super.onTouchEvent(event);
    }

    // -- onFocus --------------------------------
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        TagDispatchKey.onFocusChanged(this, gainFocus, direction, previouslyFocusedRect);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public View focusSearch(int direction) {
        TagDispatchKey.focusSearch(this, direction);
        return super.focusSearch(direction);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        TagDispatchKey.focusSearch(this, focused, direction);
        return super.focusSearch(focused, direction);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        TagDispatchKey.requestFocus(this, direction, previouslyFocusedRect);
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public View findFocus() {
        TagDispatchKey.findFocus(this);
        return super.findFocus();
    }

    @Override
    public void clearChildFocus(View child) {
        TagDispatchKey.clearChildFocus(this,child);
        super.clearChildFocus(child);
    }

    @Override
    public void clearFocus() {
        TagDispatchKey.clearFocus(this);
        super.clearFocus();
    }

    @Override
    public boolean hasWindowFocus() {
        TagDispatchKey.hasWindowFocus(this);
        return super.hasWindowFocus();
    }

    @Override
    public void focusableViewAvailable(View v) {
        TagDispatchKey.focusableViewAvailable(this,v);
        super.focusableViewAvailable(v);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        TagDispatchKey.requestChildFocus(this,child,focused);
        super.requestChildFocus(child, focused);
    }

    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        TagDispatchKey.dispatchGenericFocusedEvent(this,event);
        return super.dispatchGenericFocusedEvent(event);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        TagDispatchKey.onRequestFocusInDescendants(this,direction,previouslyFocusedRect);
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        TagDispatchKey.onWindowFocusChanged(this,hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }

}
