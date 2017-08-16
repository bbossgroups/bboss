package org.frameworkset.web.socket.handler;

import java.nio.charset.Charset;

import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTransportHandler  implements TransportHandler {

	protected static final Charset UTF8_CHARSET = Charset.forName("UTF-8");


	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private SockJsServiceConfig serviceConfig;


	@Override
	public void initialize(SockJsServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	public SockJsServiceConfig getServiceConfig() {
		return this.serviceConfig;
	}

}
