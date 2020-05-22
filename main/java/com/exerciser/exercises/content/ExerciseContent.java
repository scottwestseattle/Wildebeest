package com.exerciser.exercises.content;

import android.util.Log;

import com.exerciser.RssReader;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ExerciseContent {

    public static final int startSeconds = 15;

    /**
     * The array of items from the rss feed
     */
    public static List<ExerciseItem> exerciseList = new ArrayList<ExerciseItem>();

    public ExerciseContent(int exerciseId)
    {
        String url = "https://learnfast.xyz/lessons/rss/" + exerciseId;
        Log.i("parse", "Get Exercises from RSS...");
        RssReader.fetchExerciseList(url, exerciseList);

        if (getTotalSeconds() == 0)
        {
            // generate random exercises
            generate();
            //generateWild();
        }
    }

    public enum eInstructionType {
        none,
        switchLeg,
        switchSide
    }

    private final int mTypeStarter1     = 0;
    private final int mTypeStarter2     = 1;
    private final int mTypeStarter3     = 2;
    private final int mTypeAbs          = 3;
    private final int mTypeReverse      = 4;
    private final int mTypeFlex         = 5;
    private final int mTypeFixed        = 6;
    private final int mTypeSideLeft     = 7;
    private final int mTypeSideRight    = 8;
    private final int mTypeCloser       = 9;

    private List<ExerciseItem> load(int type)
    {
        List<ExerciseItem> list = new ArrayList<ExerciseItem>();
        int index = 0;
        int seconds = 60;
        switch(type)
        {
            case mTypeStarter1:
                list.add(new ExerciseItem("Dolphin Plank", 40, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Dolphin Plank with leg lift", 40, index++, type, eInstructionType.switchLeg));
                break;
            case mTypeStarter2:
                list.add(new ExerciseItem("Plank", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plank Cross Tap", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plank with leg lift", seconds, index++, type, eInstructionType.switchLeg));
                break;
            case mTypeStarter3:
                list.add(new ExerciseItem("Downward Dog", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Downward Dog Elbows", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Downward Dog Knees Bent", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Scissor", seconds, index++, type, eInstructionType.switchLeg));
                break;
            case mTypeAbs:
                seconds = 40;
                list.add(new ExerciseItem("Ab Scissors", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Bicycle Abs", seconds, index++, type, eInstructionType.none));
                //list.add(new ExerciseItem("Boat", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Cat", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Leg Lift", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Sprinter Situp", seconds, index++, type, eInstructionType.none));
                break;
            case mTypeReverse:
                list.add(new ExerciseItem("Bridge", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Bridge with leg lift", seconds, index++, type, eInstructionType.switchLeg));
                list.add(new ExerciseItem("Reverse Plank", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Reverse Table", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Reverse Table with leg lift", seconds, index++, type, eInstructionType.switchLeg));
                break;
            case mTypeFlex:
                list.add(new ExerciseItem("Balancing Cat", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Chair", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Fire Hydrant", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Half Bow", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Lord of the Dance", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Lunge Stretch", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plow", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plow knees bent", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Runners Lunge", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Skiers Lunge", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Squat", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Standing Glute Iso", seconds * 2, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 1", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 2", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 3", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Windmill", seconds, index++, type, eInstructionType.none));
                break;
            case mTypeFixed:
                list.add(new ExerciseItem("Curls", seconds * 2, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Push-ups", seconds, index++, type, eInstructionType.none));
                break;
            case mTypeSideLeft:
                list.add(new ExerciseItem("Side Plank Elbow Left", 40, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Side Plank Left", 30, index++, type, eInstructionType.none));
                break;
            case mTypeSideRight:
                list.add(new ExerciseItem("Side Plank Elbow Right", 40, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Side Plank Right", 30, index++, type, eInstructionType.none));
                break;
            case mTypeCloser:
                seconds = 45;
                list.add(new ExerciseItem("Bow", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Child", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Cobra", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Happy Baby", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Squatting Buddha", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Standing Forward Bend", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Superman", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Tree", seconds, index++, type, eInstructionType.switchSide));
                break;
            default:
                break;
        }

        return list;
    }

    private void generate() {
        exerciseList.clear();
        List<ExerciseItem> starter1 = load(mTypeStarter1);
        List<ExerciseItem> starter2 = load(mTypeStarter2);
        List<ExerciseItem> starter3 = load(mTypeStarter3);
        List<ExerciseItem> abs = load(mTypeAbs);
        List<ExerciseItem> reverse = load(mTypeReverse);
        List<ExerciseItem> flex = load(mTypeFlex);
        List<ExerciseItem> sideLeft = load(mTypeSideLeft);
        List<ExerciseItem> sideRight = load(mTypeSideRight);
        List<ExerciseItem> fixed = load(mTypeFixed);
        List<ExerciseItem> closer = load(mTypeCloser);

        int random = new Random().nextInt(100);

        // 12 exercises
        if (random >= 50) {
            add(getRandom(starter2));
            add(getRandom(abs));
            add(getRandom(flex));
            add(getRandom(reverse));

            add(getRandom(starter1));
            add(fixed.get(0));
            ExerciseItem itemSideLeft = getRandom(sideLeft); // needed in order to get it's position
            int order = itemSideLeft.order;
            add(itemSideLeft);
            add(sideRight.get(order)); // match up the left and right sides

            add(getRandom(starter3));
            add(getRandom(flex));
            add(getRandom(closer));
            add(fixed.get(1));
        }
        else
        {
            add(getRandom(starter1));
            add(getRandom(flex));
            add(getRandom(fixed));
            add(getRandom(reverse));

            add(getRandom(starter2));
            add(getRandom(abs));
            ExerciseItem itemSideLeft = getRandom(sideLeft); // needed in order to get it's position
            int order = itemSideLeft.order;
            add(itemSideLeft);
            add(sideRight.get(order)); // match up the left and right sides

            add(getRandom(starter3));
            add(getRandom(flex));
            add(getRandom(fixed));
            add(getRandom(closer));
        }
    }

    private void add(ExerciseItem item)
    {
        // fix order
        item.order = exerciseList.size() + 1;
        exerciseList.add(item);
    }

    private void generateWild() {
        exerciseList.clear();
        List<ExerciseItem> starter1 = load(mTypeStarter1);
        List<ExerciseItem> starter2 = load(mTypeStarter2);
        List<ExerciseItem> starter3 = load(mTypeStarter3);
        List<ExerciseItem> abs = load(mTypeAbs);
        List<ExerciseItem> reverse = load(mTypeReverse);
        List<ExerciseItem> flex = load(mTypeFlex);
        List<ExerciseItem> sideLeft = load(mTypeSideLeft);
        List<ExerciseItem> sideRight = load(mTypeSideRight);
        List<ExerciseItem> fixed = load(mTypeFixed);
        List<ExerciseItem> closer = load(mTypeCloser);
        List<ExerciseItem> all = new ArrayList<ExerciseItem>();

        all.addAll(starter1);
        all.addAll(starter2);
        all.addAll(starter3);
        all.addAll(abs);
        all.addAll(reverse);
        all.addAll(flex);
        all.addAll(sideLeft);
        all.addAll(sideRight);
        all.addAll(fixed);
        all.addAll(closer);

        int order = 0;
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
        add(getRandom(all));
    }

    private ExerciseItem getRandom(List<ExerciseItem> list) {
        int index = new Random().nextInt(list.size());
        ExerciseItem item = list.get(index);
        int i = list.size();
        while (item.mUsed)
        {
            index++;
            if (index >= list.size())
                index = 0;

            item = list.get(index);

            if (--i < 0)
                break; // don't loop forever
        }

        item.mUsed = true;

        return item;
    }

    public long getTotalSeconds() {
        long totalSeconds = 0;

        Iterator<ExerciseItem> iterator = this.exerciseList.iterator();
        while (iterator.hasNext()) {
            ExerciseItem e = iterator.next();
            totalSeconds += e.runSeconds;
            totalSeconds += e.breakSeconds;
        }

        return totalSeconds;
    }

    public String getTotalTime() {

        long totalSeconds = getTotalSeconds();

        // format the seconds to look like: 13:10
        Date dt = new Date((totalSeconds) * 1000);
        String time = new SimpleDateFormat("mm:ss").format(dt);

        return time;
    }

    private static void addItem(ExerciseItem item) {
        exerciseList.add(item);
    }

    public boolean isLoaded() {
        return (RssReader.isLoaded());
    }

    /**
     * A program item representing a piece of content.
     */
    public static class ExerciseItem {
        public String name;
        public String description;
        public String imageName;
        public int runSeconds;
        public int breakSeconds = 20;
        public int order;
        public String instructions = "";
        public int type = 0;
        public eInstructionType mInstructionType = eInstructionType.none;
        public boolean mUsed = false;

        public ExerciseItem(String name, int runSeconds, int order, int type, eInstructionType instructions) {
            this.name = name;
            this.imageName = getImageName(name);
            this.runSeconds = runSeconds;
            this.order = order;
            this.type = type;
            this.mInstructionType = instructions;
        }

        public ExerciseItem(String name, String description, String imageName, int runSeconds, int breakSeconds, int order, String instructions) {
            this.name = name;
            this.imageName = getImageName(name);
            this.description = description;
            this.runSeconds = runSeconds;
            this.breakSeconds = breakSeconds;
            this.order = order;
            this.instructions = instructions;
        }

        private String getImageName(String name) {
            String imageName = name.toLowerCase().replaceAll(" ", "_");
            imageName = imageName.replaceAll("-", "_");
            return imageName;
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