package com.e.rhino.history;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.rhino.program.ProgramContent;
import com.e.rhino.program.ProgramItem;
import com.e.rhino.R;
import com.e.rhino.RssReader;
import com.e.rhino.Tools;
import com.e.rhino.exercises.ExercisesActivity;
import com.e.rhino.history.content.HistoryContent;
import com.e.rhino.sessions.content.SessionContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HistoryActivity extends AppCompatActivity  implements HistoryFragment.OnListFragmentInteractionListener {

    private String mProgramName;
    private int mProgramId = -1;
    private String mSessionName;
    private int mSessionId = -1;
    private int mSessionSeconds = -1;
    private int mSessionExercises = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FloatingActionButton fabEnd = findViewById(R.id.fabEnd);
        fabEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setTitle("History");

        load();

        TextView tv = null;

        tv = (TextView) findViewById(R.id.textViewNextLabel);
        tv.setText("Your next exercise session is:");

        tv = (TextView) findViewById(R.id.textViewProgramName);
        tv.setText(mProgramName);

        tv = (TextView) findViewById(R.id.textViewSessionName);
        tv.setText(mSessionName);

        tv = (TextView) findViewById(R.id.textViewExerciseCount);
        tv.setText(mSessionExercises + " exercises");

        tv = (TextView) findViewById(R.id.textViewExerciseTime);
        tv.setText("Total Time: " + Tools.getTimeFromSeconds(mSessionSeconds));

        // set the background image for the next exercise
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.card_layout);
        if (null != rl) {
            ProgramItem program = RssReader.programMap.get(mProgramId);
            if (null != program)
                rl.setBackgroundResource(ProgramContent.getBackgroundImageResourceId(program.imageId));
        }
    }

    private void load()
    {
        List<ProgramItem> programItemList = ProgramContent.programList;
        for (ProgramItem item : programItemList)
        {
            // figure out the next Session from the history records
            HistoryContent.HistoryItem newestItem = HistoryContent.getNewestItem(item.id);
            if (null != newestItem)
            {
                SessionContent.SessionItem nextSession = RssReader.getNextSession(newestItem.programId, newestItem.sessionId);
                if (null != nextSession) { // if not all sessions completed then show the next session

                    mProgramId = newestItem.programId;
                    mProgramName = nextSession.parent;
                    mSessionName = nextSession.name;
                    mSessionId = nextSession.id;
                    mSessionSeconds = nextSession.seconds;
                    mSessionExercises = nextSession.exerciseCount;
                    break;
                }
            }
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
