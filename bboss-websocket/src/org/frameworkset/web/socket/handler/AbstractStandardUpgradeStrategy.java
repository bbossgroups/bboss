package org.frameworkset.web.socket.handler;

import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerContainer;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.ServletServerHttpRequest;
import org.frameworkset.http.ServletServerHttpResponse;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.container.RequestUpgradeStrategy;
import org.frameworkset.web.socket.endpoint.StandardToWebSocketExtensionAdapter;
import org.frameworkset.web.socket.endpoint.StandardWebSocketHandlerAdapter;
import org.frameworkset.web.socket.endpoint.StandardWebSocketSession;
import org.frameworkset.web.socket.inf.WebSocketExtension;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * A base class for {@link RequestUpgradeStrategy} implementations that build
 * on the standard WebSocket API for Java (JSR-356).
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractStandardUpgradeStrategy  implements RequestUpgradeStrategy {

	protected final static Logger logger = LoggerFactory.getLogger(AbstractStandardUpgradeStrategy.class);

	private volatile List<WebSocketExtension> extensions;


	protected ServerContainer getContainer(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		String attrName = "javax.websocket.server.ServerContainer";
		ServerContainer container = (ServerContainer) servletContext.getAttribute(attrName);
		Assert.notNull(container, "No 'javax.websocket.server.ServerContainer' ServletContext attribute. " +
				"Are you running in a Servlet container that supports JSR-356?");
		return container;
	}

	protected final HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {
		Assert.isTrue(request instanceof ServletServerHttpRequest);
		return ((ServletServerHttpRequest) request).getServletRequest();
	}

	protected final HttpServletResponse getHttpServletResponse(ServerHttpResponse response) {
		Assert.isTrue(response instanceof ServletServerHttpResponse);
		return ((ServletServerHttpResponse) response).getServletResponse();
	}


	@Override
	public List<WebSocketExtension> getSupportedExtensions(ServerHttpRequest request) {
		if (this.extensions == null) {
			HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
			this.extensions = getInstalledExtensions(getContainer(servletRequest));
		}
		return this.extensions;
	}

	protected List<WebSocketExtension> getInstalledExtensions(WebSocketContainer container) {
		List<WebSocketExtension> result = new ArrayList<WebSocketExtension>();
		for (Extension ext : container.getInstalledExtensions()) {
			result.add(new StandardToWebSocketExtensionAdapter(ext));
		}
		return result;
	}


	@Override
	public void upgrade(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, List<WebSocketExtension> selectedExtensions, Principal user,
			WebSocketHandler wsHandler, Map<String, Object> attrs) throws HandshakeFailureException {

		HttpHeaders headers = request.getHeaders();
		InetSocketAddress localAddr = request.getLocalAddress();
		InetSocketAddress remoteAddr = request.getRemoteAddress();

		StandardWebSocketSession session = new StandardWebSocketSession(headers, attrs, localAddr, remoteAddr, user);
		StandardWebSocketHandlerAdapter endpoint = new StandardWebSocketHandlerAdapter(wsHandler, session);

		List<Extension> extensions = new ArrayList<Extension>();
		for (WebSocketExtension extension : selectedExtensions) {
			extensions.add(new WebSocketToStandardExtensionAdapter(extension));
		}

		upgradeInternal(request, response, selectedProtocol, extensions, endpoint);
	}

	protected abstract void upgradeInternal(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, List<Extension> selectedExtensions, Endpoint endpoint)
			throws HandshakeFailureException;

}
