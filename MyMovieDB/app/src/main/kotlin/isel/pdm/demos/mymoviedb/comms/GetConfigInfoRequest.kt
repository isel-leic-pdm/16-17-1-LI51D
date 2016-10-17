package isel.pdm.demos.mymoviedb.comms

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import isel.pdm.demos.mymoviedb.models.MovieDetail

import com.fasterxml.jackson.module.kotlin.*
import isel.pdm.demos.mymoviedb.models.ConfigurationInfo

import java.io.IOException

/**
 * Class whose instances represent customized HTTP requests for getting the details of a
 * given movie.
 */
class GetConfigInfoRequest(url: String, success: (ConfigurationInfo) -> Unit, error: (VolleyError) -> Unit) : JsonRequest<ConfigurationInfo>(Method.GET, url, "", success, error) {

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<ConfigurationInfo> {

        try {
            // TODO: Refine to handle error responses
            val configInfo = mapper.readValue<ConfigurationInfo>(response.data)
            return Response.success(configInfo, null)
        } catch (e: IOException) {
            e.printStackTrace()
            return Response.error(VolleyError())
        }
    }
}
