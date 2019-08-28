package com.vsoontech.game.geniussch;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.vsoontech.game.BuildConfig;
import com.vsoontech.game.geniussch.speech.SpeechInterface;
import java.util.Locale;

public class AndroidLauncher extends AndroidApplication implements SpeechInterface {

    TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initGsConfig();
//        mTextToSpeech = new TextToSpeech(this, new OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    int result = mTextToSpeech.setLanguage(Locale.ENGLISH);
//                    if (result == TextToSpeech.LANG_MISSING_DATA
//                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        if (Logc.allowPrint()) {
//                            Logc.d("TextToSpeech not use");
//                        }
//                    } else {
//                        mTextToSpeech.speak("Vsoontech", TextToSpeech.QUEUE_FLUSH,
//                            null);
//                    }
//                }
//            }
//        });
        initialize(new GeniusSchool(this), config);
    }

    private void initGsConfig() {
        GsConfig.appVersion = BuildConfig.VERSION_CODE;
        GsConfig.appVersionName = BuildConfig.VERSION_NAME;
    }

    @Override
    public void speak(String text) {
//        if (mTextToSpeech != null) {
//            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//        }
    }
}
