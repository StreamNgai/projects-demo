package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 加载资源进度条
 *
 * @author Ngai
 */
public class LoadActor extends GsActor {

    private TextureRegion progressRegion;
    private NinePatch progressFRegion;
    private float mProgress = 87;

    public LoadActor(TextureRegion progressRegion, NinePatch progressFRegion) {
        this.progressRegion = progressRegion;
        this.progressFRegion = progressFRegion;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isDrawEffective()) {
            batch.draw(progressRegion, getX(), getY());
            progressFRegion.draw(batch, getX(), getY(), mProgress, progressRegion.getRegionHeight());
        }

    }

    // 当前 Actor 状态是否有效可 Draw
    private boolean isDrawEffective() {
        return progressRegion != null
            && progressFRegion != null
            && isVisible();
    }

    @Override
    public void clear() {
        super.clear();
    }

    public void setProgress(float progress) {
        if (progress > mProgress) {
            this.mProgress = progress;
        }
    }
}
