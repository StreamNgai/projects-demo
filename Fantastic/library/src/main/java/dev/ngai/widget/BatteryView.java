package dev.ngai.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Ngai
 * @since 2018/3/20
 * Des: 支持样式，横，横+文字，竖，竖+文字 （horizontal ；horizontal_txt  ;  vertical ; vertical_txt）
 */
public class BatteryView extends View {

    private final String TAG = "BatteryView";
    private Orien mOrien = Orien.HORIZONTAL;
    private State mState = State.NORMAL;
    private int mWidth;
    private int mHeight;
    private int mShortest; // mWidth , mHeight
    private Paint mPaint;
    private RectF tbrRect; // 框
    private RectF tbhRect; // 头部
    private Rect tbpRect; // 容量
    private Path mLPath;
    private Rect txBounds;
    private int mProgress;
    private Handler mHandler;
    private BatteryReceiver mReceiver;

    private int mBattPadding = 20;
    private int mBattPaddingRatio = 10;
    private int mCapacityPadding = 16;
    private int mCapacityPaddingRatio = 10;
    private int mCapacityOfs = mCapacityPadding / 2;

    public void setStyle(Orien style) {
        this.mOrien = style;
        invalidate();
    }

    public void setState(State state) {
        this.mState = state;
        invalidate();
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler(Looper.getMainLooper());
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        tbrRect = new RectF();
        tbhRect = new RectF();
        tbpRect = new Rect();
        mLPath = new Path();
        txBounds = new Rect();

//        countBatteryCapacity();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow !");
        mReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        getContext().registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow !");
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }

    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            countBatteryCapacity(intent);
        }
    }

    private void pollingCountAction() {
        mHandler.removeCallbacks(countActionRunnable);
        mHandler.postDelayed(countActionRunnable, 60 * 1000);
    }

    private Runnable countActionRunnable = new Runnable() {
        @Override
        public void run() {
            countBatteryCapacity(null);
        }
    };

    private void countBatteryCapacity(Intent batteryInfoIntent) {
        if (batteryInfoIntent == null) {
            batteryInfoIntent = getContext().registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        if (batteryInfoIntent != null) {
            int level = batteryInfoIntent.getIntExtra("level", 0);//电量（0-100）
            int scale = batteryInfoIntent.getIntExtra("scale", 0);// 最大容量
            int progress = (int) (((float) level / scale) * 100);

            // BatteryManager.BATTERY_STATUS_CHARGING 表示是充电状态
            // BatteryManager.BATTERY_STATUS_DISCHARGING 放电中
            // BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电
            // BatteryManager.BATTERY_STATUS_FULL 电池满
            int status = batteryInfoIntent.getIntExtra("status", 0);
            if (BatteryManager.BATTERY_STATUS_CHARGING == status) {
                mState = State.CHARGING;
            } else {
                mState = State.NORMAL;
            }
            setProgress(progress);
            Log.d(TAG, "level = " + level + " ; scale = " + scale + " ; progress = " + progress + " , 充电 = " + (BatteryManager.BATTERY_STATUS_CHARGING == status));
        } else {
            Log.d(TAG, "BatteryInfoIntent is null !");
        }

        pollingCountAction();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mShortest = mWidth > mHeight ? mHeight : mWidth;
        mBattPadding = mShortest / mBattPaddingRatio;
        mCapacityPadding = mShortest / mCapacityPaddingRatio;
        mCapacityOfs = mCapacityPadding / 2;
        Log.d(TAG, "onMeasure : Width = " + mWidth + " ; Height = " + mHeight + " ; Shortest = " + mShortest + " ; BattPadding = " + mBattPadding);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState != State.CHARGING) {
            if (mProgress < 20) {
                mState = State.LOW;
            } else if (mProgress > 20) {
                mState = State.NORMAL;
            }
        }
        mPaint.setColor(mState.getColor());

        // 闪电
        int blW = 0;
        if (mState == State.CHARGING) {
            canvas.save();
            mPaint.setStyle(Paint.Style.FILL);
            int blH = mShortest / 3;
            blW = (int) (blH * 0.7);
            mLPath.reset();
            mLPath.lineTo((float) (blW * 0.8), 0);
            mLPath.lineTo(0, (float) (blH * 0.6));
            mLPath.lineTo((float) (blW * 0.4), (float) (blH * 0.6));
            mLPath.lineTo((float) (blW * 0.2), blH);
            mLPath.lineTo(blW, (float) (blH * 0.45));
            mLPath.lineTo((float) (blW * 0.56), (float) (blH * 0.45));
            mLPath.lineTo((float) (blW * 0.8), 0);
            int tLPX = 0;
            if (isVertical()) {
                tLPX = blW / 2;
            }
            mLPath.offset(tLPX, mHeight / 2 - blH / 2);
            canvas.drawPath(mLPath, mPaint);
            canvas.restore();
        }

        // 框
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        int baW = (mShortest - mBattPadding * 2);// 电池总长度
        int brH = mShortest / 3; // 电池框宽
        int brW = baW - mBattPadding; // 电池框长
        tbrRect.set(0, 0, brW, brH);
        int brosX = mBattPadding + blW;
        int brosY = mHeight / 2 - brH / 2;
        tbrRect.offset(brosX, brosY);
        if (isVertical()) {
            int bryP = mShortest / 2;
            canvas.rotate(-90, bryP + blW, bryP);
        }
        canvas.drawRoundRect(tbrRect, 3, 3, mPaint);
        canvas.restore();

        // 容量
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        tbpRect.set(0, 0, (int) (brW * ((float) mProgress / 100) - mCapacityPadding), brH - mCapacityPadding);
        tbpRect.offset(brosX + mCapacityOfs, brosY + mCapacityOfs);
        if (isVertical()) {
            int bryP = mShortest / 2;
            canvas.rotate(-90, bryP + blW, bryP);
        }
        canvas.drawRect(tbpRect, mPaint);
        canvas.restore();

        // 头
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        int hWH = baW / mBattPaddingRatio;// 电池头长宽
        tbhRect.set(0, 0, hWH, hWH);
        tbhRect.offset(brW + mBattPadding + blW, mHeight / 2 - hWH / 2);
        if (isVertical()) {
            int bryP = mShortest / 2;
            canvas.rotate(-90, bryP + blW, bryP);
        }
        canvas.drawArc(tbhRect, 90, -180, true, mPaint);
        canvas.restore();

        // 文本
        if (isNeedText()) {
            canvas.save();
            String pro = mProgress + "%";
            mPaint.setTextSize(mShortest / 3);
            mPaint.getTextBounds(pro, 0, pro.length(), txBounds);
            if (mState == State.CHARGING) {
                if (isVertical()) {
                    canvas.drawText(pro, mShortest + mBattPadding, mHeight / 2 + txBounds.height() / 2, mPaint);
                } else {
                    canvas.drawText(pro, mShortest + mBattPadding * 2, mHeight / 2 + txBounds.height() / 2, mPaint);
                }
            } else {
                canvas.drawText(pro, mShortest, mHeight / 2 + txBounds.height() / 2, mPaint);
            }
            canvas.restore();
        }
    }

    public boolean isVertical() {
        return mOrien == Orien.VERTICAL
                || mOrien == Orien.VERTICAL_TEXT;
    }

    public boolean isHorizontal() {
        return mOrien == Orien.HORIZONTAL
                || mOrien == Orien.HORIZONTAL_TEXT;
    }

    public boolean isNeedText() {
        return mOrien == Orien.HORIZONTAL_TEXT
                || mOrien == Orien.VERTICAL_TEXT;
    }

    public enum State {
        LOW(Color.RED),
        NORMAL(Color.WHITE),
        CHARGING(Color.GREEN);

        int mColor;

        State(int color) {
            this.mColor = color;
        }

        public int getColor() {
            return mColor;
        }
    }

    public enum Orien {
        HORIZONTAL,
        HORIZONTAL_TEXT,
        VERTICAL,
        VERTICAL_TEXT
    }
}
