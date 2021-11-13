package com.baraka.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MovieResponse implements Serializable {

	private List<MovieDetails> movieDetails;
	private Integer totalResults;
	private Boolean response;
}
