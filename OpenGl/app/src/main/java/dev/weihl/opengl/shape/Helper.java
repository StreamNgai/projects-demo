package dev.weihl.opengl.shape;

public class Helper {


    static public RoundDigit createRoundDigit(float min, float max, float offset) {
        return new RoundDigit(min, max, offset);
    }

    static public class RoundDigit {

        short mDirection; // 向右；其他向左
        float mMin;
        float mMax;
        float mOffset;
        float mRadio;

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
