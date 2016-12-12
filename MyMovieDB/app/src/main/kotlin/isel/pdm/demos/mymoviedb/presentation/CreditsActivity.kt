package isel.pdm.demos.mymoviedb.presentation

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import isel.pdm.demos.mymoviedb.R
import kotlinx.android.synthetic.main.activity_credits.*

/**
 * Activity used for displaying the application credits.
 */
class CreditsActivity : BaseActivity() {

    /**
     * @property layoutResId the resource identifier of the activity layout
     */
    override val layoutResId: Int = R.layout.activity_credits

    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    override val actionBarId: Int? = R.id.toolbar

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    override val actionBarMenuResId: Int? = R.menu.action_bar_activity_credits

    /**
     * Navigates to a browser activity that shows the author's LinkedIn profile
     */
    private fun navigateToLinkedIn() {
        val url = Uri.parse(resources.getString(R.string.credits_author_linked_in))
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }

    /**
     * Callback method used to initiate the activity instance.
     * @param savedInstanceState The previously saved instance state, or null if none exists
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mgr.cancel(1)

        tmdbLogo.setOnClickListener {
            val url = Uri.parse(resources.getString(R.string.credits_data_source_url))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

        linkedInLogo.setOnClickListener { navigateToLinkedIn() }
        linkedInUrl.setOnClickListener { navigateToLinkedIn() }
    }
}
