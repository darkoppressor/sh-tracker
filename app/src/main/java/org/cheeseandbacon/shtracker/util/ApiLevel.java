/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.util;

import android.os.Build;

public class ApiLevel {
    public static boolean hasUnlockedDeviceOnlyKeyDecryption () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean hasBiometricPrompt () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }
}
