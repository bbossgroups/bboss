package org.frameworkset.web.socket.sockjs;

import javax.servlet.http.HttpServletRequest;

public interface CorsConfigurationSource {
	/**
	 * Return a {@link CorsConfiguration} based on the incoming request.
	 */
	CorsConfiguration getCorsConfiguration(HttpServletRequest request);
}
