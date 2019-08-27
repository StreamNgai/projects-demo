package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import dev.weihl.opengl.shape.helper.DigitHelper;
import dev.weihl.opengl.shape.helper.VaryHelper;

public class WarpTwo extends WarpBasic {

    private final String TAG = "WarpTwo";
    DigitHelper.RoundDigit aDigit;
    DigitHelper.RoundDigit bDigit;
    float aRadio = 0.9f;
    float bRadio = 0.1f;

    public WarpTwo(View view) {
        super(view);
        aDigit = DigitHelper.createRoundDigit(-10.0f, 10.0f, 0.02f);
        bDigit = DigitHelper.createRoundDigit(-10.0f, 10.0f, 0.02f);
    }

    // line 2 ; y = x + 0.5f
    ArrayList<Float> createLineTwo() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) (Math.sin(x) + Math.sin(x + aRadio)));
            data.add(height);
        }
        return data;
    }

    // line 1 ; y = x - 0.5f
    ArrayList<Float> createLineOne() {
        ArrayList<Float> data = new ArrayList<>();
        for (float x = -1; x <= 1; x += 0.01) {
            data.add(x);
            data.add((float) Math.sin(x + aRadio));
            data.add(height);
        }
        return data;
    }

    @Override
    void createInit() {
        super.createInit();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        aRadio = aDigit.get(); // X
        bRadio = bDigit.get();
        refreshPositions();
    }
}
