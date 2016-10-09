package isel.pdm.demos.mymoviedb

import android.util.Log

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import isel.pdm.demos.mymoviedb.models.MovieDto

import java.io.IOException

class GetRequest(url: String, success: Response.Listener<MovieDto>, error: Response.ErrorListener) : JsonRequest<MovieDto>(Request.Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<MovieDto>? {
        val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        var result: Response<MovieDto>
        try {
            val movieDto = mapper.readValue(String(response.data), MovieDto::class.java)
            Log.v("Tests", movieDto.toString())
            result = Response.success(movieDto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            result = Response.error(VolleyError())
        }

        return result
    }
}
