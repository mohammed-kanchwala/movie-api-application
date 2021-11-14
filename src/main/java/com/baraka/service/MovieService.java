package com.baraka.service;

import com.baraka.model.ApiResponse;

public interface MovieService {

	ApiResponse searchMovie(String keyword, Integer page, Integer size);
}
