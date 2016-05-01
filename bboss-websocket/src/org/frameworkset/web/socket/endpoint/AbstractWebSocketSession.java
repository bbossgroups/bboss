package org.frameworkset.web.socket.endpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.inf.BinaryMessage;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.PingMessage;
import org.frameworkset.web.socket.inf.PongMessage;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketMessage;

public abstract class AbstractWebSocketSession <T> implements NativeWebSocketSession {

	protected final Log logger = LogFactory.getLog(getClass());

	private T nativeSession;

	private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();


	/**
	 * Create a new instance and associate the given attributes with it.
	 *
	 * @param attributes attributes from the HTTP handshake to associate with the WebSocket
	 * session; the provided attributes are copied, the original map is not used.
	 */
	public AbstractWebSocketSession(Map<String, Object> attributes) {
		if (attributes != null) {
			this.attributes.putAll(attributes);
		}
	}


	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public T getNativeSession() {
		return this.nativeSession;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getNativeSession(Class<R> requiredType) {
		if (requiredType != null) {
			if (requiredType.isInstance(this.nativeSession)) {
				return (R) this.nativeSession;
			}
		}
		return null;
	}

	public void initializeNativeSession(T session) {
		Assert.notNull(session, "session must not be null");
		this.nativeSession = session;
	}

	protected final void checkNativeSessionInitialized() {
		Assert.state(this.nativeSession != null, "WebSocket session is not yet initialized");
	}

	@Override
	public final void sendMessage(WebSocketMessage<?> message) throws IOException {

		checkNativeSessionInitialized();
		Assert.isTrue(isOpen(), "Cannot send message after connection closed.");

		if (logger.isTraceEnabled()) {
			logger.trace("Sending " + message + ", " + this);
		}

		if (message instanceof TextMessage) {
			sendTextMessage((TextMessage) message);
		}
		else if (message instanceof BinaryMessage) {
			sendBinaryMessage((BinaryMessage) message);
		}
		else if (message instanceof PingMessage) {
			sendPingMessage((PingMessage) message);
		}
		else if (message instanceof PongMessage) {
			sendPongMessage((PongMessage) message);
		}
		else {
			throw new IllegalStateException("Unexpected WebSocketMessage type: " + message);
		}
	}

	protected abstract void sendTextMessage(TextMessage message) throws IOException;

	protected abstract void sendBinaryMessage(BinaryMessage message) throws IOException;

	protected abstract void sendPingMessage(PingMessage message) throws IOException;

	protected abstract void sendPongMessage(PongMessage message) throws IOException;

	@Override
	public final void close() throws IOException {
		close(CloseStatus.NORMAL);
	}

	@Override
	public final void close(CloseStatus status) throws IOException {
		checkNativeSessionInitialized();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing " + this);
		}
		closeInternal(status);
	}

	protected abstract void closeInternal(CloseStatus status) throws IOException;


	@Override
	public String toString() {
		return "WebSocket session id=" + getId();
	}

}
