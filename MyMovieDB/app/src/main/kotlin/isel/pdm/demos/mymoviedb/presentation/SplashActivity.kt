package isel.pdm.demos.mymoviedb.presentation

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.R
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieDetail

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
