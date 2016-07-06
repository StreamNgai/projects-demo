/**
 * Copyright (C) 2016
 */
package com.ngai.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

/**
 * 带拖影效果的进度条，可以显示百分比
 * Created by Ngai on 2016/5/9.
 *
 */
public class GradientProgressBar extends View {

    private static final String TAG = "SmearProgress.TAG";

    /**  当前进度. */
    private int mCurrProgress;
    /** 最大进度值,默认为100. */
    private int mMaxPrgress;
    /**  是否显示百分比. */
    private boolean isShowPercentage;
    /**  百分比显示颜色. */
    private int mPercentageColor;
    /**  进度条渲染Dra 包括有：ProgressDra,ThumbDra. */
    private LayerDrawable mLayerDra;
    private Bitmap mLayerSmearProgressBackground;
    private Bitmap mLayerSmearProgressThumb;
    /** 不确定因素颜色 . */
    private int mIndeterminacyColor;
    /** 是否为不确定 . */
    private boolean isIndeterminacy;
    /** 文字 . */
    private Paint mTextPaint;
    /** 百分比 . */
    private static final int PERCENTAGE = 100;
    /** 进度变化监听器 . */
    private OnSmearProgressBarChangeListener mListener;

    public interface OnSmearProgressBarChangeListener{
        public void onProgressChanged(View view, int progress);
    }

    public void setOnSmearProgressBarChangeListener(OnSmearProgressBarChangeListener listener){
        this.mListener = listener;
    }
    /**
     * 是否显示百分比 .
     *
     * @param show 是否需要显示
     */
    public void showPercentage(boolean show) {
        this.isShowPercentage = show;
    }

    /**
     * 获取当前进度值.
     *
     * @return int
     */
    public int getProgress() {
        return mCurrProgress;
    }

    /**
     * 设置最大值.
     *
     * @param max 值
     */
     public synchronized void setMax(int max) {
        this.mMaxPrgress = max;
    }

