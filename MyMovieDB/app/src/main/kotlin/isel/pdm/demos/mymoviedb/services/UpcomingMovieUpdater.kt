package isel.pdm.demos.mymoviedb.services

import android.app.IntentService
import android.content.Intent
import android.os.AsyncTask
import android.support.annotation.WorkerThread
import android.util.Log
import com.android.volley.toolbox.RequestFuture
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieListPage

/**
 * TODO: Document
 */
class UpcomingMovieUpdater() : IntentService("UpcomingMovieUpdater") {

    /**
     * Helper method used to synchronously fetch the upcoming movies list.
     */
    private fun fetchUpcomingMoviesSync(): MovieListPage {
        val UPCOMING_MOVIES_URL: String = "http://api.themoviedb.org/3/movie/upcoming"

        val future: RequestFuture<MovieListPage> = RequestFuture.newFuture()
        (application as MyMovieDBApplication).requestQueue.add(GetRequest<MovieListPage>(
                "$UPCOMING_MOVIES_URL?${ConfigurationInfo.API_KEY_PARAM}",
                MovieListPage::class.java,
                { response -> future.onResponse(response) },
                { error -> future.onErrorResponse(error) }
        ))

        return future.get()
    }

    /**
     * Helper method used to fetch the upcoming movies list.
     */
    private fun brokenFetchUpcomingMoviesAsync() {
        val UPCOMING_MOVIES_URL: String = "http://api.themoviedb.org/3/movie/upcoming"

        (application as MyMovieDBApplication).requestQueue.add(GetRequest<MovieListPage>(
                "$UPCOMING_MOVIES_URL?${ConfigurationInfo.API_KEY_PARAM}",
                MovieListPage::class.java,
                { /* Use list data */  },
                {  /* Handle error */ }
        ))
    }

    @WorkerThread
    override fun onHandleIntent(intent: Intent?) {

        try {
            val list = fetchUpcomingMoviesSync()
            // TODO: Use list data
        }
        catch (error: Exception) {
            // TODO: Handle error
        }
    }
}