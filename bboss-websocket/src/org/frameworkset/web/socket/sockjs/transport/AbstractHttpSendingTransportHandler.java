package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.web.socket.handler.AbstractTransportHandler;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsSession;
import org.frameworkset.web.socket.sockjs.SockJsSessionFactory;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrame;
import org.frameworkset.web.socket.sockjs.frame.SockJsFrameFormat;
import org.frameworkset.web.socket.sockjs.session.AbstractHttpSockJsSession;
import org.frameworkset.web.util.UriComponentsBuilder;
import org.frameworkset.web.util.UriUtils;

import com.frameworkset.util.StringUtil;

/**
 * Base class for HTTP transport handlers that push messages to connected clients.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractHttpSendingTransportHandler extends AbstractTransportHandler
		implements SockJsSessionFactory {

	/**
	 * Pattern for validating jsonp callback parameter values.
	 */
	private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");


	@Override
	public final void handleRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, SockJsSession wsSession) throws SockJsException {

		AbstractHttpSockJsSession sockJsSession = (AbstractHttpSockJsSession) wsSession;

		String protocol = null;  // https://github.com/sockjs/sockjs-client/issues/130
		sockJsSession.setAcceptedProtocol(protocol);

		// Set content type before writing
		response.getHeaders().setContentType(getContentType());

		handleRequestInternal(request, response, sockJsSession);
	}

	protected void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			AbstractHttpSockJsSession sockJsSession) throws SockJsException {

		if (sockJsSession.isNew()) {
			if (logger.isDebugEnabled()) {
				logger.debug(request.getMethod() + " " + request.getURI());
			}
			sockJsSession.handleInitialRequest(request, response, getFrameFormat(request));
		}
		else if (sockJsSession.isClosed()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Connection already closed (but not removed yet) for " + sockJsSession);
			}
			SockJsFrame frame = SockJsFrame.closeFrameGoAway();
			try {
				response.getBody().write(frame.getContentBytes());
			}
			catch (IOException ex) {
				throw new SockJsException("Failed to send " + frame, sockJsSession.getId(), ex);
			}
		}
		else if (!sockJsSession.isActive()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Starting " + getTransportType() + " async request.");
			}
			sockJsSession.handleSuccessiveRequest(request, response, getFrameFormat(request));
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Another " + getTransportType() + " connection still open for " + sockJsSession);
			}
			String formattedFrame = getFrameFormat(request).format(SockJsFrame.closeFrameAnotherConnectionOpen());
			try {
				response.getBody().write(formattedFrame.getBytes(SockJsFrame.CHARSET));
			}
			catch (IOException ex) {
				throw new SockJsException("Failed to send " + formattedFrame, sockJsSession.getId(), ex);
			}
		}
	}


	protected abstract MediaType getContentType();

	protected abstract SockJsFrameFormat getFrameFormat(ServerHttpRequest request);


	protected final String getCallbackParam(ServerHttpRequest request) {
		String query = request.getURI().getQuery();
		MultiValueMap<String, String> params = UriComponentsBuilder.newInstance().query(query).build().getQueryParams();
		String value = params.getFirst("c");
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		try {
			String result = UriUtils.decode(value, "UTF-8");
			return (CALLBACK_PARAM_PATTERN.matcher(result).matches() ? result : null);
		}
		catch (UnsupportedEncodingException ex) {
			// should never happen
			throw new SockJsException("Unable to decode callback query parameter", null, ex);
		}
	}

}
