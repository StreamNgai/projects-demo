package dev.weihl.opengl.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Random;

import dev.weihl.opengl.R;
import dev.whl.openglwidget.wave.VolumeWaveView;

public class MainActivity extends AppCompatActivity {

    VolumeWaveView glSurfaceView;
    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl);

        mHandler = new Handler();
        glSurfaceView = findViewById(R.id.volumeWaveView);
        glSurfaceView.setCallBack(new VolumeWaveView.CallBack() {
            @Override
            public void onClickTape() {
                mHandler.postDelayed(volRunnable, 2000);
            }
        });
    }

    Runnable volRunnable = new Runnable() {
        @Override
        public void run() {
            DecimalFormat df = new DecimalFormat("0.01");
            float lineO = new Random().nextFloat() * 6f;
            float lineT = new Random().nextFloat() * 6f;
            Log.d("VolumeWaveView", "lineO = " + lineO + " ; lineT = " + lineT);
            glSurfaceView.setAmplitude(Float.valueOf(df.format(lineO)), Float.valueOf(df.format(lineT)));
            mHandler.postDelayed(loadRunnable, 3000);
        }
    };

    Runnable loadRunnable = new Runnable() {
        @Override
        public void run() {
            glSurfaceView.doLoadingAnim();
            mHandler.postDelayed(endRunnable, 3000);
        }
    };

    Runnable endRunnable = new Runnable() {
        @Override
        public void run() {
            glSurfaceView.doEndAnim();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }
}
