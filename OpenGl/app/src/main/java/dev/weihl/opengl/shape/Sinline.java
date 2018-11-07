package dev.weihl.opengl.shape;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Sinline extends Oval {

  //  https://www.desmos.com/calculator/kihjluohq9
    float aRadio = 0.9f;
    float bRadio = 0.3f;

    float mCircleX = 0.0f;
    float mCircleY = 0.0f;
    Helper.RoundDigit aDigit;
    Helper.RoundDigit bDigit;
    Helper.RoundDigit mOvalDigit;

    public Sinline(View view) {
        super(view);
        aDigit = Helper.createRoundDigit(-0.9f, 0.9f, 0.001f);
        bDigit = Helper.createRoundDigit(-0.9f, 0.9f, 0.001f);

        mOvalDigit = Helper.createRoundDigit(0.0f, 360.0f, 0.001f);
    }

    // 三角点
    protected float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(mCircleX);             //设置圆心坐标
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

//        float angD = mOvalDigit.get();
//        mCircleX = (float) Math.cos(angD);
//        mCircleY = (float) Math.sin(angD);
//        Log.d("Sinline", "aRadio = " + aRadio + " ; bRadio = " + bRadio + " ; OvalDigit = " + angD);
        refreshPositions();
    }

}
