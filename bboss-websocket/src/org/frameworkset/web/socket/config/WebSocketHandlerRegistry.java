package org.frameworkset.web.socket.config;

import org.frameworkset.web.socket.inf.WebSocketHandler;

public interface WebSocketHandlerRegistry {


	/**
	 * Configure a WebSocketHandler at the specified URL paths.
	 */
	WebSocketHandlerRegistration addHandler(WebSocketHandler webSocketHandler, String... paths);
	 
}
