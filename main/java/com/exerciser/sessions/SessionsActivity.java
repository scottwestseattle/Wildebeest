package com.exerciser.sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.exerciser.MainActivity;
import com.exerciser.R;
import com.exerciser.exercises.ExercisesActivity;
import com.exerciser.sessions.content.SessionContent;

public class SessionsActivity extends AppCompatActivity  implements SessionsFragment.OnListFragmentInteractionListener {

    public static SessionContent sessions = null;
    public static int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        // save the courseId from the MainActivity list item click
        // the fragment will call up and get it during creation
        // todo: where should this be done?
        //
        courseId = getIntent().getIntExtra("courseId", -1);
        int sessionCount = getIntent().getIntExtra("sessionCount", -1);
        String courseName = getIntent().getStringExtra("courseName");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        setTitle(courseName + ": " + sessionCount + " sessions");
    }

    @Override
    public void onListFragmentInteraction(SessionContent.SessionItem item) {
        //
        // start the selected exercise
        //

        Intent intent = new Intent(this, ExercisesActivity.class);
        intent.putExtra("sessionName", item.parent + ": Day " + item.number);
        intent.putExtra("sessionId", item.id);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    public void navigateUp() {
        //
        // start the selected exercise
        //
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

}
