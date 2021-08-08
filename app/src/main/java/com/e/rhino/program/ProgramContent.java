package com.e.rhino.program;

import android.util.Log;

import com.e.rhino.R;
import com.e.rhino.RssReader;
import com.e.rhino.history.content.HistoryContent;
import com.e.rhino.sessions.content.SessionContent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramContent {

    /**
     * The array of items from the rss feed
     */
    public static List<ProgramItem> programList = new ArrayList<ProgramItem>();
    public static Map<Integer, ProgramItem> programMap = new HashMap<Integer, ProgramItem>();

    public static int getBackgroundImageResourceId(int index) {
        int id = 0;
        int bgImagesResourceId[] = {
                R.drawable.bg_1,
                R.drawable.bg_2,
                R.drawable.bg_0,
                R.drawable.bg_3,
                R.drawable.bg_4,
                R.drawable.bg_4,
                R.drawable.bg_4,
                R.drawable.bg_4
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

            // load the regular rss stuff
            boolean useRss = true;
            if (useRss) {
                Log.i("ProgramContent", "Getting program list from rss...");
                RssReader.fetchProgramList(programList);
            }

            // load the generated exercise program that uses all
            boolean useGen = true; // sbw
            if (useGen) {
                List<SessionContent.SessionItem> sessionItems = new ArrayList<SessionContent.SessionItem>();
                Map<Integer, SessionContent.SessionItem> sessionMap = new HashMap<Integer, SessionContent.SessionItem>();
                int sessionId = 1;

                if (null != sessionItems) {

                    String title = "Intermediate Planking";

                    for (int i = 0; i < 30; i++) {
                        SessionContent.SessionItem sessionItem = new SessionContent.SessionItem(
                                sessionId++,
                                "Day " + (i + 1),
                                "12 Planking exercises starting at 40 seconds each.",
                                i + 1,
                                title,
                                0,
                                0
                        );

                        sessionItems.add(sessionItem);
                        sessionMap.put(sessionId, sessionItem);
                    }

                    ProgramItem item = new ProgramItem(
                            0,
                            title,
                            "30 day program with 12 assorted exercises per day, starting at 40 seconds each.",
                            1, // used for image id
                            sessionItems.size(),
                            sessionItems,
                            sessionMap
                    );

                    programList.add(item);

                    if (programList.size() == 1) { // if the generated program is the only one
                        programMap.put(0, item);
                        RssReader.setProgram(programList, programMap);
                    }
                    else // adding the generated program to a rss loaded program
                    {
                        RssReader.addProgramMap(item, 0);
                    }
                }
            }
        }
        else {
            Log.i("ProgramContent", "Programs already loaded");
        }
    }
}
