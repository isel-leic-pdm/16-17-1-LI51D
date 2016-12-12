package isel.pdm.demos.mymoviedb.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.R
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo
import isel.pdm.demos.mymoviedb.models.MovieDetail
import kotlinx.android.synthetic.main.activity_movie_detail.*

/**
 * Implementation of the Activity used to display the movie details.
 */
class MovieDetailActivity : BaseActivity() {

    companion object {

        /**
         * The name of the extra that will contain the movie information to be displayed
         */
        const val EXTRA_MOVIE = "movie_detail_extra"

        /**
         * Creates an intent that can be used to start the current Activity.
         * @param origin The Context from which the iIntent is to be used
         * @param movieDetail The instance bearing the movie information to be displayed
         */
        fun createIntent(origin: Context, movieDetail: MovieDetail) =
                Intent(origin, MovieDetailActivity::class.java).putExtra(EXTRA_MOVIE, movieDetail)
    }

    /**
     * @property layoutResId the resource identifier of the activity layout
     */
    override val layoutResId: Int = R.layout.activity_movie_detail

    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    override val actionBarId: Int? = R.id.toolbar

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    override val actionBarMenuResId: Int? = R.menu.action_bar_activity_movie_details

    /**
     * Callback method used to initiate the activity instance.
     * @param savedInstanceState The previously saved instance state, or null if none exists
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var movie = intent.getParcelableExtra<MovieDetail>(EXTRA_MOVIE)
        rating.text = "${movie.rating} (10)"
        movieDescription.text = movie.overview

        genres.adapter = ArrayAdapter<MovieDetail.Genre>(
                this,
                android.R.layout.simple_list_item_1,
                movie.genres
        )

        Log.v(resources.getString(R.string.app_name), "${genres.adapter.count}")

        (application as MyMovieDBApplication).let {
            val urlBuilder: (String) -> String = {
                path -> "${it.apiConfigurationInfo?.images?.baseUrl}w342" +
                    "$path?${ConfigurationInfo.API_KEY_PARAM}"
            }
            packShot.setMovieInfo(movie, it.imageLoader, urlBuilder)
        }
    }

    /**
     * Callback method used to handle action bar events
     * @param savedInstanceState The menu instance bearing the action bar contents
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_credits -> {
            startActivity(Intent(this, CreditsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
