package dev.weihl.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 音量波形
 */
public class VolumeWaveView extends View {

    final String TAG = "VolumeWaveView";
    int mWidth;
    int mHeight;
    Paint mBluePaint;
    Path mPath;

    public VolumeWaveView(Context context) {
        this(context, null);
    }

    public VolumeWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBluePaint = new Paint();
        mBluePaint.setAntiAlias(true);
        mBluePaint.setColor(Color.BLUE);
        mBluePaint.setStrokeWidth(6);
        mBluePaint.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "width = " + mWidth + " ; height = " + mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw ！");
//        float[] pts = {0, 90, 90, 180, 90, 180, 180, 0, 180, 0, 1080, 180};
//        float[] pts = createBezierLines();
//        canvas.drawLines(pts, mBluePaint);

        mPath = new Path();
        mPath.setLastPoint(0, 90);
        mPath.cubicTo(mWidth * 3 / 20, mHeight / 2, mWidth * 3 / 20, mHeight * 20 / 20, mWidth * 6 / 20, mHeight / 2);

        mPath.cubicTo(mWidth * 17 / 20, 0,mWidth * 17 / 20,mHeight*8/20, mWidth * 4 / 20, 0);
        canvas.drawPath(mPath, mBluePaint);
        invalidate();
    }

//    private float[] createBezierLines() {
//        float[] pts = {};
//
//
//
//        return pts;
//    }


}
