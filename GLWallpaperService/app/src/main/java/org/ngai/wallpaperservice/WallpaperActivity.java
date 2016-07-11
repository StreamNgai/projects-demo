package org.ngai.wallpaperservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
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

        SimpleView simpleView = new SimpleView(this);
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

            postDelayed(mRunnable, 20);//  直接影响CPU
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

//
//
//    private Bitmap adjustSimpleSize(Context context,int resid){
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        // 不读取像素数组到内存中，仅读取图片的信息
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(context.getResources(),resid,opts);
//        // 从Options中获取图片的分辨率
//        int imageHeight = opts.outHeight;
//        int imageWidth = opts.outWidth;
//
//        // 通过实际屏幕大小计算 最合适的inSampleSize
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        int windowHeight = wm.getDefaultDisplay().getHeight();
//        int windowWidth = wm.getDefaultDisplay().getWidth();
//        // 计算采样率
//        int scaleX = imageWidth / windowWidth;
//        int scaleY = imageHeight / windowHeight;
//        int scale = 1;
//        // 采样率依照最大的方向为准
//        if (scaleX > scaleY && scaleY >= 1) {
//            scale = scaleX;
//        }
//        if (scaleX < scaleY && scaleX >= 1) {
//            scale = scaleY;
//        }
//        // false表示读取图片像素数组到内存中，依照设定的采样率
//        opts.inJustDecodeBounds = false;
//        // 采样率
//        opts.inSampleSize = 24;
//
//        return BitmapFactory.decodeResource(context.getResources(),resid,opts);
//    }

}
