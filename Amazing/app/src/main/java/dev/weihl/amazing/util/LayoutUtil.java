package dev.weihl.amazing.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LayoutUtil {
    public static void setViewWidthHeight(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams vlp = view.getLayoutParams();
            if (vlp != null) {
                vlp.width = width;
                vlp.height = height;
            } else {
                vlp = new ViewGroup.LayoutParams(width, height);
            }
            view.setLayoutParams(vlp);
        }
    }
}
