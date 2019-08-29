package dev.weihl.amazing.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author Ngai
 * @since 2018/6/13
 * Des:
 */
public class Direction {

    static final public int UPWARD = 1;
    static final public int DOWNWARD = 2;
    static final public int LEFTWARD = 3;
    static final public int RIGHTWARD = 4;

    private float[] mDownXY = new float[2];
    private float[] mMoveXY = new float[2];
    private float[] mUpXY = new float[2];
    private float[] mTempXY = new float[2];
    private float mYDirection;// 负 是否向下滚动，正 是否向上滚动。
    private float mXDirection;// 负 是否向左滚动，正 是否向右滚动。
    private Context mContext;
    private int mDirection;
    private float mOffSet;

    public Direction(Context context) {
        this.mContext = context;
    }

    public void onTouchMeasure(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownXY[0] = event.getX();
                mDownXY[1] = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mUpXY[0] = event.getX();
                mUpXY[1] = event.getY();
                mYDirection = mTempXY[1] - mUpXY[1];
                mXDirection = mTempXY[0] - mUpXY[0];
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveXY[0] = event.getX();
                mMoveXY[1] = event.getY();
                mYDirection = mTempXY[1] - mMoveXY[1];
                mXDirection = mTempXY[0] - mMoveXY[0];
                break;
            default:
                break;
        }
        mTempXY[0] = event.getX();
        mTempXY[1] = event.getY();
    }

    public int get() {
        int tX = (int) Math.abs(mXDirection);
        int tY = (int) Math.abs(mYDirection);
        if (tX > tY) {
            // 横向差距大，取左右方向
            mDirection = mXDirection > 0 ? LEFTWARD : RIGHTWARD;
            mOffSet = mXDirection;
        } else {
            mDirection = mYDirection > 0 ? UPWARD : DOWNWARD;
            mOffSet = mYDirection;
        }
        Log.d("Direction", toString(mDirection));
        return mDirection;
    }

    public String toString(int direction) {
        String dire = "";
        switch (direction) {
            case UPWARD:
                dire = "UPWARD";
                break;
            case DOWNWARD:
                dire = "DOWNWARD";
                break;
            case LEFTWARD:
                dire = "LEFTWARD";
                break;
            case RIGHTWARD:
                dire = "RIGHTWARD";
                break;
        }
        return dire;
    }

    public float getOffSet() {
        return mOffSet;
    }
}
