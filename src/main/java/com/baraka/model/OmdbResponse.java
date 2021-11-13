package com.baraka.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OmdbResponse implements Serializable {

    @JsonAlias("Search")
    private List<MovieDetails> movieDetails;
    private Integer totalResults;
    @JsonAlias("Response")
    private Boolean response;
}
