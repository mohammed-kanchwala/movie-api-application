package com.baraka.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmdbMovieDetails implements Serializable {

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
