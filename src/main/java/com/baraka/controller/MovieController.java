package com.baraka.controller;

import com.baraka.constants.ApplicationConstants;
import com.baraka.model.ApiResponse;
import com.baraka.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchMovie(HttpServletRequest request,
                                                   @RequestParam String keyword,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "20") Integer size) {
        if (size % 10 != 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().errorCode(ApplicationConstants.ErrorCode.BAD_REQUEST).
                            errorMessage("Size should always be multiple of 10").build());
        }

        return ResponseEntity.ok(ApiResponse.builder()
                .data(movieService.searchMovie(keyword, page, size))
                .build());
    }
}
