package org.frameworkset.web.socket.sockjs;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public interface TransportHandler {
	/**
	 * Initialize this handler with the given configuration.
	 * @param serviceConfig the configuration as defined by the containing
	 * {@link org.frameworkset.web.socket.sockjs.SockJsService}
	 */
	void initialize(SockJsServiceConfig serviceConfig);

	/**
	 * Return the transport type supported by this handler.
	 */
	TransportType getTransportType();

	/**
	 * Handle the given request and delegate messages to the provided
	 * {@link WebSocketHandler}.
	 * @param request the current request
	 * @param response the current response
	 * @param handler the target WebSocketHandler (never {@code null})
	 * @param session the SockJS session (never {@code null})
	 * @throws SockJsException raised when request processing fails as
	 * explained in {@link SockJsService}
	 */
	void handleRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler handler, SockJsSession session) throws SockJsException;

}
