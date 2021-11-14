package com.baraka.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.baraka.MovieApiApplicationTest;
import com.baraka.constants.ApplicationConstants;
import com.baraka.model.ApiResponse;
import com.baraka.model.OmdbResponse;
import com.baraka.service.MovieService;
import com.baraka.service.OmdbService;
import com.baraka.util.RedisWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Slf4j
class MovieControllerTest extends MovieApiApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MovieController movieController;

	@Autowired
	private MovieService movieService;

	@MockBean
	private OmdbService omdbService;

	@MockBean
	private RedisWrapper redisWrapper;

	@Test
	@DisplayName("Validate Success Response From OMDb API")
	void successResponseTest() throws Exception {
		when(redisWrapper.hasKey(anyString())).thenReturn(false);
		OmdbResponse omdbResponse = OmdbResponse.builder()
				.response(true)
				.movieDetails(new ArrayList<>())
				.totalResults(0)
				.build();
		when(omdbService.callOmdbApi(anyString(), anyInt())).thenReturn(omdbResponse);

		mockMvc.perform(get("/api/search").queryParam("keyword", anyString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(mvcResult -> {
					ApiResponse response = new ObjectMapper().readValue(
							mvcResult.getResponse().getContentAsString(), ApiResponse.class);
					assertNotNull(response);
					assertEquals(true, ((LinkedHashMap) response.getData()).get("response"));
					assertEquals(0, ((LinkedHashMap) response.getData()).get("totalResults"));
					assertNull(((LinkedHashMap) response.getData()).get("movieDetails"));
				});
	}


	@Test
	@DisplayName("Validate Failure Response From OMDb API")
	void failureResponseTest() throws Exception {
		when(redisWrapper.hasKey(anyString())).thenReturn(false);
		OmdbResponse omdbResponse = OmdbResponse.builder()
				.response(false)
				.error("Movie not found!")
				.build();
		when(omdbService.callOmdbApi(anyString(), anyInt())).thenReturn(omdbResponse);

		mockMvc.perform(get("/api/search").queryParam("keyword", anyString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(mvcResult -> {
					ApiResponse response = new ObjectMapper().readValue(
							mvcResult.getResponse().getContentAsString(), ApiResponse.class);
					assertNotNull(response);
					assertEquals(ApplicationConstants.ErrorCode.NOT_FOUND, response.getErrorCode());
					assertEquals("Movie not found!", response.getErrorMessage());
					assertNull(response.getData());
				});
	}
}
