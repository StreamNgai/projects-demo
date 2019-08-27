package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;
import dev.weihl.opengl.shape.helper.VaryHelper;

public class Movement extends Transform {

    final String TAG = "Movement";
    int mMatrix;
    float[] matrixArr;
    VaryHelper mVaryHelper;
    float mScale = 1f;
    DigitHelper.SquareRoundDigit mTranslateDigit;
    DigitHelper.RoundDigit mScaleDigit;


    public Movement(View view) {
        super(view);
        changeShaderCode();

        mVaryHelper = new VaryHelper();
    }

    private void changeShaderCode() {

        mVertexShaderCode = "" +
                "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "varying  vec4 vColor;" +
                "attribute vec4 aColor;" +
                "void main(){" +
                "gl_Position = vMatrix*vPosition;" +
                "vColor = aColor;" +
                "}";
        mFragmentShaderCode = "" +
                "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main(){" +
                "gl_FragColor = vColor;" +
                "} ";
    }

    @Override
    void createInit() {
        super.createInit();
        mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        float tOff = 10;
        mTranslateDigit = DigitHelper.createSquareRoundDigit(-tOff, -tOff, tOff, tOff, 0.01f, 0.01f);
        mScaleDigit = DigitHelper.createRoundDigit(1.5f, 35f, 0.05f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        float rate = width / (float) height;
        mVaryHelper.ortho(-rate * 6, rate * 6, -6, 6, 3, 20);
        mVaryHelper.setCamera(0, 0, 10, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // 绘制
        GLES20.glUseProgram(mProgram);

        mVaryHelper.pushMatrix();
        float[] xY = mTranslateDigit.get();
        mVaryHelper.translate(xY[0], xY[1], 0);
        mScale = mScaleDigit.get();
        Log.d(TAG, "mScale = " + mScale);
        mVaryHelper.scale(mScale, mScale, 0f);
        matrixArr = mVaryHelper.getFinalMatrix();
        if (matrixArr != null) {
            GLES20.glUniformMatrix4fv(mMatrix, 1, false, matrixArr, 0);
        }

        int vPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositionHandler);
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexSrc.length / 3);
        GLES20.glDisableVertexAttribArray(vPositionHandler);

        mVaryHelper.popMatrix();
        // 变换图形
        aRadio = aDigit.get(); // X
        bRadio = bDigit.get(); // X

        float[] angD = mOvalDigit.get();
        mCircleX = angD[0];
        mCircleY = angD[1];
        Log.d("onDrawFrame", "mCircleX = " + mCircleX + " ; mCircleY = " + mCircleY);
        refreshPositions();
    }
}
