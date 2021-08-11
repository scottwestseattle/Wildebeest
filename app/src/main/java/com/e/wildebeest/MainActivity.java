package com.e.wildebeest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.e.wildebeest.program.ProgramContent;
import com.e.wildebeest.program.ProgramItem;
import com.e.wildebeest.program.ProgramsFragment;
import com.e.wildebeest.exercises.ExercisesActivity;
import com.e.wildebeest.history.HistoryActivity;
import com.e.wildebeest.history.content.HistoryContent;
import com.e.wildebeest.sessions.SessionsActivity;
import com.e.wildebeest.sessions.content.SessionContent;
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

        // just show the history list
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);

        //
        // old way that only updated on first load
        //
        if (false) {
            // search history for the first relavent record and abort
            // this will be done again in HistoryActivity to get all pending exercises
            List<ProgramItem> programItemList = ProgramContent.programList;
            for (ProgramItem item : programItemList) {
                // figure out the next Session from the history records
                HistoryContent.HistoryItem newestItem = HistoryContent.getNewestItem(item.id);
                if (null != newestItem) {
                    SessionContent.SessionItem nextSession = RssReader.getNextSession(newestItem.programId, newestItem.sessionId);
                    if (null != nextSession) { // if not all sessions completed then show the next session

                        Intent intent2 = new Intent(this, HistoryActivity.class);
                        startActivity(intent2);
                        break;
                    }
                }
            }
        }

        //
        // old old way with user preferences file
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
                    Intent intent3 = new Intent(this, ExercisesActivity.class);
                    intent3.putExtra("sessionName", sessionItem.name);
                    intent3.putExtra("sessionId", sessionItem.id);
                    intent3.putExtra("courseId", UserPreferences.mProgramId);
                    intent3.putExtra("courseName", sessionItem.parent);
                    startActivity(intent3);
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
