package org.frameworkset.web.socket.inf;

import org.frameworkset.http.HttpHeaders;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface WebSocketSession {
	/**
	 * Return a unique session identifier.
	 */
	String getId();

	/**
	 * Return the URI used to open the WebSocket connection.
	 */
	URI getUri();

	/**
	 * Return the headers used in the handshake request.
	 */
	HttpHeaders getHandshakeHeaders();

	/**
	 * Return the map with attributes associated with the WebSocket session.
	 *
	 * <p>When the WebSocketSession is created, on the server side, the map can be
	 * through a {org.frameworkset.web.socket.server.HandshakeInterceptor}.
	 * On the client side, the map can be populated by passing attributes to the
	 * {org.frameworkset.web.socket.client.WebSocketClient} handshake
	 * methods.
	 */
	Map<String, Object> getAttributes();

	/**
	 * Return a {@link java.security.Principal} instance containing the name of the
	 * authenticated user. If the user has not been authenticated, the method returns
	 * <code>null</code>.
	 */
	Principal getPrincipal();

	/**
	 * Return the address on which the request was received.
	 */
	SocketAddress getLocalAddress();

	/**
	 * Return the address of the remote client.
	 */
    SocketAddress getRemoteAddress();

	/**
	 * Return the negotiated sub-protocol or {@code null} if none was specified or
	 * negotiated successfully.
	 */
	String getAcceptedProtocol();

	/**
	 * Configure the maximum size for an incoming text message.
	 */
	void setTextMessageSizeLimit(int messageSizeLimit);

	/**
	 * Get the configured maximum size for an incoming text message.
	 */
	int getTextMessageSizeLimit();

	/**
	 * Configure the maximum size for an incoming binary message.
	 */
	void setBinaryMessageSizeLimit(int messageSizeLimit);

	/**
	 * Get the configured maximum size for an incoming binary message.
	 */
	int getBinaryMessageSizeLimit();

	/**
	 * Return the negotiated extensions or {@code null} if none was specified or
	 * negotiated successfully.
	 */
	List<WebSocketExtension> getExtensions();

	/**
	 * Return whether the connection is still open.
	 */
	boolean isOpen();

	/**
	 * Send a WebSocket message either {@link TextMessage} or
	 * {@link BinaryMessage}.
	 */
	void sendMessage(WebSocketMessage<?> message) throws IOException;

	/**
	 * Close the WebSocket connection with status 1000, i.e. equivalent to:
	 * <pre class="code">
	 * session.close(CloseStatus.NORMAL);
	 * </pre>
	 */
	void close() throws IOException;

	/**
	 * Close the WebSocket connection with the given close status.
	 */
	void close(CloseStatus status) throws IOException;
}
