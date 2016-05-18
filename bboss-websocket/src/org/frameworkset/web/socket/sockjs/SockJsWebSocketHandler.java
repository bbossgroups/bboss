package org.frameworkset.web.socket.sockjs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.handler.TextWebSocketHandler;
import org.frameworkset.web.socket.handler.WebSocketHandlerDecorator;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.SubProtocolCapable;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketSession;
import org.frameworkset.web.socket.sockjs.session.WebSocketServerSockJsSession;

/**
 * An implementation of {@link WebSocketHandler} that adds SockJS messages frames, sends
 * SockJS heartbeat messages, and delegates lifecycle events and messages to a target
 * {@link WebSocketHandler}.
 *
*
* <p>Methods in this class allow exceptions from the wrapped {@link WebSocketHandler} to
* propagate. However, any exceptions resulting from SockJS message handling (e.g. while
* sending SockJS frames or heartbeat messages) are caught and treated as transport
* errors, i.e. routed to the
* {@link WebSocketHandler#handleTransportError(WebSocketSession, Throwable)
* handleTransportError} method of the wrapped handler and the session closed.
*
* @author Rossen Stoyanchev
* @since 4.0
*/
public class SockJsWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

	private final SockJsServiceConfig sockJsServiceConfig;

	private final WebSocketServerSockJsSession sockJsSession;

	private final List<String> subProtocols;

	private final AtomicInteger sessionCount = new AtomicInteger(0);


	public SockJsWebSocketHandler(SockJsServiceConfig serviceConfig, WebSocketHandler webSocketHandler,
			WebSocketServerSockJsSession sockJsSession) {

		Assert.notNull(serviceConfig, "serviceConfig must not be null");
		Assert.notNull(webSocketHandler, "webSocketHandler must not be null");
		Assert.notNull(sockJsSession, "session must not be null");

		this.sockJsServiceConfig = serviceConfig;
		this.sockJsSession = sockJsSession;

		webSocketHandler = WebSocketHandlerDecorator.unwrap(webSocketHandler);
		this.subProtocols = ((webSocketHandler instanceof SubProtocolCapable) ?
				new ArrayList<String>(((SubProtocolCapable) webSocketHandler).getSubProtocols()) : null);
	}

	@Override
	public List<String> getSubProtocols() {
		return this.subProtocols;
	}

	protected SockJsServiceConfig getSockJsConfig() {
		return this.sockJsServiceConfig;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
		Assert.isTrue(this.sessionCount.compareAndSet(0, 1), "Unexpected connection");
		this.sockJsSession.initializeDelegateSession(wsSession);
	}

	@Override
	public void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
		this.sockJsSession.handleMessage(message, wsSession);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) throws Exception {
		this.sockJsSession.delegateConnectionClosed(status);
	}

	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable exception) throws Exception {
		this.sockJsSession.delegateError(exception);
	}

}
