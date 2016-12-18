package isel.pdm.demos.mymoviedb.models.content

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.support.annotation.MainThread

/**
 * Class that implements the authority for movie information.
 *
 * The content provider exposes two tables: one that holds the upcoming movies; the other that
 * holds the movies currently in exhibition. The columns of both tables are identical.
 */
class MovieInfoProvider : ContentProvider() {

    /**
     * The public contract of the provided data model.
     */
    companion object {
        const val AUTHORITY = "isel.pdm.demos.mymoviedb"
        const val UPCOMING_TABLE_PATH = "upcoming"
        const val EXHIBITION_TABLE_PATH = "exhibition"

        const val UPCOMING_CONTENT = "content://$AUTHORITY/$UPCOMING_TABLE_PATH"
        val UPCOMING_CONTENT_URI: Uri = Uri.parse(UPCOMING_CONTENT)
        const val EXHIBITION_CONTENT = "content://$AUTHORITY/$EXHIBITION_TABLE_PATH"
        val EXHIBITION_CONTENT_URI: Uri = Uri.parse(EXHIBITION_CONTENT)

        val MOVIE_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/movies"
        val MOVIE_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/movie"

        const val COLUMN_ID = "_ID"
        const val COLUMN_ADULT = "ADULT"
        const val COLUMN_BACKDROP = "BACKDROP"
        const val COLUMN_POSTER = "POSTER"
        const val COLUMN_TITLE = "TITLE"
        const val COLUMN_OVERVIEW = "OVERVIEW"
        const val COLUMN_ORIGINAL_TITLE = "OTITLE"

        const val COLUMN_ID_IDX = 0
        const val COLUMN_ADULT_IDX = 1
        const val COLUMN_BACKDROP_IDX = 2
        const val COLUMN_POSTER_IDX = 3
        const val COLUMN_TITLE_IDX = 4
        const val COLUMN_OVERVIEW_IDX = 5
        const val COLUMN_ORIGINAL_TITLE_IDX = 6

        // Private constants to be used by the implementation
        private const val UPCOMING_TABLE_NAME = "Upcoming"
        private const val EXHIBITION_TABLE_NAME = "Exhibition"

        private const val UPCOMING_LIST_CODE = 1010
        private const val UPCOMING_ITEM_CODE = 1011
        private const val EXHIBITION_LIST_CODE = 1020
        private const val EXHIBITION_ITEM_CODE = 1021
    }

    /**
     * The associated helper for DB accesses and migration.
     */
    private inner class MovieInfoDbHelper(version: Int = 1, dbName: String = "MOVIE_DB") :
            SQLiteOpenHelper(this@MovieInfoProvider.context, dbName, null, version) {

        private fun createTable(db: SQLiteDatabase?, tableName: String) {
            val CREATE_CMD = "CREATE TABLE $tableName ( " +
                    "$COLUMN_ID INTEGER PRIMARY KEY , " +
                    "$COLUMN_ADULT BOOLEAN , " +
                    "$COLUMN_BACKDROP TEXT , " +
                    "$COLUMN_POSTER TEXT , " +
                    "$COLUMN_TITLE TEXT NOT NULL , " +
                    "$COLUMN_OVERVIEW TEXT NOT NULL , " +
                    "$COLUMN_ORIGINAL_TITLE TEXT NOT NULL )"
            db?.execSQL(CREATE_CMD)
        }

        private fun dropTable(db: SQLiteDatabase?, tableName: String) {
            val DROP_CMD = "DROP TABLE IF EXISTS $tableName"
            db?.execSQL(DROP_CMD)
        }

        override fun onCreate(db: SQLiteDatabase?) {
            createTable(db, UPCOMING_TABLE_NAME)
            createTable(db, EXHIBITION_TABLE_NAME)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            // Implementation note: This approach is usually unacceptable in production code.
            // Instead of droping all data and creating it anew, in most cases we should migrate it
            // instead
            dropTable(db, UPCOMING_TABLE_NAME)
            dropTable(db, EXHIBITION_TABLE_NAME)
            createTable(db, UPCOMING_TABLE_NAME)
            createTable(db, EXHIBITION_TABLE_NAME)
        }
    }

