package com.vsoontech.game.geniussch.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vsoontech.game.geniussch.GeniusSchool;
import com.vsoontech.game.geniussch.Logc;
import com.vsoontech.game.geniussch.Res;
import com.vsoontech.game.geniussch.data.LetterData;
import com.vsoontech.game.geniussch.screen.actor.LoadActor;
import com.vsoontech.game.geniussch.screen.actor.LogoActor;

/**
 * 欢迎页面，显示一下公司 Logo
 *
 * @author Ngai
 */
public class WelcomeScreen extends GsScreen {

    private GeniusSchool mGame;
    private TextureAtlas mLoadAtlas;
    private NinePatch mLoadBarProgressDraw;
    private Texture mBgTexture;
    private Texture mLogoTexture;
    private BitmapFont mBitmapFont;
    private Stage mStage;
    private float deltaSum = 0;
    private float alphaDuration = 3.0f;
    private Label mPercentText;
    private LoadActor mLoadActor;

    public WelcomeScreen(GeniusSchool game) {
        this.mGame = game;
        if (allowLog()) {
            doLog("WelcomeScreen create ! GDX width = " + Gdx.graphics.getWidth()
                + " ; height = " + Gdx.graphics.getHeight());
        }
    }

    @Override
    protected void newResize(int width, int height) {
        if (allowLog()) {
            doLog("WelcomeScreen newResize !");
        }
        mStage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        mBitmapFont = new BitmapFont(Res.filesInternal(Res.FONT_REGULAR_FNT));
        mBgTexture = new Texture(Res.filesInternal(Res.WELCOME_BACKGROUND_PNG));
        // Logo
        mLogoTexture = new Texture(Res.filesInternal(Res.WELCOME_LOGO_PNG));
        mStage.addActor(new LogoActor(new TextureRegion(mLogoTexture), alphaDuration));
        // 加载元素
        mLoadAtlas = new TextureAtlas(Res.filesInternal(Res.LOAD_BAR_ATLAS));
        mLoadBarProgressDraw = mLoadAtlas.createPatch("progress_draw");

        autoDisposable(mStage);
        autoDisposable(mBitmapFont);
        autoDisposable(mBgTexture);
        autoDisposable(mLogoTexture);
        autoDisposable(mLoadAtlas);
    }

    @Override
    public void show() {
        if (allowLog()) {
            doLog("WelcomeScreen show !");
        }
        deltaSum = 0;
    }

    @Override
    public void render(float delta) {

        // 暂时logo
        logoAction(delta);

        // 更新舞台逻辑
        mStage.act();
        // 绘制舞台
        mStage.draw();

        // 进度条填充部分
        loadBarAction(delta);
    }

    private void loadBarAction(float delta) {
        if (deltaSum == -1) {

            if (mGame.assetManager.update()) {
                // we are done loading, let's move to another screen!
                mGame.setScreen(new LetterScreen(mGame));
            }

            // display loading information
            float progress = mGame.assetManager.getProgress();
            if (mPercentText != null) {
                mPercentText.setText(Math.round(progress * 100) + " %");
            }
            if (mLoadActor != null) {
                mLoadActor.setProgress(mLoadBarProgressDraw.getTotalWidth() * progress);
            }

        }
    }

    private void logoAction(float delta) {
        if (deltaSum != -1) {
            // 累计渲染时间步
            deltaSum += delta;
            if (deltaSum >= alphaDuration) {
                deltaSum = -1;
                mStage.clear();
                addLoadActor();
            }
        }
    }

    private void addLoadActor() {
        // 背景
        Image bgImg = new Image(new TextureRegion(mBgTexture, mBgTexture.getWidth(), mBgTexture.getHeight()));
        mStage.addActor(bgImg);
        // 进度条背景
        TextureRegion pImgR = mLoadAtlas.findRegion("background");
        mLoadActor = new LoadActor(pImgR, mLoadBarProgressDraw);
        int graphicsWidthHalf = Gdx.graphics.getWidth() / 2;
        int pimgRegionWidthHalf = pImgR.getRegionWidth() / 2;
        int mLoadBarX = graphicsWidthHalf - pimgRegionWidthHalf;
        int mLoadBarY = 300;
        mLoadActor.setPosition(mLoadBarX, mLoadBarY);
        mStage.addActor(mLoadActor);

        // 文字与进度
        Label.LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = mBitmapFont;
        labelStyle.fontColor = Color.WHITE;

        Label loadText = new Label("游戏资源加载中...", labelStyle);
        loadText.setPosition(graphicsWidthHalf - loadText.getPrefWidth() / 2, 230);
        mStage.addActor(loadText);

        mPercentText = new Label("100%", labelStyle);
        mPercentText.setPosition(graphicsWidthHalf - mPercentText.getPrefWidth() / 2, mLoadBarY + 5);
        mStage.addActor(mPercentText);

    }


    @Override
    public void hide() {
        if (allowLog()) {
            doLog("WelcomeScreen hide !");
        }
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (allowLog()) {
            doLog("WelcomeScreen dispose !");
        }
    }
}
