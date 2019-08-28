package com.vsoontech.game.geniussch.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vsoontech.game.geniussch.GeniusSchool;
import com.vsoontech.game.geniussch.Res;
import com.vsoontech.game.geniussch.data.LetterData;
import com.vsoontech.game.geniussch.helper.LetterHelper;
import com.vsoontech.game.geniussch.screen.actor.LetterBlankActor;
import com.vsoontech.game.geniussch.screen.actor.LetterFillActor;
import java.util.Arrays;
import java.util.Random;

/**
 * 字母填充
 */
public class LetterScreen extends GsScreen {

    private GeniusSchool mGame;
    private Stage mStage;
    private LetterHelper mLetterHelper;
    private LetterBlankActor[] mBlankActors;
    private LetterFillActor[] mFillActors;

    public LetterScreen(GeniusSchool game) {
        mGame = game;
        mLetterHelper = new LetterHelper((LetterData) mGame.assetManager.get(Res.LETTER_DATA_JSON));
        if (allowLog()) {
            doLog("LetterScreen create ! GDX width = " + Gdx.graphics.getWidth()
                + " ; height = " + Gdx.graphics.getHeight());
        }
    }

    // 获取下个单词，并输出资源
    private void nextWord() {

        removePrevLetterActors();
        char[] letters = mLetterHelper.nextWordChars();
        String[] blankFields = mLetterHelper.getMapResFieldsBlank();
        if (allowLog()) {
            doLog("LetterScreen blankFields ! " + Arrays.toString(blankFields));
        }
        String[] fillFields = mLetterHelper.getMapResFieldsFill();
        if (allowLog()) {
            doLog("LetterScreen fillFields ! " + Arrays.toString(fillFields));
        }
        String[] aCorrectFields = mLetterHelper.getMapResFieldsAudioCorrect();
        if (allowLog()) {
            doLog("LetterScreen aCorrectFields ! " + Arrays.toString(aCorrectFields));
        }
        String[] aTouchFields = mLetterHelper.getMapResFieldsAudioTouch();
        if (allowLog()) {
            doLog("LetterScreen aTouchFields ! " + Arrays.toString(aTouchFields));
        }
        String[] dAtlasFields = mLetterHelper.getMapResFieldsDynamicAtlas();
        if (allowLog()) {
            doLog("LetterScreen dAtlasFields ! " + Arrays.toString(dAtlasFields));
        }

        createLetterActors(letters, blankFields, fillFields, dAtlasFields, aTouchFields, aCorrectFields);

    }

    private void createLetterActors(char[] letters, String[] blankFields, String[] fillFields, String[] dAtlasFields,
        String[] aTouchFields, String[] aCorrectFields) {
        if (blankFields != null && blankFields.length > 0) {
            int index = -1;
            int letterLength = 0;
            int space = 30;
            int length = blankFields.length;
            mBlankActors = new LetterBlankActor[length];
            mFillActors = new LetterFillActor[length];
            for (int i = 0; i < length; i++) {
                index++;
                // BlankLetterActor
                Texture tBlank = mGame.assetManager.get(mGame.resFields.get(blankFields[i]));
                TextureRegion tBkRegion = new TextureRegion(tBlank, tBlank.getWidth(), tBlank.getHeight());
                LetterBlankActor tBlActor = new LetterBlankActor(tBkRegion, 2);
                tBlActor.setTouchable(Touchable.disabled);
                tBlActor.setLetter(letters[i]);
                // 音效
                Music tBkMusic = mGame.assetManager.get(mGame.resFields.get(aCorrectFields[i]));
                tBlActor.setAudio(tBkMusic);
                mBlankActors[index] = tBlActor;

                // FillActor
                Texture tFill = mGame.assetManager.get(mGame.resFields.get(fillFields[i]));
                TextureRegion tFlRegion = new TextureRegion(tFill, tFill.getWidth(), tFill.getHeight());
                LetterFillActor tFlActor = new LetterFillActor(tFlRegion, 2);
                TextureAtlas tFillAtlas = mGame.assetManager.get(mGame.resFields.get(dAtlasFields[i]));
                // 动画
                Animation<TextureRegion> runningAnimation =
                    new Animation<TextureRegion>(0.033f, tFillAtlas.findRegions(String.valueOf(letters[i])),
                        PlayMode.LOOP);
                tFlActor.setAnimation(runningAnimation);
                tFlActor.setLetter(letters[i]);
                // 音效
                Music tFLMusic = mGame.assetManager.get(mGame.resFields.get(aTouchFields[i]));
                tFlActor.setAudio(tFLMusic);
                tFlActor.setTouchable(Touchable.enabled);
                mFillActors[index] = tFlActor;

                letterLength += (tBlank.getWidth() + space);
            }

            if (mBlankActors.length > 0) {
                letterLength -= space;
                int leftStart = mWidth / 2 - letterLength / 2;
                int heightStart = (int) (mHeight / 2 - mBlankActors[0].getHeight() / 2);
                Random tRandom = new Random();
                // 这里要考虑 letterLength 超过屏幕宽度时的显示 归0?
                // letterLength < 0 ; letterLength = 0;
                for (int i = 0; i < length; i++) {
                    // blank
                    LetterBlankActor blankActor = mBlankActors[i];
                    blankActor.setPosition(leftStart, heightStart);
                    mStage.addActor(blankActor);

                    // fill
                    LetterFillActor fillActor = mFillActors[i];
                    int randomX = tRandom.nextInt((int) (mWidth - fillActor.getWidth()));
                    int randomY = tRandom.nextInt((int) (mHeight - fillActor.getHeight()));
                    if (allowLog()) {
                        doLog("LetterFillActor randomX = " + randomX + " ; randomY = " + randomY);
                    }
                    fillActor.markOriginXY(randomX, randomY);
                    fillActor.setPosition(randomX, randomY);
                    mStage.addActor(fillActor);
                    fillActor.addListener(mInputListener);

                    // 下一个增加偏移量
                    leftStart += blankActor.getWidth() + 30;
                }

            }
        }
    }

