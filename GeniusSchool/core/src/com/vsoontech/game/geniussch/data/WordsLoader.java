package com.vsoontech.game.geniussch.data;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.vsoontech.game.geniussch.Logc;

public class WordsLoader extends AsynchronousAssetLoader<Words, WordsLoader.WordsParameter> {

    private Words mWords;

    public WordsLoader(InternalFileHandleResolver resolver) {
        super(resolver);
        if (Logc.allowPrint()) {
            Logc.d(" WordsLoader ");
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
        WordsLoader.WordsParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
        WordsLoader.WordsParameter parameter) {
        mWords = new Json().fromJson(Words.class, file);

        if (Logc.allowPrint()) {
            Logc.d(" WordsLoader " + mWords.toString());
        }
    }

    @Override
    public Words loadSync(AssetManager manager, String fileName, FileHandle file,
        WordsLoader.WordsParameter parameter) {
        Words words = this.mWords;
        this.mWords = null;
        return words;
    }

    public static class WordsParameter extends AssetLoaderParameters<Words> {

        public WordsParameter() {
        }
    }

}
