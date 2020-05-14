package com.exerciser.Program;

import android.util.Log;

import com.exerciser.RssReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProgramContent {

    private static RssReader rss = null;

    /**
     * The array of items from the rss feed
     */
    public static List<ProgramContent.ProgramItem> programList = new ArrayList<ProgramContent.ProgramItem>();

    ProgramContent()
    {
    }

    static {
        String url = "https://learnfast.xyz/courses/rss";
        Log.i("parse", "Getting program list from rss...");
        rss = new RssReader(url);
        rss.fetchProgramList(programList);
    }

    private static void addItem(ProgramItem item) {
        programList.add(item);
    }

    private static ProgramItem createProgramItem(int id, String name, String description, int imageId, int sessionCount) {
        return new ProgramItem(id, name, description, imageId, sessionCount);
    }

    public boolean isLoaded() {
        return (null != rss && rss.isLoaded());
    }


    /**
     * An item representing one program
     */
    public static class ProgramItem {
        public final int id;
        public final String name;
        public final String description;
        public final int imageId;
        public final int sessionCount;

        public ProgramItem(int id, String name, String description, int imageId, int sessionCount) {
            this.id = id;
            this.name = name;
            this.imageId = imageId;
            this.description = description;
            this.sessionCount = sessionCount;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
