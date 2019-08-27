package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;

public class Lines extends Shape {

    private final String TAG = "Lines";

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

    // 可变参数
    float aMove = -1.0f; // 波纹移动速率,可控
    float bAmplitude = 1.00f;// 音量大小 衰减到2.0f时不在变化
    DigitHelper.RoundDigit aMoveDigit;
    DigitHelper.RoundDigit bAmpltDigit;
    LineFuncData mLineFuncData;
    int mLineWidth = 10;

    public Lines(View view) {
        super(view);
        Log.d(TAG, "Lines() !");
        aMoveDigit = DigitHelper.createRoundDigit(0.0f, 30.0f, 1f);
        bAmpltDigit = DigitHelper.createRoundDigit(1.00f, 6.00f, 0.1f);
        mLineFuncData = new LineFuncData(bAmpltDigit.getFullFrame());

        // 待优化版本，事先生成纹理会容易OOM
//        createDatas();
    }

    private void createDatas() {
        long startTime = System.currentTimeMillis();
        Log.d(TAG, "createDatas() !");
        float tempAmplt = 0;
        float tempMove = 0;
        while (!bAmpltDigit.isMax()) {
            tempAmplt = bAmpltDigit.get();
            while (!aMoveDigit.isMax()) {
                tempMove = aMoveDigit.get();
                mLineFuncData.putLinePoints(tempMove, tempAmplt);
            }
            aMoveDigit.reset();
        }
        mLineFuncData.createIndexBuffer(tempMove, tempAmplt);
        mLineFuncData.createColorBuffer(tempMove, tempAmplt);

        aMoveDigit.reset();
        bAmpltDigit.reset();
        Log.d(TAG, "createDatas() ! s = " + (System.currentTimeMillis() - startTime) / 1000);
    }

    public void setAmplitude(float num) {
        bAmpltDigit.setDecreaseNum(num);
    }

    public void setLineWidth(int width) {
        mLineWidth = width;
    }

    void refreshPositions() {

        if (mVertexBuffer != null) {
            mVertexBuffer.clear();
        }

        mVertexSrc = mLineFuncData.getLinePoints(aMove, bAmplitude);
        if (mVertexSrc != null && mVertexSrc.length > 0) {
            mVertexBuffer = asFloatBuffer(mVertexSrc);
        } else {
            mLineFuncData.putLinePoints(aMove, bAmplitude);
            mVertexSrc = mLineFuncData.getLinePoints(aMove, bAmplitude);
            mVertexBuffer = asFloatBuffer(mVertexSrc);
        }

        mColorBuffer = mLineFuncData.getColorBuffer();
        if (mColorBuffer == null) {
            mLineFuncData.createColorBuffer(aMove, bAmplitude);
            mColorBuffer = mLineFuncData.getColorBuffer();
        }

        mIndexBuffer = mLineFuncData.getIndexBuffer();
        if (mIndexBuffer == null) {
            mLineFuncData.createIndexBuffer(aMove, bAmplitude);
            mIndexBuffer = mLineFuncData.getIndexBuffer();
        }
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
        int vColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(vColorHandler);
        GLES20.glVertexAttribPointer(vColorHandler, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glLineWidth(mLineWidth);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mVertexSrc.length / 3);
        GLES20.glDisableVertexAttribArray(vPositionHandler);

        aMove = aMoveDigit.get();
        bAmplitude = bAmpltDigit.get();
        refreshPositions();
    }

