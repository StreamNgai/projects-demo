package com.vsoontech.game.geniussch.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vsoontech.game.geniussch.GeniusSchool;
import com.vsoontech.game.geniussch.helper.LetterHelper;

/**
 * 字母填充
 */
public class LetterScreen extends GsScreen {

    private GeniusSchool mGame;
    private Stage mStage;
    private LetterHelper mLetterHelper;

    public LetterScreen(GeniusSchool game) {
        mGame = game;
        mLetterHelper = LetterHelper.getInstance();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
//        mStage = new Stage(new StretchViewport(width, height));
//        // 背景
//        Image bgImg = new Image(new TextureRegion(
//            (Texture) mGame.assetManager.get(Res.LETTER_BACKGROUND_PNG),
//            width, height));
//        mStage.addActor(bgImg);
//        autoDisposable(mStage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        mStage.act();
        mStage.draw();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
