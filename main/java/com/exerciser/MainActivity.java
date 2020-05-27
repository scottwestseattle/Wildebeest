package com.exerciser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.exerciser.Program.ProgramContent;
import com.exerciser.Program.ProgramsFragment;
import com.exerciser.exercises.ExercisesActivity;
import com.exerciser.sessions.SessionsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        UserPreferences.save(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Speech.init(getApplicationContext());

        FloatingActionButton fabEnd = findViewById(R.id.fabEnd);
        fabEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity(); // closes but doesn't exit app
            }
        });

        start();
    }

    private void start() {

        UserPreferences.load(this);

        // start the next exercise
        if (UserPreferences.mSessionId > 0) {

            int size = ProgramContent.programList.size();

            Intent intent = new Intent(this, ExercisesActivity.class);
            intent.putExtra("sessionName", "Session Name");
            intent.putExtra("sessionId", UserPreferences.mSessionId);
            intent.putExtra("courseId", UserPreferences.mProgramId);
            intent.putExtra("courseName", "Program Name");
            startActivity(intent);
        }
    }


    public void speak(CharSequence text, int queueAction) {
        Speech.speak(text, queueAction);
    }
}
