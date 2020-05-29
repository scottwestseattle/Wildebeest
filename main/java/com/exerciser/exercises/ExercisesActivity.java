package com.exerciser.exercises;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.exerciser.R;
import com.exerciser.RssReader;
import com.exerciser.Speech;
import com.exerciser.UserPreferences;
import com.exerciser.exercises.content.ExerciseContent;
import com.exerciser.history.content.HistoryContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class ExercisesActivity extends AppCompatActivity  implements StartFragment.OnListFragmentInteractionListener {

    private Toolbar mToolbar;
    public static ExerciseContent exercises = null;
    public int currentExerciseIndex = -1;
    public int programId = -1;
    public String programName = "";
    public int sessionId = -1;
    public String sessionName = "";

    @Override
    public void onListFragmentInteraction(ExerciseContent.ExerciseItem exerciseItem) {
        //
        // todo: handle click on an item in the Exercise list: edit exercise
        //
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the data
        loadSessionInfo(getIntent());

        // get the data
        exercises = new ExerciseContent(this.sessionId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        // set title and subtitle
        String title = programName + ": " + this.sessionName;
        String subTitle = exercises.exerciseList.size() + " exercises, Total Time: " + exercises.getTotalTime();

        ActionBar ab = getSupportActionBar();
        ab.setTitle(title);
        ab.setSubtitle(subTitle);

        // load the first fragment
        loadFragment("StartFragment");

        //
        // set up the bottom fab buttons
        //
        FloatingActionButton fabPlay = findViewById(R.id.fabPlay);
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the active fragment so we know which action to perform
                Fragment fragment = getActiveFragment();

                if (fragment instanceof StartFragment) {
                    loadFragment("BreakFragment");
                } else if (fragment instanceof BreakFragment) {
                    boolean paused = ((BreakFragment) fragment).onFabPlayPauseClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof ExerciseFragment) {
                    boolean paused = ((ExerciseFragment) fragment).onFabPlayPauseClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof FinishedFragment) {
                    reset();
                    loadFragment("StartFragment");
                }
            }
        });

        FloatingActionButton fabNext = findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the active fragment so we know which action to perform
                Fragment fragment = getActiveFragment();

                if (fragment instanceof StartFragment) {
                    loadFragment("BreakFragment");
                } else if (fragment instanceof BreakFragment) {
                    boolean paused = ((BreakFragment) fragment).onFabNextClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof ExerciseFragment) {
                    boolean paused = ((ExerciseFragment) fragment).onFabNextClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof FinishedFragment) {
                    reset();
                    loadFragment("StartFragment");
                }
            }
        });

        FloatingActionButton fabEnd = findViewById(R.id.fabEnd);
        fabEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the active fragment so we know which action to perform
                reset();
                boolean showPlayIcon = true;
                Fragment fragment = getActiveFragment();

                if (fragment instanceof StartFragment) {
                    end();
                } else if (fragment instanceof BreakFragment) {
                    Speech.speak("Stopping...", TextToSpeech.QUEUE_FLUSH);
                    setFabPlayIcon(showPlayIcon);
                    ((BreakFragment) fragment).onHardStop();
                } else if (fragment instanceof ExerciseFragment) {
                    Speech.speak("Stopping...", TextToSpeech.QUEUE_FLUSH);
                    setFabPlayIcon(showPlayIcon);
                    ((ExerciseFragment) fragment).onHardStop();
                } else if (fragment instanceof FinishedFragment) {
                    setFabPlayIcon(showPlayIcon);
                    loadFragment("StartFragment");
                }
            }
        });

        FloatingActionButton fabMute = findViewById(R.id.fabMute);
        setFabMuteIcon(Speech.isMuted());
        fabMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Speech.setMuted(!Speech.isMuted());
                setFabMuteIcon(Speech.isMuted());
            }
        });

        FloatingActionButton fabFastForward = findViewById(R.id.fabFastForward);
        fabFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the active fragment so we know which action to perform
                Fragment fragment = getActiveFragment();
                if (fragment instanceof BreakFragment) {
                    ((BreakFragment) fragment).onFabFastForwardClicked();
                } else if (fragment instanceof ExerciseFragment) {
                    ((ExerciseFragment) fragment).onFabFastForwardClicked();
                }
            }
        });
    }

    private void loadSessionInfo(Intent intent) {
        this.programId = intent.getIntExtra("courseId", -1);
        this.programName = intent.getStringExtra("courseName");
        this.sessionId = intent.getIntExtra("sessionId", -1);
        this.sessionName = intent.getStringExtra("sessionName");
    }

    public void onAddSecondsButtonClick(View view) {
        addSeconds(5);
    }

    public void onSubtractSecondsButtonClick(View view) {
        addSeconds(-5);
    }

    private void addSeconds(int seconds)
    {
        if (this.currentExerciseIndex < this.exercises.exerciseList.size()) {
            ExerciseContent.ExerciseItem exercise = this.exercises.exerciseList.get(this.currentExerciseIndex);

            exercise.runSeconds = exercise.runSeconds + seconds; // seconds may be positive or negative

            if (exercise.runSeconds < 0)
                exercise.runSeconds = 0;

            Fragment fragment = getActiveFragment();
            if (fragment instanceof BreakFragment) {
                ((BreakFragment) fragment).updateRunSeconds(exercise.runSeconds);
            }
        }
    }

    public void loadFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (null == fragment) {
            showFabButton(R.id.fabFastForward, false);
            switch(tag) {
                case "StartFragment":
                    fragment = new StartFragment();
                    break;
                case "BreakFragment":
                    showFabButton(R.id.fabFastForward, true);
                    fragment = new BreakFragment();
                    break;
                case "ExerciseFragment":
                    fragment = new ExerciseFragment();
                    break;
                case "FinishedFragment":
                    saveHistory();
                    fragment = new FinishedFragment();
                    break;
                default:
                    break;
            }
        }

        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }

    private void saveHistory() {

        // save preferences locally
        saveUserPreferences();

        // save the history on the server
        String url = "https://learnfast.xyz/history/add/";
        int totalSeconds = (int) this.exercises.getTotalSeconds();
        try {
            url += URLEncoder.encode(this.programName, "utf-8") + "/";
            url += this.programId + "/";
            url += URLEncoder.encode(this.sessionName, "utf-8") + "/";
            url += this.sessionId + "/";
            url += totalSeconds;
        }
        catch(Exception e)
        {
            Log.e("Exercise", "Error encoding url: " + e.getMessage());
        }

        RssReader.ping(url);

        HistoryContent.addItem(this.programName, this.programId, this.sessionName, this.sessionId, new Date(), totalSeconds);
    }

    private void saveUserPreferences() {
        UserPreferences.mProgramId = this.programId;
        UserPreferences.mSessionId = this.sessionId;
    }

    public ExerciseContent getExercises()
    {
        return exercises;
    }

    public void reset()
    {
        this.currentExerciseIndex = -1;
    }

    public void end() {
        finish();
        return;
    }

    private Fragment getActiveFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = (null != fm) ? fm.getFragments() : null;
        Fragment fragment = (null != fragments && fragments.size() > 0) ? fragments.get(0) : null;

        return fragment;
    }

    public void setFabPlayIcon(boolean paused) {
        setFabButtonIcon(R.id.fabPlay,
                paused  ? R.drawable.fab_play
                        : android.R.drawable.ic_media_pause);
    }

    public void setFabMuteIcon(boolean muted) {
        setFabButtonIcon(R.id.fabMute,
                muted ? android.R.drawable.ic_lock_silent_mode_off
                        : android.R.drawable.ic_lock_silent_mode);
    }

    public void setFabButtonIcon(int buttonId, int buttonIcon) {
        FloatingActionButton fabButton = findViewById(buttonId);
        fabButton.setImageResource(buttonIcon);
    }

    public void showFabButton(int buttonId, boolean show) {
        FloatingActionButton fabButton = findViewById(buttonId);
        fabButton.setVisibility(show ? FloatingActionButton.VISIBLE : FloatingActionButton.INVISIBLE);
    }

    public boolean isLoaded() {
        return this.exercises.isLoaded();
    }

    public boolean isLastExercise() {
        return (this.currentExerciseIndex == this.exercises.exerciseList.size() - 1);
    }

    public int getTotalExercises() {
        return this.exercises.exerciseList.size();
    }

    public int getTimerSeconds() {

        int seconds = -1;

        if (getCurrentExercise().isFirst()) {
            seconds = this.exercises.startSeconds;
        }
        else {
            // get break seconds from previous item NOT current item
            if (this.currentExerciseIndex > 0)
                seconds = this.exercises.exerciseList.get(this.currentExerciseIndex - 1).breakSeconds;
        }

        return seconds;
    }

    public ExerciseContent.ExerciseItem getNextExercise() {

        ExerciseContent.ExerciseItem ex = null;

        if (this.exercises.isLoaded()) {

            this.currentExerciseIndex++;

            if (this.currentExerciseIndex < this.exercises.exerciseList.size()) {
                ex = this.exercises.exerciseList.get(this.currentExerciseIndex);
            }
        }

        return ex;
    }

    public ExerciseContent.ExerciseItem getCurrentExercise() {
        ExerciseContent.ExerciseItem ex = null;

        if (this.currentExerciseIndex < this.exercises.exerciseList.size())
        {
            ex = this.exercises.exerciseList.get(this.currentExerciseIndex);
        }

        return ex;
    }
}
