package com.vsoontech.game.geniussch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vsoontech.game.geniussch.data.Words;
import com.vsoontech.game.geniussch.data.WordsLoader;
import com.vsoontech.game.geniussch.screen.WelcomeScreen;

public class GeniusSchool extends Game {

    // used by all screens
    public SpriteBatch spriteBatch;
    public AssetManager assetManager;

    @Override
    public void create() {
        if (Logc.allowPrint()) {
            Logc.d("GeniusSchool Game create !");
        }
        spriteBatch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.setLoader(Words.class, new WordsLoader(new InternalFileHandleResolver()));
        assetManager.load("letter/words.json", Words.class);
//        new AssetHelper().loadResource(Res.class, assetManager);

        setScreen(new WelcomeScreen(this));
    }

    @Override
    public void render() {
        // 白色清屏
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (Logc.allowPrint()) {
            Logc.d("GeniusSchool Game dispose !");
        }
    }
}
