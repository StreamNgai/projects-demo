package com.vsoontech.game.geniussch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vsoontech.game.geniussch.data.LetterData;
import com.vsoontech.game.geniussch.data.LetterDataLoader;
import com.vsoontech.game.geniussch.helper.AssetHelper;
import com.vsoontech.game.geniussch.screen.WelcomeScreen;
import java.util.HashMap;

public class GeniusSchool extends Game {

    // used by all screens
    public SpriteBatch spriteBatch;
    public AssetManager assetManager;
    public HashMap<String, String> resFields;

    @Override
    public void create() {
        if (Logc.allowPrint()) {
            Logc.d("GeniusSchool Game create !");
        }
        spriteBatch = new SpriteBatch();
        assetManager = new AssetManager();
        resFields = new HashMap<String, String>();
        new AssetHelper().loadResource(Res.class, assetManager, resFields);

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
