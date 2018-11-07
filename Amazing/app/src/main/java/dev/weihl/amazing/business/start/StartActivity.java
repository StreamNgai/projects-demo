package dev.weihl.amazing.business.start;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.weihl.amazing.R;
import dev.weihl.amazing.business.BaseActivity;
import dev.weihl.amazing.widget.PathView;

public class StartActivity extends BaseActivity implements StartContract.View {

    @BindView(R.id.pathView)
    PathView pathView;

    StartContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        pathView.setPathText(getString(R.string.app_name));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pathView.setProgress((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pathView.getPaint().setStyle(Paint.Style.FILL);
                pathView.setText(getString(R.string.app_name));

                mUIHandler.sendEmptyMessageAtTime(600,1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

        new StartPresenter(this).start();
    }

    @Override
    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
            case 1000:
                mPresenter.startMain();
                break;
        }
    }

    @Override
    public void setPresenter(StartContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }
}
