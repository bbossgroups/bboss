package org.frameworkset.web.request.async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.mvc.ServletWebRequest;

/**
 * An {@code AsyncWebRequest} to use when there is no underlying async support.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class NoSupportAsyncWebRequest extends ServletWebRequest implements AsyncWebRequest {

	public NoSupportAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}


	@Override
	public void addCompletionHandler(Runnable runnable) {
		// ignored
	}

	@Override
	public void setTimeout(Long timeout) {
		// ignored
	}

	@Override
	public void addTimeoutHandler(Runnable runnable) {
		// ignored
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}


	// Not supported

	@Override
	public void startAsync() {
		throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
	}

	@Override
	public boolean isAsyncComplete() {
		throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
	}

	@Override
	public void dispatch() {
		throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
	}

}
