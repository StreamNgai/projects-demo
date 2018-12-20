package dev.whl.openglwidget.wave;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class WaveView extends GLSurfaceView implements GLSurfaceView.Renderer {

    Wave mLineOne;
    Wave mLineTwo;
    Spread mSpreadOne;
    Spread mSpreadTwo;
    Action mAction = Action.Unknown;

    enum Action {
        Unknown,
        End,
        Loading,
        Spread,
        Wave;

    }

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);

        mLineOne = new Wave(this, "green");
        mLineOne.setAmplitude(1f);
        mLineOne.setLineColor(19f, 101f, 0f, 255f);
        mLineOne.setLineWidth(6);

        mLineTwo = new Wave(this, "blue");
        mLineTwo.setAmplitude(1f);
        mLineTwo.setLineColor(0f, 117f, 251f, 255f);
        mLineTwo.setLineWidth(10);

        mSpreadOne = new Spread(this, "green");
        mSpreadOne.setLineWidth(6);
        mSpreadOne.setType(SpreadPoints.Type.LeftRight);
        mSpreadOne.setLineColor(19f, 101f, 0f, 255f);

        mSpreadTwo = new Spread(this, "blue");
        mSpreadTwo.setLineWidth(10);
        mSpreadTwo.setType(SpreadPoints.Type.LeftRight);
        mSpreadTwo.setLineColor(0f, 117f, 251f, 255f);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mLineOne.onSurfaceCreated(gl, config);
        mLineTwo.onSurfaceCreated(gl, config);
        mSpreadOne.onSurfaceCreated(gl, config);
        mSpreadTwo.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mLineOne.onSurfaceChanged(gl, width, height);
        mLineTwo.onSurfaceChanged(gl, width, height);
        mSpreadOne.onSurfaceChanged(gl, width, height);
        mSpreadTwo.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        switch (mAction) {
            case Spread:
            case Loading:
            case End:
                mSpreadOne.onDrawFrame(gl);
                mSpreadTwo.onDrawFrame(gl);
                break;
            case Wave:
                mLineOne.onDrawFrame(gl);
                mLineTwo.onDrawFrame(gl);
                break;
            default:
                // Unknown
                break;
        }
    }

    // 展开到,建立波形动作
    public void doSpreadAnim() {
        mSpreadOne.setType(SpreadPoints.Type.LeftRight);
        mSpreadOne.setSpread();
        mSpreadTwo.setType(SpreadPoints.Type.LeftRight);
        mSpreadTwo.setSpread();
        mAction = Action.Spread;
    }

    public void doWaveAnim() {
        mAction = Action.Wave;
    }

    public void doEmptyAnim() {
        mAction = Action.Unknown;
    }

    public void doLoadingAnim() {
        mSpreadOne.setType(SpreadPoints.Type.UpDown);
        mSpreadOne.setUpDigit();
        mSpreadTwo.setType(SpreadPoints.Type.UpDown);
        mSpreadTwo.setDownDigit();
        mAction = Action.Loading;
    }

    public void doEndAnim() {
        mSpreadOne.setType(SpreadPoints.Type.LeftRight);
        mSpreadOne.setShrink();
        mSpreadTwo.setType(SpreadPoints.Type.LeftRight);
        mSpreadTwo.setShrink();
        mAction = Action.End;
    }

    public void setSpreadCallBack(Spread.CallBack callBack) {
        // 其中一条状态对就行了
        mSpreadOne.setCallBack(callBack);
    }

    /**
     * 控制线条振幅，从设置值衰减到最低
     */
    public void setAmplitude(float lineGreen, float lineBlue) {
        mLineOne.setAmplitude(lineGreen);
        mLineTwo.setAmplitude(lineBlue);
    }

}
