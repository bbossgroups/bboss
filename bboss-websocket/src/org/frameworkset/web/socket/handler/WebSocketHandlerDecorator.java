package org.frameworkset.web.socket.handler;

import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketMessage;
import org.frameworkset.web.socket.inf.WebSocketSession;

public class WebSocketHandlerDecorator  implements WebSocketHandler {

	private final WebSocketHandler delegate;


	public WebSocketHandlerDecorator(WebSocketHandler delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
	}


	public WebSocketHandler getDelegate() {
		return this.delegate;
	}

	public WebSocketHandler getLastHandler() {
		WebSocketHandler result = this.delegate;
		while (result instanceof WebSocketHandlerDecorator) {
			result = ((WebSocketHandlerDecorator) result).getDelegate();
		}
		return result;
	}

	public static WebSocketHandler unwrap(WebSocketHandler handler) {
		if (handler instanceof WebSocketHandlerDecorator) {
			return ((WebSocketHandlerDecorator) handler).getLastHandler();
		}
		else {
			return handler;
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.delegate.afterConnectionEstablished(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		this.delegate.handleMessage(session, message);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		this.delegate.handleTransportError(session, exception);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		this.delegate.afterConnectionClosed(session, closeStatus);
	}

	@Override
	public boolean supportsPartialMessages() {
		return this.delegate.supportsPartialMessages();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [delegate=" + this.delegate + "]";
	}

}
