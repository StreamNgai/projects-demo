package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 显示填出字母
 * 触摸展现动画、声音
 */
public class LetterFillActor extends GsActor {

    private Animation<TextureRegion> mAnimation;
    private Music mMusic;
    private TextureRegion mRegion;
    private float stateTime;

    public LetterFillActor(TextureRegion region, float alphaDuration) {
        this.mRegion = region;
        this.stateTime = -1;
        if (mRegion != null) {
            setSize(mRegion.getRegionWidth(), mRegion.getRegionHeight());
            getColor().a = 0.0f;
            addAction(Actions.alpha(1.0f, alphaDuration));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isDrawEffective()) {
            batch.setColor(getColor());
            if (stateTime >= 0) {
                stateTime += Gdx.graphics.getDeltaTime();
                batch.draw(mAnimation.getKeyFrame(stateTime, true), getX(), getY());
            } else if (stateTime == -1) {
                batch.draw(mRegion, getX(), getY());
            }
        }

    }

    // 当前 Actor 状态是否有效可 Draw
    private boolean isDrawEffective() {
        return mRegion != null && isVisible();
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.mAnimation = animation;
    }

    public void setAudio(Music music) {
        this.mMusic = music;
        if (mMusic != null) {
            mMusic.setLooping(true);
        }
    }

    public void startAnimation() {
        stateTime = 0;
        if (mMusic != null) {
            mMusic.play();
        }
    }

    public void stopAnimation() {
        stateTime = -1;
        if (mMusic != null) {
            mMusic.stop();
        }
    }
}
