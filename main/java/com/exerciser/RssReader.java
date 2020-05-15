package com.exerciser;

import android.util.Log;

import com.exerciser.Program.ProgramContent;
import com.exerciser.sessions.content.SessionContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RssReader {

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

    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;

    List<ProgramContent.ProgramItem> programItems = null;
    List<SessionContent.SessionItem> sessionItems = null;

    public RssReader(String url) {
        this.urlString = url;
    }

    public void fetchProgramList(List<ProgramContent.ProgramItem> programItems) {
        this.programItems = programItems;
        fetchXML();
    }

    public void fetchSessionList(List<SessionContent.SessionItem> sessionItems, int courseId) {
        this.sessionItems = sessionItems;
        this.courseId = courseId;
        fetchXML();
    }

    public void fetchXML() {

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
                        else{
                            // skip all others
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