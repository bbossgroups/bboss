package org.frameworkset.web.socket.sockjs;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public interface SockJsService {
	/**
	 * Process a SockJS HTTP request.
	 * <p>See the "Base URL", "Static URLs", and "Session URLs" sections of the <a
	 * href="http://sockjs.github.io/sockjs-protocol/sockjs-protocol-0.3.3.html">SockJS
	 * protocol</a> for details on the types of URLs expected.
	 * @param request the current request
	 * @param response the current response
	 * @param sockJsPath the remainder of the path within the SockJS service prefix
	 * @param handler the handler that will exchange messages with the SockJS client
	 * @throws SockJsException raised when request processing fails; generally, failed
	 * attempts to send messages to clients automatically close the SockJS session
	 * and raise {@link SockJsTransportFailureException}; failed attempts to read
	 * messages from clients do not automatically close the session and may result
	 * in {@link SockJsMessageDeliveryException} or {@link SockJsException};
	 * exceptions from the WebSocketHandler can be handled internally or through
	 * {@link ExceptionWebSocketHandlerDecorator} or some alternative decorator.
	 * The former is automatically added when using
	 * {@link org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler}.
	 */
	void handleRequest(ServerHttpRequest request, ServerHttpResponse response, String sockJsPath,
			WebSocketHandler handler) throws SockJsException;
}
