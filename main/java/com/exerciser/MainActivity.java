package com.exerciser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.exerciser.Program.ProgramContent;
import com.exerciser.Program.ProgramsFragment;
import com.exerciser.sessions.SessionsActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ProgramsFragment.OnListFragmentInteractionListener
{
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Programs");

        Speech.init(getApplicationContext());
    }

    public void speak(CharSequence text, int queueAction) {
        Speech.speak(text, queueAction);
    }
}
