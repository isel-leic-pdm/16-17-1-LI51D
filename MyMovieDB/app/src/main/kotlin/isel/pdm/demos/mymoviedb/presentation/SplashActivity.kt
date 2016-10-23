package isel.pdm.demos.mymoviedb.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.pdm.demos.mymoviedb.MyMovieDBApplication
import isel.pdm.demos.mymoviedb.R
import isel.pdm.demos.mymoviedb.comms.GetRequest
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo

class SplashActivity : BaseActivity() {

    /**
     * @property layoutResId the resource identifier of the activity layout
     */
    override val layoutResId: Int = R.layout.activity_splash

    private lateinit var requestQueue: RequestQueue

    private fun buildConfigUrl(): String {
        val baseUrl = resources.getString(R.string.api_base_url)
        val configPath = resources.getString(R.string.api_config_path)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$configPath?$api_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(
            GetRequest<ConfigurationInfo>(buildConfigUrl(), ConfigurationInfo::class.java,
                    {
                        (application as MyMovieDBApplication).apiConfigurationInfo = it
                        startActivity(Intent(this, MovieDetailActivity::class.java))
                    },
                    {
                        Toast.makeText(this, "TMDB API could not be reached.", Toast.LENGTH_LONG).show()
                        Handler(mainLooper).postDelayed( { finish() }, 3000)
                    }
            )
        )
    }
}
