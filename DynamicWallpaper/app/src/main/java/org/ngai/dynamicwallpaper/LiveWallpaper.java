package org.ngai.dynamicwallpaper;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

public class LiveWallpaper extends WallpaperService {
    private static final String TAG = "DynamicWallpaper";

    // 记录用户触碰点的位图
    private Bitmap bmp;

    // 实现WallpaperService必须实现的抽象方法
    @Override
    public Engine onCreateEngine() {
        // 加载将会在触碰点显示的图片
        bmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        // 返回自定义的Engine(定制动态壁纸的内容是通过定制Engine实现的)

        return new MyEngine();
    }

    class MyEngine extends Engine {
        // 记录程序界面是否可见
        private boolean mVisible;
        // 记录当前用户动作事件的发生位置
        private float mTouchX = -1;
        private float mTouchY = -1;
        // 记录当前需要绘制的矩形的数量
        private int count = 1;
        // 记录绘制第一个矩形所需坐标变换的X，Y坐标的偏移
        private int originX = 50, originY = 50;
        // 定义画笔
        private Paint mPaint = new Paint();
        // 定义一个Handler
        Handler mHandler = new Handler();
        // 定义一个周期性执行的任务
        private final Runnable drawTarget = new Runnable() {
            public void run() {
                drawFrame();
            }
        };

        private GLSurfaceView mGLSurfaceView;

        public void onCreate(android.view.SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            // 初始化画笔
            mPaint.setARGB(76, 0, 0, 255);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            // 设置处理触摸事件
            setTouchEventsEnabled(true);
            Log.d(TAG,"Engine . onCreate  ");

        }

        ;

        @Override
        public void onDestroy() {
            super.onDestroy();
            // 删除回调
            mHandler.removeCallbacks(drawTarget);
            Log.d(TAG,"Engine . onDestroy  ");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            // 当界面可见的时候,执行drawFrame方法
            if (visible) {
                // 动态的绘制图形
                drawFrame();
            } else {
                // 如果界面不可见,删除回调
                mHandler.removeCallbacks(drawTarget);
            }
            Log.d(TAG,"Engine . onVisibilityChanged = "+visible);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                                     float xOffsetStep, float yOffsetStep, int xPixelOffset,
                                     int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
                    xPixelOffset, yPixelOffset);
            drawFrame();

            Log.d(TAG,"Engine . onOffsetsChanged  ");
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            // 如果检测到滑动操作
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);

            Log.d(TAG,"Engine . onTouchEvent  ");
        }

        // 定义绘制图形的工具方法
        private void drawFrame() {
            // 获取该壁纸的SurfaceHolder
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                // 对画布加锁
                c = holder.lockCanvas();
                if (c != null) {
                    // 绘制背景色
                    c.drawColor(Color.WHITE);
                    // 在触碰点绘制心形
                    drawTouchPoint(c);
                    // 设置画笔的透明度
                    mPaint.setAlpha(76);
                    c.translate(originX, originY);
                    // 采用循环绘制count个矩形
                    for (int i = 0; i < count; i++) {
                        c.translate(80, 0);
                        c.scale(0.95f, 0.95f);
                        c.rotate(20f);
                        c.drawRect(0, 0, 150, 75, mPaint);
                    }
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }

            }

            mHandler.removeCallbacks(drawTarget);
            // 调度下一次重绘
            if (mVisible) {
                count++;
                if (count >= 50) {
                    Random rand = new Random();
                    count = 1;
                    originX += (rand.nextInt(60) - 30);
                    originY += (rand.nextInt(60) - 30);
                    try {
                        Thread.sleep(16);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 指定0.1秒后重新执行mDrawCube一次
                mHandler.postDelayed(drawTarget, 10);
            }

//            Log.d(TAG,"Engine . drawFrame  ");
        }

        // 在屏幕触碰点绘制圆圈
        private void drawTouchPoint(Canvas c) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                // 设置画笔的透明度
                mPaint.setAlpha(255);
                c.drawBitmap(bmp, mTouchX, mTouchY, mPaint);
            }

//            Log.d(TAG,"Engine . drawTouchPoint  ");
        }
    }

}
