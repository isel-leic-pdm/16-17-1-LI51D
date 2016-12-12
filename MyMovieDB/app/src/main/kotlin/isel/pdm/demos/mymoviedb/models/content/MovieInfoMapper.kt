package isel.pdm.demos.mymoviedb.models.content

import android.content.ContentValues
import android.database.AbstractCursor
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import isel.pdm.demos.mymoviedb.models.*

/**
 * Extension function that maps a given [MovieItem] to the corresponding URI and
 * [ContentValues] pair.
 * @param [movie] The instance bearing the movie information
 * @return The newly created [ContentValues] instance
 */
fun MovieItem.toContentValues(): ContentValues {
    val result = ContentValues()
    with (MovieInfoProvider) {
        result.put(COLUMN_ID, id)
        result.put(COLUMN_TITLE, title)
        result.put(COLUMN_OVERVIEW, overview)
    }
    return result
}

/**
 * Extension function that maps a given [MovieListPage] to the corresponding iterable of
 * [ContentValues].
 * @param [movie] The instance bearing the list of movie information
 * @return The newly created iterable of [ContentValues]
 */
fun MovieListPage.toContentValues() : Array<ContentValues> =
        results.map(MovieItem::toContentValues).toTypedArray()

/**
 * Function that builds a [MovieItem] instnce from the given [Cursor]
 * @param [cursor] The cursor pointing to the movie item data
 * @return The newly created [MovieItem]
 */
private fun toMovieItem(cursor: Cursor): MovieItem {
    with (MovieInfoProvider.Companion) {
        return MovieItem(
                cursor.getInt(COLUMN_ID_IDX),
                cursor.getInt(COLUMN_ADULT_IDX) != 0,
                cursor.getString(COLUMN_BACKDROP_IDX),
                cursor.getString(COLUMN_POSTER_IDX),
                cursor.getString(COLUMN_TITLE_IDX),
                cursor.getString(COLUMN_OVERVIEW_IDX),
                cursor.getString(COLUMN_ORIGINAL_TITLE_IDX),
                0.0
        )
    }
}

/**
 * Extension function that builds a list of [MovieItem] from the given [Cursor]
 * @param [cursor] The cursor bearing the set of movie items
 * @return The newly created list of [MovieItem]
 */
fun Cursor.toMovieItemList(): List<MovieItem> {

    val cursorIterator = object : AbstractIterator<MovieItem>() {
        override fun computeNext() {
            when (isAfterLast) {
                true -> done()
                false -> setNext(toMovieItem(this@toMovieItemList))
            }
        }
    }

    return mutableListOf<MovieItem>().let { it.addAll(Iterable { cursorIterator }); it }
}
