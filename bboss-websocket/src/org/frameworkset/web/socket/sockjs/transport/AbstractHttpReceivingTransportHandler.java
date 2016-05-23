package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;
import java.util.Arrays;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.handler.AbstractTransportHandler;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsSession;
import org.frameworkset.web.socket.sockjs.session.AbstractHttpSockJsSession;

/**
 * Base class for HTTP transport handlers that receive messages via HTTP POST.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractHttpReceivingTransportHandler extends AbstractTransportHandler {


	@Override
	public final void handleRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, SockJsSession wsSession) throws SockJsException {

		Assert.notNull(wsSession, "No session");
		AbstractHttpSockJsSession sockJsSession = (AbstractHttpSockJsSession) wsSession;

		handleRequestInternal(request, response, wsHandler, sockJsSession);
	}

	protected void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, AbstractHttpSockJsSession sockJsSession) throws SockJsException {

		String[] messages;
		try {
			messages = readMessages(request);
		}
		catch (IOException ex) {
			logger.error("Failed to read message", ex);
			if (ex.getClass().getName().contains("Mapping")) {
				// e.g. Jackson's JsonMappingException, indicating an incomplete payload
				handleReadError(response, "Payload expected.", sockJsSession.getId());
			}
			else {
				handleReadError(response, "Broken JSON encoding.", sockJsSession.getId());
			}
			return;
		}
		catch (Throwable ex) {
			logger.error("Failed to read message", ex);
			handleReadError(response, "Failed to read message(s)", sockJsSession.getId());
			return;
		}
		if (messages == null) {
			handleReadError(response, "Payload expected.", sockJsSession.getId());
			return;
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Received message(s): " + Arrays.asList(messages));
		}
		response.setStatusCode(getResponseStatus());
		response.getHeaders().setContentType(new MediaType("text", "plain", UTF8_CHARSET));

		sockJsSession.delegateMessages(messages);
	}

	private void handleReadError(ServerHttpResponse response, String error, String sessionId) {
		try {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().write(error.getBytes(UTF8_CHARSET));
		}
		catch (IOException ex) {
			throw new SockJsException("Failed to send error: " + error, sessionId, ex);
		}
	}

	protected abstract String[] readMessages(ServerHttpRequest request) throws IOException;

	protected abstract HttpStatus getResponseStatus();


}
