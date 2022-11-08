package com.frameworkset.common.poolman.sql;

public class ParserException extends Exception {

	public ParserException(String string) {
		super(string);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

	public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParserException() {
	}
}
