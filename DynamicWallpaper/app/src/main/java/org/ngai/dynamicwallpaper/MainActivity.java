package org.ngai.dynamicwallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    protected RenderScript mRS;
    ImageView mSrcImageView;
    ImageView mDstImageView;
    Bitmap mInBitmap,mOutBitmap;
    Allocation aIn,aOut;
    ScriptC_hello mScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSrcImageView = (ImageView) findViewById(R.id.imageViewSrc);
        mDstImageView = (ImageView) findViewById(R.id.imageViewDst);

        mInBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mOutBitmap = Bitmap.createBitmap(mInBitmap.getWidth(),mInBitmap.getHeight(), mInBitmap.getConfig());
        mSrcImageView.setImageBitmap(mInBitmap);

        RenderScript rs = RenderScript.create(getApplicationContext());
        mScript = new ScriptC_hello(rs);


        aIn = Allocation.createFromBitmap(rs, mInBitmap);
        aOut = Allocation.createFromBitmap(rs, mInBitmap);


        mScript.forEach_invert(aIn, aOut);
        aOut.copyTo(mOutBitmap);
        mDstImageView.setImageBitmap(mOutBitmap);
        rs.destroy();
    }
}
