package isel.pdm.demos.mymoviedb.presentation

import android.content.Intent
import android.view.MenuItem
import isel.pdm.demos.mymoviedb.R

/**
 * Implementation of the Activity used to display the movie details.
 */
class MovieDetailActivity : BaseActivity() {

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
