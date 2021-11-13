package com.baraka.service.impl;

import com.baraka.model.MovieDetails;
import com.baraka.model.MovieResponse;
import com.baraka.model.OmdbResponse;
import com.baraka.service.MovieService;
import com.baraka.service.OmdbService;
import com.baraka.util.RedisWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final RedisWrapper redisWrapper;

    private final OmdbService omdbService;

    @Autowired
    public MovieServiceImpl(
            RedisWrapper redisWrapper, OmdbService omdbService) {
        this.redisWrapper = redisWrapper;
        this.omdbService = omdbService;
    }

    @Override
    public MovieResponse searchMovie(String keyword, Integer page, Integer size) {

        String redisKey = keyword + "_" + page;

        if (Boolean.TRUE.equals(redisWrapper.hasKey(redisKey))) {
            return redisWrapper.getValueFromRedis(redisKey);
        }

        MovieResponse movieResponse = new MovieResponse();
        int apiPage = (page * 2) - 1;
        for (int i = 0; i < size / 10; i++, apiPage++) {
            OmdbResponse omdbResponse = omdbService.callOmdbApi(keyword, apiPage);

            MovieResponse apiResponse = new MovieResponse();
            BeanUtils.copyProperties(omdbResponse, apiResponse);
            if (apiResponse.getResponse().equals(Boolean.TRUE)) {
                createMovieResponse(movieResponse, apiResponse);
            }
            if (Objects.equals(apiResponse.getResponse(), Boolean.FALSE) || apiResponse.getTotalResults() <= 10 || apiResponse.getMovieDetails().size() < 10) {
                break;
            }
        }

        redisWrapper.saveToRedis(redisKey, movieResponse);
        return movieResponse;
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
