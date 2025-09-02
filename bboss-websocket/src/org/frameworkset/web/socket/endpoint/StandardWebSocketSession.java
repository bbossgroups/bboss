package org.frameworkset.web.socket.endpoint;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Extension;
import javax.websocket.Session;

import org.frameworkset.http.HttpHeaders;
import org.frameworkset.web.socket.inf.BinaryMessage;
import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.PingMessage;
import org.frameworkset.web.socket.inf.PongMessage;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketExtension;

public class StandardWebSocketSession  extends AbstractWebSocketSession<Session> {

	private final HttpHeaders handshakeHeaders;

	private final InetSocketAddress localAddress;

	private final InetSocketAddress remoteAddress;

	private Principal user;

	private String acceptedProtocol;

	private List<WebSocketExtension> extensions;


	/**
	 * Class constructor.
	 *
	 * @param headers the headers of the handshake request
	 * @param attributes attributes from the HTTP handshake to associate with the WebSocket
	 * session; the provided attributes are copied, the original map is not used.
	 * @param localAddress the address on which the request was received
	 * @param remoteAddress the address of the remote client
	 */
	public StandardWebSocketSession(HttpHeaders headers, Map<String, Object> attributes,
			InetSocketAddress localAddress, InetSocketAddress remoteAddress) {

		this(headers, attributes, localAddress, remoteAddress, null);
	}

	/**
	 * Class constructor that associates a user with the WebSocket session.
	 *
	 * @param headers the headers of the handshake request
	 * @param attributes attributes from the HTTP handshake to associate with the WebSocket session
	 * @param localAddress the address on which the request was received
	 * @param remoteAddress the address of the remote client
	 * @param user the user associated with the session; if {@code null} we'll
	 * 	fallback on the user available in the underlying WebSocket session
	 */
	public StandardWebSocketSession(HttpHeaders headers, Map<String, Object> attributes,
			InetSocketAddress localAddress, InetSocketAddress remoteAddress, Principal user) {

		super(attributes);

		headers = (headers != null) ? headers : new HttpHeaders();
		this.handshakeHeaders = HttpHeaders.readOnlyHttpHeaders(headers);
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		this.user = user;
	}


	@Override
	public String getId() {
		checkNativeSessionInitialized();
		return getNativeSession().getId();
	}

	@Override
	public URI getUri() {
		checkNativeSessionInitialized();
		return getNativeSession().getRequestURI();
	}

	@Override
	public HttpHeaders getHandshakeHeaders() {
		return this.handshakeHeaders;
	}

	@Override
	public Principal getPrincipal() {
		if (this.user != null) {
			return this.user;
		}
		checkNativeSessionInitialized();
		return (isOpen() ? getNativeSession().getUserPrincipal() : null);
	}

	@Override
	public SocketAddress getLocalAddress() {
		return this.localAddress;
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return this.remoteAddress;
	}

	@Override
	public String getAcceptedProtocol() {
		checkNativeSessionInitialized();
		return this.acceptedProtocol;
	}

	@Override
	public void setTextMessageSizeLimit(int messageSizeLimit) {
		checkNativeSessionInitialized();
		getNativeSession().setMaxTextMessageBufferSize(messageSizeLimit);
	}

	@Override
	public int getTextMessageSizeLimit() {
		checkNativeSessionInitialized();
		return getNativeSession().getMaxTextMessageBufferSize();
	}

	@Override
	public void setBinaryMessageSizeLimit(int messageSizeLimit) {
		checkNativeSessionInitialized();
		getNativeSession().setMaxBinaryMessageBufferSize(messageSizeLimit);
	}

	@Override
	public int getBinaryMessageSizeLimit() {
		checkNativeSessionInitialized();
		return getNativeSession().getMaxBinaryMessageBufferSize();
	}

	@Override
	public List<WebSocketExtension> getExtensions() {
		checkNativeSessionInitialized();
		if(this.extensions == null) {
			List<Extension> source = getNativeSession().getNegotiatedExtensions();
			this.extensions = new ArrayList<WebSocketExtension>(source.size());
			for(Extension e : source) {
				this.extensions.add(new StandardToWebSocketExtensionAdapter(e));
			}
		}
		return this.extensions;
	}

	@Override
	public boolean isOpen() {
		return (getNativeSession() != null && getNativeSession().isOpen());
	}

	@Override
	public void initializeNativeSession(Session session) {
		super.initializeNativeSession(session);
		if(this.user == null) {
			this.user = session.getUserPrincipal();
		}
		this.acceptedProtocol = session.getNegotiatedSubprotocol();
	}

	@Override
	protected void sendTextMessage(TextMessage message) throws IOException {
		getNativeSession().getBasicRemote().sendText(message.getPayload(), message.isLast());
	}

	@Override
	protected void sendBinaryMessage(BinaryMessage message) throws IOException {
		getNativeSession().getBasicRemote().sendBinary(message.getPayload(), message.isLast());
	}

	@Override
	protected void sendPingMessage(PingMessage message) throws IOException {
		getNativeSession().getBasicRemote().sendPing(message.getPayload());
	}

	@Override
	protected void sendPongMessage(PongMessage message) throws IOException {
		getNativeSession().getBasicRemote().sendPong(message.getPayload());
	}

	@Override
	protected void closeInternal(CloseStatus status) throws IOException {
		getNativeSession().close(new CloseReason(CloseCodes.getCloseCode(status.getCode()), status.getReason()));
	}
}