    /**
     * @property dbHelper The DB helper instance to be used for DB accesses.
     */
    @Volatile private lateinit var dbHelper: MovieInfoDbHelper

    /**
     * @property uriMatcher The instance used to match an URI to its corresponding content type
     */
    @Volatile private lateinit var uriMatcher: UriMatcher

    /**
     * Callback method that signals instance creation.
     * @return A boolean value indicating whether the instance was correctly initialized or not
     */
    @MainThread
    override fun onCreate(): Boolean {
        dbHelper = MovieInfoDbHelper()
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with (uriMatcher) {
            addURI(AUTHORITY, UPCOMING_TABLE_PATH, UPCOMING_LIST_CODE)
            addURI(AUTHORITY, "$UPCOMING_TABLE_PATH/#", UPCOMING_ITEM_CODE)
            addURI(AUTHORITY, EXHIBITION_TABLE_PATH, EXHIBITION_LIST_CODE)
            addURI(AUTHORITY, "$EXHIBITION_TABLE_PATH/#", EXHIBITION_ITEM_CODE)
        }
        return true
    }

    /** @see ContentProvider.getType */
    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE, EXHIBITION_LIST_CODE -> MOVIE_LIST_CONTENT_TYPE
        UPCOMING_ITEM_CODE, EXHIBITION_ITEM_CODE -> MOVIE_ITEM_CONTENT_TYPE
        else -> throw IllegalArgumentException("Uri $uri not supported")
    }

    /**
     * Helper function used to obtain the table information (i.e. table name and path) based on the
     * given [uri]
     * @param [uri] The table URI
     * @return A [Pair] instance bearing the table name (the pair's first) and the table path
     * part (the pair's second).
     * @throws IllegalArgumentException if the received [uri] does not refer to an existing table
     */
    private fun resolveTableInfoFromUri(uri: Uri): Pair<String, String> = when (uriMatcher.match(uri)) {
        UPCOMING_LIST_CODE -> Pair(UPCOMING_TABLE_NAME, UPCOMING_TABLE_PATH)
        EXHIBITION_LIST_CODE -> Pair(EXHIBITION_TABLE_NAME, EXHIBITION_TABLE_PATH)
        else -> null
    } ?: throw IllegalArgumentException("Uri $uri not supported")

    /**
     * Helper function used to obtain the table name and selection arguments based on the
     * given [uri]
     * @param [uri] The received URI, which may refer to a table or to an individual entry
     * @return A [Triple] instance bearing the table name (the triple's first), the selection
     * string (the triple's second) and the selection string parameters (the triple's third).
     * @throws IllegalArgumentException if the received [uri] does not refer to a valid data set
     */
    private fun resolveTableAndSelectionInfoFromUri(uri: Uri, selection: String?, selectionArgs: Array<String>?)
            : Triple<String, String?, Array<String>?> {
        val itemSelection = "$COLUMN_ID = ${uri.pathSegments.last()}"
        return when (uriMatcher.match(uri)) {
            UPCOMING_ITEM_CODE -> Triple(UPCOMING_TABLE_NAME, itemSelection, null)
            EXHIBITION_ITEM_CODE -> Triple(EXHIBITION_TABLE_NAME, itemSelection, null)
            else -> resolveTableInfoFromUri(uri).let { Triple(it.first, selection, selectionArgs) }
        }
    }

    /** @see ContentProvider.delete */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.writableDatabase
        try {
            val deletedCount = db.delete(params.first, params.second, params.third)
            if (deletedCount != 0)
                context.contentResolver.notifyChange(uri, null)
            return deletedCount
        }
        finally {
            db.close()
        }
    }

    /** @see ContentProvider.insert */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val tableInfo = resolveTableInfoFromUri(uri)
        val db = dbHelper.writableDatabase
        return try {
            val id = db.insert(tableInfo.first, null, values)
            if (id < 0) null else {
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("content://$AUTHORITY/${tableInfo.second}/$id")
            }
        }
        finally {
            db.close()
        }
    }

    /** @see ContentProvider.query */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.readableDatabase
        return db.query(params.first, projection, params.second, params.third, null, null, sortOrder)
    }

    /** @see ContentProvider.update */
    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Updates are not supported")
    }
}
