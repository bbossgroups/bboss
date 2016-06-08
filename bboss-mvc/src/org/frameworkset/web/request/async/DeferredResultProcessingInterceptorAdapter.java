package org.frameworkset.web.request.async;

import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * Abstract adapter class for the {@link DeferredResultProcessingInterceptor}
 * interface for simplified implementation of individual methods.
 *
 * @author Rossen Stoyanchev
 * @author Rob Winch
 * @since 3.2
 */
public abstract class DeferredResultProcessingInterceptorAdapter implements DeferredResultProcessingInterceptor {

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult,
			Object concurrentResult) throws Exception {
	}

	/**
	 * This implementation returns {@code true} by default allowing other interceptors
	 * to be given a chance to handle the timeout.
	 */
	@Override
	public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
	}

}
