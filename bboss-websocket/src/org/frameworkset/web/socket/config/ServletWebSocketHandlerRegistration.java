package org.frameworkset.web.socket.config;

import java.util.Arrays;

import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.util.LinkedMultiValueMap;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.socket.handler.HandshakeHandler;
import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.handler.SockJsHttpRequestHandler;
import org.frameworkset.web.socket.handler.WebSocketHttpRequestHandler;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsService;

public class ServletWebSocketHandlerRegistration extends AbstractWebSocketHandlerRegistration<MultiValueMap<HandlerMeta, String>> {

	public ServletWebSocketHandlerRegistration(TaskScheduler sockJsTaskScheduler) {
		super(sockJsTaskScheduler);
	}


	@Override
	protected MultiValueMap<HandlerMeta, String> createMappings() {
		return new LinkedMultiValueMap<HandlerMeta, String>();
	}

	@Override
	protected void addSockJsServiceMapping(MultiValueMap<HandlerMeta, String> mappings,
			SockJsService sockJsService, WebSocketHandler handler, String pathPattern) {

		SockJsHttpRequestHandler httpHandler = new SockJsHttpRequestHandler(sockJsService, handler);
		HandlerMeta handlerMeta = new HandlerMeta();
		handlerMeta.setHandler(httpHandler);
		handlerMeta.setWebsocket(true);
		mappings.add(handlerMeta, pathPattern);
	}

	@Override
	protected void addWebSocketHandlerMapping(MultiValueMap<HandlerMeta, String> mappings,
			WebSocketHandler wsHandler, HandshakeHandler handshakeHandler,
			HandshakeInterceptor[] interceptors, String path) {

		HandlerMeta handlerMeta = new HandlerMeta();
		WebSocketHttpRequestHandler httpHandler = new WebSocketHttpRequestHandler(wsHandler, handshakeHandler);
		if (!ObjectUtils.isEmpty(interceptors)) {
			httpHandler.setHandshakeInterceptors(Arrays.asList(interceptors));
		}
		handlerMeta.setHandler(httpHandler);
		handlerMeta.setWebsocket(true);
		mappings.add(handlerMeta, path);
	}

}
