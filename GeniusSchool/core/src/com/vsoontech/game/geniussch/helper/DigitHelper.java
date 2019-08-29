package com.vsoontech.game.geniussch.helper;

import java.text.DecimalFormat;

public class DigitHelper {

    static public RoundDigit createRoundDigit(float min, float max, float offset) {
        return new RoundDigit(min, max, offset);
    }

    public static SquareRoundDigit createSquareRoundDigit(float x, float y, float toX, float toY, float xOffset,
        float yOffset) {
        return new SquareRoundDigit(x, y, toX, toY, xOffset, yOffset);
    }

    /**
     * 返回 四边边内的游离坐标
     * get() = int[x,y]
     *
     * @author Ngai
     */
    static public class SquareRoundDigit {

        float mX;
        float mY;
        float mToX;
        float mToY;
        float mXOffset;
        float mYOffset;
        RoundDigit mXDigit;
        RoundDigit mYDigit;
        float[] mXY;

        // 返回正方形内游离点的坐标
        private SquareRoundDigit(float x, float y, float toX, float toY, float xOffset, float yOffset) {
            this.mX = x;
            this.mY = y;
            this.mToX = toX;
            this.mToY = toY;
            this.mXOffset = xOffset;
            this.mYOffset = yOffset;
            this.mXDigit = new RoundDigit(x, toX, xOffset);
            this.mYDigit = new RoundDigit(y, toY, yOffset);
            this.mXY = new float[]{mX, mY};
        }

        public float[] get() {
            mXY[0] = mXDigit.get();
            mXY[1] = mYDigit.get();
            return mXY;
        }
    }

    /**
     * 返回两点之间，从大到小，小到大的游离值;
     *
     * @author Ngai
     */
    static public class RoundDigit {

        short mDirection; // 1：向右；其他：向左
        float mMin;
        float mMax;
        float mOffset;
        float mValue;
        DecimalFormat df;
        float[] mData;
        int mFullFrame;
        int mCurrIndex;

        // 返回两点之间游离点
        private RoundDigit(float min, float max, float offset) {
            this.mMin = min;
            this.mMax = max;
            this.mOffset = offset;
            this.mValue = min;
            this.mDirection = 1; // 先递增
            this.mCurrIndex = -1;
            df = new DecimalFormat(String.valueOf(offset));
            mFullFrame = (int) ((mMax - mMin) / mOffset);
            mData = new float[mFullFrame + 1];
            for (int i = 0; i <= mFullFrame; i++) {
                mData[i] = Float.valueOf(df.format(min + i * offset));
            }
        }

        public void setDecreaseNum(float num) {
            mDirection = 0;
            mCurrIndex = mFullFrame;
            for (int i = mFullFrame; i > 0; i--) {
                if (num < mData[i]) {
                    mCurrIndex = i;
                }
            }
        }

        public void setIncrease(float num) {
            mDirection = 1;
            mCurrIndex = 0;
            for (int i = mFullFrame; i > 0; i--) {
                if (num < mData[i]) {
                    mCurrIndex = i;
                }
            }
        }

        public void reset() {
            mDirection = 1;
            mCurrIndex = -1;
            mValue = mMin;
        }

        public int getFullFrame() {
            return mFullFrame + 1;
        }

        public boolean isMax() {
            return mMax == mValue;
        }

        public boolean isMin() {
            return mMax == mValue;
        }

        public float get() {
            switch (mDirection) {
                case 1: // ++
                    if (mCurrIndex == mFullFrame) {
                        mDirection = 0;
                        mValue = mMax;
                    } else {
                        mValue = mData[++mCurrIndex];
                    }
                    break;
                default: // --
                    if (mCurrIndex == 0) {
                        mDirection = 1;
                        mValue = mMin;
                    } else {
                        mValue = mData[--mCurrIndex];
                    }
                    break;
            }
            return mValue;
        }
    }

}
