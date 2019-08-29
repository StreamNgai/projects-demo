package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.vsoontech.game.geniussch.Logc;

/**
 * 计时 Actor
 */
public class CountTimerActor extends GsActor {


    private Label mLabel;
    private TextureRegion mRegion;
    private CallBack mCallBack;
    private float mDeltaTime;
    private int mSecond;
    private int mCurrTime;

    public interface CallBack {

        void timeBegin();

        void timeOver();
    }

    public CountTimerActor(TextureRegion region, LabelStyle labelStyle) {
        this.mRegion = region;
        this.mLabel = new Label("", labelStyle);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        mLabel.setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        mLabel.setPosition(x, y, alignment);
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void doAgainTime(int second) {
        this.mSecond = second;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        mDeltaTime += delta;
        int tTime = (int) mDeltaTime;
        if (Logc.allowPrint()) {
            Logc.d("CountTimerActor Time = " + tTime);
        }
        if (tTime != mCurrTime
            && tTime <= mSecond) {
            mCurrTime = tTime;
            int rs = mSecond - mCurrTime;
            if (rs == 0) {
                mLabel.setText("时间到");
            } else {
                mLabel.setText(String.valueOf(mSecond - mCurrTime));
            }
        }
        mLabel.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDrawEffective()) {
            batch.draw(mRegion, getX(), getY());
        }
        super.draw(batch, parentAlpha);
        mLabel.draw(batch, parentAlpha);
    }

    // 当前 Actor 状态是否有效可 Draw
    private boolean isDrawEffective() {
        return mRegion != null && isVisible();
    }


}
