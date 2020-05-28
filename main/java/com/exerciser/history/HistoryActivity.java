package com.exerciser.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.exerciser.R;
import com.exerciser.history.content.HistoryContent;

public class HistoryActivity extends AppCompatActivity  implements HistoryFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");
    }

    @Override
    public void onListFragmentInteraction(HistoryContent.HistoryItem item) {

    }
}
