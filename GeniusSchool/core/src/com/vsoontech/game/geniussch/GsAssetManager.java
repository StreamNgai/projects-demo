package com.vsoontech.game.geniussch;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vsoontech.game.geniussch.data.LetterData;
import com.vsoontech.game.geniussch.data.LetterDataLoader;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Des:
 * Created by Weihl
 * 2019-08-28
 */
public class GsAssetManager extends AssetManager {


    public HashMap<String, String> resFields;

    enum Type {
        MP3,
        PNG,
        FNT,
        ATLAS,
        NULL,
        JSON
    }


    public GsAssetManager() {
        resFields = new HashMap<String, String>();
        loadResource(Res.class);
    }

    public String getFieldValue(String fieldName) {
        return resFields.get(fieldName);
    }

    private void addCustomLoader() {
        setLoader(LetterData.class, new LetterDataLoader(new InternalFileHandleResolver()));
    }

    /**
     * 反射 cls 常量字段；获取字段名与值；
     * 进行 assetManager 资源加载；避免过多的书写 load 代码
     */
    private void loadResource(Class<Res> cls) {
        try {
            // 增加自定义 加载器
            addCustomLoader();

            if (cls != null) {
                //将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
                Field[] fields = cls.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    if ("String".equals(f.getType().getSimpleName())) {
                        if (Logc.allowPrint()) {
                            Logc.d("[GsAssetManager]  属性名：" + f.getName()
                                    + "；属性值：" + f.get(cls)
                                    + "；字段类型：" + f.getType().getSimpleName());
                        }
                        // 自定义规则，只收 String 类型
                        if (f.get(cls) instanceof String) {
                            String fieldName = f.getName();
                            String fieldValue = (String) f.get(cls);
                            resFields.put(fieldName, fieldValue);
                            loadAction(fieldValue);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAction(String fieldVal) {
        switch (typeAs(fieldVal)) {
            case FNT:
                load(fieldVal, BitmapFont.class);
                break;
            case MP3:
                load(fieldVal, Music.class);
                break;
            case PNG:
                load(fieldVal, Texture.class);
                break;
            case ATLAS:
                load(fieldVal, TextureAtlas.class);
                break;
            case JSON:
                load(fieldVal, LetterData.class);
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
            } else if (".json".equals(suffix)) {
                return Type.JSON;
            }
        }
        return Type.NULL;
    }


}
