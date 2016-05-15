package org.frameworkset.web.socket.sockjs;

import org.frameworkset.util.beans.NestedRuntimeException;

public class SockJsException  extends NestedRuntimeException {

	private final String sessionId;


	/**
	 * Constructor for SockJsException.
	 * @param message the exception message
	 * @param cause the root cause
	 */
	public SockJsException(String message, Throwable cause) {
		this(message, null, cause);
	}

	/**
	 * Constructor for SockJsException.
	 * @param message the exception message
	 * @param sessionId the SockJS session id
	 * @param cause the root cause
	 */
	public SockJsException(String message, String sessionId, Throwable cause) {
		super(message, cause);
		this.sessionId = sessionId;
	}


	/**
	 * Return the SockJS session id.
	 */
	public String getSockJsSessionId() {
		return this.sessionId;
	}

}
