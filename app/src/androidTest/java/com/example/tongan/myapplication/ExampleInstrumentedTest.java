package com.example.tongan.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented grad_light_maya_blue_bottom_scampi_top, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under grad_light_maya_blue_bottom_scampi_top.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.tongan.myapplication", appContext.getPackageName());
    }
}
