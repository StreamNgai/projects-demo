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

public class LetterDataLoader extends AsynchronousAssetLoader<LetterData, LetterDataLoader.LetterDataParameter> {

    private LetterData mData;

    public LetterDataLoader(InternalFileHandleResolver resolver) {
        super(resolver);
        if (Logc.allowPrint()) {
            Logc.d(" LetterDataLoader ");
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
        LetterDataLoader.LetterDataParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
        LetterDataLoader.LetterDataParameter parameter) {
        mData = new Json().fromJson(LetterData.class, file);

        if (Logc.allowPrint()) {
            Logc.d(" LetterDataLoader " + mData.toString());
        }
    }

    @Override
    public LetterData loadSync(AssetManager manager, String fileName, FileHandle file,
        LetterDataLoader.LetterDataParameter parameter) {
        LetterData LetterData = this.mData;
        this.mData = null;
        return LetterData;
    }

    public static class LetterDataParameter extends AssetLoaderParameters<LetterData> {

        public LetterDataParameter() {
        }
    }

}
