package dev.ngai.fantastic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ThumbnailUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideCircleTransform extends BitmapTransformation {
    private static final String TAG = "GlideCircleTransform";
    private int mOutWidth;
    private int mOutHeight;
    private int mRadius;
    private boolean isTop = false;

    public GlideCircleTransform(Context context) {
        super(context);
    }

    public GlideCircleTransform(Context context, boolean isTop) {
        super(context);
        this.isTop = isTop;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }
        mOutWidth = outWidth;
        mOutHeight = outHeight;
        mRadius = Math.min(outWidth, outHeight) / 2;

        Bitmap bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }

        Bitmap result = isTop ? extractThumbnail(toTransform, outWidth, outHeight) : ThumbnailUtils.extractThumbnail(toTransform, outWidth, outHeight);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(result, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, outWidth, outHeight), mRadius, mRadius, paint);
        return bitmap;
    }

    @Override
    public String getId() {
        return "GlideCircleTransform{" +
                "mRadius=" + mRadius +
                ", mOutWidth=" + mOutWidth +
                ", mOutHeight=" + mOutHeight +
                '}';
    }

    private Bitmap extractThumbnail(Bitmap source, int width, int height) {
        if (source == null) {
            return null;
        }

        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = width / (float) source.getWidth();
        } else {
            scale = height / (float) source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, width, height, false);
    }

    private Bitmap transform(Matrix matrix, Bitmap source, int targetWidth, int targetHeight, boolean recycle) {
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        }

        Bitmap b1;
        if (matrix != null) {
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } else {
            b1 = source;
        }

        if (recycle && b1 != source) {
            source.recycle();
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, 0, targetWidth, targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }
}
