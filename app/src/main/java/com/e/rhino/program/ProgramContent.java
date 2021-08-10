package com.e.rhino.program;

import android.content.Intent;
import android.util.Log;

import com.e.rhino.R;
import com.e.rhino.RssReader;
import com.e.rhino.Tools;
import com.e.rhino.history.HistoryActivity;
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
    private static final int _generateIdRange = 10000;

    public static boolean isGenerated(int id)
    {
        return (id >= _generateIdRange);
    }

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
            boolean generate = true;
            if (generate) {
                List<SessionContent.SessionItem> sessionItems = new ArrayList<SessionContent.SessionItem>();
                Map<Integer, SessionContent.SessionItem> sessionMap = new HashMap<Integer, SessionContent.SessionItem>();

                if (null != sessionItems) {

                    String title = "Intermediate Planking";
                    int programId = _generateIdRange; // need a real id that doesn't clash with RSS id's
                    int sessionId = 1;  // must start at 1 for Rest Day mod to work
                    int secondsPerExercise = 40;
                    int secondsBreak = 20;

                    for (int i = 0; i < 30; i++) {

                        boolean restDay = Tools.isRestDay(i + 1);
                        int exercises = restDay ? 1 : 12;
                        int seconds = restDay ? 0 : exercises * (secondsPerExercise + secondsBreak);

                        SessionContent.SessionItem sessionItem = new SessionContent.SessionItem(
                                sessionId,
                                "Day " + Integer.toString(sessionId),
                                Integer.toString(exercises) + " Planking exercises starting at 40 seconds each.",
                                i + 1,
                                title,
                                seconds,
                                exercises,
                                true
                        );

                        sessionItems.add(sessionItem);
                        sessionMap.put(sessionId++, sessionItem);
                    }

                    ProgramItem item = new ProgramItem(
                            programId,
                            title,
                            "30 day program with 12 assorted exercises per day, starting at 40 seconds each.",
                            3, // used for image id
                            sessionItems.size(),
                            sessionItems,
                            sessionMap
                    );

                    programList.add(item);

                    if (programList.size() == 1) { // if the generated program is the only one
                        programMap.put(programId, item);
                        RssReader.setProgram(programList, programMap);
                    }
                    else // adding the generated program to a rss loaded program
                    {
                        RssReader.addProgramMap(item, programId);
                    }
                }
            }
        }
        else {
            Log.i("ProgramContent", "Programs already loaded");
        }
    }

    static public boolean updateHistory()
    {
        boolean rc = false;

        if (HistoryContent.isDirty()) {

            rc = true;

            for (ProgramItem item : programList) {
                // reset to no next item
                item.sessionNext = -1;

                // figure out the next Session from the history records
                HistoryContent.HistoryItem newestItem = HistoryContent.getNewestItem(item.id);
                if (null != newestItem) {
                    SessionContent.SessionItem nextSession = RssReader.getNextSession(newestItem.programId, newestItem.sessionId);
                    if (null != nextSession) { // if not all sessions completed then show the next session
                        item.sessionNext = nextSession.id;
                    }
                }
            }

            HistoryContent.setDirty(false);
        }

        return rc;
    }
}
