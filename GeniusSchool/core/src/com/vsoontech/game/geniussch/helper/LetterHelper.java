package com.vsoontech.game.geniussch.helper;

public class LetterHelper {

    private static LetterHelper INSTANCE = new LetterHelper();

    public static LetterHelper getInstance() {

        synchronized (LetterHelper.class) {
            if (INSTANCE == null) {
                INSTANCE = new LetterHelper();
            }
        }

        return INSTANCE;
    }

    private LetterHelper() {
        analyLetterData();
    }

    private void analyLetterData() {

    }
}
