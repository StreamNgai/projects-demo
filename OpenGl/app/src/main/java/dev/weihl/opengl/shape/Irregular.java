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

public class Irregular extends Shape {

    private final String TAG = "Irregular";

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
    float mColorSrc[];
    int mProgram;

    ShortBuffer mIndexBuffer;
    short mVertexIndexSrc[] = {
            0, 2, 3, 0, 1, 3
    };

    float[] mVertexSrc;
    float height = 0.0f;

    float aRadio = 0.9f;
    float bRadio = 0.3f;
    float cRadio = 0.3f;
    float tRadio = 1.0f; // 透明度切换
    float mCircleX = 0.0f;
    float mCircleY = 0.0f;
    DigitHelper.RoundDigit aDigit;
    DigitHelper.RoundDigit bDigit;
    DigitHelper.RoundDigit cDigit;
    DigitHelper.RoundDigit tDigit;
    DigitHelper.SquareRoundDigit mOvalDigit;

    public Irregular(View view) {
        super(view);
        aDigit = DigitHelper.createRoundDigit(-10.0f, 10.0f, 0.01f);
        bDigit = DigitHelper.createRoundDigit(0.0f, 0.5f, 0.001f);
        cDigit = DigitHelper.createRoundDigit(3.0f, 5.0f, 0.01f);
        tDigit = DigitHelper.createRoundDigit(-0.0f, 1.0f, 0.01f);
        mOvalDigit = DigitHelper.createSquareRoundDigit(-1.0f, -1.0f, 1.0f, 1.0f, 0.001f, 0.003f);
        Log.d(TAG, "Irregular() !");
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

    void refreshPositions() {
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
    private ArrayList<Float> createLineTwo() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) (bRadio * Math.cos((3 * Math.PI * x) / 10)));
            data.add(height);
        }
        return data;
    }

    // line 1 ; y = x - 0.5f
    private ArrayList<Float> createLineOne() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) Math.sin(x + aRadio));
            data.add(height);
        }
        return data;
    }

    private short[] createIndexOrder() {
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
        ArrayList<Float> data = new ArrayList<>();
        int pointNum = mVertexSrc.length / 3;
        int offset = pointNum / 2;
        for (int i = 0; i < offset; i++) {
            // 69.0f / 255, 95.0f / 255, 175.0f / 255, 0.0f,
            data.add(69.0f / 255);
            data.add(95.0f / 255);
            data.add(175.0f / 255);
            data.add(tRadio);
        }
        for (int i = 0; i < offset; i++) {
            // 36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f,
            data.add(36.0f / 255);
            data.add(30.0f / 255);
            data.add(90.0f / 255);
            data.add(tRadio);
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
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame !");
        GLES20.glEnable(GLES20.GL_BLEND);//启用透明，注意不要开启深度测试,即不要有glEnable(GL_DEPTH_TEST)
        GLES20.glUseProgram(mProgram);

        int vPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositionHandler);
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mVertexIndexSrc.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(vPositionHandler);

        aRadio = aDigit.get(); // X
        bRadio = bDigit.get(); // X
        cRadio = cDigit.get(); // X
        tRadio = tDigit.get();
//        float[] angD = mOvalDigit.get();
//        mCircleX = angD[0];
//        mCircleY = angD[1];
        refreshPositions();
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
