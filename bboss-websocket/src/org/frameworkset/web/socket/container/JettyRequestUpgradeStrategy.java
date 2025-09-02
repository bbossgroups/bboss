package org.frameworkset.web.socket.container;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.server.HandshakeRFC6455;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.ServletServerHttpRequest;
import org.frameworkset.http.ServletServerHttpResponse;
import org.frameworkset.spi.Lifecycle;
import org.frameworkset.spi.support.NamedThreadLocal;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.frameworkset.web.socket.handler.HandshakeFailureException;
import org.frameworkset.web.socket.handler.jetty.JettyWebSocketHandlerAdapter;
import org.frameworkset.web.socket.handler.jetty.JettyWebSocketSession;
import org.frameworkset.web.socket.handler.jetty.WebSocketToJettyExtensionConfigAdapter;
import org.frameworkset.web.socket.inf.WebSocketExtension;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A {@link RequestUpgradeStrategy} for use with Jetty 9.0-9.3. Based on Jetty's
 * internal {@code org.eclipse.jetty.websocket.server.WebSocketHandler} class.
 *
 * @author Phillip Webb
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class JettyRequestUpgradeStrategy implements RequestUpgradeStrategy, Lifecycle, ServletContextAware {

    private static Logger log = LoggerFactory.getLogger(JettyRequestUpgradeStrategy.class);
	// Pre-Jetty 9.3 init method without ServletContext
	private static final Method webSocketFactoryInitMethod =
			ClassUtils.getMethodIfAvailable(WebSocketServerFactory.class, "init");

	private static final ThreadLocal<WebSocketHandlerContainer> wsContainerHolder =
			new NamedThreadLocal<WebSocketHandlerContainer>("WebSocket Handler Container");


	private WebSocketServerFactory factory;

	private volatile List<WebSocketExtension> supportedExtensions;

	private ServletContext servletContext;

	private volatile boolean running = false;


	/**
	 * Default constructor that creates {@link WebSocketServerFactory} through
	 * its default constructor thus using a default {@link WebSocketPolicy}.
	 */
	public JettyRequestUpgradeStrategy() {
//		this(new WebSocketServerFactory());
	}

	/**
	 * A constructor accepting a {@link WebSocketServerFactory}.
	 * This may be useful for modifying the factory's {@link WebSocketPolicy}
	 * via {@link WebSocketServerFactory#getPolicy()}.
	 */
	 void initJettyRequestUpgradeStrategy() {
		Assert.notNull(factory, "WebSocketServerFactory must not be null");
         WebSocketServerFactory factory = new WebSocketServerFactory(this.servletContext);
         
         factory.setCreator(new WebSocketCreator() {
			@Override
			public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
				// Cast to avoid infinite recursion
				return createWebSocket((UpgradeRequest) request, (UpgradeResponse) response);
			}
			// For Jetty 9.0.x
			public Object createWebSocket(UpgradeRequest request, UpgradeResponse response) {
				WebSocketHandlerContainer container = wsContainerHolder.get();
				Assert.state(container != null, "Expected WebSocketHandlerContainer");
				response.setAcceptedSubProtocol(container.getSelectedProtocol());
				response.setExtensions(container.getExtensionConfigs());
				return container.getHandler();
			}
		});
	}


	@Override
	public String[] getSupportedVersions() {
		return new String[] { String.valueOf(HandshakeRFC6455.VERSION) };
	}

	@Override
	public List<WebSocketExtension> getSupportedExtensions(ServerHttpRequest request) {
		if (this.supportedExtensions == null) {
			this.supportedExtensions = getWebSocketExtensions();
		}
		return this.supportedExtensions;
	}

	private List<WebSocketExtension> getWebSocketExtensions() {
		List<WebSocketExtension> result = new ArrayList<WebSocketExtension>();
		for (String name : this.factory.getExtensionFactory().getExtensionNames()) {
			result.add(new WebSocketExtension(name));
		}
		return result;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}


	@Override
	public void start() {
		if (!isRunning()) {
			this.running = true;
			try {
                initJettyRequestUpgradeStrategy();
//				if (webSocketFactoryInitMethod != null) {
//					webSocketFactoryInitMethod.invoke(this.factory);
//				}
//				else {
//					this.factory.init(this.servletContext);
//				}
                this.factory.start();
			}
			catch (Exception ex) {
				throw new IllegalStateException("Unable to initialize Jetty WebSocketServerFactory", ex);
			}
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			this.running = false;
            try {
                this.factory.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
	}

	@Override
	public void upgrade(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, List<WebSocketExtension> selectedExtensions, Principal user,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {

		Assert.isInstanceOf(ServletServerHttpRequest.class, request);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		Assert.isInstanceOf(ServletServerHttpResponse.class, response);
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

		Assert.isTrue(this.factory.isUpgradeRequest(servletRequest, servletResponse), "Not a WebSocket handshake");

		JettyWebSocketSession session = new JettyWebSocketSession(attributes, user);
		JettyWebSocketHandlerAdapter handlerAdapter = new JettyWebSocketHandlerAdapter(wsHandler, session);

		WebSocketHandlerContainer container =
				new WebSocketHandlerContainer(handlerAdapter, selectedProtocol, selectedExtensions);

		try {
			wsContainerHolder.set(container);
			this.factory.acceptWebSocket(servletRequest, servletResponse);
		}
		catch (IOException ex) {
			throw new HandshakeFailureException(
					"Response update failed during upgrade to WebSocket: " + request.getURI(), ex);
		}
		finally {
			wsContainerHolder.remove();
		}
	}


	private static class WebSocketHandlerContainer {

		private final JettyWebSocketHandlerAdapter handler;

		private final String selectedProtocol;

		private final List<ExtensionConfig> extensionConfigs;

		public WebSocketHandlerContainer(JettyWebSocketHandlerAdapter handler, String protocol, List<WebSocketExtension> extensions) {
			this.handler = handler;
			this.selectedProtocol = protocol;
			if (CollectionUtils.isEmpty(extensions)) {
				this.extensionConfigs = null;
			}
			else {
				this.extensionConfigs = new ArrayList<ExtensionConfig>();
				for (WebSocketExtension e : extensions) {
					this.extensionConfigs.add(new WebSocketToJettyExtensionConfigAdapter(e));
				}
			}
		}

		public JettyWebSocketHandlerAdapter getHandler() {
			return this.handler;
		}

		public String getSelectedProtocol() {
			return this.selectedProtocol;
		}

		public List<ExtensionConfig> getExtensionConfigs() {
			return this.extensionConfigs;
		}
	}

}
