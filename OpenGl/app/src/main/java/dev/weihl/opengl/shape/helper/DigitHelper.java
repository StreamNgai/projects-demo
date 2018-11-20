package dev.weihl.opengl.shape.helper;

public class DigitHelper {


    static public RoundDigit createRoundDigit(float min, float max, float offset) {
        return new RoundDigit(min, max, offset);
    }

    public static SquareRoundDigit createSquareRoundDigit(float x, float y, float toX, float toY, float xOffset, float yOffset) {
        return new SquareRoundDigit(x, y, toX, toY, xOffset, yOffset);
    }


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


    static public class RoundDigit {

        short mDirection; // 1：向右；其他：向左
        float mMin;
        float mMax;
        float mOffset;
        float mRadio;

        // 返回两点之间游离点
        private RoundDigit(float min, float max, float offset) {
            this.mMin = min;
            this.mMax = max;
            this.mOffset = offset;
            this.mRadio = min;
            this.mDirection = 1;
        }

        public float get() {
            switch (mDirection) {
                case 1: // ++
                    mRadio += mOffset;
                    if (mRadio >= mMax) {
                        mDirection = 0;
                        mRadio = mMax;
                    }
                    break;
                default: // --
                    mRadio -= mOffset;
                    if (mRadio <= mMin) {
                        mDirection = 1;
                        mRadio = mMin;
                    }
                    break;
            }
            return mRadio;
        }
    }

}
