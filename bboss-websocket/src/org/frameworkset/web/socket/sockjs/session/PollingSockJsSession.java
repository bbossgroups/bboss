package org.frameworkset.web.socket.sockjs.session;

import java.io.IOException;
import java.util.Map;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsMessageCodec;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.SockJsTransportFailureException;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrame;

public class PollingSockJsSession  extends AbstractHttpSockJsSession {


	public PollingSockJsSession(String sessionId, SockJsServiceConfig config,
			WebSocketHandler wsHandler, Map<String, Object> attributes) {

		super(sessionId, config, wsHandler, attributes);
	}


	/**
	 * @deprecated as of 4.2 this method is no longer used.
	 */
	@Override
	@Deprecated
	protected boolean isStreaming() {
		return false;
	}

	@Override
	protected void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			boolean initialRequest) throws IOException {

		if (initialRequest) {
			writeFrame(SockJsFrame.openFrame());
			resetRequest();
		}
		else if (!getMessageCache().isEmpty()) {
			flushCache();
		}
		else {
			scheduleHeartbeat();
		}
	}

	@Override
	protected void flushCache() throws SockJsTransportFailureException {
		String[] messages = new String[getMessageCache().size()];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = getMessageCache().poll();
		}
		SockJsMessageCodec messageCodec = getSockJsServiceConfig().getMessageCodec();
		SockJsFrame frame = SockJsFrame.messageFrame(messageCodec, messages);
		writeFrame(frame);
		resetRequest();
	}

}
