package com.exerciser;

import android.util.Log;

import com.exerciser.exercises.content.ExerciseContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HandleXML {

    private String name = "";
    private int runSeconds = -1;
    private int breakSeconds = -1;
    private String description = "";
    private String imageName = "";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;
    List<ExerciseContent.ExerciseItem> exerciseItems = null;

    public HandleXML(String url, List<ExerciseContent.ExerciseItem> exerciseItems) {
        this.exerciseItems = exerciseItems;
        this.urlString = url;
        this.fetchXML();
    }

    public void fetchXML() {

        Thread thread = new Thread(new Runnable(){

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

                    exerciseItems.clear();
                    parse(myparser);

                    stream.close();
                }

                catch (Exception e) {
                    Log.i("xml:fetchXML", "exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join(); // wait for the thread to finish
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

                        if(name.equals("record")){
                            //Log.i("parse", "record end tag, save the record");

                            ExerciseContent.ExerciseItem ei = new ExerciseContent.ExerciseItem(
                                    this.name,
                                    this.description,
                                    this.imageName,
                                    this.runSeconds,
                                    this.breakSeconds,
                                    order++,
                                    this.description);

                            this.exerciseItems.add(ei);
                        }

                        else if (name.equals("name")){
                            this.name = text.trim();
                            Log.i("parse", "name value: " + text);
                        }

                        else if(name.equals("runSeconds")){
                            try {
                                this.runSeconds = testing ? testingRunSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }

                        else if(name.equals("breakSeconds")){
                            try {
                                this.breakSeconds = testing ? testingBreakSeconds : Integer.parseInt(text);
                            } catch(NumberFormatException nfe){}
                        }

                        else if(name.equals("description")){
                            this.description = text.trim();
                        }

                        else if(name.equals("imageName")){
                            // remove file extension so we can find the matching Android resource
                            //this.imageName = text.substring(0, text.lastIndexOf("."));
                            this.imageName = text;
                        }

                        else{
                            // skip all others
                        }

                        break;
                }

                event = myParser.next();

                if (testing && this.exerciseItems.size() >= testingExerciseCount)
                    break;
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