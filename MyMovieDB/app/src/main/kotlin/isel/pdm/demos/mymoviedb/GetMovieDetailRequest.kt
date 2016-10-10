package isel.pdm.demos.mymoviedb

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import isel.pdm.demos.mymoviedb.models.MovieDetail

import com.fasterxml.jackson.module.kotlin.*

import java.io.IOException

/**
 * Class whose instances represent customized HTTP requests for getting the details of a
 * given movie.
 */
class GetMovieDetailRequest(url: String, success: (MovieDetail) -> Unit, error: (VolleyError) -> Unit) : JsonRequest<MovieDetail>(Request.Method.GET, url, "", success, error) {

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<MovieDetail> {

        try {
            // TODO: Refine to handle error responses
            val movieDto = mapper.readValue<MovieDetail>(response.data)
            return Response.success(movieDto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            return Response.error(VolleyError())
        }
    }
}
