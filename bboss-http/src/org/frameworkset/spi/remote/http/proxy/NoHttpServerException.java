package org.frameworkset.spi.remote.http.proxy;

public class NoHttpServerException extends RuntimeException {
	public NoHttpServerException() {
	}

	public NoHttpServerException(String message) {
		super(message);
	}

	public NoHttpServerException(Throwable cause) {
		super(cause);
	}

	public NoHttpServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoHttpServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
