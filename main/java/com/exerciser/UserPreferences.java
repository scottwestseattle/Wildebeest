package com.exerciser;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class UserPreferences {

    public static int mProgramId = -1;
    public static int mSessionId = -1;

    public static void save(AppCompatActivity context) {
        SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("current_program_id", mProgramId);
        editor.putInt("current_session_id", mSessionId);
        editor.commit();
    }

    public static void load(AppCompatActivity context) {
        SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
        mProgramId = preferences.getInt("current_program_id", -1);
        mSessionId = preferences.getInt("current_session_id", -1);
    }
}
