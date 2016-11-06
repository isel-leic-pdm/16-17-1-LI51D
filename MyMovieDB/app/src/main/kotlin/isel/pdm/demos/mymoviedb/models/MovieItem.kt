package isel.pdm.demos.mymoviedb.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class whose instances represent movie information contained within movie lists.
 *
 * <p>As all other DTOs used in the application, the class implements the Parcelable contract
 * thereby allowing them to be passed between Android components and, eventually, across process
 * boundaries.</p>
 *
 * @property id The movie identifier
 * @property adult boolean value indicating whether the movie is rated as adult content
 * @property backdropPath The relative path for obtaining the movie's backdrop image
 * @property posterPath The relative path for obtaining the movie's poster image
 * @property title The movie's title
 * @property overview The movie's description (an overview text)
 * @property originalTitle The movie's original title
 * @property rating The movie's average rating
 **/
data class MovieItem(
        val id: Int,
        val adult: Boolean,
        @JsonProperty("backdrop_path") val backdropPath: String?,
        @JsonProperty("poster_path") val posterPath: String?,
        val title: String,
        val overview: String,
        @JsonProperty("original_title") val originalTitle: String,
        @JsonProperty("vote_average") val rating: Double) : Parcelable {

    companion object {
        /** Factory of MovieItem instances */
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<MovieItem> {
            override fun createFromParcel(source: Parcel) = MovieItem(source)
            override fun newArray(size: Int): Array<MovieItem?> = arrayOfNulls(size)
        }
    }

    /**
     * Initiates an instance from the given parcel.
     * @param source The parcel from where the data is to be loaded from
     */
    constructor(source: Parcel) : this(
            id = source.readInt(),
            adult = 1 == source.readInt(),
            backdropPath = source.readString(),
            posterPath = source.readString(),
            title = source.readString(),
            overview = source.readString(),
            originalTitle = source.readString(),
            rating = source.readDouble()
    )

    /** Not used (see android documentation for further details) */
    override fun describeContents() = 0

    /**
     * Saves the instance data to the given parcel.
     * @param dest The parcel to where the data is to be saved to
     * @param flags Not used (see android documentation for further details)
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(id)
            writeInt((if (adult) 1 else 0))
            writeString(backdropPath)
            writeString(posterPath)
            writeString(title)
            writeString(overview)
            writeString(originalTitle)
            writeDouble(rating)
        }
    }
}
