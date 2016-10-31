package isel.pdm.demos.mymoviedb.models

import java.util.*
import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class whose instances represent movie information obtained from the remote API.
 *
 * <p>As all other DTOs used in the application, the class implements the Parcelable contract
 * thereby allowing them to be passed between Android components and, eventually, across process
 * boundaries.</p>
 *
 * @property id The movie identifier
 * @property adult boolean value indicating whether the movie is rated as adult content
 * @property backdropPath The relative path for obtaining the movie's backdrop image
 * @property posterPath The relative path for obtaining the movie's poster image
 * @property homepage The movie's homepage URL
 * @property title The movie's title
 * @property overview The movie's description (an overview text)
 * @property originalTitle The movie's original title
 * @property belongsToCollection The movie collection to which the movie belongs to (if any)
 * @property genres The genres to which the movie is related to
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
        val genres: List<Genre>,
        @JsonProperty("vote_average") val rating: Double) : Parcelable {

    /**
     * Class whose instances represent movie collections in the enclosing movie detail instance.
     *
     * @property id The collection identifier
     * @property name The collection name
     * @property posterPath The relative path for obtaining the collection's poster image
     * @property backdropPath The relative path for obtaining the collection's backdrop image
     */
    data class MovieCollection(
            val id: Int,
            val name: String,
            @JsonProperty("poster_path") val posterPath: String?,
            @JsonProperty("backdrop_path") val backdropPath: String?) : Parcelable {

        companion object {
            /** Factory of MovieCollectionqç instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<MovieCollection> {
                override fun createFromParcel(source: Parcel) = MovieCollection(source)
                override fun newArray(size: Int): Array<MovieCollection?> = arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(
                id = source.readInt(),
                name = source.readString(),
                posterPath = source.readString(),
                backdropPath = source.readString()
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
                writeString(name)
                writeString(posterPath)
                writeString(backdropPath)
            }
        }
    }

    /**
     * Class whose instances represent movie genres in the enclosing movie detail instance.
     *
     * @property id The genre identifier
     * @property name The genre's name
     */
    data class Genre(val id: Int, val name: String) : Parcelable {

        override fun toString() = name

        companion object {
            /** Factory of Genre instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Genre> {
                override fun createFromParcel(source: Parcel) = Genre(source)
                override fun newArray(size: Int): Array<Genre?> = arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(id = source.readInt(), name = source.readString())

        /** Not used (see android documentation for further details) */
        override fun describeContents() = 0

        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeString(name)
        }
    }

    companion object {
        /** Factory of MovieDetail instances */
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<MovieDetail> {
            override fun createFromParcel(source: Parcel) = MovieDetail(source)
            override fun newArray(size: Int): Array<MovieDetail?> = arrayOfNulls(size)
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
            homepage = source.readString(),
            title = source.readString(),
            overview = source.readString(),
            originalTitle = source.readString(),
            belongsToCollection = source.readParcelable<MovieCollection>(MovieCollection::class.java.classLoader),
            genres = mutableListOf<Genre>().apply { source.readTypedList(this, Genre.CREATOR) },
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
            writeString(homepage)
            writeString(title)
            writeString(overview)
            writeString(originalTitle)
            dest.writeParcelable(belongsToCollection, flags)
            writeTypedList(genres)
            writeDouble(rating)
        }
    }
}
