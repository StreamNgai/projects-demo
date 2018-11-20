package dev.ngai.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Ngai
 * @since 2018/3/21
 * Des:
 */
public class LightingView extends View {

    private Paint mPaint;
    private Path mLPath;
    private int mWidth;
    private int mHeight;

    public LightingView(Context context) {
        this(context, null);
    }

    public LightingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mLPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth - 50, mHeight- 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLightning(canvas, mWidth, mHeight, 0, 0);

    }

    private void drawLightning(Canvas canvas, int width, int height, int offsetX, int offsetY) {
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        mLPath.reset();
        mLPath.lineTo((float) (width * 0.8), 0);
        mLPath.lineTo(0, (float) (height * 0.6));
        mLPath.lineTo((float) (width * 0.4), (float) (height * 0.6));
        mLPath.lineTo((float) (width * 0.2), height);
        mLPath.lineTo(width, (float) (height * 0.45));
        mLPath.lineTo((float) (width * 0.56), (float) (height * 0.45));
        mLPath.lineTo((float) (width * 0.8), 0);
        mLPath.offset(offsetX, offsetY);
        canvas.drawPath(mLPath, mPaint);
        canvas.restore();
    }

}
