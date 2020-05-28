package com.exerciser.history.content;

import android.util.Log;

import com.exerciser.RssReader;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class HistoryContent {

    /**
     * An array of sample items.
     */
    public static final List<HistoryItem> itemList = new ArrayList<HistoryItem>();

    /**
     * A map of sample items, by ID.
     */
    public static final Map<Integer, HistoryItem> itemMap = new HashMap<Integer, HistoryItem>();

    static {
        load();
    }

    private static void addItem(HistoryItem item) {
        itemList.add(item);
        itemMap.put(item.sessionId, item);
    }

    public static void load() {
        if (itemList.size() == 0) {
            String url = "https://learnfast.xyz/history/rss";
            Log.i("HistoryContent", "Getting history list from rss...");
            RssReader.fetchHistoryList(url, itemList);
        }
        else {
            Log.i("HistoryContent", "History already loaded");
        }
    }

    /**
     * A History item representing a piece of content.
     */
    public static class HistoryItem {
        public int seconds;
        public Date date;
        public String sessionName;
        public int sessionId;
        public String programName;
        public int programId;

        public HistoryItem(String programName, int programId, String sessionName, int sessionId, String sDate, int seconds) {
            this.programName = programName;
            this.programId = programId;
            this.sessionName = sessionName;
            this.sessionId = sessionId;
            this.seconds = seconds;

            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
            this.date = simpledateformat.parse(sDate + " GMT", pos);
        }

        @Override
        public String toString() {
            return programName;
        }
    }
}