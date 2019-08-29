package dev.ngai.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.ngai.library.R;

/**
 * Created by Ngai on 2017/4/13.
 */

public class BottomNavigationBar extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static ArrayList<Tab> sTabPool;
    private ViewPager mViewPager;
    private NaviBarOnClickListener mListener;
    private NaviBarTabAlphaListener mTabAlphaListener;

    public void onCorrect() {
        if (sTabPool == null) {
            sTabPool = new ArrayList<>();
        }
    }

    public interface NaviBarOnClickListener {
        void onClick(int position, Tab tab);
    }

    public void setNaviBarOnClickListener(NaviBarOnClickListener listener) {
        this.mListener = listener;
    }

    public interface NaviBarTabAlphaListener {
        // alpha : from=0.0, to=1.0
        void onCurrTab(int position, float alpha);

        void onNextTab(int position, float alpha);
    }

    public void setNaviBarTabAlphaListener(NaviBarTabAlphaListener listener) {
        this.mTabAlphaListener = listener;
    }

    public BottomNavigationBar(Context context) {
        this(context,null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (sTabPool == null) {
            sTabPool = new ArrayList<>();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("BottomNavigationBar", "onDetachedFromWindow");
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("BottomNavigationBar", "onAttachedToWindow");
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(item, smoothScroll);
        }
    }

    public void setupWithViewPager(@NonNull final ViewPager viewPager, int item) {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
        this.mViewPager = viewPager;
        this.mViewPager.addOnPageChangeListener(this);

        if (sTabPool.size() != viewPager.getAdapter().getCount()) {
            throw new RuntimeException("tab.size != viewPage.adapter.size !" + sTabPool.size());
        }

        setCurrentItem(item, false);
        onPageSelected(item);
    }


    public void addTab(@NonNull Tab tab) {
        if(!sTabPool.contains(tab)){
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            llp.weight = 1;
            tab.mLayout.setOnClickListener(this);
            addView(tab.mLayout, llp);
            sTabPool.add(tab);
        }
    }


    public Tab newTab(String unique) {

        if(TextUtils.isEmpty(unique)){
            throw new RuntimeException("unique is null !");
        }

        if(sTabPool != null && !sTabPool.isEmpty()){
            for (Tab tab: sTabPool) {
                if(unique.equals(tab.getUnique())){
                    return tab;
                }
            }
        }
        return new Tab(getContext(),unique);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            updateTabAlpha(position + 1, position, positionOffset);
        }
    }

    private void updateTabAlpha(int focusItem, int normalItem, float offset) {


        if(offset < 0.2) offset = 0f;
        if(offset > 0.8) offset = 1f;


        Tab focusTab = sTabPool.get(focusItem);
        focusTab.mImageView.setAlpha(1 - offset);
        focusTab.mImageViewSel.setAlpha(offset);
        focusTab.mTextView.setAlpha(1 - offset);
        focusTab.mTextViewSel.setAlpha(offset);

        Tab normalTab = sTabPool.get(normalItem);
        normalTab.mImageView.setAlpha(offset);
        normalTab.mImageViewSel.setAlpha(1 - offset);
        normalTab.mTextView.setAlpha(offset);
        normalTab.mTextViewSel.setAlpha(1 - offset);

        if (mTabAlphaListener != null) {
            mTabAlphaListener.onCurrTab(normalItem, 1 - offset);
            mTabAlphaListener.onNextTab(focusItem, offset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(getClass().getName(), "onPageSelected position = " + position);
        for (int i = 0; i < sTabPool.size(); i++) {
            Tab tab = sTabPool.get(i);
            if (i == position) {
                tab.mImageView.setAlpha(0L);
                tab.mImageViewSel.setAlpha(1L);
                tab.mTextView.setAlpha(0L);
                tab.mTextViewSel.setAlpha(1L);
            } else {
                tab.mImageView.setAlpha(1L);
                tab.mImageViewSel.setAlpha(0L);
                tab.mTextView.setAlpha(1L);
                tab.mTextViewSel.setAlpha(0L);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof Tab) {
            Tab tab = (Tab) v.getTag();
            int position = sTabPool.indexOf(tab);
            setCurrentItem(position, false);

            if (mListener != null)
                mListener.onClick(position, tab);
        }
    }

    public static class Tab {

        private String mUnique;
        private ImageView mImageView;
        private ImageView mImageViewSel;
        private TextView mTextView;
        private TextView mTextViewSel;
        private Drawable mSelectIcon;
        private Drawable mIcon;
        private String mTitle;
        private Context mContext;
        private RelativeLayout mLayout;

        Tab(Context context, String unique) {
            this.mContext = context;
            this.mUnique = unique;
            mLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                    R.layout.desgin_layout_tabview_bottomnavigationbar, null);
            this.mImageView = (ImageView) mLayout.findViewById(R.id.imageView);
            this.mImageViewSel = (ImageView) mLayout.findViewById(R.id.imageViewSel);
            this.mTextView = (TextView) mLayout.findViewById(R.id.textView);
            this.mTextViewSel = (TextView) mLayout.findViewById(R.id.textViewSel);

            mLayout.setFocusable(true);
            mLayout.setTag(this);
        }

        public Drawable getNormalIcon() {
            return mIcon;
        }

        public Drawable getSelectIcon() {
            return mSelectIcon;
        }

        public Tab setIcon(Drawable icon, Drawable selectIcon) {
            this.mIcon = icon;
            this.mSelectIcon = selectIcon;

            mImageViewSel.setImageDrawable(selectIcon);
            mImageViewSel.setAlpha(mLayout.isSelected() ? 1L : 0L);

            mImageView.setImageDrawable(icon);
            mImageView.setAlpha(mLayout.isSelected() ? 0L : 1L);
            return this;
        }


        public String getTitle() {
            return mTitle;
        }

        public Tab setTitle(String title, int normalColor, int focusColor) {
            this.mTitle = title;
            mTextView.setText(title);
            mTextViewSel.setText(title);

            mTextView.setTextColor(normalColor);
            mTextViewSel.setTextColor(focusColor);

            mTextView.setAlpha(mLayout.isSelected() ? 0L : 1L);
            mTextViewSel.setAlpha(mLayout.isSelected() ? 1L : 0L);
            return this;
        }
        public String getUnique() {
            return mUnique;
        }
    }

}
