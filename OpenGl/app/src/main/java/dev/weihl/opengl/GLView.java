package dev.weihl.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.Oval;
import dev.weihl.opengl.shape.Sinline;
import dev.weihl.opengl.shape.Square;


public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer{

    Square mSquare;
    Oval mOval;
    public GLView(Context context) {
        this(context, null);
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("GLView", " GLView!");

        setEGLContextClientVersion(2);
        setRenderer(this);
        mSquare =  new Square(this);
        mOval = new Sinline(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mSquare.onSurfaceCreated(gl,config);
        mOval.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mSquare.onSurfaceChanged(gl,width,height);
        mOval.onSurfaceChanged(gl,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSquare.onDrawFrame(gl);
        mOval.onDrawFrame(gl);

    }
}
