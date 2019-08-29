package dev.ngai.fantastic.business.main.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ngai on 2017/2/10.
 */

public class NoslipViewPager extends ViewPager {


    public NoslipViewPager(Context context) {
        super(context);
    }

    public NoslipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
//    }

//    @Override
//    public boolean canScrollHorizontally(int direction) {
//        return false;//super.canScrollHorizontally(direction);
//    }


    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return super.canScroll(v, checkV, dx, x, y);
    }
}
