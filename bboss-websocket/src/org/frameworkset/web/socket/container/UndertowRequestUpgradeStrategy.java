package org.frameworkset.web.socket.container;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.web.socket.endpoint.ServerEndpointRegistration;
import org.frameworkset.web.socket.handler.HandshakeFailureException;
import org.xnio.StreamConnection;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.HttpUpgradeListener;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.websockets.ServletWebSocketHttpExchange;
import io.undertow.util.PathTemplate;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSocketVersion;
import io.undertow.websockets.core.protocol.Handshake;
import io.undertow.websockets.jsr.ConfiguredServerEndpoint;
import io.undertow.websockets.jsr.EncodingFactory;
import io.undertow.websockets.jsr.EndpointSessionHandler;
import io.undertow.websockets.jsr.ServerWebSocketContainer;
import io.undertow.websockets.jsr.annotated.AnnotatedEndpointFactory;
import io.undertow.websockets.jsr.handshake.HandshakeUtil;
import io.undertow.websockets.jsr.handshake.JsrHybi07Handshake;
import io.undertow.websockets.jsr.handshake.JsrHybi08Handshake;
import io.undertow.websockets.jsr.handshake.JsrHybi13Handshake;
import io.undertow.websockets.spi.WebSocketHttpExchange;

/**
 * A WebSocket {@code RequestUpgradeStrategy} for use with WildFly and its
 * underlying Undertow web server. Also compatible with embedded Undertow usage.
 *
 * <p>Compatible with Undertow 1.0 to 1.3 - as included in WildFly 8.x, 9 and 10.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @author Juergen Hoeller
 * @since 4.0.1
 */
public class UndertowRequestUpgradeStrategy extends AbstractStandardUpgradeStrategy {

	private static final Constructor<ServletWebSocketHttpExchange> exchangeConstructor;

	private static final boolean exchangeConstructorWithPeerConnections;

	private static final Constructor<ConfiguredServerEndpoint> endpointConstructor;

	private static final boolean endpointConstructorWithEndpointFactory;

	private static final Method getBufferPoolMethod;

	private static final Method createChannelMethod;

	static {
		try {
			Class<ServletWebSocketHttpExchange> exchangeType = ServletWebSocketHttpExchange.class;
			Class<?>[] exchangeParamTypes =
					new Class<?>[] {HttpServletRequest.class, HttpServletResponse.class, Set.class};
			Constructor<ServletWebSocketHttpExchange> exchangeCtor =
					ClassUtils.getConstructorIfAvailable(exchangeType, exchangeParamTypes);
			if (exchangeCtor != null) {
				// Undertow 1.1+
				exchangeConstructor = exchangeCtor;
				exchangeConstructorWithPeerConnections = true;
			}
			else {
				// Undertow 1.0
				exchangeParamTypes = new Class<?>[] {HttpServletRequest.class, HttpServletResponse.class};
				exchangeConstructor = exchangeType.getConstructor(exchangeParamTypes);
				exchangeConstructorWithPeerConnections = false;
			}

			Class<ConfiguredServerEndpoint> endpointType = ConfiguredServerEndpoint.class;
			Class<?>[] endpointParamTypes = new Class<?>[] {ServerEndpointConfig.class, InstanceFactory.class,
					PathTemplate.class, EncodingFactory.class, AnnotatedEndpointFactory.class};
			Constructor<ConfiguredServerEndpoint> endpointCtor =
					ClassUtils.getConstructorIfAvailable(endpointType, endpointParamTypes);
			if (endpointCtor != null) {
				// Undertow 1.1+
				endpointConstructor = endpointCtor;
				endpointConstructorWithEndpointFactory = true;
			}
			else {
				// Undertow 1.0
				endpointParamTypes = new Class<?>[] {ServerEndpointConfig.class, InstanceFactory.class,
						PathTemplate.class, EncodingFactory.class};
				endpointConstructor = endpointType.getConstructor(endpointParamTypes);
				endpointConstructorWithEndpointFactory = false;
			}

			// Adapting between different Pool API types in Undertow 1.0-1.2 vs 1.3
			getBufferPoolMethod = WebSocketHttpExchange.class.getMethod("getBufferPool");
			createChannelMethod = ReflectionUtils.findMethod(Handshake.class, "createChannel", (Class<?>[]) null);
		}
		catch (Throwable ex) {
			throw new IllegalStateException("Incompatible Undertow API version", ex);
		}
	}

	private static final String[] supportedVersions = new String[] {
			WebSocketVersion.V13.toHttpHeaderValue(),
			WebSocketVersion.V08.toHttpHeaderValue(),
			WebSocketVersion.V07.toHttpHeaderValue()
	};


	private final Set<WebSocketChannel> peerConnections;


	public UndertowRequestUpgradeStrategy() {
		if (exchangeConstructorWithPeerConnections) {
			this.peerConnections = Collections.newSetFromMap(new ConcurrentHashMap<WebSocketChannel, Boolean>());
		}
		else {
			this.peerConnections = null;
		}
	}


