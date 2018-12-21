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


public class Wave extends Shape {

    private final String TAG = "Wave";
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
    float aMove = 0.0f; // 波纹移动速率,可控
    float bAmplitude = 6.00f;// 音量大小 衰减到2.0f时不在变化
    DigitHelper.RoundDigit aMoveDigit;
    DigitHelper.RoundDigit bAmpltDigit;
    WavePoints mLinePoints;
    int mLineWidth = 10;

    public Wave(View view, @NonNull String name) {
        super(view);
        mLineName = name;
        aMoveDigit = DigitHelper.createRoundDigit(0.0f, 30.0f, 0.3f);
        bAmpltDigit = DigitHelper.createRoundDigit(1.00f, 6.00f, 0.1f);
        FileCache speradP = FileCache.get(view.getContext(), "wavepoints");
        mLinePoints = (WavePoints) speradP.getAsObject("points");
        if (mLinePoints != null) {
            Log.d("FileCache", "read cache !");
        } else {
            mLinePoints = WavePoints.getInstance();
            Log.d("FileCache", "getInstance !");
        }
        Log.d(TAG, "Wave() ! " + mLineName);
    }

    public void setAmplitude(float num) {
        aMoveDigit.reset();
        bAmpltDigit.setDecreaseNum(num);
    }

    public void setLineWidth(int width) {
        mLineWidth = width;
    }

    public void setLineColor(float red, float green, float blue, float alpha) {
        mLinePoints.setColor(red, green, blue, alpha, mLineName);
    }

    void refreshPositions() {

        if (mVertexBuffer != null) {
            mVertexBuffer.clear();
        }

        mVertexSrc = mLinePoints.getLinePoints(aMove, bAmplitude);
        if (mVertexSrc != null && mVertexSrc.length > 0) {
            mVertexBuffer = asFloatBuffer(mVertexSrc);
        } else {
            mLinePoints.putLinePoints(aMove, bAmplitude);
            mVertexSrc = mLinePoints.getLinePoints(aMove, bAmplitude);
            mVertexBuffer = asFloatBuffer(mVertexSrc);
        }

        if (mColorBuffer == null) {
            mColorBuffer =  mLinePoints.createColorBuffer(mLineName);
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

        aMove = aMoveDigit.get();
        if (!bAmpltDigit.isMax()) {
            bAmplitude = bAmpltDigit.get();
        }
        refreshPositions();
    }

}
