package dev.whl.openglwidget.wave;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SpreadPoints {

    private static final SpreadPoints ourInstance = new SpreadPoints();
    float z = 0.0f;
    // 固定
    private HashMap<String, FloatBuffer> mColorBuffers;
    private HashMap<String, float[]> mColorSrcs;
    // 随参数变化
    private HashMap<String, float[]> mWavePoints;
    private Type mType = Type.LeftRight;

    public void setType(Type type) {
        this.mType = type;
    }

    public boolean isLR() {
        return mType == Type.LeftRight;
    }

    enum Type {
        LeftRight("lr"),
        UpDown("ud");

        String Tag;

        Type(String tag) {
            this.Tag = tag;
        }
    }

    public static SpreadPoints getInstance() {
        return ourInstance;
    }

    private SpreadPoints() {
        mWavePoints = new HashMap<>();
        mColorBuffers = new HashMap<>();
        mColorSrcs = new HashMap<>();
    }

    /**
     * 数据本地内存化
     */
    public FloatBuffer asFloatBuffer(float src[]) {
        // float 4个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(src.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = vbb.asFloatBuffer();
        buffer.put(src);
        buffer.position(0);
        return buffer;
    }

    public ShortBuffer asShortBuffer(short src[]) {
        // short 2个字节
        ByteBuffer ibb = ByteBuffer.allocateDirect(src.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = ibb.asShortBuffer();
        buffer.put(src);
        buffer.position(0);
        return buffer;
    }

    public void setColor(float red, float green, float blue, float alpha, String colorBufferKey) {
        try {
            mColorSrcs.put(colorBufferKey, new float[]{
                    red / 255f,
                    green / 255f,
                    blue / 255f,
                    alpha / 255f
            });
            createColorBuffer(colorBufferKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findKeyId(String tag, float value) {
        String keyId = tag + "_" + value;
        Log.d("Spread", "keyId = " + keyId);
        return keyId;
    }

    public void putLinePoints(float lr, float ud) {
        String keyId = "";
        float[] tempPoints = null;
        if (Type.LeftRight == mType) {
            keyId = findKeyId(mType.Tag, lr);
            tempPoints = createLRPositions(keyId, lr);
        } else {
            keyId = findKeyId(mType.Tag, ud);
            tempPoints = createUDPositions(keyId, ud);
        }
        mWavePoints.put(keyId, tempPoints);
    }

    private float[] createUDPositions(String keyId, float ud) {
        return new float[]{
                1, ud, 0,
                -1, ud, 0
        };
    }

    private float[] createLRPositions(String keyId, float lr) {
        return new float[]{
                lr, 0, 0,
                -lr, 0, 0
        };
    }

    public float[] getLinePoints(float lr, float ud) {
        String keyId = "";
        if (Type.LeftRight == mType) {
            keyId = findKeyId(mType.Tag, lr);
        } else {
            keyId = findKeyId(mType.Tag, ud);
        }
        return mWavePoints.get(keyId);
    }

    public FloatBuffer getColorBuffer(String colorBufferKey) {
        return mColorBuffers.get(colorBufferKey);
    }

    public void createColorBuffer(String colorBufferKey) {
        Set<String> keySet = mWavePoints.keySet();
        if (!keySet.isEmpty()) {
            Iterator<String> keyIt = keySet.iterator();
            if (keyIt.hasNext()) {
                String keyId = keyIt.next();
                float[] tempFrame = mWavePoints.get(keyId);
                if (tempFrame != null && tempFrame.length > 0) {
                    float[] colorSrc = new float[tempFrame.length * 4];
                    float[] cSrc = mColorSrcs.get(colorBufferKey);
                    for (int i = -1, j = 0; j < tempFrame.length; j++) {
                        colorSrc[++i] = cSrc[0];
                        colorSrc[++i] = cSrc[1];
                        colorSrc[++i] = cSrc[2];
                        colorSrc[++i] = cSrc[3];
                    }
                    mColorBuffers.put(colorBufferKey, asFloatBuffer(colorSrc));
                }
            }
        }
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
                data.add(z);
            }
            // 右边 贝塞尔线
            leftPoint.x = x;
            leftPoint.y = y;
        }
        createRightBezier(move, amplitude, leftPoint, data);
        data.add(1.0f);
        data.add(0.0f);
        data.add(z);
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
            data.add(z);
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
            data.add(z);
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

    class Point {
        float x;
        float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
