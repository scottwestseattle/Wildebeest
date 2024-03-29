package com.e.wildebeest.history.content;

import android.util.Log;

import com.e.wildebeest.R;
import com.e.wildebeest.RssReader;

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

    // dirty flag: unset when program list is updated / set when history is updated
    private static boolean mIsDirty = true;

    static {
        load();
    }

    public static boolean isDirty() {
        return mIsDirty;
    }

    public static void setDirty(boolean dirty) {
        mIsDirty = dirty;
    }

    public static void clear() {
        itemList.clear();
        itemMap.clear();
    }

    public static void addItem(String programName, int programId, String sessionName, int sessionId, Date date, int seconds) {
        addItem(new HistoryItem(programName, programId, sessionName, sessionId, date, seconds));
    }

    public static void addItem(HistoryItem item) {
        itemList.add(0, item);
        itemMap.put(item.sessionId, item);
        mIsDirty = true;
    }

    public static void load() {
        if (itemList.size() == 0) {
            Log.i("HistoryContent", "Getting history list from rss...");
            RssReader.fetchHistoryList(itemList);
            Log.i("HistoryContent", "History loaded from rss, count=" + itemList.size());
        }
        else {
            Log.i("HistoryContent", "History already loaded, count=" + itemList.size());
        }
    }

    public static HistoryItem getNewestItem(int programId) {
        HistoryItem item = null;
        HistoryContent.load();

        if (itemList.size() > 0)
        {
            for (HistoryItem record : HistoryContent.itemList)
            {
                if (record.programId == programId)
                {
                    item = record;
                    break;
                }
            }
        }

        return item;
    }

    public static int getBackgroundImageResourceId(int index) {
        int id = 0;
        int bgImagesResourceId[] = {
                R.drawable.bg_history_list_gradient,
                R.drawable.bg_history_list_2,
                R.drawable.bg_history_list_gradient,
                R.drawable.bg_history_list_gradient
        };

        if (index < bgImagesResourceId.length)
            id = bgImagesResourceId[index];

        return id;
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

        public HistoryItem(String programName, int programId, String sessionName, int sessionId, Date date, int seconds) {
            this.programName = programName;
            this.programId = programId;
            this.sessionName = sessionName;
            this.sessionId = sessionId;
            this.seconds = seconds;
            this.date = date;
        }

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
