package com.baraka.service;

import com.baraka.model.MovieResponse;

public interface MovieService {

	MovieResponse searchMovie(String keyword, Integer page, Integer size);
}
