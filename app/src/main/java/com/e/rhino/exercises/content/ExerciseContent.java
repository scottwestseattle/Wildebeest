package com.e.rhino.exercises.content;

import android.util.Log;

import com.e.rhino.RssReader;
import com.e.rhino.Tools;
import com.e.rhino.program.ProgramContent;
import com.e.rhino.program.ProgramItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ExerciseContent {

    public static final int startSeconds = 15;
    private int _programId = -1;
    private int _sessionId = -1;

    /**
     * The array of items from the rss feed
     */
    public static List<ExerciseItem> exerciseList = new ArrayList<ExerciseItem>();

    public ExerciseContent(int programId, int sessionId)
    {
        _programId = programId;
        _sessionId = sessionId;

        if (ProgramContent.isGenerated(_programId)) {
            generate(sessionId); //todo: new code that will cycle through each exercise

        }
        else {
            Log.i("parse", "Get Exercises from RSS...");
            RssReader.fetchExerciseList(sessionId, exerciseList);
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
    private final int mTypeKnees        = 5;
    private final int mTypeStanding     = 6;
    private final int mTypeFixed        = 7;
    private final int mTypeSideLeft     = 8;
    private final int mTypeSideRight    = 9;
    private final int mTypeCloser       = 10;

    private List<ExerciseItem> load(int type, int seconds)
    {
        List<ExerciseItem> list = new ArrayList<ExerciseItem>();
        int index = 0;
        int secondsShort = seconds - 10;
        switch(type)
        {
            case mTypeStarter1:
                list.add(new ExerciseItem("Dolphin Plank", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Dolphin Plank with leg lift", seconds, index++, type, eInstructionType.switchLeg));
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

            case mTypeKnees:
                list.add(new ExerciseItem("Balancing Cat", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Chair", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Fire Hydrant", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Half Bow", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Squat", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Lunge Stretch", seconds, index++, type, eInstructionType.none));

            case mTypeStanding:
                list.add(new ExerciseItem("Lord of the Dance", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Standing Glute Iso", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Tree", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 1", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 2", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Warrior 3", seconds, index++, type, eInstructionType.switchSide));
                list.add(new ExerciseItem("Windmill", seconds, index++, type, eInstructionType.none));
                break;

            case mTypeFixed:
                list.add(new ExerciseItem("Curls", 120, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Push-ups", 60, index++, type, eInstructionType.none));
                break;

            case mTypeSideLeft:
                list.add(new ExerciseItem("Side Plank Elbow Left", secondsShort, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plow", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Side Plank Left", secondsShort, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Plow knees bent", seconds, index++, type, eInstructionType.none));
                break;

            case mTypeSideRight:
                list.add(new ExerciseItem("Side Plank Elbow Right", secondsShort, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Skiers Lunge", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Side Plank Right", secondsShort, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Runners Lunge", seconds, index++, type, eInstructionType.none));
                break;

            case mTypeCloser:
                list.add(new ExerciseItem("Bow", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Child", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Cobra", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Happy Baby", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Squatting Buddha", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Standing Forward Bend", seconds, index++, type, eInstructionType.none));
                list.add(new ExerciseItem("Superman", seconds, index++, type, eInstructionType.switchSide));
                break;
            default:
                break;
        }

        return list;
    }

    private void generate(int sessionId) {
        exerciseList.clear();

        int seconds = 40;

        List<ExerciseItem> dolphinPlank = load(mTypeStarter1, seconds);
        List<ExerciseItem> plank = load(mTypeStarter2, seconds);
        List<ExerciseItem> downwardDog = load(mTypeStarter3, seconds);
        List<ExerciseItem> abs = load(mTypeAbs, seconds);
        List<ExerciseItem> reverse = load(mTypeReverse, seconds);
        List<ExerciseItem> knees = load(mTypeKnees, seconds);
        List<ExerciseItem> standing = load(mTypeStanding, seconds);
        List<ExerciseItem> sideLeft = load(mTypeSideLeft, seconds);
        List<ExerciseItem> sideRight = load(mTypeSideRight, seconds);
        List<ExerciseItem> fixed = load(mTypeFixed, seconds);
        List<ExerciseItem> closer = load(mTypeCloser, seconds);

        int index = sessionId - 1;
        int indexPushups = 1;

        // 12 exercises

        add(getIndex(plank, index));
        add(getIndex(abs, index));

        // Sides
        ExerciseItem itemSideLeft = getIndex(sideLeft, index); // needed in order to get it's position
        int order = itemSideLeft.order;
        add(itemSideLeft);
        add(sideRight.get(order)); // match up the left and right sides

        add(fixed.get(indexPushups));
        add(getIndex(dolphinPlank, index));
        add(getIndex(reverse, index));
        add(getIndex(knees, index));
        add(getIndex(downwardDog, index));
        add(getIndex(standing, index));
        add(fixed.get(indexPushups));
        add(getIndex(closer, index));
    }

    private void generateRandom() {
        exerciseList.clear();

        int seconds = 60;
        List<ExerciseItem> starter1 = load(mTypeStarter1, seconds);
        List<ExerciseItem> starter2 = load(mTypeStarter2, seconds);
        List<ExerciseItem> starter3 = load(mTypeStarter3, seconds);
        List<ExerciseItem> abs = load(mTypeAbs, seconds);
        List<ExerciseItem> reverse = load(mTypeReverse, seconds);
        List<ExerciseItem> flex = load(mTypeStanding, seconds);
        List<ExerciseItem> sideLeft = load(mTypeSideLeft, seconds);
        List<ExerciseItem> sideRight = load(mTypeSideRight, seconds);
        List<ExerciseItem> fixed = load(mTypeFixed, seconds);
        List<ExerciseItem> closer = load(mTypeCloser, seconds);

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
        int seconds = 60;
        List<ExerciseItem> starter1 = load(mTypeStarter1, seconds);
        List<ExerciseItem> starter2 = load(mTypeStarter2, seconds);
        List<ExerciseItem> starter3 = load(mTypeStarter3, seconds);
        List<ExerciseItem> abs = load(mTypeAbs, seconds);
        List<ExerciseItem> reverse = load(mTypeReverse, seconds);
        List<ExerciseItem> flex = load(mTypeStanding, seconds);
        List<ExerciseItem> sideLeft = load(mTypeSideLeft, seconds);
        List<ExerciseItem> sideRight = load(mTypeSideRight, seconds);
        List<ExerciseItem> fixed = load(mTypeFixed, seconds);
        List<ExerciseItem> closer = load(mTypeCloser, seconds);
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

    private ExerciseItem getIndex(List<ExerciseItem> list, int index) {

        // wrap the index around based on size of list
        if (index >= list.size())
            index = index % list.size();

        ExerciseItem item = list.get(index);

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
        return Tools.getTimeFromSeconds(getTotalSeconds());
    }

    private static void addItem(ExerciseItem item) {
        exerciseList.add(item);
    }

    public boolean isLoaded() {
        if (ProgramContent.isGenerated(_programId))
            // generated, nothing to load
            return true;
        else
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
        public int runSecondsOverride = 20;
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

            //todo test: temporary to get started, set all seconds to 20 secs
            if (false) {
                this.runSeconds = this.runSecondsOverride;
            }
        }

        public ExerciseItem(String name, String description, String imageName, int runSeconds, int breakSeconds, int order, String instructions) {
            this.name = name;
            this.imageName = getImageName(name);
            this.description = description;
            this.runSeconds = runSeconds;
            this.breakSeconds = breakSeconds;
            this.order = order;
            this.instructions = instructions;

            //todo test: temporary to get started
            if (false) {
                this.runSeconds = this.runSecondsOverride;
            }
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

