package org.frameworkset.web.socket.endpoint;

import javax.websocket.Extension;

import org.frameworkset.web.socket.inf.WebSocketExtension;

public class StandardToWebSocketExtensionAdapter extends WebSocketExtension {

	public StandardToWebSocketExtensionAdapter(Extension ext) {
		super(ext.getName());
		for (Extension.Parameter p : ext.getParameters()) {
			super.getParameters().put(p.getName(), p.getValue());
		}
	}
}
