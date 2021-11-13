package com.baraka.service.impl;

import com.baraka.constants.ApplicationConstants;
import com.baraka.model.MovieDetails;
import com.baraka.model.MovieResponse;
import com.baraka.service.MovieService;
import com.baraka.util.RedisWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Cacheable
public class MovieServiceImpl implements MovieService {

    private RestTemplate restTemplate;

    private String omdbURL;

    private String apiKey;

    private RedisWrapper redisWrapper;

    @Autowired
    public MovieServiceImpl(RestTemplate restTemplate, @Value("${omdb.url}") String omdbURL,
                            @Value("${omdb.key}") String apiKey, RedisWrapper redisWrapper) {
        this.restTemplate = restTemplate;
        this.omdbURL = omdbURL;
        this.apiKey = apiKey;
        this.redisWrapper = redisWrapper;
    }

    @Override
    public MovieResponse searchMovie(String keyword, Integer page, Integer size) {

        String redisKey = keyword + "_" + page;

        if (redisWrapper.hasKey(redisKey)) {
            return redisWrapper.getValueFromRedis(redisKey);
        }

        MovieResponse movieResponse = new MovieResponse();
        int apiPage = (page * 2) - 1;
        int counter = 0;
        for (int i = 0; i < size / 10; i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(ApplicationConstants.Headers.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(omdbURL)
                    .queryParam(ApplicationConstants.Params.S_KEY, keyword)
                    .queryParam(ApplicationConstants.Params.API_KEY, apiKey)
                    .queryParam(ApplicationConstants.Params.PAGE, counter == 0 ? apiPage : apiPage++);

            counter++;
            ResponseEntity<MovieResponse> response = callOmdbApi(headers, builder);

            if (Objects.nonNull(response.getBody()) && Objects.equals(response.getBody().getResponse(), Boolean.TRUE)) {
                MovieResponse apiResponse = response.getBody();
                createMovieResponse(movieResponse, response.getBody());
                if (Objects.equals(apiResponse.getResponse(), Boolean.FALSE) || apiResponse.getTotalResults() <= 10 || apiResponse.getMovieDetails().size() < 10) {
                    break;
                }
            }
        }

        redisWrapper.saveToRedis(redisKey, movieResponse);
        return movieResponse;
    }

    private ResponseEntity<MovieResponse> callOmdbApi(HttpHeaders headers,
                                                      UriComponentsBuilder builder) {
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, entity, MovieResponse.class);
    }

    private void createMovieResponse(MovieResponse movieResponse, MovieResponse response) {
        if (Objects.nonNull(movieResponse.getMovieDetails()) && !movieResponse.getMovieDetails().isEmpty()) {
            List<MovieDetails> movieDetailsList = movieResponse.getMovieDetails();
            movieDetailsList.addAll(response.getMovieDetails());
            movieResponse.setMovieDetails(movieDetailsList);
        } else {
            movieResponse.setMovieDetails(response.getMovieDetails());
        }
        movieResponse.setTotalResults(response.getTotalResults() / 2);
        movieResponse.setResponse(true);
    }

}
