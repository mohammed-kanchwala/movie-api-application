package com.baraka.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class MovieResponse {

	@JsonAlias("Search")
	private List<MovieDetails> search;
	private String totalResults;
	@JsonAlias("Response")
	private String response;
}
