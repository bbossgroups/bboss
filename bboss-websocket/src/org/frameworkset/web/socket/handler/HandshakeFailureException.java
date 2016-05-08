package org.frameworkset.web.socket.handler;

import org.frameworkset.util.beans.NestedRuntimeException;

public class HandshakeFailureException  extends NestedRuntimeException {

	public HandshakeFailureException(String message) {
		super(message);
	}

	public HandshakeFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
