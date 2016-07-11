package org.ngai.wallpaperservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import org.ngai.glwallpaperservice.R;

import java.util.Random;

/**
 * Created by Ngai on 2016/7/8.
 * 淡白纹理，根据赛贝尔曲线变化轨迹。<br />
 */
public class WallpaperRender {

    private static final String TAG = "WallpaperRender.TAG";

    private int mWidth = 0;
    private int mHeight = 0;

    private Bitmap mBasicBg; // 基础颜色
    private Bitmap mTexture_0; // 纹理变换
    private Bitmap mTexture_1; // 纹理变换
    private Bitmap mTexture_2; // 纹理变换

    private Rect mTexturebgRect;
    private Rect mTextureRect_0;
    private Rect mTextureRect_1;

    private float mDegrees = 0f; //
    private float mRate = 0.1f; // 速率
    boolean degreesRotary = true;

    private RectF mRenderAreaRectF;
    private RectF mRenderAreaRectFChange;

    private int mCenterX;
    private int mCenterY;

    private Paint mPaint;
    private Random random = null;
    private int alpha = 255;
    boolean alphaRotary = true;

    public WallpaperRender(Context context) {

        int background = R.drawable.wallpage_bg;
        int texture_0 = R.drawable.wallpage_texture_0;
        int texture_1 = R.drawable.wallpage_texture_1;
        int texture_2 = R.drawable.wallpage_texture_2;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 6;
        mBasicBg = BitmapFactory.decodeResource(context.getResources(), background, bfo);
        mTexturebgRect = new Rect(0, 0, mBasicBg.getWidth(), mBasicBg.getHeight());

        bfo.inSampleSize = 2;
        mTexture_0 = BitmapFactory.decodeResource(context.getResources(), texture_0, bfo);
        mTextureRect_0 = new Rect(0, 0, mTexture_0.getWidth(), mTexture_0.getHeight());

        mTexture_1 = BitmapFactory.decodeResource(context.getResources(), texture_1, bfo);
        mTextureRect_1 = new Rect(0, 0, mTexture_1.getWidth(), mTexture_1.getHeight());

        mTexture_2 = BitmapFactory.decodeResource(context.getResources(), texture_2, bfo);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);   //空心

    }

    // 1 ~ 100
    private Paint setAlpha(int alpha) {
        if (alpha > 255)
            alpha = 255;
        if (alpha < 0)
            alpha = 0;
        mPaint.setAlpha(alpha);
        return mPaint;
    }

    public void setRenderArea(int width, int height) {

        this.mWidth = width;
        this.mHeight = height;

        mRenderAreaRectF = new RectF(0, 0, width, height);
        mRenderAreaRectFChange = new RectF(0, 0, width, height);

        mCenterX = width / 2;
        mCenterY = height / 2;

        random = new Random();
    }

    public void onDraw(Canvas canvas) {
        canvas.save();

        // 背景
        canvas.drawBitmap(mBasicBg, mTexturebgRect, mRenderAreaRectF, null);

//        if (alphaRotary) {
//            alpha += 5;
//            if (alpha >= 255)
//                alphaRotary = false;
//        } else {
//            alpha -= 5;
//            if (alpha <= 2)
//                alphaRotary = true;
//        }


        if (degreesRotary) {
            mDegrees = mDegrees + mRate;
            alpha -= 1;
            if (mDegrees >= 20)
                degreesRotary = false;
        } else {
            mDegrees = mDegrees - mRate;
            alpha += 1;
            if (mDegrees <= 0)
                degreesRotary = true;
        }

//        doAnimation(canvas,++mDegrees,alpha);
//        doAnimation2(canvas, mDegrees, alpha);
        doAnimation3(canvas, mDegrees, alpha);
        Log.d(TAG, "" + mDegrees);
        canvas.restore();
    }

    private void doAnimation3(Canvas canvas, float degrees, int alpha) {
        int tAlpha = alpha;
        float degr = 0;
        mRenderAreaRectFChange.set(-(degrees * 20), -(degrees * 20), mWidth + (degrees * 20), mHeight + (degrees * 20));//  放大

        canvas.restore();
        canvas.save();
        degr = degrees - 20;
        canvas.rotate(degr, mCenterX, mCenterY);//  旋转
        canvas.drawBitmap(mTexture_0, mTextureRect_0, mRenderAreaRectFChange, setAlpha(90 - (int) (mDegrees * 4)));

//        canvas.restore();
//        canvas.save();
//        canvas.translate(-10, -160);
//        canvas.drawBitmap(mTexture_1, mTextureRect_1, new RectF(0,0,mWidth / 2 + 30,mHeight/2+ 30), setAlpha(255 - alpha));


        canvas.restore();
        canvas.save();
        degr = degrees - 30;
        canvas.rotate(degr, mCenterX, mCenterY);
        tAlpha = (255 - alpha);
        canvas.drawBitmap(mTexture_2, mTextureRect_1, mRenderAreaRectFChange, setAlpha((int) (mDegrees * 4)));//透明度最大为90


    }

    private void doAnimation2(Canvas canvas, float degrees, int alpha) {

        //  放大
        mRenderAreaRectFChange.set(-(degrees * 20), -(degrees * 20), mWidth + (degrees * 20), mHeight + (degrees * 20));
        //  旋转
        canvas.rotate(degrees, mCenterX, mCenterY);
        canvas.drawBitmap(mTexture_0, mTextureRect_0, mRenderAreaRectFChange, setAlpha(127));

//        canvas.rotate(degrees, mCenterX, mCenterY);
//        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(127));
//        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(127));

        canvas.restore();
        canvas.save();
        canvas.translate(-mCenterX + 60, -mCenterY + 60);
        canvas.rotate(10);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(255 - alpha));

    }

    private void doAnimation(Canvas canvas, float degrees, int alpha) {

        canvas.rotate(degrees, mCenterX / 2, mCenterY);
        canvas.drawBitmap(mTexture_0, mTextureRect_0, mRenderAreaRectF, setAlpha(alpha));

        canvas.rotate(degrees, mCenterX / 2 - 10, mCenterY / 2 - 10);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(alpha));
        canvas.rotate(degrees, mCenterX / 2, mCenterY / 2);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(alpha));

        canvas.restore();
        canvas.save();
        canvas.translate(mCenterX / 2, mCenterY / 2);
        canvas.rotate(30, mCenterX, mCenterY);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(alpha));

        canvas.restore();
        canvas.save();
        canvas.translate(-mCenterX - 30, -mCenterY - 30);
        canvas.rotate(30, mCenterX, mCenterY);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(255 - alpha));

        canvas.restore();
        canvas.save();
        canvas.translate(0, mCenterY / 2);
        canvas.rotate(30, mCenterX, mCenterY);
        canvas.drawBitmap(mTexture_1, mTextureRect_1, mRenderAreaRectF, setAlpha(alpha));
    }

    private int randomX() {
        int max = (mCenterX / 4) * 3;
        int min = -mCenterX;
        return random.nextInt(max) % (max - min + 1) + min;
    }

    private int randomY() {
        int max = (mCenterY / 4) * 3;
        int min = -mCenterY;
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
