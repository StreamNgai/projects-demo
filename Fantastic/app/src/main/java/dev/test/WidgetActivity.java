package dev.test;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.widget.BatteryView;
import dev.ngai.widget.LightingView;
import dev.ngai.widget.PathView;

public class WidgetActivity extends Activity {

    @BindView(R.id.horizontal)
    Button horizontal;
    @BindView(R.id.horizontal_text)
    Button horizontalText;
    @BindView(R.id.vertical)
    Button vertical;
    @BindView(R.id.vertical_text)
    Button verticalText;
    @BindView(R.id.batteryView)
    BatteryView batteryView;
    @BindView(R.id.low)
    Button low;
    @BindView(R.id.nor)
    Button nor;
    @BindView(R.id.charge)
    Button charge;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.lightingView)
    LightingView lightingView;
    @BindView(R.id.pathView)
    PathView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);

        horizontal.setOnClickListener(onClickListener);
        horizontalText.setOnClickListener(onClickListener);
        vertical.setOnClickListener(onClickListener);
        verticalText.setOnClickListener(onClickListener);

        low.setOnClickListener(stateOnClickListener);
        nor.setOnClickListener(stateOnClickListener);
        charge.setOnClickListener(stateOnClickListener);

        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                batteryView.setProgress(progress);

//                float ratio = 1 + ((float)progress / 100);
//                lightingView.setScaleX(ratio);
//                lightingView.setScaleY(ratio);
//                ViewGroup.LayoutParams vlp = lightingView.getLayoutParams();
//                if(vlp != null){
//                    vlp.width = (int) (vlp.width * ratio);
//                    vlp.height = (int) (vlp.height* ratio);
//                    lightingView.setLayoutParams(vlp);
//                }
//                Toast.makeText(WidgetActivity.this,""+ratio,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pathView.setText("PathView 1");
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setRepeatCount(100);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(10000);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            int mRepeat = 0;

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mRepeat++;
                pathView.setText("PathView " + mRepeat);
                Logc.d("ValueAnimator", "Repeat = " + mRepeat);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pathView.setProgress((Float) animation.getAnimatedValue());
                Logc.d("ValueAnimator", "Value = " + (Float) animation.getAnimatedValue());
            }
        });

//        valueAnimator.start();

    }

    View.OnClickListener stateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.low:
                    batteryView.setState(BatteryView.State.LOW);
                    break;
                case R.id.nor:
                    batteryView.setState(BatteryView.State.NORMAL);
                    break;
                case R.id.charge:
                    batteryView.setState(BatteryView.State.CHARGING);
                    break;
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.horizontal:
                    batteryView.setStyle(BatteryView.Orien.HORIZONTAL);
                    break;
                case R.id.horizontal_text:
                    batteryView.setStyle(BatteryView.Orien.HORIZONTAL_TEXT);
                    break;
                case R.id.vertical:
                    batteryView.setStyle(BatteryView.Orien.VERTICAL);
                    break;
                case R.id.vertical_text:
                    batteryView.setStyle(BatteryView.Orien.VERTICAL_TEXT);
                    break;
            }
        }
    };

}
