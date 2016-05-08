package org.frameworkset.http;

public interface ServerHttpAsyncRequestControl {
	/**
	 * Enable asynchronous processing after which the response remains open until a call
	 * to {@link #complete()} is made or the server times out the request. Once enabled,
	 * additional calls to this method are ignored.
	 */
	void start();

	/**
	 * A variation on {@link #start()} that allows specifying a timeout value to use to
	 * use for asynchronous processing. If {@link #complete()} is not called within the
	 * specified value, the request times out.
	 */
	void start(long timeout);

	/**
	 * Return whether asynchronous request processing has been started.
	 */
	boolean isStarted();

	/**
	 * Mark asynchronous request processing as completed.
	 */
	void complete();

	/**
	 * Return whether asynchronous request processing has been completed.
	 */
	boolean isCompleted();
}
