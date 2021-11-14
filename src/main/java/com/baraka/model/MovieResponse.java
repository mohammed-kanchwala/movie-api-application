package com.baraka.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse implements Serializable {

	private List<MovieDetails> movieDetails;
	private Integer totalResults;
	private Boolean response;
}
