package com.baraka.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OmdbResponse implements Serializable {

	@JsonAlias("Search")
	private List<OmdbMovieDetails> movieDetails;
	private Integer totalResults;
	@JsonAlias("Response")
	private Boolean response;
	@JsonAlias("Error")
	private String error;
}
