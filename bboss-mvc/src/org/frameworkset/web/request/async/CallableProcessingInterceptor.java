package org.frameworkset.web.request.async;

import java.util.concurrent.Callable;

import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * Intercepts concurrent request handling, where the concurrent result is
 * obtained by executing a {@link Callable} on behalf of the application with
 * an {@link AsyncTaskExecutor}.
 *
 * <p>A {@code CallableProcessingInterceptor} is invoked before and after the
 * invocation of the {@code Callable} task in the asynchronous thread, as well
 * as on timeout from a container thread, or after completing for any reason
 * including a timeout or network error.
 *
 * <p>As a general rule exceptions raised by interceptor methods will cause
 * async processing to resume by dispatching back to the container and using
 * the Exception instance as the concurrent result. Such exceptions will then
 * be processed through the {@code HandlerExceptionResolver} mechanism.
 *
 * <p>The {@link #handleTimeout(NativeWebRequest, Callable) afterTimeout} method
 * can select a value to be used to resume processing.
 *
 * @author Rossen Stoyanchev
 * @author Rob Winch
 * @since 3.2
 */
public interface CallableProcessingInterceptor {

	static final Object RESULT_NONE = new Object();

	static final Object RESPONSE_HANDLED = new Object();

	/**
	 * Invoked <em>before</em> the start of concurrent handling in the original
	 * thread in which the {@code Callable} is submitted for concurrent handling.
	 * <p>This is useful for capturing the state of the current thread just prior to
	 * invoking the {@link Callable}. Once the state is captured, it can then be
	 * transferred to the new {@link Thread} in
	 * {@link #preProcess(NativeWebRequest, Callable)}. Capturing the state of
	 * bboss Security's SecurityContextHolder and migrating it to the new Thread
	 * is a concrete example of where this is useful.
	 * @param request the current request
	 * @param task the task for the current async request
	 * @throws Exception in case of errors
	 */
	<T> void  beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception;

	/**
	 * Invoked <em>after</em> the start of concurrent handling in the async
	 * thread in which the {@code Callable} is executed and <em>before</em> the
	 * actual invocation of the {@code Callable}.
	 * @param request the current request
	 * @param task the task for the current async request
	 * @throws Exception in case of errors
	 */
	<T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception;

	/**
	 * Invoked <em>after</em> the {@code Callable} has produced a result in the
	 * async thread in which the {@code Callable} is executed. This method may
	 * be invoked later than {@code afterTimeout} or {@code afterCompletion}
	 * depending on when the {@code Callable} finishes processing.
	 * @param request the current request
	 * @param task the task for the current async request
	 * @param concurrentResult the result of concurrent processing, which could
	 * be a {@link Throwable} if the {@code Callable} raised an exception
	 * @throws Exception in case of errors
	 */
	<T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception;

	/**
	 * Invoked from a container thread when the async request times out before
	 * the {@code Callable} task completes. Implementations may return a value,
	 * including an {@link Exception}, to use instead of the value the
	 * {@link Callable} did not return in time.
	 * @param request the current request
	 * @param task the task for the current async request
	 * @return a concurrent result value; if the value is anything other than
	 * {@link #RESULT_NONE} or {@link #RESPONSE_HANDLED}, concurrent processing
	 * is resumed and subsequent interceptors are not invoked
	 * @throws Exception in case of errors
	 */
	<T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception;

	/**
	 * Invoked from a container thread when async processing completes for any
	 * reason including timeout or network error.
	 * @param request the current request
	 * @param task the task for the current async request
	 * @throws Exception in case of errors
	 */
	<T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception;


}
