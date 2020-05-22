package com.exerciser.exercises;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.exerciser.R;
import com.exerciser.exercises.content.ExerciseContent;

import java.util.Random;

public class ExerciseFragment extends Fragment {

    private boolean timerPaused = false;
    private int secondsRemaining = -1;
    private int secondsRewind = 5;
    private int secondsFastForward = secondsRewind;
    private final int second = 1000; // 1 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {

            secondsRemaining--;
            updateTimerDisplay(secondsRemaining);

            if (secondsRemaining >= 1) {
                handler.postDelayed(runnable, second); // update in 1 second
                updateTimerAudio(secondsRemaining);
            } else {
                stopTimer();
                showNextFragment();
            }
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                speak("Stopping", TextToSpeech.QUEUE_FLUSH);
                onHardStop();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        loadCurrent();
    }

    public void onHardStop() {
        stopTimer();
        loadFragment("StartFragment");
    }

    public boolean onFabNextClicked() {
        ((ExercisesActivity) getActivity()).shutup();
        stopTimer();
        showNextFragment();

        return timerPaused;
    }

    public boolean onFabPlayPauseClicked() {

        if (timerPaused) {
            speak("Continued.  ", TextToSpeech.QUEUE_FLUSH);
            startTimer(secondsRemaining); // restart timer
        }
        else {
            speak("paused.  ", TextToSpeech.QUEUE_FLUSH);
            stopTimer();
        }

        timerPaused = !timerPaused;

        return timerPaused;
    }

    public void onFabRewindClicked() {
        this.secondsRemaining += this.secondsRewind;
        if (timerPaused)
            updateTimerDisplay(secondsRemaining);
    }

    public void onFabFastForwardClicked() {
        this.secondsRemaining -= this.secondsFastForward;
        if (this.secondsRemaining <= 0)
            this.secondsRemaining = timerPaused ? 0 : 1;

        if (timerPaused) {
            updateTimerDisplay(secondsRemaining);
        }
    }

    private void setButtonText(String text, int buttonId) {
        Button button = this.getView().findViewById(buttonId);
        if (null != button)
            button.setText(text);
    }

    private void updateTimerAudio(int seconds) {

        if (seconds > 10 && ((seconds - 1) % 10) == 0) {
            //
            // give instructions 1 seconds before 10 seconds threshold
            //
            speakInstructions();
        }
        else if (seconds > 10 && (seconds % 10) == 0) {
            //
            // give update every 10 seconds
            //
            speak(getSecondsRemainingMessage(seconds), TextToSpeech.QUEUE_ADD);
        }
        else if (seconds == 11)
        {
            //
            // give last instructions before countdown
            //
            //speakInstructions();
        }
        else if (seconds <= 10 && seconds > 0) {
            //
            // countdown last 10 seconds
            //
            speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH);
        }
    }

    public void speakInstructions() {
        ExerciseContent.ExerciseItem exerciseItem = ((ExercisesActivity) getActivity()).getCurrentExercise();
        if (exerciseItem.mInstructions == 0 && exerciseItem.instructions.length() > 0) {
            // if exercise has recognized written instructions, standardize and randomize them
            if (exerciseItem.instructions.contains("legs"))
                exerciseItem.mInstructions = ExerciseContent.mInstructionsLeg;
            else if (exerciseItem.instructions.contains("sides"))
                exerciseItem.mInstructions = ExerciseContent.mInstructionsSide;
        }

        String instructions = getInstructions(exerciseItem.mInstructions);

        if (instructions.length() > 0) {
            speak(instructions, TextToSpeech.QUEUE_ADD);
        }
    }

    private String getInstructions(int instructions) {

        int random = new Random().nextInt(2);
        String sInstructions = "";

        switch(instructions) {
            case ExerciseContent.mInstructionsLeg:
                sInstructions = random == 0 ? "Switch legs" : "Change legs";
                break;
            case ExerciseContent.mInstructionsSide:
                sInstructions = random == 0 ? "Switch sides" : "Change sides";
                break;
            default:
                sInstructions = "";
                break;
        }

        return sInstructions;
    }

    private String getSecondsRemainingMessage(int seconds) {
        String msg = "";
        int option = new Random().nextInt(7);

        switch(option) {
            case 0:
                msg = Integer.toString(seconds) + " seconds to go";
                break;
            case 1:
                msg = Integer.toString(seconds) + " seconds remaining";
                break;
            case 2:
                msg = Integer.toString(seconds) + " more seconds";
                break;
            case 3:
                msg = "Go for " + Integer.toString(seconds) + " more seconds";
                break;
            case 4:
                msg = "Keep it up for " + Integer.toString(seconds) + " seconds longer";
                break;
            case 5:
                msg = "Keep going for " + Integer.toString(seconds) + " more seconds";
                break;
            case 6:
                msg = "Continue for " + Integer.toString(seconds) + " seconds longer";
                break;
            default:
                msg = Integer.toString(seconds) + " seconds, random message out of range";
        }

        return msg + ".";
    }

    private void speak(String text, int queueAction)
    {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        activity.speak(text, queueAction);
    }

    private void loadCurrent() {

        ExercisesActivity activity = (ExercisesActivity) getActivity();
        ExerciseContent.ExerciseItem exerciseItem = activity.getCurrentExercise();
        if (null != exerciseItem) {
            setStaticViews(exerciseItem, activity.getTotalExercises());
            String instructions = getInstructions(exerciseItem.mInstructions);
            if (instructions.length() > 0)
                instructions += " every 10 seconds.";
            speak("Begin.  Do " + exerciseItem.name + " -- for " + exerciseItem.runSeconds + " seconds.  " + instructions, TextToSpeech.QUEUE_FLUSH);
            startTimer(exerciseItem.runSeconds);
        }
    }

    private void showNextFragment() {
        if ( ((ExercisesActivity)getActivity()).isLastExercise() )
            loadFragment("FinishedFragment");
        else
            loadFragment("BreakFragment");
    }

    private void startTimer(int seconds)
    {
        // start the exercise timer
        this.secondsRemaining = seconds;
        updateTimerDisplay(seconds);
        handler.postDelayed(runnable, second); // update in 1 second
    }

    private void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    private void setStaticViews(ExerciseContent.ExerciseItem exerciseItem, int totalExercises)
    {
        //
        // set static field values
        //
        TextView exerciseCount = this.getView().findViewById(R.id.textview_exercise_count);
        if (null != exerciseCount)
            exerciseCount.setText(exerciseItem.order + " of " + totalExercises);

        TextView exerciseName = this.getView().findViewById(R.id.textview_exercise_name);
        if (null != exerciseName)
            exerciseName.setText(exerciseItem.name);

        TextView exerciseSeconds = this.getView().findViewById(R.id.textview_exercise_seconds);
        if (null != exerciseSeconds)
            exerciseSeconds.setText(Integer.toString(exerciseItem.runSeconds) + " seconds");

        ImageView iv = this.getView().findViewById(R.id.imageViewCurrent);
        if (null != iv) {
            int id = getResources().getIdentifier(exerciseItem.imageName, "drawable", getContext().getPackageName());
            iv.setImageResource(id);
        }

    }

    private void updateTimerDisplay(int seconds)
    {
        View view = this.getView();
        if (null != view) {
            TextView countDown = view.findViewById(R.id.textview_countdown);
            if (null != countDown)
                countDown.setText(Integer.toString(seconds));
        }
    }

    public void loadFragment(String tag)
    {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        if (null != activity) {
            activity.loadFragment(tag);
        }
    }
}