    /**
     * 设置当前进度.
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.mCurrProgress = progress;
        if(isIndeterminacy){
            Log.d(TAG,"Indeterminacy !");
        }else {
            invalidate();
            if (mListener != null) {
                mListener.onProgressChanged(this, mCurrProgress);
            }
        }

    }

    // 获取当前百分比.
    private String getCurrPercentage() {
        return ((mCurrProgress * PERCENTAGE) / mMaxPrgress) + "%";
    }

    // 计算Thumb拉升的长度
    private int calculatePullupLength() {
        return (getWidth() * mCurrProgress) / mMaxPrgress;
    }

    //***********************************************

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    int arcD_ = 1; // 扫过的角度
    int arcO_ = -90; // 扫点
    int limite_ = 0; // 最大跨度

    private void drawAnimation(Canvas canvas){

        if(limite_ >= arcDatas.size())
            limite_ = 0;

        arcO_ = arcDatas.get(limite_).getArcO();
        arcD_ = arcDatas.get(limite_).getArcD();
        limite_ += 1;

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mIndeterminacyColor);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO_, arcD_, true, paint);
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth()/2, getHeight()/2, (getWidth()/2)-dpToPx(4, getResources()), transparentPaint);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }
    //***********************************************

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isIndeterminacy){
            drawAnimation(canvas);
            invalidate();
        }else {
            canvas.drawBitmap(mLayerSmearProgressBackground,
                    new Rect(0, 0, mLayerSmearProgressBackground.getWidth(), mLayerSmearProgressBackground.getHeight()),
                    new RectF(0,0,getWidth(),getHeight()),null);
            int progressLength = calculatePullupLength();
            if (progressLength > 0) {
                mLayerSmearProgressThumb = drawable2Bitmap(mLayerDra.findDrawableByLayerId(R.id.layerSmearProgressThumb),progressLength,getHeight());
                canvas.drawBitmap(mLayerSmearProgressThumb,
                        new Rect(0, 0, mLayerSmearProgressThumb.getWidth(), mLayerSmearProgressThumb.getHeight()),
                        new RectF(0, 0, progressLength, getHeight()), null);
            }
            if(isShowPercentage){
                if(null == mTextPaint){
                    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    mTextPaint.setStrokeWidth(1);
                    mTextPaint.setColor(mPercentageColor);
                    mTextPaint.setStyle(Paint.Style.FILL);
                    mTextPaint.setTextSize(getHeight()/3 * 2);
                    mTextPaint.setAntiAlias(true);
                    Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
                }
                String percentStr = getCurrPercentage();
                float textWidth = mTextPaint.measureText(percentStr);
                Rect bounds = new Rect();
                Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
                int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                canvas.drawText(percentStr,progressLength - textWidth - 6, baseline, mTextPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSizeMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSizeMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG,"widthSize = "+widthSize+"heightSize = "+heightSize);

        mLayerSmearProgressBackground = drawable2Bitmap(mLayerDra.findDrawableByLayerId(R.id.layerSmearProgressBackground),widthSize,heightSize);
//        mLayerSmearProgressThumb = drawable2Bitmap(mLayerDra.findDrawableByLayerId(R.id.layerSmearProgressThumb),widthSize,heightSize);

    }

    private Bitmap drawable2Bitmap(Drawable drawable, int widthSize, int heightSize) {
        // 取 drawable 的长宽
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
        if (widthSize == 0 || heightSize == 0) return null;

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(widthSize, heightSize, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, widthSize, heightSize);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


    private  ArrayList<ArcData> arcDatas ;
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        if(arcDatas == null){
            arcDatas = new ArrayList<ArcData>();
            for (int i = 0; i < 18; i++) {
                ArcData arcData = new ArcData();
                arcData.setArcO(i-120);
                arcData.setArcD(90 + 14 * (i - 1));
                arcDatas.add(arcData);
            }
            for (int i = 0; i < 23; i++) {
                ArcData arcData = new ArcData();
                arcData.setArcO(-72 + 14 * (i - 1));
                arcData.setArcD(328 - 12 * (i - 1));
                arcDatas.add(arcData);
            }
            for (int i = 1; i < 18; i++) {
                ArcData arcData = new ArcData();
                arcData.setArcO(arcDatas.get(40).getArcO()+i);
                arcData.setArcD(arcDatas.get(40).getArcD()+i);
                arcDatas.add(arcData);
            }
        }


        // 参数
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.GradientProgressBar, defStyleAttr, 0);

        isIndeterminacy = typedArray.getBoolean(R.styleable.GradientProgressBar_spIndeterminacy,false);
        mIndeterminacyColor = typedArray.getColor(R.styleable.GradientProgressBar_spIndeterminacyColor,Color.RED);

        mMaxPrgress = typedArray.getInt(R.styleable.GradientProgressBar_spMax, PERCENTAGE);
        mCurrProgress = typedArray.getInt(R.styleable.GradientProgressBar_spProgress, 0);
        mPercentageColor = typedArray.getColor(R.styleable.GradientProgressBar_spPrecentageColor, Color.BLACK);
        isShowPercentage = typedArray.getBoolean(R.styleable.GradientProgressBar_spPrecentageShow, false);
        mLayerDra = (LayerDrawable) typedArray.getDrawable(R.styleable.GradientProgressBar_spLayerDra);
        if (mLayerDra == null) {
            Log.d(TAG, "Drawable Error ! , Please set the background color and schedule !");
            mLayerDra = (LayerDrawable) getContext().getResources().getDrawable(R.drawable.gradientprogress_horizontal);
        }

        typedArray.recycle();

    }

    public GradientProgressBar(Context context) {
        this(context, null);
    }

    public GradientProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private class ArcData{
        private int arcO;
        private int arcD;

        public int getArcO() {
            return arcO;
        }

        public void setArcO(int arcO) {
            this.arcO = arcO;
        }

        public int getArcD() {
            return arcD;
        }

        public void setArcD(int arcD) {
            this.arcD = arcD;
        }
    }
}
