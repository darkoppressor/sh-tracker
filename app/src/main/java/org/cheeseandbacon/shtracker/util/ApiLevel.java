package org.cheeseandbacon.shtracker.util;

import android.os.Build;

public class ApiLevel {
    static boolean hasVibrationEffects () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasOnBodySensor () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasUnlockedDeviceOnlyKeyDecryption () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean hasBiometricPrompt () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }
}
