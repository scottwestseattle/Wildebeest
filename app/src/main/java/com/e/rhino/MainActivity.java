package com.e.rhino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.e.rhino.program.ProgramContent;
import com.e.rhino.program.ProgramItem;
import com.e.rhino.program.ProgramsFragment;
import com.e.rhino.exercises.ExercisesActivity;
import com.e.rhino.history.HistoryActivity;
import com.e.rhino.history.content.HistoryContent;
import com.e.rhino.sessions.SessionsActivity;
import com.e.rhino.sessions.content.SessionContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProgramsFragment.OnListFragmentInteractionListener
{
    public MainActivity()
    {
        Log.i("Constructor", "executed");
    }

    @Override
    public void onListFragmentInteraction(ProgramItem item) {
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
                finishAffinity(); // closes but app is still in the background
            }
        });

        FloatingActionButton fabHistory = findViewById(R.id.fabHistory);
        fabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHistory();
            }
        });
    }

    public void onContinueButtonClick(View view) {

        ProgramItem program = (ProgramItem) view.getTag();

        Intent intent = new Intent(this, ExercisesActivity.class);
        intent.putExtra("sessionName", program.sessionMap.get(program.sessionNext).name);
        intent.putExtra("sessionId", program.sessionNext);
        intent.putExtra("courseId", program.id);
        intent.putExtra("courseName", program.name);

        startActivity(intent);
    }

    private void showHistory() {

        // search history for the first relavent record and abort
        // this will be done again in HistoryActivity to get all pending exercises
        List<ProgramItem> programItemList = ProgramContent.programList;
        for (ProgramItem item : programItemList)
        {
            // figure out the next Session from the history records
            HistoryContent.HistoryItem newestItem = HistoryContent.getNewestItem(item.id);
            if (null != newestItem)
            {
                SessionContent.SessionItem nextSession = RssReader.getNextSession(newestItem.programId, newestItem.sessionId);
                if (null != nextSession) { // if not all sessions completed then show the next session

                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }

        //
        // old way with user preferences file
        //
        if (false) {
            // get the saved user preferences if any
            UserPreferences.load(this);

            if (UserPreferences.mSessionId > 0) {
                //
                // user is already doing a program, show it and the history
                //
                SessionContent.SessionItem sessionItem = RssReader.getNextSession(UserPreferences.mProgramId, UserPreferences.mSessionId);
                if (null != sessionItem) {
                    Intent intent = new Intent(this, ExercisesActivity.class);
                    intent.putExtra("sessionName", sessionItem.name);
                    intent.putExtra("sessionId", sessionItem.id);
                    intent.putExtra("courseId", UserPreferences.mProgramId);
                    intent.putExtra("courseName", sessionItem.parent);
                    startActivity(intent);
                }
                else {
                    // next session not found so consider this program to be finished
                    // so clear the settings
                    UserPreferences.clear(this);
                }
            } else {
                // no session started, just show programs
            }
        }
    }

    public void speak(CharSequence text, int queueAction) {
        Speech.speak(text, queueAction);
    }
}
