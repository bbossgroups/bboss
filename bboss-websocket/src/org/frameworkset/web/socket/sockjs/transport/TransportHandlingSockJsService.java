package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.spi.Lifecycle;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.socket.handler.HandshakeFailureException;
import org.frameworkset.web.socket.handler.HandshakeHandler;
import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.handler.HandshakeInterceptorChain;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.AbstractSockJsService;
import org.frameworkset.web.socket.sockjs.Jackson2SockJsMessageCodec;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsMessageCodec;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.SockJsSession;
import org.frameworkset.web.socket.sockjs.SockJsSessionFactory;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.frameworkset.web.socket.sockjs.TransportType;

public class TransportHandlingSockJsService  extends AbstractSockJsService implements SockJsServiceConfig, Lifecycle {

	private static final boolean jackson2Present = ClassUtils.isPresent(
			"com.fasterxml.jackson.databind.ObjectMapper", TransportHandlingSockJsService.class.getClassLoader());


	private final Map<TransportType, TransportHandler> handlers = new HashMap<TransportType, TransportHandler>();

	private SockJsMessageCodec messageCodec;

	private final List<HandshakeInterceptor> interceptors = new ArrayList<HandshakeInterceptor>();

	private final Map<String, SockJsSession> sessions = new ConcurrentHashMap<String, SockJsSession>();

	private ScheduledFuture<?> sessionCleanupTask;

	private boolean running;


	/**
	 * Create a TransportHandlingSockJsService with given {@link TransportHandler handler} types.
	 * @param scheduler a task scheduler for heart-beat messages and removing timed-out sessions;
	 * the provided TaskScheduler should be declared as a Spring bean to ensure it gets
	 * initialized at start-up and shuts down when the application stops
	 * @param handlers one or more {@link TransportHandler} implementations to use
	 */
	public TransportHandlingSockJsService(TaskScheduler scheduler, TransportHandler... handlers) {
		this(scheduler, Arrays.asList(handlers));
	}

	/**
	 * Create a TransportHandlingSockJsService with given {@link TransportHandler handler} types.
	 * @param scheduler a task scheduler for heart-beat messages and removing timed-out sessions;
	 * the provided TaskScheduler should be declared as a Spring bean to ensure it gets
	 * initialized at start-up and shuts down when the application stops
	 * @param handlers one or more {@link TransportHandler} implementations to use
	 */
	public TransportHandlingSockJsService(TaskScheduler scheduler, Collection<TransportHandler> handlers) {
		super(scheduler);

		if (CollectionUtils.isEmpty(handlers)) {
			logger.warn("No transport handlers specified for TransportHandlingSockJsService");
		}
		else {
			for (TransportHandler handler : handlers) {
				handler.initialize(this);
				this.handlers.put(handler.getTransportType(), handler);
			}
		}

		if (jackson2Present) {
			this.messageCodec = new Jackson2SockJsMessageCodec();
		}
	}


	/**
	 * Return the registered handlers per transport type.
	 */
	public Map<TransportType, TransportHandler> getTransportHandlers() {
		return Collections.unmodifiableMap(this.handlers);
	}

	/**
	 * The codec to use for encoding and decoding SockJS messages.
	 */
	public void setMessageCodec(SockJsMessageCodec messageCodec) {
		this.messageCodec = messageCodec;
	}

	public SockJsMessageCodec getMessageCodec() {
		Assert.state(this.messageCodec != null, "A SockJsMessageCodec is required but not available: " +
				"Add Jackson 2 to the classpath, or configure a custom SockJsMessageCodec.");
		return this.messageCodec;
	}

	/**
	 * Configure one or more WebSocket handshake request interceptors.
	 */
	public void setHandshakeInterceptors(List<HandshakeInterceptor> interceptors) {
		this.interceptors.clear();
		if (interceptors != null) {
			this.interceptors.addAll(interceptors);
		}
	}

	/**
	 * Return the configured WebSocket handshake request interceptors.
	 */
	public List<HandshakeInterceptor> getHandshakeInterceptors() {
		return this.interceptors;
	}


	@Override
	public void start() {
		if (!isRunning()) {
			this.running = true;
			for (TransportHandler handler : this.handlers.values()) {
				if (handler instanceof Lifecycle) {
					((Lifecycle) handler).start();
				}
			}
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			this.running = false;
			for (TransportHandler handler : this.handlers.values()) {
				if (handler instanceof Lifecycle) {
					((Lifecycle) handler).stop();
				}
			}
		}
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}