	@Override
	public String[] getSupportedVersions() {
		return supportedVersions;
	}

	@Override
	protected void upgradeInternal(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, List<Extension> selectedExtensions, final Endpoint endpoint)
			throws HandshakeFailureException {

		HttpServletRequest servletRequest = getHttpServletRequest(request);
		HttpServletResponse servletResponse = getHttpServletResponse(response);

		final ServletWebSocketHttpExchange exchange = createHttpExchange(servletRequest, servletResponse);
		exchange.putAttachment(HandshakeUtil.PATH_PARAMS, Collections.<String, String>emptyMap());

		ServerWebSocketContainer wsContainer = (ServerWebSocketContainer) getContainer(servletRequest);
		final EndpointSessionHandler endpointSessionHandler = new EndpointSessionHandler(wsContainer);

		final ConfiguredServerEndpoint configuredServerEndpoint = createConfiguredServerEndpoint(
				selectedProtocol, selectedExtensions, endpoint, servletRequest);

		final Handshake handshake = getHandshakeToUse(exchange, configuredServerEndpoint);

		exchange.upgradeChannel(new HttpUpgradeListener() {
			@Override
			public void handleUpgrade(StreamConnection connection, HttpServerExchange serverExchange) {
				Object bufferPool = ReflectionUtils.invokeMethod(getBufferPoolMethod, exchange);
				WebSocketChannel channel = (WebSocketChannel) ReflectionUtils.invokeMethod(
						createChannelMethod, handshake, exchange, connection, bufferPool);
				if (peerConnections != null) {
					peerConnections.add(channel);
				}
				endpointSessionHandler.onConnect(exchange, channel);
			}
		});

		handshake.handshake(exchange);
	}

	private ServletWebSocketHttpExchange createHttpExchange(HttpServletRequest request, HttpServletResponse response) {
		try {
			return (this.peerConnections != null ?
					exchangeConstructor.newInstance(request, response, this.peerConnections) :
					exchangeConstructor.newInstance(request, response));
		}
		catch (Exception ex) {
			throw new HandshakeFailureException("Failed to instantiate ServletWebSocketHttpExchange", ex);
		}
	}

	private Handshake getHandshakeToUse(ServletWebSocketHttpExchange exchange, ConfiguredServerEndpoint endpoint) {
		Handshake handshake = new JsrHybi13Handshake(endpoint);
		if (handshake.matches(exchange)) {
			return handshake;
		}
		handshake = new JsrHybi08Handshake(endpoint);
		if (handshake.matches(exchange)) {
			return handshake;
		}
		handshake = new JsrHybi07Handshake(endpoint);
		if (handshake.matches(exchange)) {
			return handshake;
		}
		// Should never occur
		throw new HandshakeFailureException("No matching Undertow Handshake found: " + exchange.getRequestHeaders());
	}

	private ConfiguredServerEndpoint createConfiguredServerEndpoint(String selectedProtocol,
			List<Extension> selectedExtensions, Endpoint endpoint, HttpServletRequest servletRequest) {

		String path = servletRequest.getRequestURI();  // shouldn't matter
		ServerEndpointRegistration endpointRegistration = new ServerEndpointRegistration(path, endpoint);
		endpointRegistration.setSubprotocols(Arrays.asList(selectedProtocol));
		endpointRegistration.setExtensions(selectedExtensions);

        EncodingFactory encodingFactory = new EncodingFactory(
                Collections.<Class<?>, List<InstanceFactory<? extends Encoder>>>emptyMap(),
                Collections.<Class<?>, List<InstanceFactory<? extends Decoder>>>emptyMap(),
                Collections.<Class<?>, List<InstanceFactory<? extends Encoder>>>emptyMap(),
                Collections.<Class<?>, List<InstanceFactory<? extends Decoder>>>emptyMap());
		try {
			return (endpointConstructorWithEndpointFactory ?
					endpointConstructor.newInstance(endpointRegistration,
							new EndpointInstanceFactory(endpoint), null, encodingFactory, null) :
					endpointConstructor.newInstance(endpointRegistration,
							new EndpointInstanceFactory(endpoint), null, encodingFactory));
		}
		catch (Exception ex) {
			throw new HandshakeFailureException("Failed to instantiate ConfiguredServerEndpoint", ex);
		}
	}


	private static class EndpointInstanceFactory implements InstanceFactory<Endpoint> {

		private final Endpoint endpoint;

		public EndpointInstanceFactory(Endpoint endpoint) {
			this.endpoint = endpoint;
		}

		@Override
		public InstanceHandle<Endpoint> createInstance() throws InstantiationException {
			return new InstanceHandle<Endpoint>() {
				@Override
				public Endpoint getInstance() {
					return endpoint;
				}
				@Override
				public void release() {
				}
			};
		}
	}

}
