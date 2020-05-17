package com.exerciser.sessions.content;

import android.util.Log;

import com.exerciser.RssReader;

import java.util.ArrayList;
import java.util.List;

public class SessionContent {

    public static int courseId = -1;
    public static List<SessionContent.SessionItem> sessionList = new ArrayList<SessionItem>();

    public SessionContent(int courseId)
    {
        //
        // todo: do the rss read again here because I couldn't figure out how to pass in the data from the programs read
        //
        sessionList.clear();
        this.courseId = courseId;
        String url = "https://learnfast.xyz/courses/rss";
        Log.i("parse", "Getting Session list from rss...");
        RssReader.fetchSessionList(url, sessionList, courseId);
    }

    private static void addItem(SessionItem item) {
        sessionList.add(item);
    }

    /**
     * A Session Item
     */
    public static class SessionItem {
        public final int id;
        public final int number;
        public final String name;
        public final String description;
        public final String parent;
        public final int seconds;
        public final int exerciseCount;

        public SessionItem(int id, String name, String description, int number, String parent, int seconds, int exerciseCount) {
            this.id = id;
            this.number = number;
            this.name = name;
            this.description = description;
            this.parent = parent;
            this.seconds = seconds;
            this.exerciseCount = exerciseCount;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
