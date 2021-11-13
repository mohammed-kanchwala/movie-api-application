package com.baraka.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieDetails implements Serializable {

	private String title;
	private String year;
	private String imdbID;
	private String type;
	private String poster;
}
