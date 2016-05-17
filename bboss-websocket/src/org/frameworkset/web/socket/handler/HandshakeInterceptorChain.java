package org.frameworkset.web.socket.handler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public class HandshakeInterceptorChain {

	private static final Log logger = LogFactory.getLog(HandshakeInterceptorChain.class);

	private final List<HandshakeInterceptor> interceptors;

	private final WebSocketHandler wsHandler;

	private int interceptorIndex = -1;


	public HandshakeInterceptorChain(List<HandshakeInterceptor> interceptors, WebSocketHandler wsHandler) {
		this.interceptors = (interceptors != null ? interceptors : Collections.<HandshakeInterceptor>emptyList());
		this.wsHandler = wsHandler;
	}


	public boolean applyBeforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			Map<String, Object> attributes) throws Exception {

		for (int i = 0; i < this.interceptors.size(); i++) {
			HandshakeInterceptor interceptor = this.interceptors.get(i);
			if (!interceptor.beforeHandshake(request, response, this.wsHandler, attributes)) {
				if (logger.isDebugEnabled()) {
					logger.debug(interceptor + " returns false from beforeHandshake - precluding handshake");
				}
				applyAfterHandshake(request, response, null);
				return false;
			}
			this.interceptorIndex = i;
		}
		return true;
	}


	public void applyAfterHandshake(ServerHttpRequest request, ServerHttpResponse response, Exception failure) {
		for (int i = this.interceptorIndex; i >= 0; i--) {
			HandshakeInterceptor interceptor = this.interceptors.get(i);
			try {
				interceptor.afterHandshake(request, response, this.wsHandler, failure);
			}
			catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn(interceptor + " threw exception in afterHandshake: " + ex);
				}
			}
		}
	}

}
