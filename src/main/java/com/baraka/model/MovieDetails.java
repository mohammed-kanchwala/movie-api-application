package com.baraka.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class MovieDetails {

	@JsonAlias("Title")
	private String title;
	@JsonAlias("Year")
	private String year;
	private String imdbID;
	@JsonAlias("Type")
	private String type;
	@JsonAlias("Poster")
	private String poster;
}
