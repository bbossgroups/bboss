package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;
import java.util.Map;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.SockJsTransportFailureException;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.frame.DefaultSockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.AbstractHttpSockJsSession;
import org.frameworkset.web.socket.sockjs.session.StreamingSockJsSession;
import org.frameworkset.web.util.JavaScriptUtils;

import com.frameworkset.util.StringUtil;

/**
 * An HTTP {@link TransportHandler} that uses a famous browser document.domain technique:
 * <a href="http://stackoverflow.com/questions/1481251/what-does-document-domain-document-domain-do">
 * http://stackoverflow.com/questions/1481251/what-does-document-domain-document-domain-do</a>
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class HtmlFileTransportHandler extends AbstractHttpSendingTransportHandler {

	private static final String PARTIAL_HTML_CONTENT;

	// Safari needs at least 1024 bytes to parse the website.
	// http://code.google.com/p/browsersec/wiki/Part2#Survey_of_content_sniffing_behaviors
	private static final int MINIMUM_PARTIAL_HTML_CONTENT_LENGTH = 1024;


	static {
		StringBuilder sb = new StringBuilder(
				"<!doctype html>\n" +
				"<html><head>\n" +
				"  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
				"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
				"</head><body><h2>Don't panic!</h2>\n" +
				"  <script>\n" +
				"    document.domain = document.domain;\n" +
				"    var c = parent.%s;\n" +
				"    c.start();\n" +
				"    function p(d) {c.message(d);};\n" +
				"    window.onload = function() {c.stop();};\n" +
				"  </script>"
				);

		while (sb.length() < MINIMUM_PARTIAL_HTML_CONTENT_LENGTH) {
			sb.append(" ");
		}
		PARTIAL_HTML_CONTENT = sb.toString();
	}


	@Override
	public TransportType getTransportType() {
		return TransportType.HTML_FILE;
	}

	@Override
	protected MediaType getContentType() {
		return new MediaType("text", "html", UTF8_CHARSET);
	}

	@Override
	public StreamingSockJsSession createSession(
			String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {

		return new HtmlFileStreamingSockJsSession(sessionId, getServiceConfig(), handler, attributes);
	}

	@Override
	public void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			AbstractHttpSockJsSession sockJsSession) throws SockJsException {

		String callback = getCallbackParam(request);
		if (!StringUtil.hasText(callback)) {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			try {
				response.getBody().write("\"callback\" parameter required".getBytes(UTF8_CHARSET));
			}
			catch (IOException ex) {
				sockJsSession.tryCloseWithSockJsTransportError(ex, CloseStatus.SERVER_ERROR);
				throw new SockJsTransportFailureException("Failed to write to response", sockJsSession.getId(), ex);
			}
			return;
		}

		super.handleRequestInternal(request, response, sockJsSession);
	}

	@Override
	protected SockJsFrameFormat getFrameFormat(ServerHttpRequest request) {
		return new DefaultSockJsFrameFormat("<script>\np(\"%s\");\n</script>\r\n") {
			@Override
			protected String preProcessContent(String content) {
				return JavaScriptUtils.javaScriptEscape(content);
			}
		};
	}


	private class HtmlFileStreamingSockJsSession extends StreamingSockJsSession {

		public HtmlFileStreamingSockJsSession(String sessionId, SockJsServiceConfig config,
				WebSocketHandler wsHandler, Map<String, Object> attributes) {

			super(sessionId, config, wsHandler, attributes);
		}

		@Override
		protected byte[] getPrelude(ServerHttpRequest request) {
			// We already validated the parameter above...
			String callback = getCallbackParam(request);
			String html = String.format(PARTIAL_HTML_CONTENT, callback);
			return html.getBytes(UTF8_CHARSET);
		}
	}
}
