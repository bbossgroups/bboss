package org.frameworkset.web.socket.handler;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Extension;

import org.frameworkset.web.socket.inf.WebSocketExtension;
/**
 * Adapt an instance of {@link WebSocketExtension} to
 * the {@link javax.websocket.Extension} interface.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class WebSocketToStandardExtensionAdapter  implements Extension {

	private final String name;

	private final List<Parameter> parameters = new ArrayList<Parameter>();


	public WebSocketToStandardExtensionAdapter(final WebSocketExtension extension) {
		this.name = extension.getName();
		for (final String paramName : extension.getParameters().keySet()) {
			this.parameters.add(new Parameter() {
				@Override
				public String getName() {
					return paramName;
				}
				@Override
				public String getValue() {
					return extension.getParameters().get(paramName);
				}
			});
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<Parameter> getParameters() {
		return this.parameters;
	}

}
