package org.frameworkset.web.socket.handler;

import javax.servlet.ServletContext;

import org.frameworkset.web.servlet.context.ServletContextAware;

public class DefaultHandshakeHandler extends AbstractHandshakeHandler implements ServletContextAware {

	public DefaultHandshakeHandler() {
	}

	public DefaultHandshakeHandler(RequestUpgradeStrategy requestUpgradeStrategy) {
		super(requestUpgradeStrategy);
	}


	@Override
	public void setServletContext(ServletContext servletContext) {
		RequestUpgradeStrategy strategy = getRequestUpgradeStrategy();
		if (strategy instanceof ServletContextAware) {
			((ServletContextAware) strategy).setServletContext(servletContext);
		}
	}

}
