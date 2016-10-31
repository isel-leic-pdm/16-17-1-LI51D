package isel.pdm.demos.mymoviedb.services

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader

/**
 * TODO:
 */
class LruImageCache : ImageLoader.ImageCache {

    override fun getBitmap(url: String?): Bitmap {
        throw UnsupportedOperationException("not implemented")
    }

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        throw UnsupportedOperationException("not implemented")
    }
}