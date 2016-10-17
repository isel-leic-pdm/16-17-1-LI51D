package isel.pdm.demos.mymoviedb.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class whose instances represent configuration information obtained from the remote API.
 */
data class ConfigurationInfo(
        val images: Images,
        @JsonProperty("change_keys") val changeKeys: Collection<String>
    ) {

    data class Images(
            @JsonProperty("base_url") val baseUrl: String?,
            @JsonProperty("secure_base") val secureBase: String?,
            @JsonProperty("backdrop_sizes") val backdropSizes: Collection<String>,
            @JsonProperty("logo_sizes") val logoSizes: Collection<String>,
            @JsonProperty("poster_sizes") val posterSizes: Collection<String>,
            @JsonProperty("profile_sizes") val profileSizes: Collection<String>,
            @JsonProperty("still_sizes") val stillSizes: Collection<String>
    )
}
