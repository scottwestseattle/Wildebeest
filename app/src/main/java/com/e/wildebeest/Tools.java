package com.e.wildebeest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.style.TtsSpan;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Random;
import java.util.Calendar;
import java.util.TimeZone;

public class Tools {

    // calculate rest day
    public static boolean isRestDay(int day) {
        return ((day % 7) == 0);
    }

    // format the seconds to look like: 13:10
    public static String getTimeFromSeconds(long seconds) {
        Date dt = new Date((seconds) * 1000);
        String time = new SimpleDateFormat("mm:ss").format(dt);
        return time;
    }

    // get current date/time
    public static java.util.Date getDateTimeNow() {
        //String[] ids = TimeZone.getAvailableIDs();
        TimeZone tz = TimeZone.getTimeZone("Etc/GMT");
        java.util.Date dt = Calendar.getInstance(tz).getTime();
//        SimpleDateFormat df = new SimpleDateFormat("MM/dd HH:mm");
//        String rc = df.format(dt);

        return dt;
    }


    public static int[] secondsToMinutes(int seconds, boolean roundup)
    {
        int time[] = {0, 0};
        int minutes = 0;
        int secondsRemaining = seconds;

        if (seconds > 60) {
            minutes = seconds / 60;
            float fMinutes = seconds / 60.0f;
            float fSeconds = seconds / minutes;
            float fRem = fMinutes - (float) minutes;
            secondsRemaining = (int) ((60.0f * fRem) + 0.05f);

            if (roundup && secondsRemaining % 10 != 0) {
                secondsRemaining++;
            }
        }

        time[0] = minutes;
        time[1] = secondsRemaining;

        return time;
    }

    // Returns: string with speach format, example: "2 minutes and 10 seconds" or "2 minutes"
    public static String secondsToMinutesLong(int seconds)
    {
        String msg = "";
        int[] time = secondsToMinutes(seconds, true);
        int minutes = time[0];
        int secondsRemaining = time[1];

        if (minutes > 0) {
            if (secondsRemaining > 0) {
                msg = Integer.toString(minutes)
                        + " minutes and " + Integer.toString(secondsRemaining) + " seconds";
            } else {
                msg = Integer.toString(minutes)
                        + " minutes";
            }
        }
        else
        {
            msg = Integer.toString(secondsRemaining) + " seconds";
        }

        return msg;
    }

    // Returns: string with format mm:ss, example: "2:05"
    public static String secondsToMinutesShort(int seconds)
    {
        String msg = "";
        int[] time = secondsToMinutes(seconds, false);
        int minutes = time[0];
        int secondsRemaining = time[1];

        if (minutes > 0) {
                msg = Integer.toString(minutes) + ":";

                if (secondsRemaining < 10)
                    msg += "0";

                msg += Integer.toString(secondsRemaining);
        }
        else
        {
            msg = Integer.toString(secondsRemaining);
        }

        return msg;
    }

    public static String totalTimeDisplay(int seconds)
    {
        String msg = Tools.secondsToMinutesShort(seconds);

        if (seconds > 60)
            msg = "Time: " + msg;
        else
            msg += " seconds";

        return msg;
    }


    public static String getRandomString(String ... msgs)
    {
        return msgs[new Random().nextInt(msgs.length)];
    }

    public static Bitmap getThumbnail(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
