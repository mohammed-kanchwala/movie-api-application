package com.baraka.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baraka.model.MovieResponse;
import com.baraka.service.MovieService;

@RestController
@RequestMapping("/api")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@GetMapping("/search")
	public ResponseEntity<MovieResponse> searchMovie(HttpServletRequest request,
			@RequestParam String keyword) {
		return ResponseEntity.ok(movieService.searchMovie(keyword));
	}
}
