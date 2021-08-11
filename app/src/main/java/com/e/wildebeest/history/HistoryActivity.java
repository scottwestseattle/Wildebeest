package com.e.wildebeest.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.e.wildebeest.R;
import com.e.wildebeest.history.content.HistoryContent;
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
