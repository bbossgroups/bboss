package org.frameworkset.websocket;

import org.frameworkset.web.servlet.handler.AbstractHandlerMapping;
import org.frameworkset.web.socket.config.ServletWebSocketHandlerRegistry;

public class TestWebsocketServiceRegist {
	public void webSocketHandlerMapping() {
		ServletWebSocketHandlerRegistry registry = new ServletWebSocketHandlerRegistry(defaultSockJsTaskScheduler());
//		registerWebSocketHandlers(registry);
		AbstractHandlerMapping hm = registry.getHandlerMapping();
//		hm.setOrder(1);
//		return hm;
	}

}
