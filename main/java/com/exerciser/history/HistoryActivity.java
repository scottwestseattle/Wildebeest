package com.exerciser.history;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exerciser.Program.ProgramContent;
import com.exerciser.Program.ProgramItem;
import com.exerciser.R;
import com.exerciser.RssReader;
import com.exerciser.Tools;
import com.exerciser.UserPreferences;
import com.exerciser.exercises.ExercisesActivity;
import com.exerciser.history.content.HistoryContent;

public class HistoryActivity extends AppCompatActivity  implements HistoryFragment.OnListFragmentInteractionListener {

    private String mProgramName;
    private int mProgramId = -1;
    private String mSessionName;
    private int mSessionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        Intent i = getIntent();
        mProgramId = i.getIntExtra("courseId", -1);
        mProgramName = i.getStringExtra("courseName");
        mSessionId = i.getIntExtra("sessionId", -1);
        mSessionName = i.getStringExtra("sessionName");
        int seconds = i.getIntExtra("sessionSeconds", -1);
        int exercises = i.getIntExtra("sessionExercises", -1);

        TextView tv = null;

        tv = (TextView) findViewById(R.id.textViewProgramName);
        tv.setText(mProgramName);

        tv = (TextView) findViewById(R.id.textViewSessionName);
        tv.setText(mSessionName);

        tv = (TextView) findViewById(R.id.textViewExerciseCount);
        tv.setText(exercises + " exercises");

        tv = (TextView) findViewById(R.id.textViewExerciseTime);
        tv.setText("Total Time: " + Tools.getTimeFromSeconds(seconds));

        // set the background image for the next exercise
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.card_layout);
        if (null != rl) {
            ProgramItem program = RssReader.programMap.get(mProgramId);
            if (null != program)
                rl.setBackgroundResource(ProgramContent.getBackgroundImageResourceId(program.imageId));
        }
    }

    public void onStartButtonClick(View view) {
        Intent intent = new Intent(this, ExercisesActivity.class);
        intent.putExtra("sessionName", mSessionName);
        intent.putExtra("sessionId", mSessionId);
        intent.putExtra("courseId", mProgramId);
        intent.putExtra("courseName", mProgramName);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(HistoryContent.HistoryItem item) {

    }
}
