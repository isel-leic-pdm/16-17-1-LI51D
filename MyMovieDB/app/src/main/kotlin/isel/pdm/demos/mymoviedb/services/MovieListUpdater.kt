package isel.pdm.demos.mymoviedb.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.volley.VolleyError
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieListPage
import isel.pdm.demos.mymoviedb.models.content.MovieInfoProvider
import isel.pdm.demos.mymoviedb.models.content.toContentValues

/**
 * Implementation of a started service which is responsible for updating the local replica.
 *
 * TODO: Document contract
 */
class MovieListUpdater : Service() {

    companion object {
        val MOVIE_LIST_ID_EXTRA_KEY = "listID"
        val UPCOMING_LIST_ID_EXTRA_VALUE = "upcoming"
        val EXHIBITION_LIST_ID_EXTRA_VALUE = "now_playing"

        private val BASE_URL = "http://api.themoviedb.org/3/movie/"
        private val LIST_IDS = listOf(UPCOMING_LIST_ID_EXTRA_VALUE, EXHIBITION_LIST_ID_EXTRA_VALUE)
    }

    /** {@inheritDoc} */
    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val movieListId = intent?.let {
            val listID: String? = it.getStringExtra(MOVIE_LIST_ID_EXTRA_KEY)
            if(listID in LIST_IDS) listID else null
        }

        if(movieListId == null) {
            stopSelf(startId)
            return Service.START_NOT_STICKY
        }

        (application as MyMovieDBApplication).requestQueue.add(
                GetRequest<MovieListPage>(
                        "$BASE_URL$movieListId?${ConfigurationInfo.API_KEY_PARAM}",
                        MovieListPage::class.java,
                        { processMovies(it, movieListId); stopSelf(startId) },
                        { handleError(it); stopSelf(startId) }
                )
        )

        return Service.START_REDELIVER_INTENT
    }

    /**
     * Function used to update the local repository with the given set of movies.
     * @param context The host context
     * @param movies The set of movies to be processed
     */
    private fun processMovies(movies: MovieListPage, movieListId: String): Unit {

        // Implementation note: This solution removes all existing entries from the DB before
        // inserting the new ones. This is not the best approach.

        val tableUri =
            if (movieListId == UPCOMING_LIST_ID_EXTRA_VALUE) MovieInfoProvider.UPCOMING_CONTENT_URI
            else MovieInfoProvider.EXHIBITION_CONTENT_URI

        contentResolver.delete(tableUri, null, null)
        contentResolver.bulkInsert(tableUri, movies.toContentValues())

        Log.v("DEMO", "Successfully updated $movieListId movie list")
    }

    /**
     * Function used to handle the given communication error.
     * @param error The instance representing the underlying communication error
     */
    private fun handleError(error: VolleyError): Unit {
        // TODO
        Log.v("DEMO", "CABBUMMM")
    }
}

