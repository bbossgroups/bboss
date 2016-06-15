package org.frameworkset.web.socket.handler.jetty;


import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.OpCode;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.frameworkset.web.socket.inf.BinaryMessage;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.PongMessage;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketHandler;

/**
 * Adapts {@link WebSocketHandler} to the Jetty 9 WebSocket API.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
@WebSocket
public class JettyWebSocketHandlerAdapter {

	private static final ByteBuffer EMPTY_PAYLOAD = ByteBuffer.wrap(new byte[0]);

	private static final Log logger = LogFactory.getLog(JettyWebSocketHandlerAdapter.class);


	private final WebSocketHandler webSocketHandler;

	private final JettyWebSocketSession wsSession;


	public JettyWebSocketHandlerAdapter(WebSocketHandler webSocketHandler, JettyWebSocketSession wsSession) {
		Assert.notNull(webSocketHandler, "WebSocketHandler must not be null");
		Assert.notNull(wsSession, "WebSocketSession must not be null");
		this.webSocketHandler = webSocketHandler;
		this.wsSession = wsSession;
	}


	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		try {
			this.wsSession.initializeNativeSession(session);
			this.webSocketHandler.afterConnectionEstablished(this.wsSession);
		}
		catch (Throwable ex) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, ex, logger);
		}
	}

	@OnWebSocketMessage
	public void onWebSocketText(String payload) {
		TextMessage message = new TextMessage(payload);
		try {
			this.webSocketHandler.handleMessage(this.wsSession, message);
		}
		catch (Throwable ex) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, ex, logger);
		}
	}

	@OnWebSocketMessage
	public void onWebSocketBinary(byte[] payload, int offset, int length) {
		BinaryMessage message = new BinaryMessage(payload, offset, length, true);
		try {
			this.webSocketHandler.handleMessage(this.wsSession, message);
		}
		catch (Throwable ex) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, ex, logger);
		}
	}

	@OnWebSocketFrame
	public void onWebSocketFrame(Frame frame) {
		if (OpCode.PONG == frame.getOpCode()) {
			ByteBuffer payload = frame.getPayload() != null ? frame.getPayload() : EMPTY_PAYLOAD;
			PongMessage message = new PongMessage(payload);
			try {
				this.webSocketHandler.handleMessage(this.wsSession, message);
			}
			catch (Throwable ex) {
				ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, ex, logger);
			}
		}
	}

	@OnWebSocketClose
	public void onWebSocketClose(int statusCode, String reason) {
		CloseStatus closeStatus = new CloseStatus(statusCode, reason);
		try {
			this.webSocketHandler.afterConnectionClosed(this.wsSession, closeStatus);
		}
		catch (Throwable ex) {
			if (logger.isErrorEnabled()) {
				logger.error("Unhandled error for " + this.wsSession, ex);
			}
		}
	}

	@OnWebSocketError
	public void onWebSocketError(Throwable cause) {
		try {
			this.webSocketHandler.handleTransportError(this.wsSession, cause);
		}
		catch (Throwable ex) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, ex, logger);
		}
	}
}
