package org.ngai.glwallpaperservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        SimpleView simpleView = new SimpleView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        simpleView.setLayoutParams(layoutParams);
        layout.addView(simpleView);

    }

    class SimpleView extends View {

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpage_bg);
            Rect mBgBitmapRect = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());

            Bitmap mTextureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpage_texture_0);
            Rect mTextureBitmapRect = new Rect(0, 0, mTextureBitmap.getWidth(), mTextureBitmap.getHeight());

            RectF mBgRectF = new RectF(0, 0, getWidth(), getRight());
            canvas.drawBitmap(mBgBitmap, mBgBitmapRect, mBgRectF, null);
            canvas.drawBitmap(mTextureBitmap, mTextureBitmapRect, mBgRectF, null);

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

}
