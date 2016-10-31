package isel.pdm.demos.mymoviedb.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class whose instances represent configuration information obtained from the remote API.
 *
 * <p>As all other DTOs used in the application, the class implements the Parcelable contract
 * thereby allowing them to be passed between Android components and, eventually, across process
 * boundaries.</p>
 *
 * @property images The configuration info required for obtaining images
 * @property changeKeys ...
 */
data class ConfigurationInfo(
        val images: Images,
        @JsonProperty("change_keys") val changeKeys: List<String>) : Parcelable {

    /**
     * Class whose instances represent movie collections in the enclosing movie detail instance.
     *
     * @property baseUrl The base URL (HTTP) used to build images URLs
     * @property secureBase The base URL (HTTPS) used to build images URLs
     * @property backdropSizes The supported sizes of backdrop images
     * @property logoSizes The supported logo sizes
     * @property posterSizes The supported sizes of poster images
     * @property profileSizes The supported sizes of profile images
     * @property stillSizes The supported sizes of still images
     */
    data class Images(
            @JsonProperty("base_url") val baseUrl: String?,
            @JsonProperty("secure_base_url") val secureBase: String?,
            @JsonProperty("backdrop_sizes") val backdropSizes: List<String>,
            @JsonProperty("logo_sizes") val logoSizes: List<String>,
            @JsonProperty("poster_sizes") val posterSizes: List<String>,
            @JsonProperty("profile_sizes") val profileSizes: List<String>,
            @JsonProperty("still_sizes") val stillSizes: List<String>) : Parcelable {

        companion object {
            /** Factory of Images instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Images> {
                override fun createFromParcel(source: Parcel) = Images(source)
                override fun newArray(size: Int): Array<Images?> = arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(
            baseUrl = source.readString(),
            secureBase = source.readString(),
            backdropSizes = source.createStringArrayList(),
            logoSizes = source.createStringArrayList(),
            posterSizes = source.createStringArrayList(),
            profileSizes = source.createStringArrayList(),
            stillSizes = source.createStringArrayList()
        )

        /** Not used (see android documentation for further details) */
        override fun describeContents() = 0

        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {

            with(dest) {
                writeString(baseUrl)
                writeString(secureBase)
                writeStringList(backdropSizes)
                writeStringList(logoSizes)
                writeStringList(posterSizes)
                writeStringList(profileSizes)
                writeStringList(stillSizes)
            }
        }
    }

    companion object {
        /** Factory of Images instances */
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<ConfigurationInfo> {
            override fun createFromParcel(source: Parcel) = ConfigurationInfo(source)
            override fun newArray(size: Int): Array<ConfigurationInfo?> = arrayOfNulls(size)
        }

        val API_KEY_NAME = "api_key"
        val API_KEY_VALUE = "c45808d49ff7af92014ae030f009cd17"
        val API_KEY_PARAM: String = "$API_KEY_NAME=$API_KEY_VALUE"
    }

    /**
     * Initiates an instance from the given parcel.
     * @param source The parcel from where the data is to be loaded from
     */
    constructor(source: Parcel) : this(
        source.readParcelable<Images>(Images::class.java.classLoader),
        source.createStringArrayList()
    )

    /** Not used (see android documentation for further details) */
    override fun describeContents() = 0

    /**
     * Saves the instance data to the given parcel.
     * @param dest The parcel to where the data is to be saved to
     * @param flags Not used (see android documentation for further details)
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(images, flags)
        dest.writeStringList(changeKeys)
    }
}
