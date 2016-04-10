package org.frameworkset.web.socket.endpoint;

import org.frameworkset.web.socket.inf.WebSocketSession;

public interface NativeWebSocketSession  extends WebSocketSession {

	/**
	 * Return the underlying native WebSocketSession, if available.
	 * @return the native session or {@code null}
	 */
	Object getNativeSession();

	/**
	 * Return the underlying native WebSocketSession, if available.
	 * @param requiredType the required type of the session
	 * @return the native session of the required type or {@code null}
	 */
	<T> T getNativeSession(Class<T> requiredType);

}
