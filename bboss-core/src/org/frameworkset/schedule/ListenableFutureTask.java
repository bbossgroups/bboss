package org.frameworkset.schedule;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<T>  extends FutureTask<T> implements ListenableFuture<T> {

	private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<T>();


	/**
	 * Create a new {@code ListenableFutureTask} that will, upon running,
	 * execute the given {@link Callable}.
	 * @param callable the callable task
	 */
	public ListenableFutureTask(Callable<T> callable) {
		super(callable);
	}

	/**
	 * Create a {@code ListenableFutureTask} that will, upon running,
	 * execute the given {@link Runnable}, and arrange that {@link #get()}
	 * will return the given result on successful completion.
	 * @param runnable the runnable task
	 * @param result the result to return on successful completion
	 */
	public ListenableFutureTask(Runnable runnable, T result) {
		super(runnable, result);
	}


	@Override
	public void addCallback(ListenableFutureCallback<? super T> callback) {
		this.callbacks.addCallback(callback);
	}

	@Override
	protected final void done() {
		Throwable cause;
		try {
			T result = get();
			this.callbacks.success(result);
			return;
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			return;
		}
		catch (ExecutionException ex) {
			cause = ex.getCause();
			if (cause == null) {
				cause = ex;
			}
		}
		catch (Throwable ex) {
			cause = ex;
		}
		this.callbacks.failure(cause);
	}

}
