package org.frameworkset.schedule;

public interface ListenableFutureCallback<T> {

	/**
	 * Called when the {@link ListenableFuture} successfully completes.
	 * @param result the result
	 */
	void onSuccess(T result);

	/**
	 * Called when the {@link ListenableFuture} fails to complete.
	 * @param t the exception that triggered the failure
	 */
	void onFailure(Throwable t);

}
