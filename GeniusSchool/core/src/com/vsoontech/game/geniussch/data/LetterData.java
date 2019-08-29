package com.vsoontech.game.geniussch.data;

import java.util.ArrayList;

/**
 * Letter 游戏单词表
 */
public class LetterData {

    public int version;
    public ArrayList<String> words;


    @Override
    public String toString() {
        return "Words{" +
            "words=" + words +
            ", version=" + version +
            '}';
    }
}
