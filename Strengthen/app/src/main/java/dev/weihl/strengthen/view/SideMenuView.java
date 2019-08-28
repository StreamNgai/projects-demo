package dev.weihl.strengthen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Des:
 * Created by Weihl
 * 2019/2/24
 */
public class SideMenuView extends LinearLayout {

    ArrayList<SideItem> mSideItems;
    SideScrollView mFirstScrollView;

    public SideMenuView(Context context) {
        this(context, null);
    }

    public SideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSideItems = new ArrayList<>();
        //add first
        mFirstScrollView = new SideScrollView(context);
        mFirstScrollView.setLayoutParams(new LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mFirstScrollView);


    }

    public void updateMenuItem(ArrayList<SideItem> items) {
        mSideItems.clear();
        this.mSideItems.addAll(items);
        refreshFirstScrollView();
    }

    private void refreshFirstScrollView() {
        if (!mSideItems.isEmpty()) {
            mFirstScrollView.removeItemsView();
            mFirstScrollView.
        }
    }


}
