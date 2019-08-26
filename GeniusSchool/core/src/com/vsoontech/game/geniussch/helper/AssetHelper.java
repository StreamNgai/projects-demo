package com.vsoontech.game.geniussch.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vsoontech.game.geniussch.Logc;
import com.vsoontech.game.geniussch.Res;
import com.vsoontech.game.geniussch.data.Words;
import com.vsoontech.game.geniussch.data.WordsLoader;
import java.lang.reflect.Field;

public class AssetHelper {


    enum Type {
        MP3,
        PNG,
        FNT,
        ATLAS,
        NULL
    }

    /**
     * 反射 cls 常量字段；获取字段名与值；
     * 进行 assetManager 资源加载；避免过多的书写 load 代码
     */
    public void loadResource(Class<Res> cls, AssetManager assetManager) {
        try {
            if (cls != null && assetManager != null) {
                //将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
                Field[] fields = cls.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    if ("String".equals(f.getType().getSimpleName())) {
                        if (Logc.allowPrint()) {
                            Logc.d("[AssetHelper]  属性名：" + f.getName()
                                + "；属性值：" + f.get(cls)
                                + ";字段类型：" + f.getType().getSimpleName());
                        }
                        loadAction((String) f.get(cls), assetManager);
                    }

                }


                assetManager.setLoader(Words.class, new WordsLoader(new InternalFileHandleResolver()));
                assetManager.load("assets/letter/words.json", Words.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAction(String fieldVal, AssetManager assetManager) {
        switch (typeAs(fieldVal)) {
            case FNT:
                assetManager.load(fieldVal, BitmapFont.class);
                break;
            case MP3:
                assetManager.load(fieldVal, Music.class);
                break;
            case PNG:
                assetManager.load(fieldVal, Texture.class);
                break;
            case ATLAS:
                assetManager.load(fieldVal, TextureAtlas.class);
                break;
            default:
                // nothing
                break;
        }

    }

    private Type typeAs(String fieldVal) {
        if (fieldVal != null && !fieldVal.isEmpty()) {
            int pIndex = fieldVal.lastIndexOf(".");
            String suffix = fieldVal.substring(pIndex);
            if (".png".equals(suffix)) {
                return Type.PNG;
            } else if (".atlas".equals(suffix)) {
                return Type.ATLAS;
            } else if (".fnt".equals(suffix)) {
                return Type.FNT;
            } else if (".mp3".equals(suffix)) {
                return Type.MP3;
            }
        }
        return Type.NULL;
    }
}
