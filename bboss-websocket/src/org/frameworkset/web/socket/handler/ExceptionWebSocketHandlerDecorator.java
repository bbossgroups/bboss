package org.frameworkset.web.socket.handler;

import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketMessage;
import org.frameworkset.web.socket.inf.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final Logger logger = LoggerFactory.getLogger(ExceptionWebSocketHandlerDecorator.class);


	public ExceptionWebSocketHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}


	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		try {
			getDelegate().afterConnectionEstablished(session);
		}
		catch (Throwable ex) {
			tryCloseWithError(session, ex, logger);
		}
	}

	public static void tryCloseWithError(WebSocketSession session, Throwable exception, Logger logger) {
		logger.error("Closing due to exception for " + session, exception);
		if (session.isOpen()) {
			try {
				session.close(CloseStatus.SERVER_ERROR);
			}
			catch (Throwable t) {
				// ignore
			}
		}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
		try {
			getDelegate().handleMessage(session, message);
		}
		catch (Throwable ex) {
			tryCloseWithError(session, ex, logger);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		try {
			getDelegate().handleTransportError(session, exception);
		}
		catch (Throwable ex) {
			tryCloseWithError(session, ex, logger);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
		try {
			getDelegate().afterConnectionClosed(session, closeStatus);
		}
		catch (Throwable t) {
			logger.error("Unhandled error for " + this, t);
		}
	}

}
