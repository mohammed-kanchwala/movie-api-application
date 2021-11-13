package com.baraka.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Headers {
		public static final String ACCEPT = "Accept";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Params {
		public static final String S_KEY = "s";
		public static final String API_KEY = "apikey";
		public static final String PAGE = "page";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ErrorCode {
		public static final String BAD_REQUEST = "BAD_REQUEST";
	}
}
