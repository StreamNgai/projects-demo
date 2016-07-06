package org.streamngai.frame.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Ngai on 2016/3/24.
 */
public class NstatusBar extends LinearLayout {

    protected void init(){

        int statusHeight = getStatusBarHeight();

//        int left, int top, int right, int bottom
        setPadding(getPaddingLeft(), getPaddingTop() + statusHeight, getPaddingRight(), getPaddingBottom());


        ViewGroup.LayoutParams lp = getLayoutParams();
        if(lp == null){
            lp =  new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        setLayoutParams(lp);

    }

    private int getStatusBarHeight() {
        int statusHeight = 0;
        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            statusHeight = getContext().getResources().getDimensionPixelSize(i5);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    public NstatusBar(Context context) {
        super(context);
        init();
    }

    public NstatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NstatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

}
