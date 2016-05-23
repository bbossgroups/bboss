package org.frameworkset.web.socket.sockjs.transport;

import java.util.Map;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsTransportFailureException;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.frame.DefaultSockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.AbstractHttpSockJsSession;
import org.frameworkset.web.socket.sockjs.session.PollingSockJsSession;
import org.frameworkset.web.util.JavaScriptUtils;

import com.frameworkset.util.StringUtil;

/**
 * A TransportHandler that sends messages via JSONP polling.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class JsonpPollingTransportHandler extends AbstractHttpSendingTransportHandler {

	@Override
	public TransportType getTransportType() {
		return TransportType.JSONP;
	}

	@Override
	protected MediaType getContentType() {
		return new MediaType("application", "javascript", UTF8_CHARSET);
	}

	@Override
	public PollingSockJsSession createSession(
			String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {

		return new PollingSockJsSession(sessionId, getServiceConfig(), handler, attributes);
	}

	@Override
	public void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			AbstractHttpSockJsSession sockJsSession) throws SockJsException {

		try {
			String callback = getCallbackParam(request);
			if (!StringUtil.hasText(callback)) {
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
				response.getBody().write("\"callback\" parameter required".getBytes(UTF8_CHARSET));
				return;
			}
		}
		catch (Throwable ex) {
			sockJsSession.tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
			throw new SockJsTransportFailureException("Failed to send error", sockJsSession.getId(), ex);
		}

		super.handleRequestInternal(request, response, sockJsSession);
	}

	@Override
	protected SockJsFrameFormat getFrameFormat(ServerHttpRequest request) {
		// We already validated the parameter above...
		String callback = getCallbackParam(request);

		return new DefaultSockJsFrameFormat("/**/" + callback + "(\"%s\");\r\n") {
			@Override
			protected String preProcessContent(String content) {
				return JavaScriptUtils.javaScriptEscape(content);
			}
		};
	}

}
