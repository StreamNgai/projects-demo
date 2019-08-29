package org.ngai.wallpaperservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import org.ngai.glwallpaperservice.R;


/**
 * Created by Ngai on 2016/7/6.
 */
public class TextureWallpaper extends WallpaperService {

    private static final String TAG = "TextureWallpaper";
    private int mWidth, mHeight, maxWidth;
    public static final int SCENE_WIDTH = 150;

    WallpaperRender wallpaperRender;

    @Override
    public Engine onCreateEngine() {
        return new TextureEngine();
    }

    class TextureEngine extends Engine{

        private final Object mSurfaceLock = new Object();
        private DrawThread mThread;
        private boolean mVisible;

        private void postDraw() {
            if (mVisible  && mThread == null) {
                mThread = new DrawThread(getSurfaceHolder());
                mThread.start();
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
//           mThread.interrupt();
//           mThread = null;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                postDraw();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            maxWidth = Math.max(width, height) * SCENE_WIDTH / 100;
            mWidth = width;
            mHeight = height;

            if(wallpaperRender == null){
                wallpaperRender = new WallpaperRender(getApplicationContext());
                wallpaperRender.setRenderArea(mWidth, mHeight);
            }
            postDraw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            if(mThread != null){
                mThread.interrupt();
                mThread = null;
            }

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            postDraw();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(false); // 暂时不考虑触屏事件
        }

        ///*************
        private class DrawThread extends Thread {
            private static final long SLEEP_TIME = 1 * 32;
            private SurfaceHolder mHolder;

            public DrawThread(SurfaceHolder holder) {
                mHolder = holder;
            }

            @Override
            public void run() {
                while(true) {
                    synchronized (mSurfaceLock) {
                        Log.d(TAG,""+mVisible);
                        Canvas canvas = mHolder.lockCanvas();
                        if (canvas != null) {
                            wallpaperRender.onDraw(canvas);
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
        }

    }
}
