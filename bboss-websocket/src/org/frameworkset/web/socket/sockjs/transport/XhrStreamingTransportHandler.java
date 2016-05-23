package org.frameworkset.web.socket.sockjs.transport;

import java.util.Map;

import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.frame.DefaultSockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.StreamingSockJsSession;

/**
 * A {@link TransportHandler} that sends messages over an HTTP streaming request.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class XhrStreamingTransportHandler extends AbstractHttpSendingTransportHandler {

	private static final byte[] PRELUDE = new byte[2049];

	static {
		for (int i = 0; i < 2048; i++) {
			PRELUDE[i] = 'h';
		}
		PRELUDE[2048] = '\n';
	}


	@Override
	public TransportType getTransportType() {
		return TransportType.XHR_STREAMING;
	}

	@Override
	protected MediaType getContentType() {
		return new MediaType("application", "javascript", UTF8_CHARSET);
	}

	@Override
	public StreamingSockJsSession createSession(
			String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {

		return new XhrStreamingSockJsSession(sessionId, getServiceConfig(), handler, attributes);
	}

	@Override
	protected SockJsFrameFormat getFrameFormat(ServerHttpRequest request) {
		return new DefaultSockJsFrameFormat("%s\n");
	}


	private class XhrStreamingSockJsSession extends StreamingSockJsSession {

		public XhrStreamingSockJsSession(String sessionId, SockJsServiceConfig config,
				WebSocketHandler wsHandler, Map<String, Object> attributes) {

			super(sessionId, config, wsHandler, attributes);
		}

		@Override
		protected byte[] getPrelude(ServerHttpRequest request) {
			return PRELUDE;
		}
	}


}
