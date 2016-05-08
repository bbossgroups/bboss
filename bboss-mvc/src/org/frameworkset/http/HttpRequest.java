package org.frameworkset.http;

import java.net.URI;

import org.frameworkset.util.annotations.HttpMethod;

public interface HttpRequest  extends HttpMessage {

	/**
	 * Return the HTTP method of the request.
	 * @return the HTTP method as an HttpMethod enum value, or {@code null}
	 * if not resolvable (e.g. in case of a non-standard HTTP method)
	 */
	HttpMethod getMethod();

	/**
	 * Return the URI of the request.
	 * @return the URI of the request (never {@code null})
	 */
	URI getURI();
}
