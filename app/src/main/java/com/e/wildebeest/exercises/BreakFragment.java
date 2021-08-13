package com.e.wildebeest.exercises;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.wildebeest.R;
import com.e.wildebeest.Speech;
import com.e.wildebeest.exercises.content.ExerciseContent;
import com.e.wildebeest.Tools;

import java.util.Random;

public class BreakFragment extends Fragment {

    static private boolean autoStart = false;
    static private boolean started = false;
    private int secondsRemaining = 0;
    private int secondsRewind = 5;
    private int secondsFastForward = secondsRewind;
    private final int second = 1000; // 1 Second
    private int countdownSeconds = 5;
    private final int nextCountdownSeconds = 3;
    private final int getReadySeconds = countdownSeconds + 1;
    private final int dontInterruptUntilSeconds = 3;
    private Handler handler = new Handler();
    private boolean timerPaused = false;

    private String startMsgsEn[] = {
            "Okay fat boy, Here we go!",
            "Okay Lard Ass, Giddy up!",
            "Okay fatty, It's Go Go Time!",
            "Get your fat ass off of that couch!",
            "Okay Fatty, Let's get jiggy with it!",
            "Okay Fatty, Get ready to Shake your money maker!",
            "Okay Fatty, Your last easy day was yesterday!",
            "Let's go jelly belly!",
            "It's go time, butter-ball!",
            "Okay fatty, Put down that big plate of food!",
            "Okay fatty, Take that chicken leg out of your fat face!",
            "Get your fat face out of that bucket of ice-cream!",
            "Okay fatty, Get ready for an avalanche of pain!",
            "Okay fatty, it's about to get really real!",
            "Okay, don't be a little bitch!"
    };

    private String startMsgs[] = {
        "Here we go",
    };

    private Runnable runnable = new Runnable(){
        public void run() {

            secondsRemaining--;
            updateTimerDisplay(secondsRemaining);

            if (secondsRemaining >= 1) {
                handler.postDelayed(runnable, second); // update in 1 second
                updateTimerAudio(secondsRemaining);
            }
            else {
                loadFragment("ExerciseFragment");
            }
        }
    };

    private Runnable startUp = new Runnable(){
        public void run() {

            ExercisesActivity activity = (ExercisesActivity) getActivity();
            if (null != activity) {
                if (activity.isLoaded()) {
                    start();
                } else {
                    Log.i("startup", "waiting one second");
                    handler.postDelayed(startUp, second); // update in 1 second
                }
            }
        }
    };

