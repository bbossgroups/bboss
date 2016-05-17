package org.frameworkset.web.socket.handler;

import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.web.socket.sockjs.SockJsServiceConfig;
import org.frameworkset.web.socket.sockjs.TransportHandler;

public abstract class AbstractTransportHandler  implements TransportHandler {

	protected static final Charset UTF8_CHARSET = Charset.forName("UTF-8");


	protected final Log logger = LogFactory.getLog(this.getClass());

	private SockJsServiceConfig serviceConfig;


	@Override
	public void initialize(SockJsServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	public SockJsServiceConfig getServiceConfig() {
		return this.serviceConfig;
	}

}
