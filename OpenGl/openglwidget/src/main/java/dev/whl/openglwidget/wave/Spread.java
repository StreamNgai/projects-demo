package dev.whl.openglwidget.wave;

import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dev.whl.openglwidget.DigitHelper;
import dev.whl.openglwidget.FileCache;
import dev.whl.openglwidget.Shape;


public class Spread extends Shape {

    private final String TAG = "Spread";
    String mLineName;

    String mVertexShaderCode = "" +
            "attribute vec4 vPosition;" +
            "varying  vec4 vColor;" +
            "attribute vec4 aColor;" +
            "void main(){" +
            "gl_Position = vPosition;" +
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
    int mProgram;
    float[] mVertexSrc;

    // 可变参数
    float aLeftRightMove = 0.0f; // 左右延伸
    float bUpDownMove = 1.00f;// 上下平移
    DigitHelper.RoundDigit aLeftRightDigit;
    DigitHelper.RoundDigit bUpDownDigit;
    SpreadPoints mSpreadPoints;
    int mLineWidth = 10;


    public Spread(View view, @NonNull String name) {
        super(view);
        mLineName = name;
        aLeftRightDigit = DigitHelper.createRoundDigit(0.0f, 1.0f, 0.1f);
        bUpDownDigit = DigitHelper.createRoundDigit(-0.6f, 0.6f, 0.05f);

        FileCache speradP = FileCache.get(view.getContext(), "speradpoints");
        mSpreadPoints = (SpreadPoints) speradP.getAsObject("points");
        if (mSpreadPoints != null) {

            Log.d("FileCache", "read cache !");
        } else {
            mSpreadPoints = SpreadPoints.getInstance();
            Log.d("FileCache", "getInstance !");
        }

        Log.d(TAG, "Spread() ! " + mLineName);
    }

    public void setType(SpreadPoints.Type type) {
        mSpreadPoints.setType(type);
    }

    public void setSpread() {
        aLeftRightDigit.setIncreaseNum(0.1f);
    }

    public void setShrink() {
        aLeftRightDigit.setDecreaseNum(0.8f);
    }

    public void setUpDigit() {
        bUpDownDigit.setIncreaseNum(0.0f);
    }

    public void setDownDigit() {
        bUpDownDigit.setDecreaseNum(0.0f);
    }

    public void setLineWidth(int width) {
        mLineWidth = width;
    }

    public void setLineColor(float red, float green, float blue, float alpha) {
        mSpreadPoints.setColor(red, green, blue, alpha, mLineName);
    }

    void refreshPositions() {

        if (mVertexBuffer != null) {
            mVertexBuffer.clear();
        }

        mVertexSrc = mSpreadPoints.getLinePoints(aLeftRightMove, bUpDownMove);
        if (mVertexSrc != null && mVertexSrc.length > 0) {
            mVertexBuffer = asFloatBuffer(mVertexSrc);
            Log.d("FileCache", "Spread(" + mLineName + ") VertexSrc get cache !");
        } else {
            mSpreadPoints.putLinePoints(aLeftRightMove, bUpDownMove);
            mVertexSrc = mSpreadPoints.getLinePoints(aLeftRightMove, bUpDownMove);
            mVertexBuffer = asFloatBuffer(mVertexSrc);
            Log.d("FileCache", "Spread(" + mLineName + ") new VertexSrc !");
        }

        if (mColorBuffer == null) {
            mColorBuffer = mSpreadPoints.createColorBuffer(mLineName);
        }
    }

    void createInit() {
        refreshPositions();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        GLES20.glAttachShader(mProgram, vertexShader);
        int colorFragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);
        GLES20.glAttachShader(mProgram, colorFragShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated !");
        createInit();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged !");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame !");
        //启用透明，注意不要开启深度测试,即不要有glEnable(GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glUseProgram(mProgram);
        int vPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositionHandler);
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glLineWidth(mLineWidth);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mVertexSrc.length / 3);
        GLES20.glDisableVertexAttribArray(vPositionHandler);

        if (mSpreadPoints.isLR()) {
            if (aLeftRightDigit.isMax()) {
                if (mCallBack != null) {
                    mCallBack.doSpreadAnimEnd();
                }
            } else if (aLeftRightDigit.isMin()) {
                if (mCallBack != null) {
                    mCallBack.doShrinkAnimEnd();
                }
            } else {
                aLeftRightMove = aLeftRightDigit.get();
            }
        } else {
            bUpDownMove = bUpDownDigit.get();
        }
        refreshPositions();
    }

    CallBack mCallBack;

    interface CallBack {
        void doSpreadAnimEnd();

        void doShrinkAnimEnd();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
}
