package org.ngai.wallpaperservice;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;


/**
 * Created by Ngai on 2016/7/6.
 */
public class SimpleTextureWallpaper extends WallpaperService {

    private static final String TAG = "TextureWallpaper";
    private int mWidth, mHeight, maxWidth;
    public static final int SCENE_WIDTH = 150;

    private WallpaperRender wallpaperRender;

    @Override
    public Engine onCreateEngine() {
        return new TextureEngine();
    }

    class TextureEngine extends Engine {

        private final Handler mHandler = new Handler();
        private boolean mVisible;
        private final Runnable drawer = new Runnable() {
            public void run() {
                drawFrame();
            }
        };


       private void drawFrame() {

           final SurfaceHolder holder = getSurfaceHolder();
           Canvas c = null;
           try {
               c = holder.lockCanvas();
               if (c != null) {
                   wallpaperRender.onDraw(c);
               }
           } finally {
               if (c != null) {
                   holder.unlockCanvasAndPost(c);
               }
           }

           postDraw();
       }

        private void postDraw() {
           mHandler.removeCallbacks(drawer);
            if (mVisible) {
               mHandler.postDelayed(drawer, 1 * 35);
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
           mHandler.removeCallbacks(drawer);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                postDraw();
            } else {
               mHandler.removeCallbacks(drawer);
            }
            Log.d(TAG, "onVisibilityChanged " + visible);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            maxWidth = Math.max(width, height) * SCENE_WIDTH / 100;
            mWidth = width;
            mHeight = height;

            if (wallpaperRender == null) {
                wallpaperRender = new WallpaperRender(getApplicationContext());
                wallpaperRender.setRenderArea(mWidth, mHeight);
            }

            Log.d(TAG, "onSurfaceChanged  mWidth=" + mWidth + " ,  mHeight = " + mHeight);
            postDraw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
           mHandler.removeCallbacks(drawer);
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
    }

}
