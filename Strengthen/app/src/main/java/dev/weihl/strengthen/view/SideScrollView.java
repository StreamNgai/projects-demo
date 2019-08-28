package dev.weihl.strengthen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Des:
 * Created by Weihl
 * 2019/2/24
 */
public class SideScrollView extends ScrollView {


    LinearLayout mLayout;

    public SideScrollView(Context context) {
        this(context,null);
    }

    public SideScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // bg
        mLayout = new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mLayout);
    }

    @Override
    public View findFocus() {
        return super.findFocus();
    }

    @Override
    public View focusSearch(View focused, int direction) {



        return super.focusSearch(focused, direction);
    }

    public void removeItemsView() {
        mLayout.removeAllViews();
    }
}
