/*
 * Copyright (c) 2019 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext () {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("org.cheeseandbacon.shtracker", appContext.getPackageName());
    }
}
