package dev.weihl.amazing.util;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.R;

import static android.animation.ObjectAnimator.ofFloat;

public class AnimUtil {

    public static void scaleView(View view, boolean hasFocus, float normalSize, float scaleSize, long duration) {
        scaleView(view, hasFocus, normalSize, scaleSize, duration, null);
    }

    public static void scaleView(View view, boolean hasFocus, float normalSize, float scaleSize, long duration, TimeInterpolator interpolator) {
        float startValue = hasFocus ? normalSize : scaleSize;
        float endValue = hasFocus ? scaleSize : normalSize;
        scaleView(view, startValue, endValue, duration, interpolator);
    }

    public static void scaleView(View view, float start, float end, long duration, TimeInterpolator interpolator) {
        scaleView(view, start, end, start, end, duration, interpolator);
    }

    public static void scaleViewX(View view, float start, float end, long duration, TimeInterpolator interpolator) {
        scaleView(view, start, end, 0, 0, duration, interpolator);
    }

    public static void scaleViewY(View view, float start, float end, long duration, TimeInterpolator interpolator) {
        scaleView(view, 0, 0, start, end, duration, interpolator);
    }

    public static void scaleView(View view, float startXSize, float endXSize, float startYSize, float endYSize, long duration, TimeInterpolator interpolator) {
        if (startXSize != endXSize) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", startXSize, endXSize).setDuration(duration);
            if (interpolator != null) {
                animator.setInterpolator(interpolator);
            }
            animator.start();
        }
        if (startYSize != endYSize) {
            ObjectAnimator animator = ofFloat(view, "scaleY", startYSize, endYSize).setDuration(duration);
            if (interpolator != null) {
                animator.setInterpolator(interpolator);
            }
            animator.start();
        }
    }

    public static void rotation(View view, float start, float end, long duration) {
        rotation(view, start, end, start, end, duration);
    }

    public static void rotationX(View view, float start, float end, long duration) {
        rotation(view, start, end, 0, 0, duration);
    }

    public static void rotationY(View view, float start, float end, long duration) {
        rotation(view, 0, 0, start, end, duration);
    }

    public static void rotation(View view, float startXSize, float endXSize, float startYSize, float endYSize, long duration) {
        if (startXSize != endXSize) {
            ofFloat(view, "rotationX", startXSize, endXSize).setDuration(duration).start();
        }
        if (startYSize != endYSize) {
            ofFloat(view, "rotationY", startXSize, endXSize).setDuration(duration).start();
        }
    }

    public static void alphaView(View view, float start, float end, long duration) {
        alphaView(view, start, end, duration, null);
    }

    public static void alphaView(View view, float start, float end, long duration, TimeInterpolator interpolator) {
        ObjectAnimator alpha = ofFloat(view, "alpha", start, end).setDuration(duration);
        if (interpolator != null) {
            alpha.setInterpolator(interpolator);
        }
        alpha.start();
    }

    public static Animation translateView(View view,
                                          float fromXDelta, float toXDelta,
                                          float fromYDelta, float toYDelta,
                                          long duration, boolean isFillAfter,
                                          Interpolator interpolator) {
        view.clearAnimation();
        TranslateAnimation tAnim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        tAnim.setDuration(duration);
        tAnim.setFillAfter(isFillAfter);
        view.startAnimation(tAnim);
        if (interpolator != null)
            tAnim.setInterpolator(interpolator);
        return tAnim;
    }

    public static void shakeView(View view) {
        Animation tAnimation = AnimationUtils.loadAnimation(MainApplication.getContext(), R.anim.sample_shake);
        view.setAnimation(tAnimation);
        tAnimation.setInterpolator(new CycleInterpolator(5));
        Vibrator vibrator = (Vibrator) MainApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }
}