    public BreakFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        stopTimer();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_break, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Speech.speak("Stopping", TextToSpeech.QUEUE_FLUSH);
                onHardStop();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if (this.started) {
            loadNext();
        }
        else if (this.autoStart) {
            // not used
            handler.postDelayed(this.startUp, this.second * 2);
        }
        else
        {
            start();
        }
    }

    public boolean onFabPlayPauseClicked() {
        if (started) {
            if (timerPaused) {
                Speech.speak("Continued.  ", TextToSpeech.QUEUE_FLUSH);
                startTimer(secondsRemaining); // restart timer
            } else {
                Speech.speak("paused.  ", TextToSpeech.QUEUE_FLUSH);
                stopTimer();
            }

            timerPaused = !timerPaused;
        }
        else
        {
            start();
        }

        return timerPaused;
    }

    public boolean onFabNextClicked() {
        if (started) {
            timerPaused = false;
            loadFragment("ExerciseFragment");
        }
        else {
            start();
        }

        return timerPaused;
    }

    // takes timer down to 3 seconds
    public boolean onFabFastForwardClicked() {

        if (started) {
            if (timerPaused) {
                Speech.speak("Resuming...  ", TextToSpeech.QUEUE_FLUSH);
                startTimer(nextCountdownSeconds); // restart timer
                timerPaused = false;
            } else {
                int seconds = nextCountdownSeconds + 1;
                if (secondsRemaining > seconds)
                    secondsRemaining = seconds; // countdown from 3
                else
                    secondsRemaining = 1; // do it now
            }
        } else {
            start();
        }

        ((ExercisesActivity) getActivity()).setFabPlayIcon(timerPaused);

        return timerPaused;
    }

    public void onFabRewindClicked() {
        this.secondsRemaining += this.secondsRewind;
        if (timerPaused)
            updateTimerDisplay(secondsRemaining);
    }

    private String getRandomMessage(String[] msgs) {
        int ix = new Random().nextInt(msgs.length);
        return msgs[ix];
    }

    private void start() {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        if (null != activity) {
            if (activity.isLoaded()) {
                Speech.speak(getRandomMessage(startMsgs), TextToSpeech.QUEUE_ADD);
                this.started = true;
                activity.reset();
                loadNext();
            } else {
                Speech.speak("Wait for exercises to finish loading...", TextToSpeech.QUEUE_ADD);
                handler.postDelayed(this.startUp, this.second); // wait 1 second
            }
        }
    }

    public void onHardStop() {
        this.started = false;
        stopTimer();
        loadFragment("StartFragment");
    }

    private void loadNext() {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        if (null == activity)
            return;

        ExerciseContent.ExerciseItem exerciseItem = activity.getNextExercise();

        if (null != exerciseItem)
        {
            int seconds = activity.getTimerSeconds();
            String title = "";
            String text = "";

            if (exerciseItem.order == 1) // first exercise
            {
                if (exerciseItem.isRestDay())
                {
                    text = "Today is a rest day.";
                    seconds = 7;
                }
                else
                {
                    // Get ready to start in # seconds
                    title = getResources().getString(R.string.exercise_get_ready);
                    text = title + " to start in " + seconds + " seconds.";

                    // The first exercis is, # , for # seconds
                    String msgTime = Tools.secondsToMinutesLong(exerciseItem.runSeconds);
                    text += "  The first exercise is, " + exerciseItem.name + ", for " + msgTime + ".";
                }
            }
            else
            {
                if (exerciseItem.name.compareTo("Inhale") == 0) //todo: hardcoded for breathing exercise
                {
                    text = "Exhale";
                    seconds++;
                    this.countdownSeconds = 2;
                }
                else
                {
                    title = getResources().getString(R.string.exercise_take_a_break);
                    text = title + " for " + seconds + " seconds.";

                    String next = (exerciseItem.order == activity.getTotalExercises()) ? "last" : "next";
                    String msgTime = Tools.secondsToMinutesLong(exerciseItem.runSeconds);
                    text += "  The " + next + " exercise is, " + exerciseItem.name + ", for " + msgTime + ".";
                }
            }

            Speech.speak(text, TextToSpeech.QUEUE_ADD);

            // start
            setStaticViews(activity, exerciseItem, title);
            startTimer(seconds);
            activity.setFabPlayIcon(false);
        }
        else {
            // end
            Speech.speak("All exercises completed.", TextToSpeech.QUEUE_ADD);
            Speech.speak("Well done!  Congratulations!", TextToSpeech.QUEUE_ADD);
            stopTimer();
            activity.setFabPlayIcon(true);
        }
    }

    private void startTimer(int seconds)
    {
        this.secondsRemaining = seconds;
        updateTimerDisplay(seconds);
        handler.postDelayed(this.runnable, this.second); // update in 1 second
    }

    private void stopTimer() {
        handler.removeCallbacks(this.runnable);
    }

    private void updateTimerDisplay(int seconds)
    {
        if (seconds >= 0) {
            View view = this.getView();
            if (null != view) {
                TextView countDown = view.findViewById(R.id.textview_countdown);
                if (null != countDown) {
                    String msg = Tools.secondsToMinutesShort(seconds);
                    countDown.setText(msg);
                    //countDown.setText(Integer.toString(seconds));
                }
            }
        }
    }

    public void updateRunSeconds(int seconds) {
        TextView tv = this.getView().findViewById(R.id.textview_exercise_seconds);
        if (null != tv)
            tv.setText(Integer.toString(seconds) + " seconds");
    }

    private void setStaticViews(ExercisesActivity activity, ExerciseContent.ExerciseItem exerciseItem, String title)
    {
        //
        // set static values
        //
        TextView tv = this.getView().findViewById(R.id.textview_title);
        if (null != tv)
            tv.setText(title);

        tv = this.getView().findViewById(R.id.textview_coming_up);
        if (null != tv)
            tv.setText("Coming up " + exerciseItem.order + " of " + activity.getTotalExercises());

        tv = this.getView().findViewById(R.id.textview_exercise_name);
        if (null != tv)
            tv.setText(exerciseItem.name);

        tv = this.getView().findViewById(R.id.textview_exercise_seconds);
        if (null != tv)
            tv.setText(Tools.totalTimeDisplay(exerciseItem.runSeconds));

        tv = this.getView().findViewById(R.id.textview_countdown);
        if (null != tv)
            tv.setText(Integer.toString(this.secondsRemaining));

        ImageView iv = this.getView().findViewById(R.id.imageViewCurrent);
        if (null != iv) {
            int id = getResources().getIdentifier(exerciseItem.imageName, "drawable", getContext().getPackageName());
            iv.setImageResource(id);
        }
    }

    private void updateTimerAudio(int seconds) {

        if (Speech.isSpeaking() && seconds > this.dontInterruptUntilSeconds)
        {
            // still speaking, so don't interrupt
        }
        else if (seconds == this.getReadySeconds) {
            Speech.speak("Starting in: ", TextToSpeech.QUEUE_FLUSH);
        }
        else if (seconds <= this.countdownSeconds && seconds > 0) {
            Speech.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH);
        }
    }

    private void loadFragment(String tag)
    {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        if (null != activity) {
            activity.loadFragment(tag);
        }
    }
}
