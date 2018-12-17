package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;
import dev.weihl.opengl.shape.helper.VaryHelper;

public class WarpBasic extends Shape {

    private final String TAG = "WarpBasic";

    String mVertexShaderCode = "" +
            "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "varying  vec4 vColor;" +
            "attribute vec4 aColor;" +
            "void main(){" +
            "gl_Position = vMatrix*vPosition;" +
            "vColor = aColor;" +
            "}";

    String mFragmentShaderCode = "" +
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main(){" +
            "gl_FragColor = vColor;" +
            "} ";

    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;
    float mColorSrc[];
    int mProgram;

    ShortBuffer mIndexBuffer;
    short mVertexIndexSrc[] = {
            0, 2, 3, 0, 1, 3
    };

    float[] mVertexSrc;
    float height = 0.0f;
    float tAlpha = 0.0f; // 透明度切换
    CallBack mCallBack;
    DigitHelper.RoundDigit tAlphaDigit;

    VaryHelper mVaryHelper;
    int mMatrix;
    float[] matrixArr;
    float mScale = 1f;
    DigitHelper.SquareRoundDigit mTranslateDigit;
    DigitHelper.RoundDigit mScaleDigit;

    interface CallBack {
        void onAlpha(float alpha);
    }

    void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    WarpBasic(View view) {
        super(view);
        tAlphaDigit = DigitHelper.createRoundDigit(0.0f, 0.3f, 0.001f);
        mVaryHelper = new VaryHelper();
        Log.d(TAG, "WarpBasic !");
    }

    void createInit() {
        Log.d(TAG, "createInit !");
        refreshPositions();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        GLES20.glAttachShader(mProgram, vertexShader);
        int colorFragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);
        GLES20.glAttachShader(mProgram, colorFragShader);
        GLES20.glLinkProgram(mProgram);

        mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        float tOff = 10;
        mTranslateDigit = DigitHelper.createSquareRoundDigit(-tOff, -tOff, tOff, tOff, 0.01f, 0.01f);
        mScaleDigit = DigitHelper.createRoundDigit(1.5f, 35f, 0.05f);
    }

    void refreshPositions() {
        Log.d(TAG, "refreshPositions !");
        mVertexSrc = null;
        mVertexIndexSrc = null;
        mColorSrc = null;
        System.gc();

        mVertexSrc = createPositions();
        mVertexIndexSrc = createIndexOrder();
        mColorSrc = createColorSrc();

        if (mVertexBuffer != null) {
            mVertexBuffer.clear();
        }
        if (mColorBuffer != null) {
            mColorBuffer.clear();
        }
        if (mIndexBuffer != null) {
            mIndexBuffer.clear();
        }
        mVertexBuffer = asFloatBuffer(mVertexSrc);
        mColorBuffer = asFloatBuffer(mColorSrc);
        mIndexBuffer = asShortBuffer(mVertexIndexSrc);
    }

    float[] createPositions() {
        Log.d(TAG, "createPositions !");
        ArrayList<Float> allPoints = new ArrayList<>();
        allPoints.addAll(createLineOne());
        allPoints.addAll(createLineTwo());
        float[] f = new float[allPoints.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = allPoints.get(i);
        }
        Log.d(TAG, "createPositions = " + Arrays.toString(f));
        return f;
    }

    // line 2 ; y = x + 0.5f
    ArrayList<Float> createLineTwo() {
        ArrayList<Float> data = new ArrayList<>();
        return data;
    }

    // line 1 ; ax^2+bx+c
    ArrayList<Float> createLineOne() {
        ArrayList<Float> data = new ArrayList<>();
        return data;
    }

    short[] createIndexOrder() {
        ArrayList<Short> data = new ArrayList<>();
        int pointNum = mVertexSrc.length / 3;
        int offset = pointNum / 2;
        int num = pointNum - 2;
        short lineOneLastIndex = 0;
        short lineTwoLastIndex = (short) offset;
        for (int i = 0; i < num; i++) {
            // 交替取三点组成三角形
            if (i % 2 == 0) {
                // line 1 取两点
                data.add(lineOneLastIndex);
                data.add(++lineOneLastIndex);
                data.add(lineTwoLastIndex);
            } else {
                // line 2
                data.add(lineOneLastIndex);
                data.add(lineTwoLastIndex);
                data.add(++lineTwoLastIndex);
            }
        }
        short[] f = new short[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        Log.d(TAG, "createIndexOrder = " + Arrays.toString(f));
        return f;
    }

    float[] createColorSrc() {
        Log.d(TAG, "createColorSrc !");
        ArrayList<Float> data = new ArrayList<>();
        int pointNum = mVertexSrc.length / 3;
        int offset = pointNum / 2;
        for (int i = 0; i < offset; i++) {
            data.add(115.0f / 255);
            data.add(185.0f / 255);
            data.add(245.0f / 255);
            data.add(tAlpha);
        }
        for (int i = 0; i < offset; i++) {
            data.add(36.0f / 255);
            data.add(30.0f / 255);
            data.add(90.0f / 255);
            data.add(tAlpha);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated !");
        createInit();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged !");
        float rate = width / (float) height;
        mVaryHelper.ortho(-rate * 6, rate * 6, -6, 6, 3, 20);
        mVaryHelper.setCamera(0, 0, 10, 0, 0, 0, 0, 1, 0);
    }

    public void setAlpha(float alpha) {
        tAlpha = alpha;
    }

    public float getAlpha() {
        return tAlpha;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame !");
        GLES20.glEnable(GLES20.GL_BLEND);//启用透明，注意不要开启深度测试,即不要有glEnable(GL_DEPTH_TEST)
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
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mVertexIndexSrc.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(vPositionHandler);
        GLES20.glDisable(GLES20.GL_BLEND);

        mVaryHelper.popMatrix();
        if (mCallBack != null) {
            mCallBack.onAlpha(tAlpha);
        }
        tAlpha = tAlphaDigit.get();
    }
}
