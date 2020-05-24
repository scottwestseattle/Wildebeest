package com.exerciser;

import android.util.Log;

import com.exerciser.Program.ProgramContent;
import com.exerciser.exercises.content.ExerciseContent;
import com.exerciser.sessions.content.SessionContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RssReader {

    static private XmlPullParserFactory xmlFactoryObject;
    static private volatile boolean parsingComplete = false;
    static public String urlString = "";

    static private String programName = "";
    static private int programId = -1;
    static private String programDescription = "";
    static private int sessionCount = 0;

    static private int sessionCourseId = -1;
    static private int sessionId = -1;
    static private int sessionNumber = -1;
    static private String sessionName = "";
    static private String sessionDescription = "";
    static private int sessionExerciseCount = -1;
    static private int sessionSeconds = -1;
    static private String sessionParentName = "";

    static private String exerciseName = "";
    static private int exerciseRunSeconds = -1;
    static private int exerciseBreakSeconds = -1;
    static private String exerciseDescription = "";
    static private String exerciseImageName = "";

    static List<ProgramContent.ProgramItem> programItems = null;
    static List<SessionContent.SessionItem> sessionItems = null;
    static List<ExerciseContent.ExerciseItem> exerciseItems = null;

    private RssReader()
    {
        // only allow static use
    }

    static public void fetchProgramList(String url, List<ProgramContent.ProgramItem> items) {
        items.clear();
        programItems = items;
        sessionCourseId = -1; // flag that we're not loading sessions

        fetchXML(url);
    }

    static public void fetchSessionList(String url, List<SessionContent.SessionItem> items, int id) {
        items.clear();
        sessionItems = items;
        sessionCourseId = id;

        fetchXML(url);
    }

    static public void fetchExerciseList(String url, List<ExerciseContent.ExerciseItem> items) {
        items.clear();
        exerciseItems = items;

        fetchXML(url);
    }

    static public void fetchXML(String url) {

        parsingComplete = false;
        urlString = url;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parse(myparser);

                    stream.close();
                } catch (Exception e) {
                    Log.i("RssReader:fetchXML", "exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join(); // wait for the thread to finish
        } catch (InterruptedException e) {
            Log.e("RssReader:thread.join()", e.getMessage());
            e.printStackTrace();
        }
    }

    static public void parse(XmlPullParser myParser) {
        int event;
        String text = null;
        int order = 1;

        // testing data
        boolean testing = false;
        int testingRunSeconds = 3;
        int testingBreakSeconds = 3;
        int testingExerciseCount = 1;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                String name = myParser.getName();
                //Log.i("parse", "name tag: " + name);

                switch (event) {

                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        //
                        // the 'course' block
                        //
                        if(name.equals("course")){
                            if (sessionCourseId <= 0) { // make sure we're not loading sessions
                                ProgramContent.ProgramItem item = new ProgramContent.ProgramItem(
                                        programId,
                                        programName,
                                        programDescription,
                                        -1,
                                        sessionCount
                                );

                                programItems.add(item);

                                sessionCount = 0; // re-start the count
                            }
                        }
                        else if (name.equals("course_name")){
                            programName = text.trim();
                        }
                        else if(name.equals("course_id")){
                            try {
                                programId = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("course_description")){
                            programDescription = text.trim();
                        }
                        //
                        // the 'lesson' block
                        //
                        else if(name.equals("lesson")){

                            sessionCount++; // count the session from the program list

                            if (null != sessionItems && sessionCourseId > 0 && programId == sessionCourseId) {
                                SessionContent.SessionItem item = new SessionContent.SessionItem(
                                        sessionId,
                                        sessionName,
                                        sessionDescription,
                                        sessionNumber,
                                        sessionParentName,
                                        sessionSeconds,
                                        sessionExerciseCount
                                );

                                sessionItems.add(item);
                            }
                        }
                        else if(name.equals("lesson_id")){
                            try {
                                sessionId = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if (name.equals("lesson_name")){
                            sessionName = text.trim();
                        }
                        else if(name.equals("lesson_description")){
                            sessionDescription = text.trim();
                        }
                        else if(name.equals("lesson_parent")){
                            sessionParentName = text.trim();
                        }
                        else if(name.equals("lesson_number")){
                            try {
                                sessionNumber = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("lesson_exercise_count")){
                            try {
                                sessionExerciseCount = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("lesson_seconds")){
                            try {
                                sessionSeconds = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        //
                        // the 'exercise' block
                        //
                        if(name.equals("record")){
                            //Log.i("parse", "record end tag, save the record");
                            ExerciseContent.ExerciseItem ei = new ExerciseContent.ExerciseItem(
                                    exerciseName,
                                    exerciseDescription,
                                    exerciseImageName,
                                    exerciseRunSeconds,
                                    exerciseBreakSeconds,
                                    order++,
                                    exerciseDescription);

                            exerciseItems.add(ei);
                        }
                        else if (name.equals("name")){
                            exerciseName = text.trim();
                            Log.i("parse", "name value: " + text);
                        }
                        else if(name.equals("runSeconds")){
                            try {
                                exerciseRunSeconds = testing ? testingRunSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("breakSeconds")){
                            try {
                                exerciseBreakSeconds = testing ? testingBreakSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("description")){
                            exerciseDescription = text.trim();
                        }
                        else if(name.equals("imageName")){
                            // remove file extension so we can find the matching Android resource
                            //imageName = text.substring(0, text.lastIndexOf("."));
                            exerciseImageName = text;
                        }

                        //
                        // skip all others
                        //
                        else{
                            // skip
                        }

                        break;
                }

                event = myParser.next();
            }

            parsingComplete = true;
        }

        catch (Exception e) {
            Log.i("xml:parse", "exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static public boolean isLoaded()
    {
        return parsingComplete;
    }
}