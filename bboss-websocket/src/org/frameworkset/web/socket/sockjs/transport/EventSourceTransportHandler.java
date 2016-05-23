package org.frameworkset.web.socket.sockjs.transport;

import java.util.Map;

import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.frame.DefaultSockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.StreamingSockJsSession;

/**
 * A TransportHandler for sending messages via Server-Sent events:
 * <a href="http://dev.w3.org/html5/eventsource/">http://dev.w3.org/html5/eventsource/</a>.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class EventSourceTransportHandler extends AbstractHttpSendingTransportHandler {

	@Override
	public TransportType getTransportType() {
		return TransportType.EVENT_SOURCE;
	}

	@Override
	protected MediaType getContentType() {
		return new MediaType("text", "event-stream", UTF8_CHARSET);
	}

	@Override
	public StreamingSockJsSession createSession(
			String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {

		return new EventSourceStreamingSockJsSession(sessionId, getServiceConfig(), handler, attributes);
	}

	@Override
	protected SockJsFrameFormat getFrameFormat(ServerHttpRequest request) {
		return new DefaultSockJsFrameFormat("data: %s\r\n\r\n");
	}


	private class EventSourceStreamingSockJsSession extends StreamingSockJsSession {

		public EventSourceStreamingSockJsSession(String sessionId, SockJsServiceConfig config,
				WebSocketHandler wsHandler, Map<String, Object> attributes) {

			super(sessionId, config, wsHandler, attributes);
		}

		@Override
		protected byte[] getPrelude(ServerHttpRequest request) {
			return new byte[] { '\r', '\n' };
		}
	}


}
