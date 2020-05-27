package com.exerciser.Program;

import android.util.Log;

import com.exerciser.RssReader;

import java.util.ArrayList;
import java.util.List;

public class ProgramContent {

    /**
     * The array of items from the rss feed
     */
    public static List<ProgramItem> programList = new ArrayList<ProgramItem>();

    ProgramContent()
    {
        Log.i("parse", "ProgramContent started");
    }

    static {
        if (programList.size() == 0) {
            String url = "https://learnfast.xyz/courses/rss";
            Log.i("ProgramContent", "Getting program list from rss...");
            RssReader.fetchProgramList(url, programList);
        }
        else {
            Log.i("ProgramContent", "Programs already loaded");
        }
    }
}
