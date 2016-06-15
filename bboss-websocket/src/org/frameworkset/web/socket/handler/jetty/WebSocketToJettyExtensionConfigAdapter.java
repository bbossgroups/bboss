package org.frameworkset.web.socket.handler.jetty;

import java.util.Map;

import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.frameworkset.web.socket.inf.WebSocketExtension;

/**
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class WebSocketToJettyExtensionConfigAdapter extends ExtensionConfig {

	public WebSocketToJettyExtensionConfigAdapter(WebSocketExtension extension) {
		super(extension.getName());
		for (Map.Entry<String,String> p : extension.getParameters().entrySet()) {
			super.setParameter(p.getKey(), p.getValue());
		}
	}
}