    class Point {
        float x;
        float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    class LineFuncData implements Serializable {
        private int mFullFrame;

        // 固定
        private FloatBuffer mColorBuffer;
        private ShortBuffer mIndexBuffer;

        // 随参数变化
        private HashMap<String, float[]> mLinePoints;
        private HashMap<String, FloatBuffer> mVertexBuffers;

        public LineFuncData(int fullFrame) {
            mFullFrame = fullFrame;
            mVertexBuffers = new HashMap<>();
            mLinePoints = new HashMap<>();
            Log.d("LineFuncData", "FullFrame = " + mFullFrame);
        }

        private String findKeyId(float aMove, float bAmplitude) {
            String keyId = aMove + "_" + bAmplitude;
            Log.d("LineFuncData", "keyId = " + keyId);
            return keyId;
        }

        public void putLinePoints(float move, float amplitude) {
            String keyId = findKeyId(move, amplitude);
            float[] tempPoints = createPositions(move, amplitude);
            mLinePoints.put(keyId, tempPoints);
            mVertexBuffers.put(keyId, asFloatBuffer(tempPoints));
        }

        public float[] getLinePoints(float move, float amplitude) {
            String keyId = findKeyId(move, amplitude);
            return mLinePoints.get(keyId);
        }

        public FloatBuffer getVertexBuffer(float move, float amplitude) {
            String keyId = findKeyId(move, amplitude);
            return mVertexBuffers.get(keyId);
        }

        public FloatBuffer getColorBuffer() {
            return mColorBuffer;
        }

        public ShortBuffer getIndexBuffer() {
            return mIndexBuffer;
        }

        public void createIndexBuffer(float move, float amplitude) {
            float[] lastFrame = mLinePoints.get(findKeyId(move, amplitude));
            short[] indexSrc = new short[lastFrame.length];
            for (short i = 0; i < lastFrame.length; i++) {
                indexSrc[i] = i;
            }
            mIndexBuffer = asShortBuffer(indexSrc);
        }

        public void createColorBuffer(float move, float amplitude) {
            float[] lastFrame = mLinePoints.get(findKeyId(move, amplitude));
            float[] colorSrc = new float[lastFrame.length * 4];
            for (int i = -1, j = 0; j < lastFrame.length; j++) {
                colorSrc[++i] = 0.0f / 255;
                colorSrc[++i] = 117.0f / 255;
                colorSrc[++i] = 117.0f / 255;
                colorSrc[++i] = 0.0f;
            }
            mColorBuffer = asFloatBuffer(colorSrc);
        }

        // 计算点线
        float[] createPositions(float move, float amplitude) {
            ArrayList<Float> data = new ArrayList<>(10);
            createSin(data, move, amplitude);
            float[] f = new float[data.size()];
            for (int i = 0; i < f.length; i++) {
                f[i] = data.get(i);
            }
            return f;
        }

        private void createSin(ArrayList<Float> data, float move, float amplitude) {
            Point rightPoint = new Point(0, 0);
            Point leftPoint = new Point(0, 0);
            for (float x = -0.8f; x <= 0.8f; x += 0.01) {
                float y = (float) (Math.sin(1.3 * Math.PI * x - move) / amplitude);
                // 左边 贝塞尔线
                if (x == -0.8f) {
                    rightPoint.x = x;
                    rightPoint.y = y;
                    createLeftBezier(move, amplitude, rightPoint, data);
                }
                // 中间
                if (x >= -0.6f && x <= 0.6f) {
                    data.add(x);
                    data.add(y);
                    data.add(0f);
                }
                // 右边 贝塞尔线
                leftPoint.x = x;
                leftPoint.y = y;
            }
            createRightBezier(move, amplitude, leftPoint, data);
        }

        private void createLeftBezier(float move, float amplitude, Point rightPoint, ArrayList<Float> data) {
            Point p1 = new Point(-1f, 0f);
            Point p2 = new Point(-0.8f, 0f);
            Point p3 = new Point(rightPoint.x, rightPoint.y);
            float x_ = rightPoint.x + 0.2f;
            float y_ = (float) (Math.sin(1.3 * Math.PI * x_ - move) / amplitude);
            Point p4 = new Point(x_, y_);

            for (double t = 0.0; t <= 1.0; t += 0.05) {
                Point tP = setBezier(p1, p2, p3, p4, t);
                data.add(tP.x);
                data.add(tP.y);
                data.add(0f);
            }
        }

        private void createRightBezier(float move, float amplitude, Point rightPoint, ArrayList<Float> data) {
            float x_ = rightPoint.x - 0.2f;
            float y_ = (float) (Math.sin(1.3 * Math.PI * x_ - move) / amplitude);
            Point p1 = new Point(x_, y_);
            Point p2 = new Point(rightPoint.x, rightPoint.y);
            Point p3 = new Point(0.8f, 0f);
            Point p4 = new Point(1f, 0f);

            for (double t = 0.0; t <= 1.0; t += 0.05) {
                Point tP = setBezier(p1, p2, p3, p4, t);
                data.add(tP.x);
                data.add(tP.y);
                data.add(0f);
            }
        }

        Point setBezier(Point p1, Point p2, Point p3, Point p4, double t) {
            Point p = new Point(0, 0);
            double a1 = (float) Math.pow((1 - t), 3);
            double a2 = (float) (Math.pow((1 - t), 2) * 3 * t);
            double a3 = (float) (3 * t * t * (1 - t));
            double a4 = (float) (t * t * t);
            p.x = (float) (a1 * p1.x + a2 * p2.x + a3 * p3.x + a4 * p4.x);
            p.y = (float) (a1 * p1.y + a2 * p2.y + a3 * p3.y + a4 * p4.y);
            return p;
        }
    }

}
