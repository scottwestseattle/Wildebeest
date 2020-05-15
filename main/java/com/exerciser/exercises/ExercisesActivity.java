package com.exerciser.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.exerciser.R;
import com.exerciser.exercises.content.ExerciseContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExercisesActivity extends AppCompatActivity  implements StartFragment.OnListFragmentInteractionListener {

    public static ExerciseContent exercises = null;
    public int currentExerciseIndex = -1;
    public TextToSpeech tts = null;
    public boolean isSpeechLoaded = false;
    public int programId = -1;
    public String sessionName = "";

    @Override
    public void onListFragmentInteraction(ExerciseContent.ExerciseItem exerciseItem) {
        //
        // handle click from any item in Exercise list: start exercise
        //
        Fragment f = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (null != f) {
            //todo: speak("Ready to start.", TextToSpeech.QUEUE_ADD);
            //todo: NavHostFragment.findNavController(f).navigate(R.id.action_StartFragment_to_BreakFragment);
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

        String title = this.sessionName;
        String subTitle = exercises.exerciseList.size() + " exercises, Time: " + exercises.getTotalTime();
        setTitle(title + ": " + subTitle);

        FloatingActionButton fabPlay = findViewById(R.id.fabPlay);
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the active fragment so we know which action to perform
                Fragment fragment = getActiveFragment();

                if (fragment instanceof StartFragment) {
                    loadFragment();

                    //todo: NavHostFragment.findNavController(fragment).navigate(R.id.action_StartFragment_to_BreakFragment);
                } else if (fragment instanceof BreakFragment) {
                    boolean paused = ((BreakFragment) fragment).onFabPlayPauseClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof ExerciseFragment) {
                    boolean paused = ((ExerciseFragment) fragment).onFabPlayPauseClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof FinishedFragment) {
                    reset();
                    //todo: NavHostFragment.findNavController(fragment).navigate(R.id.action_finishedFragment_to_StartFragment);
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
                    //todo: NavHostFragment.findNavController(fragment).navigate(R.id.action_StartFragment_to_BreakFragment);
                } else if (fragment instanceof BreakFragment) {
                    boolean paused = ((BreakFragment) fragment).onFabNextClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof ExerciseFragment) {
                    boolean paused = ((ExerciseFragment) fragment).onFabNextClicked();
                    setFabPlayIcon(paused);
                } else if (fragment instanceof FinishedFragment) {
                    reset();
                    //todo: NavHostFragment.findNavController(fragment).navigate(R.id.action_finishedFragment_to_StartFragment);
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
                    //todo: NavHostFragment.findNavController(fragment).navigate(R.id.action_finishedFragment_to_StartFragment);
                }
            }
        });

    }

    public void loadFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.exercisesFragment, new BreakFragment());
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

        /* todo:
        int speechStatus = tts.speak(text, queueAction, null);
        if (speechStatus == TextToSpeech.ERROR) {
            Log.i("TTS", "Error in converting Text to Speech!");
        }

         */
    }

    private Fragment getActiveFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = (null != fm) ? fm.getFragments() : null;
        Fragment fragment = (null != fragments && fragments.size() > 0) ? fragments.get(0) : null;

        //Fragment nav = getSupportFragmentManager().getPrimaryNavigationFragment();
        //List<Fragment> fragments = (null != nav) ? nav.getChildFragmentManager().getFragments() : null;
        //Fragment fragment = (null != fragments && fragments.size() > 0) ? fragments.get(0) : null;

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
}
