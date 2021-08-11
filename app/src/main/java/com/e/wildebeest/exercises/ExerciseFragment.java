package com.e.wildebeest.exercises;

import com.e.wildebeest.R;
import com.e.wildebeest.Speech;
import com.e.wildebeest.Tools;
import com.e.wildebeest.exercises.content.ExerciseContent;

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

public class ExerciseFragment extends Fragment {

    private boolean timerPaused = false;
    private int secondsRemaining = -1;
    private int secondsRewind = 5;
    private int countdownSeconds = 10;
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
                Speech.speak("Stopping", TextToSpeech.QUEUE_FLUSH);
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
        Speech.shutup();
        stopTimer();
        showNextFragment();

        return timerPaused;
    }

    public boolean onFabPlayPauseClicked() {

        if (timerPaused) {
            Speech.speak("Continued.  ", TextToSpeech.QUEUE_FLUSH);
            startTimer(secondsRemaining); // restart timer
        }
        else {
            Speech.speak("paused.  ", TextToSpeech.QUEUE_FLUSH);
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
            Speech.speak(getSecondsRemainingMessage(seconds), TextToSpeech.QUEUE_ADD);
        }
        else if (seconds <= countdownSeconds && seconds > 0) {
            //
            // countdown last "countdownSeconds" seconds
            //
            Speech.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH);
        }
    }

    public void speakInstructions() {
        ExerciseContent.ExerciseItem exerciseItem = ((ExercisesActivity) getActivity()).getCurrentExercise();
        String instructions = getInstructionsDeluxe(exerciseItem);
        if (instructions.length() > 0) {
            Speech.speak(instructions, TextToSpeech.QUEUE_ADD);
        }
    }

    private String getInstructionsDeluxe(ExerciseContent.ExerciseItem exerciseItem)
    {
        String instructions;

        if (exerciseItem.mInstructionType == ExerciseContent.eInstructionType.none) {
            // if exercise has recognized written instructions, standardize and randomize them
            if (exerciseItem.name.toLowerCase().toLowerCase().contains("leg lift")) {
                exerciseItem.mInstructionType = ExerciseContent.eInstructionType.switchLeg;
            }
            if (exerciseItem.instructions.length() > 0) {
                if (exerciseItem.instructions.toLowerCase().contains("legs"))
                    exerciseItem.mInstructionType = ExerciseContent.eInstructionType.switchLeg;
                else if (exerciseItem.instructions.toLowerCase().contains("sides"))
                    exerciseItem.mInstructionType = ExerciseContent.eInstructionType.switchSide;
            }
        }

        return getInstructions(exerciseItem.mInstructionType);
    }

    private String getInstructions(ExerciseContent.eInstructionType instructionType) {

        int random = 0;
        String sInstructions = "";

        switch(instructionType) {
            case switchLeg: {
                sInstructions = Tools.getRandomString("Change legs", "Switch legs");
                break;
            }
            case switchSide: {
                sInstructions = Tools.getRandomString("Switch sides", "Change sides");
                break;
            }
            default:
                sInstructions = "";
                break;
        }

        return sInstructions;
    }

    private String getSecondsRemainingMessage(int seconds) {

        String msg = Tools.getRandomString(
                "# seconds to go",
                "# seconds remaining",
                "# more seconds",
                "Go for # seconds longer",
                "Go for # more seconds",
                "Keep it up for # seconds longer",
                "Keep going for # more seconds",
                "Continue for # seconds longer",
                "There are # seconds remaining",
                "There are # more seconds to go"
        );

        String msgES = Tools.getRandomString(
                "# segundos restantes",
                "Continua durante # segundos más",
                "Hay # segundos más"
        );

        String voiceMsg = "";

        if (seconds > 60)
        {
            String msgTime = Tools.secondsToMinutesLong(seconds);
            voiceMsg = msgTime + " remaining.";
        }
        else
        {
            voiceMsg = msg.replace("#", (CharSequence)Integer.toString(seconds)) + ".";
        }

        return voiceMsg;
    }

    private void loadCurrent() {

        ExercisesActivity activity = (ExercisesActivity) getActivity();
        ExerciseContent.ExerciseItem exerciseItem = activity.getCurrentExercise();
        if (null != exerciseItem) {
            setStaticViews(exerciseItem, activity.getTotalExercises());
            String instructions = getInstructionsDeluxe(exerciseItem);
            if (instructions.length() > 0)
                instructions += " every 10 seconds.";

            if (exerciseItem.name.compareTo("Inhale") == 0) //todo: hardcoded for breathing exercise
            {
                Speech.speak("Inhale", TextToSpeech.QUEUE_FLUSH);
                exerciseItem.runSeconds++;
                countdownSeconds = 2;
            }
            else
            {
                String msgTime = Tools.secondsToMinutesLong(exerciseItem.runSeconds);
                Speech.speak("Begin.  Do " + exerciseItem.name + " -- for " + msgTime + ".  " + instructions, TextToSpeech.QUEUE_FLUSH);
            }

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
            exerciseSeconds.setText(Tools.totalTimeDisplay(exerciseItem.runSeconds));

        ImageView iv = this.getView().findViewById(R.id.imageViewCurrent);
        if (null != iv) {
            int id = getResources().getIdentifier(exerciseItem.imageName, "drawable", getContext().getPackageName());
            iv.setImageResource(id);
        }

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

    public void loadFragment(String tag)
    {
        ExercisesActivity activity = (ExercisesActivity) getActivity();
        if (null != activity) {
            activity.loadFragment(tag);
        }
    }
}