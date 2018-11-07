package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Oval extends Shape {

    private final String TAG = "Triangle";

//    float mVertexSrc[] = {
//            -0.5f, 0.5f, 0.0f, // 0
//            0.5f, 0.5f, 0.0f,// 1
//            -0.5f, -0.5f, 0.0f,// 2
//            0.5f, -0.5f, 0.0f// 3
//    };

//    float mColorSrc[] = {//
//            69.0f / 255, 95.0f / 255, 175.0f / 255, 0.0f,
//            36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f,
//            36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f
//    };

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


    float radius = 1.0f;
    int n = 360;  //切割份数

    float[] mVertexSrc;

    float height = 0.0f;

    public Oval(View view) {
        super(view);
        Log.d(TAG, "Triangle() !");
    }

    void refreshPositions(){
        mVertexSrc = null;
        mVertexIndexSrc = null;
        mColorSrc = null;
        System.gc();

        mVertexSrc = createPositions();
        mVertexIndexSrc = createIndexOrder();
        mColorSrc = createColorSrc();

        if(mVertexBuffer != null){
            mVertexBuffer.clear();
        }
        if(mColorBuffer != null){
            mColorBuffer.clear();
        }
        if(mIndexBuffer != null){
            mIndexBuffer.clear();
        }
        mVertexBuffer = asFloatBuffer(mVertexSrc);
        mColorBuffer = asFloatBuffer(mColorSrc);
        mIndexBuffer = asShortBuffer(mVertexIndexSrc);
    }

    protected float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(height);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    void createInit() {
        refreshPositions();
        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        GLES20.glAttachShader(mProgram, vertexShader);
        int colorFragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);
        GLES20.glAttachShader(mProgram, colorFragShader);
        GLES20.glLinkProgram(mProgram);
    }

    private float[] createColorSrc() {
        ArrayList<Float> data = new ArrayList<>();
        // 69.0f / 255, 95.0f / 255, 175.0f / 255, 0.0f,
        data.add(69.0f / 255);
        data.add(95.0f / 255);
        data.add(175.0f / 255);
        data.add(0.0f);
        // 0,1,2,3,4,5,6
        for (int i = 0; i < mVertexSrc.length; i++) {
            // 36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f,
            data.add(36.0f / 255);
            data.add(30.0f / 255);
            data.add(90.0f / 255);
            data.add(0.0f);

        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    private short[] createIndexOrder() {
        ArrayList<Short> data = new ArrayList<>();
        // 0,1,2,3,4,5,6
        for (int i = 0; i < mVertexSrc.length; i++) {

            data.add((short) 0);
            data.add((short) (i + 1));
            data.add((short) (i + 2));


        }
        short[] f = new short[data.size()];
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

        GLES20.glUseProgram(mProgram);

        int vPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositionHandler);
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

//        int vColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
//        GLES20.glUniform4fv(vColorHandler, 1, mColorSrc, 0);
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexSrc.length / 3);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, mVertexIndexSrc.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(vPositionHandler);
    }
}
