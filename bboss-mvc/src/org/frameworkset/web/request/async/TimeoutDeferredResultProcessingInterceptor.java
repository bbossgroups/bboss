package org.frameworkset.web.request.async;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.web.servlet.mvc.NativeWebRequest;

/**
 * Sends a 503 (SERVICE_UNAVAILABLE) in case of a timeout if the response is not
 * already committed. Registered at the end, after all other interceptors and
 * therefore invoked only if no other interceptor handles the timeout.
 *
 * <p>Note that according to RFC 7231, a 503 without a 'Retry-After' header is
 * interpreted as a 500 error and the client should not retry. Applications
 * can install their own interceptor to handle a timeout and add a 'Retry-After'
 * header if necessary.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class TimeoutDeferredResultProcessingInterceptor extends DeferredResultProcessingInterceptorAdapter {

	@Override
	public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		HttpServletResponse servletResponse = request.getNativeResponse(HttpServletResponse.class);
		if (!servletResponse.isCommitted()) {
			servletResponse.sendError(HttpStatus.SERVICE_UNAVAILABLE.value());
		}
		return false;
	}
}
