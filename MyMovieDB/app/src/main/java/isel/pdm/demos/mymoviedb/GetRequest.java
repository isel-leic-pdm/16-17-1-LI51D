package isel.pdm.demos.mymoviedb;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GetRequest extends JsonRequest<MovieDto> {

    public GetRequest(String url, Response.Listener<MovieDto> success, Response.ErrorListener error) {
        super(Method.GET, url, "", success, error);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        final ObjectMapper mapper =
                new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Response result;
        try {
            final MovieDto movieDto = mapper.readValue(new String(response.data), MovieDto.class);
            Log.v("Tests", movieDto.toString());
            result = Response.success(movieDto, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = Response.error(new VolleyError());
        }
        return result;
    }
}
