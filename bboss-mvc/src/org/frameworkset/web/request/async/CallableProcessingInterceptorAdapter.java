package org.frameworkset.web.request.async;

import java.util.concurrent.Callable;

import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * Abstract adapter class for the {@link CallableProcessingInterceptor} interface,
 * for simplified implementation of individual methods.
 *
 * @author Rossen Stoyanchev
 * @author Rob Winch
 * @since 3.2
 */
public abstract class CallableProcessingInterceptorAdapter implements CallableProcessingInterceptor {

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
	}

	/**
	 * This implementation always returns
	 * {@link CallableProcessingInterceptor#RESULT_NONE RESULT_NONE}.
	 */
	@Override
	public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
		return RESULT_NONE;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
	}

}
