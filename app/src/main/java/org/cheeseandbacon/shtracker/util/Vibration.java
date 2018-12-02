package org.cheeseandbacon.shtracker.util;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class Vibration {
    // ms
    private static final long BUTTON_PRESS_LENGTH = 5;
    private static final int BUTTON_PRESS_AMPLITUDE = 72;

    public static void buttonPress (Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {
            if (ApiLevel.hasVibrationEffects() && vibrator.hasAmplitudeControl()) {
                vibrator.vibrate(VibrationEffect.createOneShot(BUTTON_PRESS_LENGTH, BUTTON_PRESS_AMPLITUDE));
            } else {
                vibrator.vibrate(BUTTON_PRESS_LENGTH);
            }
        }
    }
}
