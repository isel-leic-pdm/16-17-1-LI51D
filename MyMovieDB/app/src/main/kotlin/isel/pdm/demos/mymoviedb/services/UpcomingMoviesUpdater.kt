package isel.pdm.demos.mymoviedb.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieListPage

class UpcomingMoviesUpdater : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val UPCOMING_MOVIES_URL: String = "http://api.themoviedb.org/3/movie/upcoming"

        (application as MyMovieDBApplication).requestQueue.add(
                GetRequest<MovieListPage>(
                    "$UPCOMING_MOVIES_URL?${ConfigurationInfo.API_KEY_PARAM}",
                    MovieListPage::class.java,
                    {
                        Log.v("DEMO", "TCHARAM")
                        /* TODO: Use list data */
                        stopSelf(startId)
                    },
                    {
                        Log.v("DEMO", "CABBUMMM")
                        /* TODO: Handle error */
                        stopSelf(startId)
                    }
                )
        )

        return Service.START_FLAG_REDELIVERY
    }
}
