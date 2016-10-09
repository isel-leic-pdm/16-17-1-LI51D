package isel.pdm.demos.mymoviedb

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import android.util.Log

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumentation test, which will execute on an Android device. Used to check whether the
 * testing environment is correctly configured.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class CheckEnvironment {

    private var targetContext: Context? = null

    @Before
    fun prepare() {
        // Context of the app under test.
        targetContext = InstrumentationRegistry.getTargetContext()
    }

    @Test
    @Throws(Exception::class)
    fun test_useAppContext() {
        assertEquals("isel.pdm.demos.mymoviedb", targetContext!!.packageName)
    }

    @Test
    fun test_checkPermissions() {
        assertEquals(
                PackageManager.PERMISSION_GRANTED.toLong(),
                targetContext!!.packageManager.checkPermission(
                        "android.permission.INTERNET", "isel.pdm.demos.mymoviedb").toLong())
    }
}
