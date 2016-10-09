package isel.pdm.demos.mymoviedb

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.core.deps.guava.base.Strings
import android.support.test.runner.AndroidJUnit4

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import isel.pdm.demos.mymoviedb.models.MovieDto

import junit.framework.Assert
import isel.pdm.demos.mymoviedb.GetRequest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumentation test, which will execute on an Android device. Used to check whether the
 * accesses to the Remote API are behaving as expected.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class CheckAPIResponsiveness {
    private var requestQueue: RequestQueue? = null

    // Synchronization between test harness thread and callbacks thread
    private var latch: CountDownLatch? = null
    private var error: AssertionError? = null

    private fun waitForCompletion() {
        try {
            if (latch!!.await(60, TimeUnit.SECONDS)) {
                if (error != null)
                    throw error as AssertionError
            } else {
                Assert.fail("Test harness thread timeout while waiting for completion")
            }
        } catch (_: InterruptedException) {
            Assert.fail()
        }

    }

    private fun executeAndPublishResult(assertions: Runnable) {
        try {
            assertions.run()
        } catch (error: AssertionError) {
            this.error = error
        } finally {
            latch!!.countDown()
        }
    }

    @Before
    fun prepare() {
        // Preparing Volley's request queue
        requestQueue = Volley.newRequestQueue(InstrumentationRegistry.getTargetContext())
        requestQueue!!.cache.clear()
        // Preparing test harness thread synchronization artifacts
        latch = CountDownLatch(1)
        error = null
    }

    @Test
    fun test_checkAPIResponsiveness() {

        // Java sucks...
        requestQueue!!.add(
                StringRequest(Request.Method.GET, URL,
                        Response.Listener<kotlin.String> { response -> executeAndPublishResult(Runnable { Assert.assertFalse(Strings.isNullOrEmpty(response)) }) },
                        Response.ErrorListener { error -> executeAndPublishResult(Runnable { Assert.assertNotNull(error.networkResponse) }) }))

        waitForCompletion()
    }

    @Test
    fun test_successfulResponseParsing() {
        val request = GetRequest(URL, Response.Listener<MovieDto> { response -> executeAndPublishResult(Runnable { Assert.assertNotNull(response) }) }, Response.ErrorListener { executeAndPublishResult(Runnable { Assert.fail() }) })

        requestQueue!!.add(request)
        waitForCompletion()
    }

    companion object {

        private val URL = "http://api.themoviedb.org/3/movie/76341?api_key=c45808d49ff7af92014ae030f009cd17"
    }
}
