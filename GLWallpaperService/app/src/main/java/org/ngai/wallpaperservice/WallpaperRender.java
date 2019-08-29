package org.ngai.wallpaperservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import org.ngai.glwallpaperservice.R;

import java.util.Random;

/**
 * Created by Ngai on 2016/7/8.
 */
public class WallpaperRender {

    private static final String TAG = "WallpaperRender.TAG";

    private int mWidth = 0;
    private int mHeight = 0;

    private Bitmap mBasicBg; // 基础颜色
    private Rect mBasicBgRect;
    private Bitmap mTexture; // 纹理变换
    private Rect mTextureRect;

    private float mDegrees = -0.1f; //
    private float mRate = 0.1f; // 速率 0.1
    boolean degreesRotary = true;

    private Paint mPaint;
//    private Paint mGradientPaint;
    private float alpha = 0; // 0 ~ 127

    private RectF mRenderAreaRectF;
    private RectF mRenderAreaRectFChange2;

    private int mCenterX;
    private int mCenterY;

    public WallpaperRender(Context context) {

        int background = R.drawable.wallpage_bg;
        int texture_0 = R.drawable.wallpage_texture;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 3;
        mBasicBg = BitmapFactory.decodeResource(context.getResources(), background, bfo);
        mBasicBgRect = new Rect(0, 0, mBasicBg.getWidth(), mBasicBg.getHeight());

        bfo.inSampleSize = 2;
        mTexture = BitmapFactory.decodeResource(context.getResources(), texture_0, bfo);
        mTextureRect = new Rect(0, 0, mTexture.getWidth(), mTexture.getHeight());

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);   //空心

    }

    // 1 ~ 127 // 最大透明 50 %
    private Paint setAlpha(int alpha) {
        if (alpha > 60)
            alpha = 60;
        if (alpha < 0)
            alpha = 0;
        mPaint.setAlpha(alpha);
        return mPaint;
    }

    public void setRenderArea(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;

        mCenterX = width / 2;
        mCenterY = height / 2;

        tDx = -mWidth / 2 + 150;

        mRenderAreaRectF = new RectF(0, 0, width + 100, height+ 100);
        mRenderAreaRectFChange2  = new RectF(0, 0, width + (width / 2)+100, height);

//        mGradientPaint = new Paint();

        // 虽然降低了内存消耗，但分出现分层
//        LinearGradient gradient = new LinearGradient(mWidth, 0, mWidth, mHeight, Color.parseColor("#012e50"), Color.parseColor("#07182a"),
//                Shader.TileMode.REPEAT);
//        mGradientPaint.setShader(gradient);
//        mGradientPaint.setAntiAlias(true);
//        mGradientPaint.setStyle(Paint.Style.FILL);

    }

    public void onDraw(Canvas canvas) {
        canvas.save();

        // 背景
        canvas.drawBitmap(mBasicBg, mBasicBgRect, mRenderAreaRectF, null);
//        canvas.drawRect(0, 0, mWidth, mHeight, mGradientPaint);// 这方式有效减少内存消耗

        // 一直循环
        doCycle();
        doAnimation(canvas, mDegrees, alpha);
        Log.d(TAG, "degrees = " + mDegrees + " , alpha = " + alpha);
        canvas.restore();
    }

    private float mDegress3 = -0.07f;
    private float mDegress2 = -0.07f;

    private void doCycle() {
        if (mDegrees >= 360)
            mDegrees = -0.09f;

        mDegrees += mRate;

        if (mDegress3 >= 130) {
            mDegress3 = -0.07f;
        }

        mDegress3 += 0.07f;

        if (alpha <= 60 && degreesRotary) {
            if (mDegrees < 86) {
                alpha = (float) (mDegrees * 6); // mDegrees = 86 时，alpha = 60
            } else {
                alpha += 0.2;
            }

        } else {
            if (mDegrees > 130 && mDegrees <= 209) { // 纹理1 消失
                alpha -= 0.2;
                degreesRotary = false;
            } else {
                degreesRotary = true;
            }
        }

    }

    private float tDy = 0.1f; // 纹路2，当< 0时，开始旋转
    private float tDx;
    private float mAlpha3 = 60;

    private void doAnimation(Canvas canvas, float degrees, float alpha) {

        canvas.restore();
        canvas.save();
        canvas.rotate(degrees, mCenterX, mCenterY);//  旋转
        canvas.drawBitmap(mTexture, mTextureRect, mRenderAreaRectF, setAlpha(Math.round(alpha)));

//      2
        if (mDegrees > 130) {
            canvas.restore();
            canvas.save();
            tDy =  mHeight - (( mHeight / 300) * mDegrees) - 65;
            mDegress2 += 0.05f;
            canvas.rotate(-mDegress2,tDx / 2, tDy / 2);
            canvas.translate(tDx, tDy);
            canvas.drawBitmap(mTexture, mTextureRect, mRenderAreaRectFChange2, setAlpha(60));
        }else {
            mDegress2 = 0;
        }

//      3
        canvas.restore();
        canvas.save();
        canvas.translate(mWidth / 2 - 100, -mHeight);
        canvas.rotate(mDegress3);

        if (mDegress3 < 80 && mDegress3 > 30) {
            mAlpha3 -= 0.3;
            if (mAlpha3 <= 0)
                mAlpha3 = 0;
        } else if (mDegrees > 80) {
            mAlpha3 += 0.4;
            if (mAlpha3 >= 60)
                mAlpha3 = 60;
        }
        canvas.drawBitmap(mTexture, mTextureRect, mRenderAreaRectF, setAlpha((int) mAlpha3));

    }


}
