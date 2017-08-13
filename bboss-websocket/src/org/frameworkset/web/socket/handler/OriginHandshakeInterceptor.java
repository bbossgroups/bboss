package org.frameworkset.web.socket.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginHandshakeInterceptor  implements HandshakeInterceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final Set<String> allowedOrigins = new LinkedHashSet<String>();


	/**
	 * Default constructor with only same origin requests allowed.
	 */
	public OriginHandshakeInterceptor() {
	}

	/**
	 * Constructor using the specified allowed origin values.
	 * @see #setAllowedOrigins(Collection)
	 */
	public OriginHandshakeInterceptor(Collection<String> allowedOrigins) {
		setAllowedOrigins(allowedOrigins);
	}


	/**
	 * Configure allowed {@code Origin} header values. This check is mostly
	 * designed for browsers. There is nothing preventing other types of client
	 * to modify the {@code Origin} header value.
	 * <p>Each provided allowed origin must have a scheme, and optionally a port
	 * (e.g. "http://example.org", "http://example.org:9090"). An allowed origin
	 * string may also be "*" in which case all origins are allowed.
	 * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454: The Web Origin Concept</a>
	 */
	public void setAllowedOrigins(Collection<String> allowedOrigins) {
		Assert.notNull(allowedOrigins, "Allowed origins Collection must not be null");
		this.allowedOrigins.clear();
		this.allowedOrigins.addAll(allowedOrigins);
	}

	/**
	 * @since 4.1.5
	 * @see #setAllowedOrigins
	 */
	public Collection<String> getAllowedOrigins() {
		return Collections.unmodifiableSet(this.allowedOrigins);
	}


	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		if (!WebUtils.isSameOrigin(request) && !WebUtils.isValidOrigin(request, this.allowedOrigins)) {
			response.setStatusCode(HttpStatus.FORBIDDEN);
			if (logger.isDebugEnabled()) {
				logger.debug("Handshake request rejected, Origin header value " +
						request.getHeaders().getOrigin() + " not allowed");
			}
			return false;
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Exception exception) {
	}

}
