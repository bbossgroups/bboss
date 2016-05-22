package org.frameworkset.schedule;

import java.util.LinkedList;
import java.util.Queue;

import org.frameworkset.util.Assert;

public class ListenableFutureCallbackRegistry<T> {
	private final Queue<ListenableFutureCallback<? super T>> callbacks =
			new LinkedList<ListenableFutureCallback<? super T>>();

	private State state = State.NEW;

	private Object result = null;

	private final Object mutex = new Object();


	/**
	 * Adds the given callback to this registry.
	 * @param callback the callback to add
	 */
	@SuppressWarnings("unchecked")
	public void addCallback(ListenableFutureCallback<? super T> callback) {
		Assert.notNull(callback, "'callback' must not be null");

		synchronized (mutex) {
			switch (state) {
				case NEW:
					callbacks.add(callback);
					break;
				case SUCCESS:
					callback.onSuccess((T)result);
					break;
				case FAILURE:
					callback.onFailure((Throwable) result);
					break;
			}
		}
	}

	/**
	 * Triggers a {@link ListenableFutureCallback#onSuccess(Object)} call on all added
	 * callbacks with the given result
	 * @param result the result to trigger the callbacks with
	 */
	public void success(T result) {
		synchronized (mutex) {
			state = State.SUCCESS;
			this.result = result;

			while (!callbacks.isEmpty()) {
				callbacks.poll().onSuccess(result);
			}
		}
	}

	/**
	 * Triggers a {@link ListenableFutureCallback#onFailure(Throwable)} call on all added
	 * callbacks with the given {@code Throwable}.
	 * @param t the exception to trigger the callbacks with
	 */
	public void failure(Throwable t) {
		synchronized (mutex) {
			state = State.FAILURE;
			this.result = t;

			while (!callbacks.isEmpty()) {
				callbacks.poll().onFailure(t);
			}
		}
	}

	private enum State {NEW, SUCCESS, FAILURE}
}
