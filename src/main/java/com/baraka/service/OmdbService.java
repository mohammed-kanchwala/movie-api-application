package com.baraka.service;

import com.baraka.model.OmdbResponse;

public interface OmdbService {

    OmdbResponse callOmdbApi(String keyword,
                             Integer apiPage);
}
