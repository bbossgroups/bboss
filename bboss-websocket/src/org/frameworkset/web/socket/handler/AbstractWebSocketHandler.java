package org.frameworkset.web.socket.handler;

import org.frameworkset.web.socket.inf.BinaryMessage;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.PongMessage;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketMessage;
import org.frameworkset.web.socket.inf.WebSocketSession;

public abstract class AbstractWebSocketHandler  implements WebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message instanceof TextMessage) {
			handleTextMessage(session, (TextMessage) message);
		}
		else if (message instanceof BinaryMessage) {
			handleBinaryMessage(session, (BinaryMessage) message);
		}
		else if (message instanceof PongMessage) {
			handlePongMessage(session, (PongMessage) message);
		}
		else {
			throw new IllegalStateException("Unexpected WebSocket message type: " + message);
		}
	}

	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	}

	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
	}

	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
