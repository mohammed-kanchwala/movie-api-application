package com.baraka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baraka.controller.MovieController;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class MovieApiApplicationTest {

	@Autowired
	private MovieController movieController;

	@Test
	void contextLoads () {
		assertNotNull(movieController);
	}
}
