package com.exerciser;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speech  {

    private static TextToSpeech tts = null;

    public static void init(Context context)
    {
        if (null != tts) {
            Log.i("Speech", "Already loaded.");
            //speak("Speech already loaded.", TextToSpeech.QUEUE_FLUSH);
            return;
        }

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }

                    Log.i("TTS", "Initialization success.");
                    speak("Ready.", TextToSpeech.QUEUE_ADD);

                } else {
                    Log.i("TTS", "TTS Initialization failed!");
                }
            }
        });
    }

    public static void speak(CharSequence text, int queueMode)
    {
        if (null != tts)
            tts.speak(text, queueMode, null, "");
    }

    public static void shutup() {
        if (null != tts && tts.isSpeaking())
            tts.stop();
    }

    public static void shutdown() {
        if (null != tts) {
            tts.shutdown();
        }
    }

}