	@Override
	protected void handleRawWebSocketRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler handler) throws IOException {

		TransportHandler transportHandler = this.handlers.get(TransportType.WEBSOCKET);
		if (!(transportHandler instanceof HandshakeHandler)) {
			logger.error("No handler configured for raw WebSocket messages");
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		HandshakeInterceptorChain chain = new HandshakeInterceptorChain(this.interceptors, handler);
		HandshakeFailureException failure = null;

		try {
			Map<String, Object> attributes = new HashMap<String, Object>();
			if (!chain.applyBeforeHandshake(request, response, attributes)) {
				return;
			}
			((HandshakeHandler) transportHandler).doHandshake(request, response, handler, attributes);
			chain.applyAfterHandshake(request, response, null);
		}
		catch (HandshakeFailureException ex) {
			failure = ex;
		}
		catch (Throwable ex) {
			failure = new HandshakeFailureException("Uncaught failure for request " + request.getURI(), ex);
		}
			finally {
			if (failure != null) {
				chain.applyAfterHandshake(request, response, failure);
				throw failure;
			}
		}
	}

	@Override
	protected void handleTransportRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler handler, String sessionId, String transport) throws SockJsException {

		TransportType transportType = TransportType.fromValue(transport);
		if (transportType == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("Unknown transport type for " + request.getURI());
			}
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		TransportHandler transportHandler = this.handlers.get(transportType);
		if (transportHandler == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("No TransportHandler for " + request.getURI());
			}
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		SockJsException failure = null;
		HandshakeInterceptorChain chain = new HandshakeInterceptorChain(this.interceptors, handler);

		try {
			HttpMethod supportedMethod = transportType.getHttpMethod();
			if (supportedMethod != request.getMethod()) {
				if (HttpMethod.OPTIONS == request.getMethod() && transportType.supportsCors()) {
					if (checkOrigin(request, response, HttpMethod.OPTIONS, supportedMethod)) {
						response.setStatusCode(HttpStatus.NO_CONTENT);
						addCacheHeaders(response);
					}
				}
				else if (transportType.supportsCors()) {
					sendMethodNotAllowed(response, supportedMethod, HttpMethod.OPTIONS);
				}
				else {
					sendMethodNotAllowed(response, supportedMethod);
				}
				return;
			}

			SockJsSession session = this.sessions.get(sessionId);
			if (session == null) {
				if (transportHandler instanceof SockJsSessionFactory) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					if (!chain.applyBeforeHandshake(request, response, attributes)) {
						return;
					}
					SockJsSessionFactory sessionFactory = (SockJsSessionFactory) transportHandler;
					session = createSockJsSession(sessionId, sessionFactory, handler, attributes);
				}
				else {
					response.setStatusCode(HttpStatus.NOT_FOUND);
					if (logger.isDebugEnabled()) {
						logger.debug("Session not found, sessionId=" + sessionId +
								". The session may have been closed " +
								"(e.g. missed heart-beat) while a message was coming in.");
					}
					return;
				}
			}
			else {
				if (session.getPrincipal() != null) {
					if (!session.getPrincipal().equals(request.getPrincipal())) {
						logger.debug("The user for the session does not match the user for the request.");
						response.setStatusCode(HttpStatus.NOT_FOUND);
						return;
					}
				}
			}

			if (transportType.sendsNoCacheInstruction()) {
				addNoCacheHeaders(response);
			}

			if (transportType.supportsCors()) {
				if (!checkOrigin(request, response)) {
					return;
				}
			}

			transportHandler.handleRequest(request, response, handler, session);
			chain.applyAfterHandshake(request, response, null);
		}
		catch (SockJsException ex) {
			failure = ex;
		}
		catch (Throwable ex) {
			failure = new SockJsException("Uncaught failure for request " + request.getURI(), sessionId, ex);
		}
		finally {
			if (failure != null) {
				chain.applyAfterHandshake(request, response, failure);
				throw failure;
			}
		}
	}

	@Override
	protected boolean validateRequest(String serverId, String sessionId, String transport) {
		if (!super.validateRequest(serverId, sessionId, transport)) {
			return false;
		}

		if (!this.allowedOrigins.contains("*")) {
			TransportType transportType = TransportType.fromValue(transport);
			if (transportType == null || !transportType.supportsOrigin()) {
				if (logger.isWarnEnabled()) {
					logger.warn("Origin check enabled but transport '" + transport + "' does not support it.");
				}
				return false;
			}
		}

		return true;
	}

	private SockJsSession createSockJsSession(String sessionId, SockJsSessionFactory sessionFactory,
			WebSocketHandler handler, Map<String, Object> attributes) {

		SockJsSession session = this.sessions.get(sessionId);
		if (session != null) {
			return session;
		}
		if (this.sessionCleanupTask == null) {
			scheduleSessionTask();
		}
		session = sessionFactory.createSession(sessionId, handler, attributes);
		this.sessions.put(sessionId, session);
		return session;
	}

	private void scheduleSessionTask() {
		synchronized (this.sessions) {
			if (this.sessionCleanupTask != null) {
				return;
			}
			final List<String> removedSessionIds = new ArrayList<String>();
			this.sessionCleanupTask = getTaskScheduler().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					for (SockJsSession session : sessions.values()) {
						try {
							if (session.getTimeSinceLastActive() > getDisconnectDelay()) {
								sessions.remove(session.getId());
								session.close();
							}
						}
						catch (Throwable ex) {
							// Could be part of normal workflow (e.g. browser tab closed)
							logger.debug("Failed to close " + session, ex);
						}
					}
					if (logger.isDebugEnabled() && !removedSessionIds.isEmpty()) {
						logger.debug("Closed " + removedSessionIds.size() + " sessions " + removedSessionIds);
						removedSessionIds.clear();
					}
				}
			}, getDisconnectDelay());
		}
	}

}
