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

    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;
    public String urlString = "";

    private String programName = "";
    private int programId = -1;
    private String programDescription = "";
    private int sessionCount = 0;

    private int courseId = -1;
    private int sessionId = -1;
    private int sessionNumber = -1;
    private String sessionName = "";
    private String sessionDescription = "";
    private int sessionExerciseCount = -1;
    private int sessionSeconds = -1;
    private String sessionParentName = "";

    private String exerciseName = "";
    private int exerciseRunSeconds = -1;
    private int exerciseBreakSeconds = -1;
    private String exerciseDescription = "";
    private String exerciseImageName = "";

    List<ProgramContent.ProgramItem> programItems = null;
    List<SessionContent.SessionItem> sessionItems = null;
    List<ExerciseContent.ExerciseItem> exerciseItems = null;

    public RssReader()
    {
    }

    public void fetchProgramList(String url, List<ProgramContent.ProgramItem> items) {
        items.clear();
        this.programItems = items;
        fetchXML(url);
    }

    public void fetchSessionList(String url, List<SessionContent.SessionItem> items, int courseId) {
        items.clear();
        this.sessionItems = items;
        this.courseId = courseId;
        fetchXML(url);
    }

    public void fetchExerciseList(String url, List<ExerciseContent.ExerciseItem> items) {
        items.clear();
        this.exerciseItems = items;
        fetchXML(url);
    }

    public void fetchXML(String url) {

        this.urlString = url;

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
                    Log.i("xml:fetchXML", "exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join(); // wait for the thread to finish
            //thread.join(10000);
        } catch (InterruptedException e) {
            Log.e("rss thread", e.getMessage());
            e.printStackTrace();
        }
    }

    public void parse(XmlPullParser myParser) {
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
                            if (null != this.programItems) {
                                ProgramContent.ProgramItem item = new ProgramContent.ProgramItem(
                                        this.programId,
                                        this.programName,
                                        this.programDescription,
                                        -1,
                                        this.sessionCount
                                );

                                this.programItems.add(item);

                                this.sessionCount = 0; // re-start the count
                            }
                        }
                        else if (name.equals("course_name")){
                            this.programName = text.trim();
                        }
                        else if(name.equals("course_id")){
                            try {
                                this.programId = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("course_description")){
                            this.programDescription = text.trim();
                        }
                        //
                        // the 'lesson' block
                        //
                        else if(name.equals("lesson")){

                            this.sessionCount++; // count the session from the program list

                            if (null != this.sessionItems && -1 != this.courseId && this.programId == this.courseId) {
                                SessionContent.SessionItem item = new SessionContent.SessionItem(
                                        this.sessionId,
                                        this.sessionName,
                                        this.sessionDescription,
                                        this.sessionNumber,
                                        this.sessionParentName,
                                        this.sessionSeconds,
                                        this.sessionExerciseCount
                                );

                                this.sessionItems.add(item);
                            }
                        }
                        else if(name.equals("lesson_id")){
                            try {
                                this.sessionId = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if (name.equals("lesson_name")){
                            this.sessionName = text.trim();
                        }
                        else if(name.equals("lesson_description")){
                            this.sessionDescription = text.trim();
                        }
                        else if(name.equals("lesson_parent")){
                            this.sessionParentName = text.trim();
                        }
                        else if(name.equals("lesson_number")){
                            try {
                                this.sessionNumber = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("lesson_exercise_count")){
                            try {
                                this.sessionExerciseCount = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("lesson_seconds")){
                            try {
                                this.sessionSeconds = Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        //
                        // the 'exercise' block
                        //
                        if(name.equals("record")){
                            //Log.i("parse", "record end tag, save the record");
                            ExerciseContent.ExerciseItem ei = new ExerciseContent.ExerciseItem(
                                    this.exerciseName,
                                    this.exerciseDescription,
                                    this.exerciseImageName,
                                    this.exerciseRunSeconds,
                                    this.exerciseBreakSeconds,
                                    order++,
                                    this.exerciseDescription);

                            this.exerciseItems.add(ei);
                        }
                        else if (name.equals("name")){
                            this.exerciseName = text.trim();
                            Log.i("parse", "name value: " + text);
                        }
                        else if(name.equals("runSeconds")){
                            try {
                                this.exerciseRunSeconds = testing ? testingRunSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("breakSeconds")){
                            try {
                                this.exerciseBreakSeconds = testing ? testingBreakSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }
                        else if(name.equals("description")){
                            this.exerciseDescription = text.trim();
                        }
                        else if(name.equals("imageName")){
                            // remove file extension so we can find the matching Android resource
                            //this.imageName = text.substring(0, text.lastIndexOf("."));
                            this.exerciseImageName = text;
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

    public boolean isLoaded()
    {
        return this.parsingComplete;
    }
}