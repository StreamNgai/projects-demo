package dev.ngai.fantasticservice.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import dev.ngai.fantasticservice.R;
import dev.ngai.fantasticservice.TagDispatchKey;
import dev.ngai.fantasticservice.tasks.Mm131Task;
import dev.ngai.fantasticservice.tasks.TaskScheduler;

public class MainActivity extends AppCompatActivity {

    private String TAG = "FantasticServic";

    private RelativeLayout mainView;
    private RelativeLayout mainView2;
    private Button btnView;
    private Button btnView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dispatchTest();

        fantasticDataCrawl();

        guardAgainstTheft();
    }

    private void guardAgainstTheft() {

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String url = "http://img1.mm131.me/pic/3627/0.jpg";
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "http://www.mm131.com/xinggan/3627.html")
                .build());
        Glide.with(this).load(glideUrl).into(imageView);

    }

    private void fantasticDataCrawl() {
        TaskScheduler.execute(new Mm131Task());
    }

    private void dispatchTest() {
        mainView = (RelativeLayout) findViewById(R.id.mainView1);
        mainView2 = (RelativeLayout) findViewById(R.id.mainView2);
        btnView = (Button) findViewById(R.id.btn1);
        btnView2 = (Button) findViewById(R.id.btn2);

        btnView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                Log.d(TagDispatchKey.KeyDispatch, "setOnKeyListener ： View(" + view.getTag()
                        + ") KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
                );
                return false;
            }
        });


        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TagDispatchKey.KeyDispatch, "setOnClickListener ： View(" + view.getTag()
                        + ") ");
            }
        });

        btnView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TagDispatchKey.KeyDispatch, "setOnLongClickListener ： View(" + view.getTag()
                        + ") ");
                return false;
            }
        });

        btnView2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                Log.d(TagDispatchKey.KeyDispatch, "setOnKeyListener ： View(" + view.getTag()
                        + ") KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
                );
                return false;
            }
        });

        btnView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d(TagDispatchKey.TouchDispatch, "setOnTouchListener ： View(" + view.getTag()
                        + ") KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
                );
                return false;
            }
        });
        btnView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d(TagDispatchKey.TouchDispatch, "setOnTouchListener ： View(" + view.getTag()
                        + ") KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
                );
                return false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchKeyEvent ："
                + " KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchKeyShortcutEvent ："
                + " KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchTouchEvent ："
                + " MotionEvent = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchTrackballEvent ："
                + " MotionEvent = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.dispatchTrackballEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchGenericMotionEvent ："
                + " MotionEvent = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity dispatchPopulateAccessibilityEvent ："
                + " AccessibilityEvent = " + event.getAction()
        );
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity onKeyDown ："
                + " KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity onKeyUp ："
                + " KeyEvent = " + (event.getAction() == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : "ACTION_UP")
        );
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity onWindowFocusChanged ："
                + " hasFocus = " + hasFocus
        );
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean hasWindowFocus() {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity hasWindowFocus ："
        );
        return super.hasWindowFocus();
    }

    @Nullable
    @Override
    public View getCurrentFocus() {
        Log.d(TagDispatchKey.Dispatch, "-->> MainActivity getCurrentFocus ："
        );
        return super.getCurrentFocus();
    }

}
