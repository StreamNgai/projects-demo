package org.ngai.wallpaperservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import org.ngai.glwallpaperservice.R;

public class WallpaperActivity extends AppCompatActivity {

    //    Bitmap mBgBitmap;
//    Rect mBgBitmapRect;
//    RectF mBgRectF;
//    Bitmap mTextureBitmap;
//    Rect mTextureBitmapRect;
//    Bitmap mTextureallBitmap;
//    Paint vPaint;
    WallpaperRender wallpaperRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        int windowHeight = wm.getDefaultDisplay().getHeight() + 50;
//        int windowWidth = wm.getDefaultDisplay().getWidth();

//        // 建立Paint 物件
//        vPaint = new Paint();
//        vPaint .setStyle( Paint.Style.STROKE );   //空心
//
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = false;
//        opts.inSampleSize = 6;
//
//        mBgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wallpage_bg,opts);
//
//        mBgBitmapRect = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
//
//          opts.inSampleSize = 2;
//          mTextureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpage_texture,opts);
//          mTextureBitmapRect = new Rect(0, 0, mTextureBitmap.getWidth(), mTextureBitmap.getHeight());
//        mTextureallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpage_texture_all,opts);
//
//
//          mBgRectF = new RectF(0, 0, windowWidth, windowHeight);


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int windowHeight = wm.getDefaultDisplay().getHeight() + 50;
        int windowWidth = wm.getDefaultDisplay().getWidth();
        wallpaperRender = new WallpaperRender(this);
        wallpaperRender.setRenderArea(windowWidth, windowHeight);

//        SimpleView simpleView = new SimpleView(this);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        simpleView.setLayoutParams(layoutParams);
//        layout.addView(simpleView);

        PathView simpleView = new PathView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        simpleView.setLayoutParams(layoutParams);
        layout.addView(simpleView);

    }

//    private Paint createPaintAlpha(int alpha){
//        vPaint .setAlpha( alpha );
//        return vPaint;
//    }

    class SimpleView extends View {

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            wallpaperRender.onDraw(canvas);

            postDelayed(mRunnable, 35);//  直接影响CPU 100
        }

        public SimpleView(Context context) {
            super(context);
        }

        public SimpleView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }

    class PathView extends SurfaceView {
        private final Object mSurfaceLock = new Object();
        private DrawThread mThread;
        private Paint mPaint;
        private Path mPath;

        private float x = -2;
        // 用线程去驱动 Canvas 来减少对 UI 线程的负担
        private void doDraw(Canvas canvas) {

            canvas.drawColor(Color.parseColor("#F1F1F1"));

            canvas.drawPath(mPath,mPaint);

        }

        private float culY(float x) {
            return (float) (0.5 * (Math.pow(4,2.5) / (4+Math.pow(x,2)))* Math.sin((0.75*x-0.5)*Math.PI));
        }


        private class DrawThread extends Thread {
            private static final long SLEEP_TIME = 1 * 1000;
            private SurfaceHolder mHolder;
            private boolean mIsRun = false;

            public DrawThread(SurfaceHolder holder) {
                mHolder = holder;
            }

            @Override
            public void run() {
                while(true) {
                    synchronized (mSurfaceLock) {
                        if (!mIsRun) {
                            return;
                        }
                        Canvas canvas = mHolder.lockCanvas();
                        if (canvas != null) {

                            if(mPaint == null){
                                mPaint = new Paint();
                                mPaint.setStyle(Paint.Style.STROKE);
                                mPaint.setColor(Color.BLUE);
                                mPath = new Path();
                                mPath.moveTo(0,300);
                                while (x < 2){
                                    x += 0.1;
                                    float y = culY(x);
                                    mPath.lineTo(x * 100+100,y* 100+300);
                                    Log.d("DrawThread","x = "+x+", y = "+y);
                                }

                            }

                            doDraw(canvas);  //这里做真正绘制的事情
                            mHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void setRun(boolean isRun) {
                this.mIsRun = isRun;
            }
        }

        public PathView(Context context) {
            this(context,null);
        }
        public PathView(Context context, AttributeSet attrs) {
            this(context, attrs,0);

        }

        public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mThread = new DrawThread(getHolder());
            mThread.setRun(true);
            mThread.start();
        }

    }

}
