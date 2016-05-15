package org.frameworkset.web.socket.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.ServletServerHttpRequest;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public class HttpSessionHandshakeInterceptor  implements HandshakeInterceptor {

	/**
	 * The name of the attribute under which the HTTP session id is exposed when
	 * {@link #setCopyHttpSessionId(boolean) copyHttpSessionId} is "true".
	 */
	public static final String HTTP_SESSION_ID_ATTR_NAME = "HTTP.SESSION.ID";


	private final Collection<String> attributeNames;

	private boolean copyAllAttributes;

	private boolean copyHttpSessionId = true;

	private boolean createSession;


	/**
	 * Default constructor for copying all HTTP session attributes and the HTTP
	 * session id.
	 * @see #setCopyAllAttributes
	 * @see #setCopyHttpSessionId
	 */
	public HttpSessionHandshakeInterceptor() {
		this.attributeNames = Collections.emptyList();
		this.copyAllAttributes = true;
	}

	/**
	 * Constructor for copying specific HTTP session attributes and the HTTP
	 * session id.
	 * @param attributeNames session attributes to copy
	 * @see #setCopyAllAttributes
	 * @see #setCopyHttpSessionId
	 */
	public HttpSessionHandshakeInterceptor(Collection<String> attributeNames) {
		this.attributeNames = Collections.unmodifiableCollection(attributeNames);
		this.copyAllAttributes = false;
	}


	/**
	 * Return the configured attribute names to copy (read-only).
	 */
	public Collection<String> getAttributeNames() {
		return this.attributeNames;
	}

	/**
	 * Whether to copy all attributes from the HTTP session. If set to "true",
	 * any explicitly configured attribute names are ignored.
	 * <p>By default this is set to either "true" or "false" depending on which
	 * constructor was used (default or with attribute names respectively).
	 * @param copyAllAttributes whether to copy all attributes
	 */
	public void setCopyAllAttributes(boolean copyAllAttributes) {
		this.copyAllAttributes = copyAllAttributes;
	}

	/**
	 * Whether to copy all HTTP session attributes.
	 */
	public boolean isCopyAllAttributes() {
		return this.copyAllAttributes;
	}

	/**
	 * Whether the HTTP session id should be copied to the handshake attributes
	 * under the key {@link #HTTP_SESSION_ID_ATTR_NAME}.
	 * <p>By default this is "true".
	 * @param copyHttpSessionId whether to copy the HTTP session id.
	 */
	public void setCopyHttpSessionId(boolean copyHttpSessionId) {
		this.copyHttpSessionId = copyHttpSessionId;
	}

	/**
	 * Whether to copy the HTTP session id to the handshake attributes.
	 */
	public boolean isCopyHttpSessionId() {
		return this.copyHttpSessionId;
	}

	/**
	 * Whether to allow the HTTP session to be created while accessing it.
	 * <p>By default set to {@code false}.
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public void setCreateSession(boolean createSession) {
		this.createSession = createSession;
	}

	/**
	 * Whether the HTTP session is allowed to be created.
	 */
	public boolean isCreateSession() {
		return this.createSession;
	}


	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		HttpSession session = getSession(request);
		if (session != null) {
			if (isCopyHttpSessionId()) {
				attributes.put(HTTP_SESSION_ID_ATTR_NAME, session.getId());
			}
			Enumeration<String> names = session.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				if (isCopyAllAttributes() || getAttributeNames().contains(name)) {
					attributes.put(name, session.getAttribute(name));
				}
			}
		}
		return true;
	}

	private HttpSession getSession(ServerHttpRequest request) {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			return serverRequest.getServletRequest().getSession(isCreateSession());
		}
		return null;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Exception ex) {
	}


}
