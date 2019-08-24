/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.util;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

public class Vibration {
    // ms
    private static final long BUTTON_PRESS_LENGTH = 5;
    private static final int BUTTON_PRESS_AMPLITUDE = DEFAULT_AMPLITUDE;

    public static void buttonPress (Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(BUTTON_PRESS_LENGTH, BUTTON_PRESS_AMPLITUDE));
        }
    }
}
