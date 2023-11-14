package xyz.ttyz.tourfrxohc;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import xyz.ttyz.toubasemvvm.utils.TouUtils;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@Suite.SuiteClasses(TouUtils.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {

//        int a = 0;
//        int b = 10 / a;
        TouUtils.getNetVideoBitmap("aa");
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

//        assertEquals("xyz.ttyz.tourfrxohc111", appContext.getPackageName());
    }
}
