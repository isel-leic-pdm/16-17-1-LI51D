package isel.pdm.demos.mymoviedb.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu

/**
 * Base class for all the application's activities.
 *
 * <p>The goal is to eliminate the need for replicated code, namely, the code required for setting
 * up the action bar and the activity's contents. The implementation is based on the <em>Template
 * Method</em> design pattern. Derived classes <em>must</em> provide an implementation of the
 * [layoutResId] abstract property. If the derived class contains an action bar, then it must
 * override the [actionBarId] and [actionBarMenuResId] properties: the former <em>must</em> produce
 * the identifier of the action bar control as specified in the activity's layout; the latter
 * <em>must</em> produce the resource identifier of the menu resource file describing the action
 * bar contents.</p>
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * @property layoutResId the resource identifier of the activity layout
     */
    protected abstract val layoutResId: Int

    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    protected open val actionBarId: Int? = null

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    protected open val actionBarMenuResId: Int? = null

    /**
     * Method used to initiate the activity layout and action bar, if one exists.
     */
    private fun initContents() {
        setContentView(layoutResId)
        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }
    }

    /**
     * Callback method used to initiate the activity instance
     * @param savedInstanceState The previously saved instance state, or null if none exists
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContents()
    }

    /**
     * Callback method used to initiate the activity instance
     * @param savedInstanceState The previously saved instance state, or null if none exists
     * @param persistentState The previously saved persistent instance state, or null if none exists
     */
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initContents()
    }

    /**
     * Callback method used to initiate the activity action bar by inflating its contents
     * @param savedInstanceState The menu instance where the action bar contents are to be placed
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        actionBarMenuResId?.let {
            menuInflater.inflate(it, menu);
            return true
        }

        return super.onCreateOptionsMenu(menu)
    }
}
