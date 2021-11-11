package com.baraka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.baraka.constants.ApplicationConstants;
import com.baraka.model.MovieResponse;
import com.baraka.service.MovieService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${omdb.url}")
	private String omdbURL;

	@Value("${omdb.key}")
	private String apiKey;


	@Override
	public MovieResponse searchMovie(String keyword) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(omdbURL)
				.queryParam(ApplicationConstants.PARAMS.S_KEY, keyword)
				.queryParam(ApplicationConstants.PARAMS.API_KEY, apiKey)
				.queryParam(ApplicationConstants.PARAMS.PAGE, "1");

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<MovieResponse> response = restTemplate.exchange(builder.toUriString(),
				HttpMethod.GET, entity, MovieResponse.class);

		log.debug("HTTP Response: {}", response);

		return response.getBody();
	}
}
