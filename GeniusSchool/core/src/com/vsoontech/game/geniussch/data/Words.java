package com.vsoontech.game.geniussch.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;

/**
 * 单词数据
 *
 * @author Ngai
 */
public class Words implements Json.Serializable {

    private ArrayList<Item> items;

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonValue) {

    }


    static class Item implements Json.Serializable{

        public String level;
        public ArrayList<String> words;

        @Override
        public String toString() {
            return "Item{" +
                "level='" + level + '\'' +
                ", words=" + words +
                '}';
        }

        @Override
        public void write(Json json) {

        }

        @Override
        public void read(Json json, JsonValue jsonValue) {

        }
    }

}
