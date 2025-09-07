package org.frameworkset.web.request.async;

import java.util.concurrent.Callable;

import org.frameworkset.schedule.AsyncTaskExecutor;
import org.frameworkset.spi.ApplicationContextAware;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.Assert;
import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * Holder for a {@link Callable}, a timeout value, and a task executor.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 3.2
 */
public class WebAsyncTask<V> implements ApplicationContextAware {

	private final Callable<V> callable;

	private Long timeout;

	private AsyncTaskExecutor executor;

	private String executorName;

	private BaseApplicationContext beanFactory;

	private Callable<V> timeoutCallback;

	private Runnable completionCallback;


	/**
	 * Create a {@code WebAsyncTask} wrapping the given {@link Callable}.
	 * @param callable the callable for concurrent handling
	 */
	public WebAsyncTask(Callable<V> callable) {
		Assert.notNull(callable, "Callable must not be null");
		this.callable = callable;
	}

	/**
	 * Create a {@code WebAsyncTask} with a timeout value and a {@link Callable}.
	 * @param timeout a timeout value in milliseconds
	 * @param callable the callable for concurrent handling
	 */
	public WebAsyncTask(long timeout, Callable<V> callable) {
		this(callable);
		this.timeout = timeout;
	}

	/**
	 * Create a {@code WebAsyncTask} with a timeout value, an executor name, and a {@link Callable}.
	 * @param timeout timeout value in milliseconds; ignored if {@code null}
	 * @param executorName the name of an executor bean to use
	 * @param callable the callable for concurrent handling
	 */
	public WebAsyncTask(Long timeout, String executorName, Callable<V> callable) {
		this(callable);
		Assert.notNull(executorName, "Executor name must not be null");
		this.executorName = executorName;
		this.timeout = timeout;
	}

	/**
	 * Create a {@code WebAsyncTask} with a timeout value, an executor instance, and a Callable.
	 * @param timeout timeout value in milliseconds; ignored if {@code null}
	 * @param executor the executor to use
	 * @param callable the callable for concurrent handling
	 */
	public WebAsyncTask(Long timeout, AsyncTaskExecutor executor, Callable<V> callable) {
		this(callable);
		Assert.notNull(executor, "Executor must not be null");
		this.executor = executor;
		this.timeout = timeout;
	}


	/**
	 * Return the {@link Callable} to use for concurrent handling (never {@code null}).
	 */
	public Callable<?> getCallable() {
		return this.callable;
	}

	/**
	 * Return the timeout value in milliseconds, or {@code null} if no timeout is set.
	 */
	public Long getTimeout() {
		return this.timeout;
	}

	/**
	 * A {@link BeanFactory} to use for resolving an executor name.
	 * <p>This factory reference will automatically be set when
	 * {@code WebAsyncTask} is used within a bboss MVC controller.
	 */
	public void setApplicationContext(BaseApplicationContext beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * Return the AsyncTaskExecutor to use for concurrent handling,
	 * or {@code null} if none specified.
	 */
	public AsyncTaskExecutor getExecutor() {
		if (this.executor != null) {
			return this.executor;
		}
		else if (this.executorName != null) {
			Assert.state(this.beanFactory != null, "BeanFactory is required to look up an executor bean by name");
			return this.beanFactory.getTBeanObject(this.executorName, AsyncTaskExecutor.class);
		}
		else {
			return null;
		}
	}


	/**
	 * Register code to invoke when the async request times out.
	 * <p>This method is called from a container thread when an async request times
	 * out before the {@code Callable} has completed. The callback is executed in
	 * the same thread and therefore should return without blocking. It may return
	 * an alternative value to use, including an {@link Exception} or return
	 * {@link CallableProcessingInterceptor#RESULT_NONE RESULT_NONE}.
	 */
	public void onTimeout(Callable<V> callback) {
		this.timeoutCallback = callback;
	}

	/**
	 * Register code to invoke when the async request completes.
	 * <p>This method is called from a container thread when an async request
	 * completed for any reason, including timeout and network error.
	 */
	public void onCompletion(Runnable callback) {
		this.completionCallback = callback;
	}

	CallableProcessingInterceptor getInterceptor() {
		return new CallableProcessingInterceptorAdapter() {
			@Override
			public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
				return (timeoutCallback != null ? timeoutCallback.call() : CallableProcessingInterceptor.RESULT_NONE);
			}
			@Override
			public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
				if (completionCallback != null) {
					completionCallback.run();
				}
			}
		};
	}

}
