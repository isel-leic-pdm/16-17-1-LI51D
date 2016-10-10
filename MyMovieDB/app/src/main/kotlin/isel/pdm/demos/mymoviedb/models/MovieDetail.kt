package isel.pdm.demos.mymoviedb.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class whose instances represent movie information obtained from the remote API.
 */
data class MovieDetail(
        val id: Int,
        val adult: Boolean,
        @JsonProperty("backdrop_path") val backdropPath: String?,
        @JsonProperty("poster_path") val posterPath: String?,
        val homepage: String,
        val title: String,
        val overview: String,
        @JsonProperty("original_title") val originalTitle: String,
        @JsonProperty("belongs_to_collection") val belongsToCollection: MovieCollection?,
        val genres: Collection<Genre>
    ) {

    data class MovieCollection(
            val id: Int,
            val name: String,
            @JsonProperty("poster_path") val posterPath: String?,
            @JsonProperty("backdrop_path") val backdropPath: String?)

    data class Genre(val id: Int, val name: String)
}
