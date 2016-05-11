package org.frameworkset.schedule;

import java.util.concurrent.Future;

public interface ListenableFuture<T> extends Future<T> {

	/**
	 * Registers the given callback to this {@code ListenableFuture}. The callback will
	 * be triggered when this {@code Future} is complete or, if it is already complete,
	 * immediately.
	 * @param callback the callback to register
	 */
	void addCallback(ListenableFutureCallback<? super T> callback);

}
