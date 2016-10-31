package isel.pdm.demos.mymoviedb.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.android.volley.toolbox.ImageLoader
import isel.pdm.demos.mymoviedb.R
import isel.pdm.demos.mymoviedb.models.MovieDetail
import kotlinx.android.synthetic.main.pack_shot_view.view.*

/**
 * UI control that displays the movie pack shot along with its title.
 */
class PackShotView(val ctx: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(ctx, attrs, defStyle) {

    init {
        inflate(context, R.layout.pack_shot_view, this);
        packShotImage.setDefaultImageResId(R.drawable.pack_shot_empty)
        packShotImage.setErrorImageResId(R.drawable.pack_shot_empty)

        if(isInEditMode)
            movieTitle.text = resources.getString(R.string.movie_details_tools_movie_title)
    }

    constructor(ctx: Context) : this(ctx, null, 0)
    constructor(ctx: Context, attrs: AttributeSet) : this(ctx, attrs, 0)

    /**
     * Displays the pack shot for the given movie.
     * @param movieDetail The movie information
     * @param imageLoader The pack shot image provider
     * @param urlBuilder The function used to convert the given image path to the corresponding URL
     */
    fun setMovieInfo(movieDetail: MovieDetail,
                     imageLoader: ImageLoader,
                     urlBuilder: (path: String) -> String) {

        movieTitle.text = movieDetail.title
        movieDetail.posterPath?.let {
            val url = urlBuilder(it)
            Log.v(resources.getString(R.string.app_name), "Displaying image from URL $url")
            packShotImage.setImageUrl(url, imageLoader)
        }
    }
}