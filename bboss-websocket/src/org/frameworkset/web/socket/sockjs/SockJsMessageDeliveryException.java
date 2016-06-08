package org.frameworkset.web.socket.sockjs;

import java.util.List;

public class SockJsMessageDeliveryException  extends SockJsException {

	private final List<String> undeliveredMessages;


	public SockJsMessageDeliveryException(String sessionId, List<String> undeliveredMessages, Throwable cause) {
		super("Failed to deliver message(s) " + undeliveredMessages + " for session " + sessionId, sessionId, cause);
		this.undeliveredMessages = undeliveredMessages;
	}

	public SockJsMessageDeliveryException(String sessionId, List<String> undeliveredMessages, String message) {
		super("Failed to deliver message(s) " + undeliveredMessages + " for session "
				+ sessionId + ": " + message, sessionId, null);
		this.undeliveredMessages = undeliveredMessages;
	}

	public List<String> getUndeliveredMessages() {
		return this.undeliveredMessages;
	}

}
