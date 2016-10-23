package isel.pdm.demos.mymoviedb

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo

/**
 * Class used to customize the application context.
 *
 * <p>This class may be instantiated multiple times, specifically, there will be as many instances
 * as application processes (one per process). For this reason it is to be used judiciously:
 * contained data <em>should be mostly read only</em> or we risk having bugs caused by data
 * divergence.</p>
 */
class MyMovieDBApplication : Application() {

    /**
     * @property apiConfigurationInfo The configuration information provided by the remote API,
     * or null if we could not reach it
     */
    var apiConfigurationInfo: ConfigurationInfo? = null

    /**
     * @property requestQueue The request queue to be used for request dispatching
     */
    lateinit var requestQueue: RequestQueue

    /**
     * Initiates the application instance
     */
    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
    }
}