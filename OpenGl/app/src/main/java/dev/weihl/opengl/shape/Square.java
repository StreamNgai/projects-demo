package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Square extends Shape {

    private final String TAG = "Triangle";

    float mVertexSrc[] = {
            -1.0f, 1.0f, 0.0f, // 0
            1.0f, 1.0f, 0.0f,// 1
            -1.0f, -1.0f, 0.0f,// 2
            1.0f, -1.0f, 0.0f// 3
    };

    float mColorSrc[] = {
            36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f, //
            36.0f / 255, 30.0f / 255, 90.0f / 255, 0.0f, //
            69.0f / 255, 95.0f / 255, 175.0f / 255, 0.0f, //
            69.0f / 255, 95.0f / 255, 175.0f / 255, 0.0f
    };

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

    ShortBuffer mIndexBuffer;
    short mVertexIndexSrc[] = {
            0, 2, 3, 0, 1, 3
    };


    public Square(View view) {
        super(view);
        Log.d(TAG, "Triangle() !");
    }

    private void createInit() {
        mVertexBuffer = asFloatBuffer(mVertexSrc);
        mColorBuffer = asFloatBuffer(mColorSrc);
        mIndexBuffer = asShortBuffer(mVertexIndexSrc);

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

        GLES20.glUseProgram(mProgram);

        int vPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPositionHandler);
        GLES20.glVertexAttribPointer(vPositionHandler, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);

//        int vColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
//        GLES20.glUniform4fv(vColorHandler, 1, mColorSrc, 0);
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mVertexIndexSrc.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(vPositionHandler);
    }
}
