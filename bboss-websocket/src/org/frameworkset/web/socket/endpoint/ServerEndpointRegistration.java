package org.frameworkset.web.socket.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.frameworkset.spi.ApplicationContextAware;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.Assert;
import org.frameworkset.web.socket.handler.BeanCreatingHandlerProvider;

/**
 * An implementation of {@link javax.websocket.server.ServerEndpointConfig} for use in
 * bboss applications. A {@link ServerEndpointRegistration} bean is detected by
 * ServerEndpointExporter and registered with a Java WebSocket runtime at startup.
 *
 * <p>Class constructors accept a singleton {@link javax.websocket.Endpoint} instance
 * or an Endpoint specified by type {@link Class}. When specified by type, the endpoint
 * will be instantiated and initialized through the bboss ApplicationContext before
 * each client WebSocket connection.
 *
 * <p>This class also extends
 * {@link javax.websocket.server.ServerEndpointConfig.Configurator} to make it easier to
 * override methods for customizing the handshake process.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class ServerEndpointRegistration extends ServerEndpointConfig.Configurator
		implements ServerEndpointConfig,   ApplicationContextAware  {

	private final String path;

	private final BeanCreatingHandlerProvider<Endpoint> endpointProvider;

	private final Endpoint endpoint;

    private List<Class<? extends Encoder>> encoders = new ArrayList<Class<? extends Encoder>>();

    private List<Class<? extends Decoder>> decoders = new ArrayList<Class<? extends Decoder>>();

	private List<String> protocols = new ArrayList<String>();

	private List<Extension> extensions = new ArrayList<Extension>();

	private final Map<String, Object> userProperties = new HashMap<String, Object>();


	/**
	 * Create a new {@link ServerEndpointRegistration} instance from an
	 * {@code javax.websocket.Endpoint} class.
	 * @param path the endpoint path
	 * @param endpointClass the endpoint class
	 */
	public ServerEndpointRegistration(String path, Class<? extends Endpoint> endpointClass) {
		Assert.hasText(path, "path must not be empty");
		Assert.notNull(endpointClass, "endpointClass must not be null");
		this.path = path;
		this.endpointProvider = new BeanCreatingHandlerProvider<Endpoint>(endpointClass);
		this.endpoint = null;
	}

	/**
	 * Create a new {@link ServerEndpointRegistration} instance from an
	 * {@code javax.websocket.Endpoint} instance.
	 * @param path the endpoint path
	 * @param endpoint the endpoint instance
	 */
	public ServerEndpointRegistration(String path, Endpoint endpoint) {
		Assert.hasText(path, "path must not be empty");
		Assert.notNull(endpoint, "endpoint must not be null");
		this.path = path;
		this.endpointProvider = null;
		this.endpoint = endpoint;
	}


	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public Class<? extends Endpoint> getEndpointClass() {
		return (this.endpoint != null) ?
				this.endpoint.getClass() : ((Class<? extends Endpoint>) this.endpointProvider.getHandlerType());
	}

	public Endpoint getEndpoint() {
		return (this.endpoint != null) ? this.endpoint : this.endpointProvider.getHandler();
	}

	public void setSubprotocols(List<String> protocols) {
		this.protocols = protocols;
	}

	@Override
	public List<String> getSubprotocols() {
		return this.protocols;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}

	@Override
	public List<Extension> getExtensions() {
		return this.extensions;
	}

	public void setUserProperties(Map<String, Object> userProperties) {
		this.userProperties.clear();
		this.userProperties.putAll(userProperties);
	}

	@Override
	public Map<String, Object> getUserProperties() {
		return this.userProperties;
	}

	public void setEncoders(List<Class<? extends Encoder>> encoders) {
		this.encoders = encoders;
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		return this.encoders;
	}

	public void setDecoders(List<Class<? extends Decoder>> decoders) {
		this.decoders = decoders;
	}

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		return this.decoders;
	}

	@Override
	public Configurator getConfigurator() {
		return this;
	}

	@Override
	public void setApplicationContext(BaseApplicationContext beanFactory){
		if (this.endpointProvider != null) {
			this.endpointProvider.setApplicationContext(beanFactory);
		}
	}


	// Implementations of ServerEndpointConfig.Configurator

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
		return (T) getEndpoint();
	}

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		super.modifyHandshake(this, request, response);
	}

	@Override
	public boolean checkOrigin(String originHeaderValue) {
		return super.checkOrigin(originHeaderValue);
	}

	@Override
	public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
		return super.getNegotiatedSubprotocol(supported, requested);
	}

	@Override
	public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {
		return super.getNegotiatedExtensions(installed, requested);
	}


	@Override
	public String toString() {
		return "ServerEndpointRegistration for path '" + getPath() + "': " + getEndpointClass();
	}

}
