package com.exerciser.exercises;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.exerciser.R;
import com.exerciser.Speech;
import com.exerciser.exercises.content.ExerciseContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExercisesActivity extends AppCompatActivity  implements StartFragment.OnListFragmentInteractionListener {

    private Toolbar mToolbar;
    public static ExerciseContent exercises = null;
    public int currentExerciseIndex = -1;
    public int programId = -1;
    public String sessionName = "";

    @Override
    public void onListFragmentInteraction(ExerciseContent.ExerciseItem exerciseItem) {
        //
        // handle click from any item in Exercise list: start exercise
        //
        Fragment f = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (null != f) {
            speak("Ready to start.", TextToSpeech.QUEUE_ADD);
            loadFragment("BreakFragment");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the data
        this.programId = getIntent().getIntExtra("courseId", -1);
        int exerciseId = getIntent().getIntExtra("sessionId", -1);
        this.sessionName = getIntent().getStringExtra("sessionName");

        // get the data
        exercises = new ExerciseContent(exerciseId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        // set title and subtitle
        String title = this.sessionName;
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
                    speak("Stopping...", TextToSpeech.QUEUE_FLUSH);
                    setFabPlayIcon(showPlayIcon);
                    ((BreakFragment) fragment).onHardStop();
                } else if (fragment instanceof ExerciseFragment) {
                    speak("Stopping...", TextToSpeech.QUEUE_FLUSH);
                    setFabPlayIcon(showPlayIcon);
                    ((ExerciseFragment) fragment).onHardStop();
                } else if (fragment instanceof FinishedFragment) {
                    setFabPlayIcon(showPlayIcon);
                    loadFragment("StartFragment");
                }
            }
        });

        FloatingActionButton fabMinus = findViewById(R.id.fabMinus);
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the active fragment so we know which action to perform
                Fragment fragment = getActiveFragment();
                if (fragment instanceof BreakFragment) {
                    ((BreakFragment) fragment).onFabRewindClicked();
                } else if (fragment instanceof ExerciseFragment) {
                    ((ExerciseFragment) fragment).onFabRewindClicked();
                }
            }
        });

        FloatingActionButton fabPlus = findViewById(R.id.fabPlus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
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

    public void loadFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment = fm.findFragmentByTag(tag);
        if (null == fragment) {
            switch(tag) {
                case "StartFragment":
                    fragment = new StartFragment();
                    break;
                case "BreakFragment":
                    fragment = new BreakFragment();
                    break;
                case "ExerciseFragment":
                    fragment = new ExerciseFragment();
                    break;
                case "FinishedFragment":
                    fragment = new FinishedFragment();
                    break;
                default:
                    break;
            }
        }

        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
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

    public void speak(String text, int queueAction) {
        Speech.speak(text, queueAction);
    }

    public void shutup() {
        Speech.shutup();
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
                paused  ? android.R.drawable.ic_media_play
                        : android.R.drawable.ic_media_pause);
    }

    public void setFabButtonIcon(int buttonId, int buttonIcon) {
        FloatingActionButton fabPlay = findViewById(buttonId);
        fabPlay.setImageResource(buttonIcon);
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
