package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 公司logo
 *
 * @author Ngai
 */
public class LogoActor extends GsActor {

    private TextureRegion logoRegion;
    private int x, y;

    public LogoActor(TextureRegion region, float alphaDuration) {
        super();
        this.logoRegion = region;
        this.x = Gdx.graphics.getWidth() / 2 - logoRegion.getRegionWidth() / 2;
        this.y = Gdx.graphics.getHeight() / 2 - logoRegion.getRegionHeight() / 2;
        getColor().a = 0.0f;
        addAction(Actions.alpha(1.0f, alphaDuration));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (allowLog()) {
            doLog("delta = " + delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isDrawEffective()) {
            batch.setColor(getColor());
            batch.draw(logoRegion, x, y);
        }

    }

    // 当前 Actor 状态是否有效可 Draw
    private boolean isDrawEffective() {
        return logoRegion != null && isVisible();
    }


    @Override
    public void clear() {
        super.clear();
    }
}
