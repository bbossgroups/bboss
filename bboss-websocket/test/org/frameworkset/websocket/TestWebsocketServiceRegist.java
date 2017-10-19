package org.frameworkset.websocket;

import org.frameworkset.schedule.ThreadPoolTaskScheduler;
import org.frameworkset.web.servlet.handler.HandlerMappingsTable;
import org.frameworkset.web.socket.config.ServletWebSocketHandlerRegistry;

public class TestWebsocketServiceRegist {
	public void webSocketHandlerMapping() {
		ServletWebSocketHandlerRegistry registry = new ServletWebSocketHandlerRegistry(defaultSockJsTaskScheduler());
//		registerWebSocketHandlers(registry);
		HandlerMappingsTable hm = null;
		registry.registHandlerMapping(hm);
//		hm.setOrder(1);
//		return hm;
	}
	
	public ThreadPoolTaskScheduler defaultSockJsTaskScheduler() {		
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("SockJS-");
		scheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
		scheduler.setRemoveOnCancelPolicy(true);
		return scheduler;
	}

}
