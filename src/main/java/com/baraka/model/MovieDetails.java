package com.baraka.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MovieDetails implements Serializable {

	private String title;
	private String year;
	private String imdbID;
	private String type;
	private String poster;
}
