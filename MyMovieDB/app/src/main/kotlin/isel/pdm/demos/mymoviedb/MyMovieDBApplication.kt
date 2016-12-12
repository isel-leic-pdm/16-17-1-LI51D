package isel.pdm.demos.mymoviedb

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.services.NullImageCache
import isel.pdm.demos.mymoviedb.services.MovieListUpdater

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
    @Volatile var apiConfigurationInfo: ConfigurationInfo? = null

    /**
     * @property requestQueue The request queue to be used for request dispatching
     */
    @Volatile lateinit var requestQueue: RequestQueue

    /**
     * @property imageLoader The image loader instance, used to load images from the network
     */
    @Volatile lateinit var imageLoader: ImageLoader

    /**
     * Helper method used to schedule the update of the movie list with the given identifier
     */
    /**
     * Initiates the application instance
     */
    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, NullImageCache())

        fun scheduleUpdate(listId: String) {
            val action = Intent(this, MovieListUpdater::class.java)
                    .putExtra(MovieListUpdater.MOVIE_LIST_ID_EXTRA_KEY, listId)
            (getSystemService(ALARM_SERVICE) as AlarmManager).setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    0,
                    AlarmManager.INTERVAL_DAY,
                    PendingIntent.getService(this, 1, action, PendingIntent.FLAG_UPDATE_CURRENT)
            )
        }

        // Implementation note: This solution does not persist Alarm schedules across reboots

        scheduleUpdate(MovieListUpdater.UPCOMING_LIST_ID_EXTRA_VALUE)
        scheduleUpdate(MovieListUpdater.EXHIBITION_LIST_ID_EXTRA_VALUE)
    }
}