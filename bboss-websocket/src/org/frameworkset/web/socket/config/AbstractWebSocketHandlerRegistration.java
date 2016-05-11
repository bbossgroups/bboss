package org.frameworkset.web.socket.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.util.Assert;
import org.frameworkset.util.LinkedMultiValueMap;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.web.socket.handler.HandshakeHandler;
import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.handler.OriginHandshakeInterceptor;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public abstract class AbstractWebSocketHandlerRegistration<M> implements WebSocketHandlerRegistry {

	private final TaskScheduler sockJsTaskScheduler;

	private MultiValueMap<WebSocketHandler, String> handlerMap = new LinkedMultiValueMap<WebSocketHandler, String>();

	private HandshakeHandler handshakeHandler;

	private final List<HandshakeInterceptor> interceptors = new ArrayList<HandshakeInterceptor>();

	private final List<String> allowedOrigins = new ArrayList<String>();

	private SockJsServiceRegistration sockJsServiceRegistration;


	public AbstractWebSocketHandlerRegistration(TaskScheduler defaultTaskScheduler) {
		this.sockJsTaskScheduler = defaultTaskScheduler;
	}


	@Override
	public WebSocketHandlerRegistration addHandler(WebSocketHandler handler, String... paths) {
		Assert.notNull(handler);
		Assert.notEmpty(paths);
		this.handlerMap.put(handler, Arrays.asList(paths));
		return this;
	}

	@Override
	public WebSocketHandlerRegistration setHandshakeHandler(HandshakeHandler handshakeHandler) {
		this.handshakeHandler = handshakeHandler;
		return this;
	}

	protected HandshakeHandler getHandshakeHandler() {
		return this.handshakeHandler;
	}

	@Override
	public WebSocketHandlerRegistration addInterceptors(HandshakeInterceptor... interceptors) {
		if (!ObjectUtils.isEmpty(interceptors)) {
			this.interceptors.addAll(Arrays.asList(interceptors));
		}
		return this;
	}

	@Override
	public WebSocketHandlerRegistration setAllowedOrigins(String... allowedOrigins) {
		this.allowedOrigins.clear();
		if (!ObjectUtils.isEmpty(allowedOrigins)) {
			this.allowedOrigins.addAll(Arrays.asList(allowedOrigins));
		}
		return this;
	}

	@Override
	public SockJsServiceRegistration withSockJS() {
		this.sockJsServiceRegistration = new SockJsServiceRegistration(this.sockJsTaskScheduler);
		HandshakeInterceptor[] interceptors = getInterceptors();
		if (interceptors.length > 0) {
			this.sockJsServiceRegistration.setInterceptors(interceptors);
		}
		if (this.handshakeHandler != null) {
			WebSocketTransportHandler transportHandler = new WebSocketTransportHandler(this.handshakeHandler);
			this.sockJsServiceRegistration.setTransportHandlerOverrides(transportHandler);
		}
		if (!this.allowedOrigins.isEmpty()) {
			this.sockJsServiceRegistration.setAllowedOrigins(this.allowedOrigins.toArray(new String[this.allowedOrigins.size()]));
		}
		return this.sockJsServiceRegistration;
	}

	protected HandshakeInterceptor[] getInterceptors() {
		List<HandshakeInterceptor> interceptors = new ArrayList<HandshakeInterceptor>();
		interceptors.addAll(this.interceptors);
		interceptors.add(new OriginHandshakeInterceptor(this.allowedOrigins));
		return interceptors.toArray(new HandshakeInterceptor[interceptors.size()]);
	}

	protected final M getMappings() {
		M mappings = createMappings();
		if (this.sockJsServiceRegistration != null) {
			SockJsService sockJsService = this.sockJsServiceRegistration.getSockJsService();
			for (WebSocketHandler wsHandler : this.handlerMap.keySet()) {
				for (String path : this.handlerMap.get(wsHandler)) {
					String pathPattern = path.endsWith("/") ? path + "**" : path + "/**";
					addSockJsServiceMapping(mappings, sockJsService, wsHandler, pathPattern);
				}
			}
		}
		else {
			HandshakeHandler handshakeHandler = getOrCreateHandshakeHandler();
			HandshakeInterceptor[] interceptors = getInterceptors();
			for (WebSocketHandler wsHandler : this.handlerMap.keySet()) {
				for (String path : this.handlerMap.get(wsHandler)) {
					addWebSocketHandlerMapping(mappings, wsHandler, handshakeHandler, interceptors, path);
				}
			}
		}

		return mappings;
	}

	private HandshakeHandler getOrCreateHandshakeHandler() {
		return (this.handshakeHandler != null) ? this.handshakeHandler : new DefaultHandshakeHandler();
	}

	protected abstract M createMappings();

	protected abstract void addSockJsServiceMapping(M mappings, SockJsService sockJsService,
			WebSocketHandler handler, String pathPattern);

	protected abstract void addWebSocketHandlerMapping(M mappings, WebSocketHandler wsHandler,
			HandshakeHandler handshakeHandler, HandshakeInterceptor[] interceptors, String path);


}
