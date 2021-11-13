package com.baraka.service.impl;

import com.baraka.constants.ApplicationConstants;
import com.baraka.model.OmdbResponse;
import com.baraka.service.OmdbService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class OmdbServiceImpl implements OmdbService {

    private final RestTemplate restTemplate;

    private final String omdbURL;

    private final String apiKey;

    OmdbServiceImpl(RestTemplate restTemplate, @Value("${omdb.url}") String omdbURL,
                    @Value("${omdb.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.omdbURL = omdbURL;
        this.apiKey = apiKey;
    }

    @Override
    public OmdbResponse callOmdbApi(String keyword,
                                    Integer apiPage) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(ApplicationConstants.Headers.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(omdbURL)
                .queryParam(ApplicationConstants.Params.S_KEY, keyword)
                .queryParam(ApplicationConstants.Params.API_KEY, apiKey)
                .queryParam(ApplicationConstants.Params.PAGE, apiPage);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<OmdbResponse> apiResponse = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, entity, OmdbResponse.class);

        if (Objects.nonNull(apiResponse.getBody())) {
            return apiResponse.getBody();
        }
        return new OmdbResponse();
    }
}
