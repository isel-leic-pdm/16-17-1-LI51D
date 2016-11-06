package isel.pdm.demos.mymoviedb.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Class whose instances represent a page of a list of movies obtained from the remote API. Notice
 * that produced lists are always paginated.
 *
 * <p>As all other DTOs used in the application, the class implements the Parcelable contract
 * thereby allowing them to be passed between Android components and, eventually, across process
 * boundaries.</p>
 *
 * @property page The page number in the list
 * @property totalPages The number of pages that the list comprises
 * @property totalResults The total number of movies contained in the list
 **/
data class MovieListPage(
        val page: Int,
        @JsonProperty("total_pages") val totalPages: Int,
        @JsonProperty("total_results") val totalResults: Int,
        val dates: DatesInterval,
        val results: List<MovieItem>) : Parcelable {

    /**
     * Class whose instances represent date intervals.
     *
     * @property minimum The lower bound of the interval
     * @property name The upper bound of the interval
     */
    data class DatesInterval(
            val minimum: String,
            val maximum: String
    ) : Parcelable {

        companion object {
            /** Factory of DatesInterval instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<MovieListPage.DatesInterval> {
                override fun createFromParcel(source: Parcel) = MovieListPage.DatesInterval(source)
                override fun newArray(size: Int): Array<MovieListPage.DatesInterval?> = arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(source.readString(), source.readString())

        /** Not used (see android documentation for further details) */
        override fun describeContents() = 0

        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(minimum)
            dest.writeString(maximum)
        }
    }

    companion object {
        /** Factory of MovieListPage instances */
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<MovieListPage> {
            override fun createFromParcel(source: Parcel) = MovieListPage(source)
            override fun newArray(size: Int): Array<MovieListPage?> = arrayOfNulls(size)
        }
    }

    /**
     * Initiates an instance from the given parcel.
     * @param source The parcel from where the data is to be loaded from
     */
    constructor(source: Parcel) : this(
            page = source.readInt(),
            totalPages = source.readInt(),
            totalResults = source.readInt(),
            dates = source.readParcelable<MovieListPage.DatesInterval>(MovieListPage.DatesInterval::class.java.classLoader),
            results = mutableListOf<MovieItem>().apply { source.readTypedList(this, MovieItem.CREATOR) }
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
            writeInt(page)
            writeInt(totalPages)
            writeInt(totalResults)
            writeParcelable(dates, flags)
            writeTypedList(results)
        }
    }
}