package dev.weihl.opengl.shape;

import android.util.Log;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Warp extends Shape {

    Square mSquare;
    WarpOne mWarpOne;
    WarpTwo mWarpTwo;
    int mCount = 0;

    public Warp(View view) {
        super(view);
        mSquare = new Square(view);
        mWarpOne = new WarpOne(view);
        mWarpTwo = new WarpTwo(view);
        mWarpOne.setCallBack(callBack);
        mWarpTwo.setCallBack(callBack);
    }

    WarpBasic.CallBack callBack = new WarpBasic.CallBack() {
        @Override
        public void onAlpha(float alpha) {
            Log.d("WarpAction", "alpha = " + alpha);
            if (alpha == 0.0f) {
                ++mCount;
                Log.d("WarpAction", "count = " + mCount);
            }
        }
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSquare.onSurfaceCreated(gl, config);
//        mWarpOne.onSurfaceCreated(gl, config);
//        mWarpTwo.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mSquare.onSurfaceChanged(gl, width, height);
//        mWarpOne.onSurfaceChanged(gl, width, height);
//        mWarpTwo.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mSquare.onDrawFrame(gl);
//        if (mCount % 2 == 0) {
//            mWarpOne.onDrawFrame(gl);
//        } else {
//            mWarpTwo.onDrawFrame(gl);
//        }
        mWarpTwo.onDrawFrame(gl);
    }
}
