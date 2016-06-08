package org.frameworkset.web.socket.sockjs.transport;

import java.util.Map;

import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.frame.DefaultSockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.PollingSockJsSession;

public class XhrPollingTransportHandler  extends AbstractHttpSendingTransportHandler {

	@Override
	public TransportType getTransportType() {
		return TransportType.XHR;
	}

	@Override
	protected MediaType getContentType() {
		return new MediaType("application", "javascript", UTF8_CHARSET);
	}

	@Override
	protected SockJsFrameFormat getFrameFormat(ServerHttpRequest request) {
		return new DefaultSockJsFrameFormat("%s\n");
	}

	@Override
	public PollingSockJsSession createSession(
			String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {

		return new PollingSockJsSession(sessionId, getServiceConfig(), handler, attributes);
	}


}
