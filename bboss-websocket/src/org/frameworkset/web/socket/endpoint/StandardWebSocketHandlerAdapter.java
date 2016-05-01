package org.frameworkset.web.socket.endpoint;

import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.frameworkset.web.socket.inf.BinaryMessage;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.PongMessage;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public class StandardWebSocketHandlerAdapter extends Endpoint {

	private static final Log logger = LogFactory.getLog(StandardWebSocketHandlerAdapter.class);

	private final WebSocketHandler handler;

	private final StandardWebSocketSession wsSession;


	public StandardWebSocketHandlerAdapter(WebSocketHandler handler, StandardWebSocketSession wsSession) {
		Assert.notNull(handler, "handler must not be null");
		Assert.notNull(wsSession, "wsSession must not be null");
		this.handler = handler;
		this.wsSession = wsSession;
	}


	@Override
	public void onOpen(final javax.websocket.Session session, EndpointConfig config) {

		this.wsSession.initializeNativeSession(session);

		if (this.handler.supportsPartialMessages()) {
			session.addMessageHandler(new MessageHandler.Partial<String>() {
				@Override
				public void onMessage(String message, boolean isLast) {
					handleTextMessage(session, message, isLast);
				}
			});
			session.addMessageHandler(new MessageHandler.Partial<ByteBuffer>() {
				@Override
				public void onMessage(ByteBuffer message, boolean isLast) {
					handleBinaryMessage(session, message, isLast);
				}
			});
		}
		else {
			session.addMessageHandler(new MessageHandler.Whole<String>() {
				@Override
				public void onMessage(String message) {
					handleTextMessage(session, message, true);
				}
			});
			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
				@Override
				public void onMessage(ByteBuffer message) {
					handleBinaryMessage(session, message, true);
				}
			});
		}

		session.addMessageHandler(new MessageHandler.Whole<javax.websocket.PongMessage>() {
			@Override
			public void onMessage(javax.websocket.PongMessage message) {
				handlePongMessage(session, message.getApplicationData());
			}
		});

		try {
			this.handler.afterConnectionEstablished(this.wsSession);
		}
		catch (Throwable t) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, t, logger);
			return;
		}
	}

	private void handleTextMessage(javax.websocket.Session session, String payload, boolean isLast) {
		TextMessage textMessage = new TextMessage(payload, isLast);
		try {
			this.handler.handleMessage(this.wsSession, textMessage);
		}
		catch (Throwable t) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, t, logger);
		}
	}

	private void handleBinaryMessage(javax.websocket.Session session, ByteBuffer payload, boolean isLast) {
		BinaryMessage binaryMessage = new BinaryMessage(payload, isLast);
		try {
			this.handler.handleMessage(this.wsSession, binaryMessage);
		}
		catch (Throwable t) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, t, logger);
		}
	}

	private void handlePongMessage(javax.websocket.Session session, ByteBuffer payload) {
		PongMessage pongMessage = new PongMessage(payload);
		try {
			this.handler.handleMessage(this.wsSession, pongMessage);
		}
		catch (Throwable t) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, t, logger);
		}
	}

	@Override
	public void onClose(javax.websocket.Session session, CloseReason reason) {
		CloseStatus closeStatus = new CloseStatus(reason.getCloseCode().getCode(), reason.getReasonPhrase());
		try {
			this.handler.afterConnectionClosed(this.wsSession, closeStatus);
		}
		catch (Throwable t) {
			logger.error("Unhandled error for " + this.wsSession, t);
		}
	}

	@Override
	public void onError(javax.websocket.Session session, Throwable exception) {
		try {
			this.handler.handleTransportError(this.wsSession, exception);
		}
		catch (Throwable t) {
			ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.wsSession, t, logger);
		}
	}

}
