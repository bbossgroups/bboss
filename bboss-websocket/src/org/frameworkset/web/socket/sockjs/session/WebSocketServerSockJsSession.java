package org.frameworkset.web.socket.sockjs.session;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.endpoint.NativeWebSocketSession;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketExtension;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketSession;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.SockJsTransportFailureException;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrame;

import com.frameworkset.util.StringUtil;

public class WebSocketServerSockJsSession  extends AbstractSockJsSession implements NativeWebSocketSession {

	private WebSocketSession webSocketSession;

	private volatile boolean openFrameSent;

	private final Queue<String> initSessionCache = new LinkedBlockingDeque<String>();

	private final Object initSessionLock = new Object();

	private volatile boolean disconnected;


	public WebSocketServerSockJsSession(String id, SockJsServiceConfig config,
			WebSocketHandler handler, Map<String, Object> attributes) {

		super(id, config, handler, attributes);
	}


	@Override
	public URI getUri() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getUri();
	}

	@Override
	public HttpHeaders getHandshakeHeaders() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getHandshakeHeaders();
	}

	@Override
	public Principal getPrincipal() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getPrincipal();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getLocalAddress();
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getRemoteAddress();
	}

	@Override
	public String getAcceptedProtocol() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getAcceptedProtocol();
	}

	@Override
	public void setTextMessageSizeLimit(int messageSizeLimit) {
		checkDelegateSessionInitialized();
		this.webSocketSession.setTextMessageSizeLimit(messageSizeLimit);
	}

	@Override
	public int getTextMessageSizeLimit() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getTextMessageSizeLimit();
	}

	@Override
	public void setBinaryMessageSizeLimit(int messageSizeLimit) {
		checkDelegateSessionInitialized();
		this.webSocketSession.setBinaryMessageSizeLimit(messageSizeLimit);
	}

	@Override
	public int getBinaryMessageSizeLimit() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getBinaryMessageSizeLimit();
	}

	@Override
	public List<WebSocketExtension> getExtensions() {
		checkDelegateSessionInitialized();
		return this.webSocketSession.getExtensions();
	}

	private void checkDelegateSessionInitialized() {
		Assert.state(this.webSocketSession != null, "WebSocketSession not yet initialized");
	}

	@Override
	public Object getNativeSession() {
		if ((this.webSocketSession != null) && (this.webSocketSession instanceof NativeWebSocketSession)) {
			return ((NativeWebSocketSession) this.webSocketSession).getNativeSession();
		}
		return null;
	}

	@Override
	public <T> T getNativeSession(Class<T> requiredType) {
		if ((this.webSocketSession != null) && (this.webSocketSession instanceof NativeWebSocketSession)) {
			return ((NativeWebSocketSession) this.webSocketSession).getNativeSession(requiredType);
		}
		return null;
	}


	public void initializeDelegateSession(WebSocketSession session) {
		synchronized (this.initSessionLock) {
			this.webSocketSession = session;
			try {
				// Let "our" handler know before sending the open frame to the remote handler
				delegateConnectionEstablished();
				this.webSocketSession.sendMessage(new TextMessage(SockJsFrame.openFrame().getContent()));

				// Flush any messages cached in the mean time
				while (!this.initSessionCache.isEmpty()) {
					writeFrame(SockJsFrame.messageFrame(getMessageCodec(), this.initSessionCache.poll()));
				}
				scheduleHeartbeat();
				this.openFrameSent = true;
			}
			catch (Exception ex) {
				tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
			}
		}
	}

	@Override
	public boolean isActive() {
		return (this.webSocketSession != null && this.webSocketSession.isOpen() && !this.disconnected);
	}

	public void handleMessage(TextMessage message, WebSocketSession wsSession) throws Exception {
		String payload = message.getPayload();
		if (StringUtil.isEmpty(payload)) {
			return;
		}
		String[] messages;
		try {
			messages = getSockJsServiceConfig().getMessageCodec().decode(payload);
		}
		catch (Throwable ex) {
			logger.error("Broken data received. Terminating WebSocket connection abruptly", ex);
			tryCloseWithSockJsTransportError(ex, CloseStatus.BAD_DATA);
			return;
		}
		delegateMessages(messages);
	}

	@Override
	public void sendMessageInternal(String message) throws SockJsTransportFailureException {

		// Open frame not sent yet?
		// If in the session initialization thread, then cache, otherwise wait.

		if (!this.openFrameSent) {
			synchronized (this.initSessionLock) {
				if (!this.openFrameSent) {
					this.initSessionCache.add(message);
					return;
				}
			}
		}
		cancelHeartbeat();
		writeFrame(SockJsFrame.messageFrame(getMessageCodec(), message));
		scheduleHeartbeat();
	}

	@Override
	protected void writeFrameInternal(SockJsFrame frame) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("Writing " + frame);
		}
		TextMessage message = new TextMessage(frame.getContent());
		this.webSocketSession.sendMessage(message);
	}

	@Override
	protected void disconnect(CloseStatus status) throws IOException {
		synchronized (this) {
			if (isActive()) {
				this.disconnected = true;
				this.webSocketSession.close(status);
			}
		}
	}


}
