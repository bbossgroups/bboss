package org.frameworkset.schedule;

public interface ErrorHandler {

	/**
	 * Handle the given error, possibly rethrowing it as a fatal exception.
	 */
	void handleError(Throwable t);

}
