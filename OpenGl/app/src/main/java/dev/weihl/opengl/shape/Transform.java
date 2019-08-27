package dev.weihl.opengl.shape;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;

public class Transform extends Oval {

    //  https://www.desmos.com/calculator/kihjluohq9
    float aRadio = 0.9f;
    float bRadio = 0.3f;

    float mCircleX = 0.0f;
    float mCircleY = 0.0f;
    DigitHelper.RoundDigit aDigit;
    DigitHelper.RoundDigit bDigit;
    DigitHelper.SquareRoundDigit mOvalDigit;

    public Transform(View view) {
        super(view);
        aDigit = DigitHelper.createRoundDigit(-0.9f, 0.9f, 0.001f);
        bDigit = DigitHelper.createRoundDigit(-0.9f, 0.9f, 0.001f);

        mOvalDigit = DigitHelper.createSquareRoundDigit(-1.0f, -1.0f, 1.0f, 1.0f, 0.001f, 0.003f);
    }

    // 三角点   https://www.desmos.com/calculator/kihjluohq9
    protected float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(mCircleX);//设置圆心坐标
        data.add(mCircleY);
        data.add(height);
        float angDegSpan = 0.05f;
        for (float x = -1; x < 1; x += angDegSpan) {
            data.add((float) (aRadio * Math.cos((3 * Math.PI * x) / bRadio)));
            x += angDegSpan;
            data.add((float) (aRadio * Math.cos(3 * (Math.PI * x) / bRadio)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        aRadio = aDigit.get(); // X
        bRadio = bDigit.get(); // X

        float[] angD = mOvalDigit.get();
        mCircleX = angD[0];
        mCircleY = angD[1];
        Log.d("onDrawFrame", "mCircleX = " + mCircleX + " ; mCircleY = " + mCircleY);
        refreshPositions();
    }

}
