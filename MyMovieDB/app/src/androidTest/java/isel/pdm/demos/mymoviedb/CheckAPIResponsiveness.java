package isel.pdm.demos.mymoviedb;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Instrumentation test, which will execute on an Android device. Used to check whether the
 * accesses to the Remote API are behaving as expected.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CheckAPIResponsiveness {

    private static final String URL = "http://api.themoviedb.org/3/movie/76341?api_key=c45808d49ff7af92014ae030f009cd17";
    private RequestQueue requestQueue;

    // Synchronization between test harness thread and callbacks thread
    private CountDownLatch latch;
    private AssertionError error;

    private void waitForCompletion() {
        try {
            if (latch.await(60, TimeUnit.SECONDS)) {
                if (error != null)
                    throw error;
            } else {
                Assert.fail("Test harness thread timeout while waiting for completion");
            }
        }
        catch (InterruptedException _) {
            Assert.fail();
        }
    }

    private void executeAndPublishResult(Runnable assertions) {
        try {
            assertions.run();
        }
        catch (AssertionError error) {
            this.error = error;
        }
        finally {
            latch.countDown();
        }
    }

    @Before
    public void prepare() {
        // Preparing Volley's request queue
        requestQueue = Volley.newRequestQueue(InstrumentationRegistry.getTargetContext());
        requestQueue.getCache().clear();
        // Preparing test harness thread synchronization artifacts
        latch = new CountDownLatch(1);
        error = null;
    }

    @Test
    public void test_checkAPIResponsiveness() {

        // Java sucks...
        requestQueue.add(
            new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        executeAndPublishResult(new Runnable() {
                            @Override
                            public void run() {
                                Assert.assertFalse(Strings.isNullOrEmpty(response));
                            }
                        });
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        executeAndPublishResult(new Runnable() {
                            @Override
                            public void run() {
                                Assert.assertNotNull(error.networkResponse);
                            }
                        });
                    }
                }
            )
        );

        waitForCompletion();
    }

    @Test
    public void test_successfulResponseParsing() {
        final GetRequest request = new GetRequest(URL, new Response.Listener<MovieDto>() {
            @Override
            public void onResponse(final MovieDto response) {
                executeAndPublishResult(new Runnable() {
                    @Override
                    public void run() {
                        Assert.assertNotNull(response);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                executeAndPublishResult(new Runnable() {
                    @Override
                    public void run() {
                        Assert.fail();
                    }
                });
            }
        });

        requestQueue.add(request);
        waitForCompletion();
    }
}
