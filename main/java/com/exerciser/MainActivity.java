package com.exerciser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.exerciser.Program.ProgramContent;
import com.exerciser.sessions.SessionsActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ProgramsFragment.OnListFragmentInteractionListener
{

    public TextToSpeech tts = null;

    public MainActivity()
    {
        Log.i("Constructor", "executed");
    }

    @Override
    public void onListFragmentInteraction(ProgramContent.ProgramItem item) {
        //
        // handle click from Program list: load Sessions
        //

        Intent intent = new Intent(this, SessionsActivity.class);
        intent.putExtra("courseId", item.id);
        intent.putExtra("courseName", item.name);
        intent.putExtra("sessionCount", item.sessionCount);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("onDestroy", "destroying...");

        if (null != tts)
            Log.i("onDestroy", "shutting down speech");
        tts.shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Programs");

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

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

    public void speak(CharSequence text, int queueAction) {
        int speechStatus = tts.speak(text, queueAction, null, "");
        if (speechStatus == TextToSpeech.ERROR) {
            Log.i("TTS Speak", "Error in converting Text to Speech!");
        }
    }
}
