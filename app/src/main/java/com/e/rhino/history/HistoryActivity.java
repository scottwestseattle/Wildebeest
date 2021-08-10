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
    }

    @Override
    public void onListFragmentInteraction(HistoryContent.HistoryItem item) {

    }
}
