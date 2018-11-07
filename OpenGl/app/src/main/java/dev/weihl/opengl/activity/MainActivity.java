package dev.weihl.opengl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dev.weihl.opengl.GLView;
import dev.weihl.opengl.R;

public class MainActivity extends AppCompatActivity {

    GLView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl);

        glSurfaceView = findViewById(R.id.glview);

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
