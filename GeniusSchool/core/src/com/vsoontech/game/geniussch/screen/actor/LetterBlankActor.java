package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 显示空白字母
 */
public class LetterBlankActor extends GsActor {


    private TextureRegion mRegion;

    public LetterBlankActor(TextureRegion region, float alphaDuration) {
        this.mRegion = region;
        if (mRegion != null) {
            setWidth(mRegion.getRegionWidth());
            setHeight(mRegion.getRegionHeight());
            getColor().a = 0.0f;
            addAction(Actions.alpha(1.0f, alphaDuration));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isDrawEffective()) {
            batch.setColor(getColor());
            batch.draw(mRegion, getX(), getY());
        }

    }

    // 当前 Actor 状态是否有效可 Draw
    private boolean isDrawEffective() {
        return mRegion != null && isVisible();
    }


}
