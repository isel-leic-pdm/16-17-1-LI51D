package isel.pdm.demos.mymoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class whose instances represent movie information obtained from the remote API.
 */
public class MovieDto {

    private boolean adult;

    public boolean getAdult() {
        return adult;
    }

    @JsonProperty("title")
    private String title;

    public String isTitle() {
        return title;
    }

    private long id;

    public long getId() {
        return id;
    }
}
