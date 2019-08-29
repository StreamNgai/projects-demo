package dev.ngai.fantasticservice.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import dev.ngai.fantasticservice.TagDispatchKey;

/**
 * @author Ngai
 * @since 2018/1/4
 * Des:
 */
public class BtnView extends Button {
    public BtnView(Context context) {
        super(context);
    }

    public BtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BtnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TagDispatchKey.dispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TagDispatchKey.onTouchEvent(this, event);
        return super.onTouchEvent(event);
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
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        TagDispatchKey.onFocusChanged(this,gainFocus,direction,previouslyFocusedRect);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    // -- onFocus --------------------------------

    @Override
    public View focusSearch(int direction) {
        TagDispatchKey.focusSearch(this, direction);
        return super.focusSearch(direction);
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
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        TagDispatchKey.dispatchGenericFocusedEvent(this,event);
        return super.dispatchGenericFocusedEvent(event);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        TagDispatchKey.onWindowFocusChanged(this,hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }


}
