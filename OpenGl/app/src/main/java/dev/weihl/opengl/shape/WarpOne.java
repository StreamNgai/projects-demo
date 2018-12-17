package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;

public class WarpOne extends WarpBasic {

    private final String TAG = "WarpOne";

    float aRadio = 0.9f;
    float bRadio = 0.3f;
    float cRadio = 0.3f;
    DigitHelper.RoundDigit aDigit;
    DigitHelper.RoundDigit bDigit;
    DigitHelper.RoundDigit cDigit;

    public WarpOne(View view) {
        super(view);
        aDigit = DigitHelper.createRoundDigit(-1.0f, 1.0f, 0.01f);
        bDigit = DigitHelper.createRoundDigit(-1.0f, 1.0f, 0.01f);
        cDigit = DigitHelper.createRoundDigit(-1.0f, 3.0f, 0.01f);
    }

     ArrayList<Float> createLineTwo() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) (Math.sin(x - aRadio)));
            data.add(height);
        }
        return data;
    }

     ArrayList<Float> createLineOne() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) (aRadio * Math.pow(x, 2) + bRadio * x + cRadio));
            data.add(height);
        }
        return data;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        aRadio = aDigit.get(); // X
        bRadio = bDigit.get(); // X
        cRadio = cDigit.get(); // X
        refreshPositions();
    }
}
