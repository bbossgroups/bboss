package org.frameworkset.http;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.HttpMethod;


public abstract class CorsUtils {

	/**
	 * Returns {@code true} if the request is a valid CORS one.
	 */
	public static boolean isCorsRequest(HttpServletRequest request) {
		return (request.getHeader(HttpHeaders.ORIGIN) != null);
	}

	/**
	 * Returns {@code true} if the request is a valid CORS pre-flight one.
	 */
	public static boolean isPreFlightRequest(HttpServletRequest request) {
		return (isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) &&
				request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
	}

}
