package org.frameworkset.web.request.async;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.web.servlet.mvc.NativeWebRequest;

public class TimeoutCallableProcessingInterceptor  extends CallableProcessingInterceptorAdapter {

	@Override
	public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
		HttpServletResponse servletResponse = request.getNativeResponse(HttpServletResponse.class);
		if (!servletResponse.isCommitted()) {
			servletResponse.sendError(HttpStatus.SERVICE_UNAVAILABLE.value());
		}
		return CallableProcessingInterceptor.RESPONSE_HANDLED;
	}

}
