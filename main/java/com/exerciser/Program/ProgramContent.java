package com.exerciser.Program;

import android.util.Log;

import com.exerciser.R;
import com.exerciser.RssReader;
import com.exerciser.history.content.HistoryContent;
import com.exerciser.sessions.content.SessionContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramContent {

    /**
     * The array of items from the rss feed
     */
    public static List<ProgramItem> programList = new ArrayList<ProgramItem>();

    public static int getBackgroundImageResourceId(int index) {
        int id = 0;
        int bgImagesResourceId[] = {
                R.drawable.bg_1,
                R.drawable.bg_2,
                R.drawable.bg_0,
                R.drawable.bg_3
        };

        if (index < bgImagesResourceId.length)
            id = bgImagesResourceId[index];

        return id;
    }

    ProgramContent()
    {
        Log.i("parse", "ProgramContent started");
    }

    static {
        if (programList.size() == 0) {
            Log.i("ProgramContent", "Getting program list from rss...");
            RssReader.fetchProgramList(programList);
        }
        else {
            Log.i("ProgramContent", "Programs already loaded");
        }
    }
}
