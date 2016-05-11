package org.frameworkset.web.socket.config;

import java.util.Arrays;

import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.util.LinkedMultiValueMap;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.HttpRequestHandler;
import org.frameworkset.web.socket.handler.HandshakeHandler;
import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public class ServletWebSocketHandlerRegistration extends AbstractWebSocketHandlerRegistration<MultiValueMap<HttpRequestHandler, String>> {

	public ServletWebSocketHandlerRegistration(TaskScheduler sockJsTaskScheduler) {
		super(sockJsTaskScheduler);
	}


	@Override
	protected MultiValueMap<HttpRequestHandler, String> createMappings() {
		return new LinkedMultiValueMap<HttpRequestHandler, String>();
	}

	@Override
	protected void addSockJsServiceMapping(MultiValueMap<HttpRequestHandler, String> mappings,
			SockJsService sockJsService, WebSocketHandler handler, String pathPattern) {

		SockJsHttpRequestHandler httpHandler = new SockJsHttpRequestHandler(sockJsService, handler);
		mappings.add(httpHandler, pathPattern);
	}

	@Override
	protected void addWebSocketHandlerMapping(MultiValueMap<HttpRequestHandler, String> mappings,
			WebSocketHandler wsHandler, HandshakeHandler handshakeHandler,
			HandshakeInterceptor[] interceptors, String path) {

		WebSocketHttpRequestHandler httpHandler = new WebSocketHttpRequestHandler(wsHandler, handshakeHandler);
		if (!ObjectUtils.isEmpty(interceptors)) {
			httpHandler.setHandshakeInterceptors(Arrays.asList(interceptors));
		}
		mappings.add(httpHandler, path);
	}

}
