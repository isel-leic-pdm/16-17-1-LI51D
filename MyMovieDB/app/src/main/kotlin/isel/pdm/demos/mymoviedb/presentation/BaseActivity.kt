package isel.pdm.demos.mymoviedb.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu

/**
 * Base class for all the application's activities.
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

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResId)
        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }
    }

    /** {@inheritDoc} */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        actionBarMenuResId?.let {
            menuInflater.inflate(it, menu);
            return true
        }

        Log.v("Paulo", "Damn!!")
        return super.onCreateOptionsMenu(menu)
    }
}
