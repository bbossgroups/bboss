package org.frameworkset.web.socket.sockjs.transport;

import java.io.IOException;

import org.frameworkset.http.HttpStatus;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.web.socket.sockjs.TransportType;

public class XhrReceivingTransportHandler  extends AbstractHttpReceivingTransportHandler {

	@Override
	public TransportType getTransportType() {
		return TransportType.XHR_SEND;
	}

	@Override
	protected String[] readMessages(ServerHttpRequest request) throws IOException {
		return getServiceConfig().getMessageCodec().decodeInputStream(request.getBody());
	}

	@Override
	protected HttpStatus getResponseStatus() {
		return HttpStatus.NO_CONTENT;
	}


}
