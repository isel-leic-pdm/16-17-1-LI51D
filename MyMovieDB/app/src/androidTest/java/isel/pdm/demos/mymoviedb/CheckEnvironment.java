package isel.pdm.demos.mymoviedb;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device. Used to check whether the
 * testing environment is correctly configured.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CheckEnvironment {

    private Context targetContext;

    @Before
    public void prepare() {
        // Context of the app under test.
        targetContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void test_useAppContext() throws Exception {
        assertEquals("isel.pdm.demos.mymoviedb", targetContext.getPackageName());
    }

    @Test
    public void test_checkPermissions() {
        Assert.assertEquals(
                PackageManager.PERMISSION_GRANTED,
                targetContext.getPackageManager().checkPermission(
                    "android.permission.INTERNET", "isel.pdm.demos.mymoviedb"
                )
        );
    }
}
