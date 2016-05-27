package org.frameworkset.web.socket.inf;

public interface WebSocketMessage <T> {

	/**
	 * Returns the message payload. This will never be {@code null}.
	 */
	T getPayload();


	/**
	 * Return the number of bytes contained in the message.
	 */
	int getPayloadLength();

	/**
	 * When partial message support is available and requested via
	 * {@link org.frameworkset.web.socket.WebSocketHandler#supportsPartialMessages()},
	 * this method returns {@code true} if the current message is the last part of the
	 * complete WebSocket message sent by the client. Otherwise {@code false} is returned
	 * if partial message support is either not available or not enabled.
	 */
	boolean isLast();
}