    private void removePrevLetterActors() {
        if (mBlankActors != null
            && mBlankActors.length > 0
            && mStage != null) {
            for (Actor actor : mBlankActors) {
                mStage.getRoot().removeActor(actor);
            }
        }
    }


    @Override
    protected void newResize(int width, int height) {
        if (allowLog()) {
            doLog("LetterScreen newResize ! GDX width = " + Gdx.graphics.getWidth()
                + " ; height = " + Gdx.graphics.getHeight());
        }
        mStage = new Stage(new StretchViewport(width, height));
        Gdx.input.setInputProcessor(mStage);
        // 背景
        Texture backgroundTexture = mGame.assetManager.get(Res.LETTER_BACKGROUND_PNG);
        Image bgImg = new Image(new TextureRegion(
            backgroundTexture,
            backgroundTexture.getWidth(), backgroundTexture.getHeight()));
        mStage.addActor(bgImg);
        nextWord();
        autoDisposable(mStage);
    }

    private InputListener mInputListener = new InputListener() {

        private float touchDownX, touchDownY;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Actor actor = event.getTarget();
            if (allowLog()) {
                doLog("LetterScreen InputListener.touchDown ! x = " + x + " ; y = " + y + " ; actor = " + actor);
            }
            if (actor instanceof LetterFillActor) {
                touchDownX = x;
                touchDownY = y;
                actor.setZIndex(mStage.getActors().size);
                ((LetterFillActor) actor).startAnimation();
            }
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Actor actor = event.getTarget();
            if (allowLog()) {
                doLog("LetterScreen InputListener.touchUp ! x = " + x + " ; y = " + y);
            }
            if (actor instanceof LetterFillActor) {
                LetterFillActor fillActor = (LetterFillActor) actor;
                fillActor.stopAnimation();
                LetterBlankActor blankActor = checkMoveCorrect(fillActor);
                if (blankActor != null) {
                    fillActor.moveXY(blankActor.getX(), blankActor.getY());
                } else {
                    fillActor.moveOriginXY();
                }
            }
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (allowLog()) {
                doLog("LetterScreen InputListener.touchDragged ! x = " + x + " ; y = " + y);
            }
            Actor actor = event.getTarget();
            if (actor instanceof LetterFillActor) {
                int offsetX = (int) (x - touchDownX);
                int offsetY = (int) (y - touchDownY);
                actor.setPosition(actor.getX() + offsetX, actor.getY() + offsetY);
            }
        }
    };

    private LetterBlankActor checkMoveCorrect(LetterFillActor fillActor) {
        for (LetterBlankActor bkActor : mBlankActors) {
            if (bkActor.getLetter() == fillActor.getLetter()
                && bkActor.nonAdsorb()) {
                Rectangle bkRt = new Rectangle();
                bkRt.set(bkActor.getX(), bkActor.getY(),
                    bkActor.getWidth(), bkActor.getHeight());
                Rectangle flRt = new Rectangle();
                flRt.set(fillActor.getX(), fillActor.getY(),
                    fillActor.getWidth(), fillActor.getHeight());
                if (bkRt.overlaps(flRt)) {
                    bkActor.setAdsorb();
                    fillActor.removeListener(mInputListener);
                    return bkActor;
                }
            }
        }
        return null;
    }

    @Override
    public void render(float delta) {

        mStage.act();
        mStage.draw();

    }

    @Override
    public void hide() {
        if (allowLog()) {
            doLog("LetterScreen hide !");
        }
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (allowLog()) {
            doLog("LetterScreen dispose !");
        }
    }
}
