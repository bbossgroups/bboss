package org.frameworkset.web.socket.handler;

import java.util.Map;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public interface HandshakeInterceptor {
	/**
	 * Invoked before the handshake is processed.
	 * @param request the current request
	 * @param response the current response
	 * @param wsHandler the target WebSocket handler
	 * @param attributes attributes from the HTTP handshake to associate with the WebSocket
	 * session; the provided attributes are copied, the original map is not used.
	 * @return whether to proceed with the handshake ({@code true}) or abort ({@code false})
	 */
	boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception;

	/**
	 * Invoked after the handshake is done. The response status and headers indicate
	 * the results of the handshake, i.e. whether it was successful or not.
	 * @param request the current request
	 * @param response the current response
	 * @param wsHandler the target WebSocket handler
	 * @param exception an exception raised during the handshake, or {@code null} if none
	 */
	void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Exception exception);
}
