package dev.ngai.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Ngai
 * @since 2018/3/22
 * Des:
 */
public class PathView extends View {

    final String TAG = "PathView";
    String mText;
    TextPaint mPaint;
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

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new TextPaint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(100);

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

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
    }

    public void setText(String text) {
        mDst.reset();
        this.mText = text;
        mTextWidth = Layout.getDesiredWidth(mText, mPaint);
        mPath.reset();
        mPaint.getTextPath(mText, 0, mText.length(), 0, 0, mPath);
        mPath.offset(30, 200);
        mPathMeasure.setPath(mPath, false);
        mFontLength = mPathMeasure.getLength();
        mMaxPathLength = countMaxPathLength(mPath);
        Log.d(TAG, "MaxPathLength = " + mMaxPathLength);
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
        Log.d(TAG, "FontLength = " + mFontLength + " ; Length = " + tLength);
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
        setMeasuredDimension(mWidth > mTextWidth ? mWidth : (int) mTextWidth, mHeight);
        Log.d(TAG, "onMeasure : Width = " + mWidth + " ; Height = " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEffectPath.lineTo(mCurPos[0], mCurPos[1]);
        canvas.drawPath(mEffectPath, mEffectPaint);
        canvas.drawPath(mDst, mPaint);
    }

    public float getMaxPathLength() {
        return mMaxPathLength;
    }
}
