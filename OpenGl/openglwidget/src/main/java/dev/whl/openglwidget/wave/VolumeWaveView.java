package dev.whl.openglwidget.wave;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.openglwidget.R;

public class VolumeWaveView extends RelativeLayout {

    WaveView mWaveView;
    Button mTapeBtn;

    public VolumeWaveView(Context context) {
        this(context, null);
    }

    public VolumeWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.desgin_volumewave, this);
        mWaveView = findViewById(R.id.waveView);
        mTapeBtn = findViewById(R.id.tapeBtn);
        mTapeBtn.setOnClickListener(getTapeClickListener());
        mWaveView.setSpreadCallBack(getSpreadCallBack());
    }

    private Spread.CallBack getSpreadCallBack() {
        return new Spread.CallBack() {
            @Override
            public void doSpreadAnimEnd() {
                Log.d("SpreadAA", "doSpreadAnimEnd");
                mWaveView.doWaveAnim();
            }

            @Override
            public void doShrinkAnimEnd() {
                Log.d("SpreadAA", "doShrinkAnimEnd");
                mWaveView.doEmptyAnim();
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator narrowX = ObjectAnimator.ofFloat(mTapeBtn, "scaleX", 0, 1);
                        ObjectAnimator narrowY = ObjectAnimator.ofFloat(mTapeBtn, "scaleY", 0, 1);
                        narrowY.setDuration(600);
                        narrowX.setDuration(600);
                        narrowY.start();
                        narrowX.start();
                    }
                });
            }
        };
    }

    private OnClickListener getTapeClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator narrowX = ObjectAnimator.ofFloat(v, "scaleX", 1, 0);
                ObjectAnimator narrowY = ObjectAnimator.ofFloat(v, "scaleY", 1, 0);
                narrowY.setDuration(600);
                narrowX.setDuration(600);
                narrowY.start();
                narrowX.start();
                narrowY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mWaveView.doSpreadAnim();
                    }
                });
                if(mCallBack != null){
                    mCallBack.onClickTape();
                }
            }
        };
    }

    public void onPause() {
        mWaveView.onPause();
    }

    public void onResume() {
        mWaveView.onResume();
    }

    public void setAmplitude(Float lineOne, Float lineTwo) {
        mWaveView.setAmplitude(lineOne, lineTwo);
    }

    public void doLoadingAnim() {
        mWaveView.doLoadingAnim();
    }

    public void doEndAnim() {
        mWaveView.doEndAnim();
    }

    CallBack mCallBack;

    public interface CallBack {
        void onClickTape();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
}
