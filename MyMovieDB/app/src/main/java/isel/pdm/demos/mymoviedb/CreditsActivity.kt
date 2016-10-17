package isel.pdm.demos.mymoviedb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_credits.*

class CreditsActivity : AppCompatActivity() {

    private fun navigateToLinkedIn() {
        val url = Uri.parse(resources.getString(R.string.credits_author_linked_in))
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        tmdbLogo.setOnClickListener {
            val url = Uri.parse(resources.getString(R.string.credits_data_source_url))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

        linkedInLogo.setOnClickListener { navigateToLinkedIn() }
        linkedInUrl.setOnClickListener { navigateToLinkedIn() }
    }
}
