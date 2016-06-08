package org.frameworkset.web.socket.handler;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.web.socket.inf.WebSocketExtension;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public interface RequestUpgradeStrategy {
	/**
	 * Return the supported WebSocket protocol versions.
	 */
	String[] getSupportedVersions();

	/**
	 * Return the WebSocket protocol extensions supported by the underlying WebSocket server.
	 */
	List<WebSocketExtension> getSupportedExtensions(ServerHttpRequest request);

	/**
	 * Perform runtime specific steps to complete the upgrade. Invoked after successful
	 * negotiation of the handshake request.
	 * @param request the current request
	 * @param response the current response
	 * @param selectedProtocol the selected sub-protocol, if any
	 * @param selectedExtensions the selected WebSocket protocol extensions
	 * @param user the user to associate with the WebSocket session
	 * @param wsHandler the handler for WebSocket messages
	 * @param attributes handshake request specific attributes to be set on the WebSocket
	 * session via {@link org.frameworkset.web.socket.server.HandshakeInterceptor} and
	 * thus made available to the {@link org.frameworkset.web.socket.WebSocketHandler}
	 * @throws HandshakeFailureException thrown when handshake processing failed to
	 * complete due to an internal, unrecoverable error, i.e. a server error as
	 * opposed to a failure to successfully negotiate the requirements of the
	 * handshake request.
	 */
	void upgrade(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, List<WebSocketExtension> selectedExtensions, Principal user,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException;

}
