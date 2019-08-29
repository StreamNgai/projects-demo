package com.vsoontech.game.geniussch.helper;

import com.vsoontech.game.geniussch.data.LetterData;

public class LetterHelper {

    LetterData mData;
    int mCurrIndex = -1;
    char[] mCurrLetters;

    public LetterHelper(LetterData letterData) {
        mData = letterData;
    }

    private boolean isEffective() {
        return mData != null
            && mData.words != null
            && !mData.words.isEmpty();
    }

    public char[] nextWordChars() {
        if (isEffective()) {
            mCurrIndex++;
            if (mCurrIndex >= mData.words.size()) {
                mCurrIndex = 0;
            }
            String word = mData.words.get(mCurrIndex);
            mCurrLetters = word.toCharArray();
            return mCurrLetters;
        }
        return null;
    }

    private String[] mapResFields(String group, String suffix, char[] letters) {
        if (letters != null && letters.length > 0) {
            String[] resFields = new String[letters.length];
            int i = -1;
            for (char ch : letters) {
                resFields[++i] = ("LETTER_" + group + "_" + ch + "_" + suffix).toUpperCase();
            }
            return resFields;
        }
        return null;
    }

    public String[] getMapResFieldsBlank() {
        return mapResFields("blank", "png", mCurrLetters);
    }

    public String[] getMapResFieldsFill() {
        return mapResFields("fill", "png", mCurrLetters);
    }

    public String[] getMapResFieldsAudioCorrect() {
        return mapResFields("AUDIO_CORRECT", "mp3", mCurrLetters);
    }

    public String[] getMapResFieldsAudioTouch() {
        return mapResFields("AUDIO_TOUCH", "mp3", mCurrLetters);
    }

    public String[] getMapResFieldsDynamicAtlas() {
        return mapResFields("DYNAMIC", "ATLAS", mCurrLetters);
    }

    public int lengthCurrChars() {
        if (mCurrLetters != null) {
            return mCurrLetters.length;
        }
        return 0;
    }
}
