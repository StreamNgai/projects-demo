package dev.weihl.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideRoundTransform extends BitmapTransformation {
    private static final String TAG = "GlideRoundTransform";
    public static final int DRAW_DEFAULT = 0;
    public static final int DRAW_TOP = 1;
    public static final int DRAW_BOTTOM = 1 << 1;

    private float mRadius;
    private int mDrawType = DRAW_DEFAULT;

    private int mOutWidth;
    private int mOutHeight;

    public GlideRoundTransform(Context context, float radius) {
        this(context, radius, DRAW_DEFAULT);
    }

    public GlideRoundTransform(Context context, float radius, int drawType) {
        super();
        this.mRadius = radius;
        this.mDrawType = drawType;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }
        mOutWidth = outWidth;
        mOutHeight = outHeight;
        Bitmap bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }

        Bitmap result;
        if (mDrawType == DRAW_DEFAULT) {
            result = ThumbnailUtils.extractThumbnail(toTransform, outWidth, outHeight);
        } else {
            result = extractThumbnail(toTransform, outWidth, outHeight, mDrawType == DRAW_TOP);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(result, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, outWidth, outHeight), mRadius, mRadius, paint);
        return bitmap;
    }

    public String getId() {
        return "GlideRoundTransform{" +
                "mRadius=" + mRadius +
                ", mDrawType=" + mDrawType +
                ", mOutWidth=" + mOutWidth +
                ", mOutHeight=" + mOutHeight +
                '}';
    }

    private Bitmap extractThumbnail(Bitmap source, int width, int height, boolean isTop) {
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
        return transform(matrix, source, width, height, false, isTop);
    }

    private Bitmap transform(Matrix matrix, Bitmap source, int targetWidth, int targetHeight, boolean recycle, boolean isTop) {
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
        int dy1 = isTop ? 0 : Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1, targetWidth, targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
