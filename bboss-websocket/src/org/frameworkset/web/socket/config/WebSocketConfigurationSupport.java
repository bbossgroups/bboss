package org.frameworkset.web.socket.config;

import org.frameworkset.schedule.ThreadPoolTaskScheduler;
import org.frameworkset.spi.BaseApplicationContext;

public class WebSocketConfigurationSupport {
	private BaseApplicationContext websocketContainer;

	public WebSocketConfigurationSupport(BaseApplicationContext websocketContainer) {
		this.websocketContainer = websocketContainer;
	}
	
	public void webSocketHandlerMapping() {
		ServletWebSocketHandlerRegistry registry = new ServletWebSocketHandlerRegistry(defaultSockJsTaskScheduler());
		registerWebSocketHandlers(registry);
//		return registry.getHandlerMapping();
	}

	protected void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	}

	/**
	 * The default TaskScheduler to use if none is configured via
	 * {@link SockJsServiceRegistration#setTaskScheduler}, i.e.
	 * <pre class="code">
	 * &#064;Configuration
	 * &#064;EnableWebSocket
	 * public class WebSocketConfig implements WebSocketConfigurer {
	 *
	 *   public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	 *     registry.addHandler(myWsHandler(), "/echo").withSockJS().setTaskScheduler(myScheduler());
	 *   }
	 *
	 *   // ...
	 * }
	 * </pre>
	 */
	public ThreadPoolTaskScheduler defaultSockJsTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("SockJS-");
		scheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
		scheduler.setRemoveOnCancelPolicy(true);
		return scheduler;
	}

}
