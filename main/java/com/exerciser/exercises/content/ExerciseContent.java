package com.exerciser.exercises.content;

import android.util.Log;

import com.exerciser.HandleXML;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExerciseContent {

    public static final int startSeconds = 15;
    public static final int breakEndCountdownSeconds = 5;
    public static final int exerciseEndCountdownSeconds = 10;
    private static HandleXML xml = null;

    /**
     * The array of items from the rss feed
     */
    public static List<ExerciseItem> exerciseList = new ArrayList<ExerciseItem>();

    public ExerciseContent(int exerciseId)
    {
        String url = "https://learnfast.xyz/lessons/rss/" + exerciseId;
        Log.i("parse", "Get Exercises from RSS...");
        xml = new HandleXML(url, exerciseList);
    }

    public String getTotalTime() {

        float totalSeconds = 0.0f;

        Iterator<ExerciseItem> iterator = this.exerciseList.iterator();
        while (iterator.hasNext()) {
            ExerciseItem e = iterator.next();
            totalSeconds += e.runSeconds;
            totalSeconds += e.breakSeconds;
        }

        // format the seconds to look like: 13:10
        Date dt = new Date(((long) totalSeconds) * 1000);
        String time = new SimpleDateFormat("mm:ss").format(dt);

        return time;
    }

    private static void addItem(ExerciseItem item) {
        exerciseList.add(item);
    }

    public boolean isLoaded() {
        return (null != xml && xml.isLoaded());
    }

    /**
     * A program item representing a piece of content.
     */
    public static class ExerciseItem {
        public String name;
        public String description;
        public String imageName;
        public int runSeconds;
        public int breakSeconds;
        public int order;
        public String instructions;

        public ExerciseItem(String name, String description, String imageName, int runSeconds, int breakSeconds, int order, String instructions) {
            this.name = name;
            this.imageName = imageName;
            this.description = description;
            this.runSeconds = runSeconds;
            this.breakSeconds = breakSeconds;
            this.order = order;
            this.instructions = instructions;
        }

        public boolean isFirst() {
            return this.order <= 1;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}