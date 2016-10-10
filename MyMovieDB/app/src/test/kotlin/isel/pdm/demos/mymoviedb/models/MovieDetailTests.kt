package isel.pdm.demos.mymoviedb.models

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieDetailTests {

    @Test
    fun genreInstantiation_producesCorrectObject() {

        val ID = 10
        val NAME = "Action"

        val genre = MovieDetail.Genre(id = ID, name = NAME)
        val (id, name) = genre

        assertEquals(ID, genre.id)
        assertEquals(ID, id)
        assertEquals(NAME, name)
        assertEquals(NAME, genre.name)
    }

    @Test
    fun movieCollectionInstantiation_producesCorrectObject() {

        val ID = 10
        val NAME = "Star Wars"
        val POSTER_PATH = "/thePath"

        val movieCollection = MovieDetail.MovieCollection(ID, NAME, POSTER_PATH, null)

        assertEquals(ID, movieCollection.id)
        assertEquals(NAME, movieCollection.name)
        assertEquals(POSTER_PATH, movieCollection.posterPath)
        assertNull(movieCollection.backdropPath)
    }

    @Test
    fun movieDetailInstantiation_producesCorrectObject() {
        // TODO
    }
}