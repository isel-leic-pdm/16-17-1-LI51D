package isel.pdm.demos.mymoviedb.presentation

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.RequestFuture
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.R
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieDetail
import isel.pdm.demos.mymoviedb.models.MovieListPage
import isel.pdm.demos.mymoviedb.services.UpcomingMoviesUpdater

/**
 * Implementation of the Activity used to display the splash screen, which is presented at startup.
 */
class SplashActivity : BaseActivity() {

    /**
     * @property layoutResId the resource identifier of the activity layout
     */
    override val layoutResId: Int = R.layout.activity_splash

    /**
     * Helper method used to construct the URL of the API's configuration endpoint.
     * @return The string bearing the URL
     */
    private fun buildConfigUrl(): String {
        val baseUrl = resources.getString(R.string.api_base_url)
        val configPath = resources.getString(R.string.api_config_path)
        // Implementation note: All requests contain the api key. We will implement a general solution
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$configPath?$api_key"
    }

    /**
     * Callback method used to initiate the activity instance
     * @param savedInstanceState The previously saved instance state, or null if none exists
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyMovieDBApplication).requestQueue.add(
            GetRequest<ConfigurationInfo>(buildConfigUrl(), ConfigurationInfo::class.java,
                    {
                        (application as MyMovieDBApplication).apiConfigurationInfo = it
                        fetchMovieInfo()
                        //fetchUpcomingMoviesWithAsyncTask()
                    },
                    { handleFatalError() }
            )
        )
    }

    /**
     * Called whenever a fatal error occurs while starting up the application.
     */
    private fun handleFatalError() {
        Toast.makeText(this, R.string.splash_api_unreachable, Toast.LENGTH_LONG).show()
        Handler(mainLooper).postDelayed( { finish() }, 3000)
    }

    /**
     * Helper method used to fetch the upcoming movies list.
     */
    private fun fetchUpcomingMoviesWithAsyncTask() {
        val UPCOMING_MOVIES_URL: String = "http://api.themoviedb.org/3/movie/upcoming"

        val future: RequestFuture<MovieListPage> = RequestFuture.newFuture()
        val queue = (application as MyMovieDBApplication).requestQueue

        (object: AsyncTask<String, Unit, MovieListPage>() {
            override fun doInBackground(vararg p0: String?): MovieListPage {
                Log.v("Paulo", "doInBackground in ${Thread.currentThread().id}")
                queue.add(GetRequest<MovieListPage>(
                        "$UPCOMING_MOVIES_URL?${ConfigurationInfo.API_KEY_PARAM}",
                        MovieListPage::class.java,
                        { response -> future.onResponse(response) },
                        { error -> future.onErrorResponse(error) }
                ))

                return future.get()
            }


            override fun onPostExecute(result: MovieListPage?) {
                Log.v("Paulo", "onPostExecute in ${Thread.currentThread().id}")
            }
        }).execute()
    }

    /**
     * Helper method used to fetch the upcoming movies list.
     */
    private fun fetchUpcomingMovies() {

        val UPCOMING_MOVIES_URL: String = "http://api.themoviedb.org/3/movie/upcoming"

        (application as MyMovieDBApplication).let {
            it.requestQueue.add(GetRequest<MovieListPage>(
                    "$UPCOMING_MOVIES_URL?${ConfigurationInfo.API_KEY_PARAM}",
                    MovieListPage::class.java,
                    { movies -> Log.v("Paulo", "Done!") },
                    { handleFatalError() }
            ))
        }
    }

    /**
     * Helper method used to fetch the movie information.
     */
    private fun fetchMovieInfo() {

        val MOVIE_URL: String = "http://api.themoviedb.org/3/movie/76341"

        (application as MyMovieDBApplication).let {
            it.requestQueue.add(GetRequest<MovieDetail>(
                "$MOVIE_URL?${ConfigurationInfo.API_KEY_PARAM}",
                MovieDetail::class.java,
                { movie -> startActivity(MovieDetailActivity.createIntent(this, movie)) },
                { handleFatalError() }
            ))
        }
    }
}
