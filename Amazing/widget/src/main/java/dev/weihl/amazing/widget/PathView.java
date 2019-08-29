package dev.weihl.amazing.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * @author Ngai
 * @since 2018/3/22
 * Des:
 */
public class PathView extends AppCompatTextView {

    final String TAG = "PathView";
    String mText;
    int mWidth;
    int mHeight;
    float mTextWidth;
    Path mPath;
    PathMeasure mPathMeasure;
    Path mDst;
    float mFontLength;
    float mMaxPathLength;
    Paint mEffectPaint;

    //当前绘画位置
    float[] mCurPos = new float[2];
    Path mEffectPath;
    float mBaseLine;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(3);
        getPaint().setAntiAlias(true);

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mDst = new Path();
        // 花样 mEffectPaint
        mEffectPaint = new Paint();
        mEffectPaint.setStyle(Paint.Style.STROKE);
        mEffectPaint.setStrokeWidth(5);
        mEffectPaint.setColor(Color.YELLOW);
        mEffectPaint.setAntiAlias(true);

        mEffectPath = new Path();

    }

    public void setPathText(String text) {
        mDst.reset();
        this.mText = text;
        mTextWidth = Layout.getDesiredWidth(mText, getPaint());
        mPath.reset();
        getPaint().getTextPath(mText, 0, mText.length(), 0, 0, mPath);
        Paint.FontMetrics tFontMetrics = getPaint().getFontMetrics();
        mBaseLine = tFontMetrics.leading + tFontMetrics.ascent;
        mPath.offset(0, -mBaseLine);

        mPathMeasure.setPath(mPath, false);
        mFontLength = mPathMeasure.getLength();
        mMaxPathLength = countMaxPathLength(mPath);

        ViewGroup.LayoutParams vlp = getLayoutParams();
        if (vlp != null) {
            vlp.width = (int) mTextWidth;
            vlp.height = (int) (tFontMetrics.bottom-tFontMetrics.top);
            setLayoutParams(vlp);
        }
        Log.d(TAG, "MaxPathLength = " + mMaxPathLength + " ; BaseLine = " + mBaseLine);
        Log.d("FontMetrics", "tFontMetrics.leading = " + tFontMetrics.leading
                + " ; tFontMetrics.ascent = " + tFontMetrics.ascent
                + " ; tFontMetrics.descent = " + tFontMetrics.descent
                + " ; tFontMetrics.top = " + tFontMetrics.top
                + " ; tFontMetrics.bottom = " + tFontMetrics.bottom);

    }

    private float countMaxPathLength(Path path) {
        float tMaxPathLength = 0;
        PathMeasure tPathMeasure = new PathMeasure();
        tPathMeasure.setPath(path, false);
        do {
            tMaxPathLength = tMaxPathLength + tPathMeasure.getLength();
        } while (tPathMeasure.nextContour());
        return tMaxPathLength;
    }

    public void setProgress(float progress) {
        float tLength = mMaxPathLength * progress;
        float def = mFontLength - tLength;
        float defNextFort = mPathMeasure.getLength() - def;
        mPathMeasure.getSegment(0, defNextFort, mDst, true);

        mEffectPath.reset();
        mPathMeasure.getPosTan(defNextFort, mCurPos, null);

        if (tLength > mFontLength) {
            mPathMeasure.nextContour();
            mFontLength = mFontLength + mPathMeasure.getLength();
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        Log.d(TAG, "onMeasure : Width = " + mWidth + " ; Height = " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mEffectPath.lineTo(mCurPos[0], mCurPos[1]);
//        canvas.drawPath(mEffectPath, mEffectPaint);
        canvas.drawPath(mDst, getPaint());
    }

    public float getMaxPathLength() {
        return mMaxPathLength;
    }

}
