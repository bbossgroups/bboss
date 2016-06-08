package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.converter.FormHttpMessageConverter;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsMessageCodec;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.frameworkset.web.socket.sockjs.TransportType;
import org.frameworkset.web.socket.sockjs.session.AbstractHttpSockJsSession;

import com.frameworkset.util.StringUtil;

/**
 * A {@link TransportHandler} that receives messages over HTTP.
 *
 * @author Rossen Stoyanchev
 */
public class JsonpReceivingTransportHandler extends AbstractHttpReceivingTransportHandler {

	private final FormHttpMessageConverter formConverter = new FormHttpMessageConverter();


	@Override
	public TransportType getTransportType() {
		return TransportType.JSONP_SEND;
	}

	@Override
	public void handleRequestInternal(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, AbstractHttpSockJsSession sockJsSession) throws SockJsException {

		super.handleRequestInternal(request, response, wsHandler, sockJsSession);
		try {
			response.getBody().write("ok".getBytes(UTF8_CHARSET));
		}
		catch (IOException ex) {
			throw new SockJsException("Failed to write to the response body", sockJsSession.getId(), ex);
		}
	}

	@Override
	protected String[] readMessages(ServerHttpRequest request) throws IOException {
		SockJsMessageCodec messageCodec = getServiceConfig().getMessageCodec();
		MediaType contentType = request.getHeaders().getContentType();
		if (contentType != null && MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
			MultiValueMap<String, String> map = this.formConverter.read(null, request);
			String d = map.getFirst("d");
			return (StringUtil.hasText(d) ? messageCodec.decode(d) : null);
		}
		else {
			return messageCodec.decodeInputStream(request.getBody());
		}
	}

	@Override
	protected HttpStatus getResponseStatus() {
		return HttpStatus.OK;
	}

}
