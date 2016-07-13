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
 * 第一种实现方式用 Handler mHandler 驱动,频率跟不上<br / >
 * 第二种DrawThread 比较合理,内存与CPU的消耗都OK<br / >
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

//       private final Handler mHandler = new Handler();
       private boolean mVisible;
//       private final Runnable drawer = new Runnable() {
//           public void run() {
//               drawFrame();
//           }
//       };


//       private void drawFrame() {
//
//           final SurfaceHolder holder = getSurfaceHolder();
//           Canvas c = null;
//           try {
//               c = holder.lockCanvas();
//               if (c != null) {
//                   wallpaperRender.onDraw(c);
//               }
//           } finally {
//               if (c != null) {
//                   holder.unlockCanvasAndPost(c);
//               }
//           }
//
//           postDraw();
//       }

       private void postDraw() {
//           mHandler.removeCallbacks(drawer);
           if (mVisible  && mThread == null) {
//               mHandler.postDelayed(drawer, 1 * 100);
               mThread = new DrawThread(getSurfaceHolder());
               mThread.start();
           }
           mThread.setRun(true);
       }


       @Override
       public void onDestroy() {
           super.onDestroy();
//           mHandler.removeCallbacks(drawer);
           mThread.interrupt();
           mThread = null;
       }

       @Override
       public void onVisibilityChanged(boolean visible) {
           mVisible = visible;
           if (visible) {
               postDraw();
           } else {
//               mHandler.removeCallbacks(drawer);
//               mThread.interrupt();
//               mThread = null;
               mThread.setRun(false);
           }
           Log.d(TAG,"onVisibilityChanged " + visible);
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

           Log.d(TAG,"onSurfaceChanged  mWidth="+mWidth+" ,  mHeight = "+mHeight);
           postDraw();
       }

       @Override
       public void onSurfaceDestroyed(SurfaceHolder holder) {
           super.onSurfaceDestroyed(holder);
           mVisible = false;
//           mHandler.removeCallbacks(drawer);
//           mThread.interrupt();
//           mThread = null;
           mThread.setRun(false);
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
//                           doDraw(canvas);  //这里做真正绘制的事情
                           if (canvas != null) {
                               wallpaperRender.onDraw(canvas);
                           }
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

   }

}
