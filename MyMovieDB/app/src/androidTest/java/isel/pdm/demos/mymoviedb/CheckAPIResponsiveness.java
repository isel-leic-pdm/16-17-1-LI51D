package isel.pdm.demos.mymoviedb;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CheckAPIResponsiveness {

    private Context targetContext;

    @Before
    public void prepare() {
        // Context of the app under test.
        targetContext = InstrumentationRegistry.getTargetContext();
    }

    private volatile boolean success;

    @Test
    public void test_checkResponse() {
        final String url = "http://api.themoviedb.org/3/movie/76341?api_key=c45808d49ff7af92014ae030f009cd17";

        final RequestQueue queue = Volley.newRequestQueue(targetContext);
        queue.add(new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        success = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        success = false;
                    }
                }
        ));

        try { Thread.sleep(5000); }
        catch (InterruptedException _) {}

        Assert.assertTrue(success);
    }
}
