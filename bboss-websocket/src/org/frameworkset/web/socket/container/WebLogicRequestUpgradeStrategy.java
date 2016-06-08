package org.frameworkset.web.socket.container;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.CloseReason;

import org.frameworkset.util.DirectFieldAccessor;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.web.socket.handler.HandshakeFailureException;
import org.glassfish.tyrus.core.TyrusUpgradeResponse;
import org.glassfish.tyrus.core.Utils;
import org.glassfish.tyrus.spi.Connection;
import org.glassfish.tyrus.spi.WebSocketEngine.UpgradeInfo;
import org.glassfish.tyrus.spi.Writer;

/**
 * A WebSocket {@code RequestUpgradeStrategy} for Oracle's WebLogic.
 * Supports 12.1.3 as well as 12.2.1, as of Spring Framework 4.2.3.
 *
 * @author Rossen Stoyanchev
 * @since 4.1
 */
public class WebLogicRequestUpgradeStrategy extends AbstractTyrusRequestUpgradeStrategy {

	private static final boolean WLS_12_1_3 = isWebLogic1213();

	private static final TyrusEndpointHelper endpointHelper =
			(WLS_12_1_3 ? new Tyrus135EndpointHelper() : new Tyrus17EndpointHelper());

	private static final TyrusMuxableWebSocketHelper webSocketHelper = new TyrusMuxableWebSocketHelper();

	private static final WebLogicServletWriterHelper servletWriterHelper = new WebLogicServletWriterHelper();

	private static final Connection.CloseListener noOpCloseListener = new Connection.CloseListener() {

		@Override
		public void close(CloseReason reason) {
		}
	};



	@Override
	protected TyrusEndpointHelper getEndpointHelper() {
		return endpointHelper;
	}

	@Override
	protected void handleSuccess(HttpServletRequest request, HttpServletResponse response,
			UpgradeInfo upgradeInfo, TyrusUpgradeResponse upgradeResponse) throws IOException, ServletException {

		response.setStatus(upgradeResponse.getStatus());
		for (Map.Entry<String, List<String>> entry : upgradeResponse.getHeaders().entrySet()) {
			response.addHeader(entry.getKey(), Utils.getHeaderFromList(entry.getValue()));
		}

		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(-1L);

		Object nativeRequest = getNativeRequest(request);
		DirectFieldAccessor beanWrapper = new DirectFieldAccessor(nativeRequest);
		Object httpSocket = beanWrapper.getPropertyValue("connection.connectionHandler.rawConnection");
		Object webSocket = webSocketHelper.newInstance(request, httpSocket);
		webSocketHelper.upgrade(webSocket, httpSocket, request.getServletContext());

		response.flushBuffer();

		boolean isProtected = request.getUserPrincipal() != null;
		Writer servletWriter = servletWriterHelper.newInstance(response, webSocket, isProtected);
		Connection connection = upgradeInfo.createConnection(servletWriter, noOpCloseListener);
		new DirectFieldAccessor(webSocket).setPropertyValue("connection", connection);
		new DirectFieldAccessor(servletWriter).setPropertyValue("connection", connection);
		webSocketHelper.registerForReadEvent(webSocket);
	}


	private static boolean isWebLogic1213() {
		try {
			type("weblogic.websocket.tyrus.TyrusMuxableWebSocket").getDeclaredConstructor(
					type("weblogic.servlet.internal.MuxableSocketHTTP"));
			return true;
		}
		catch (NoSuchMethodException ex) {
			return false;
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("No compatible WebSocket version found", ex);
		}
	}

	private static Class<?> type(String className) throws ClassNotFoundException {
		return WebLogicRequestUpgradeStrategy.class.getClassLoader().loadClass(className);
	}

	private static Method method(String className, String method, Class<?>... paramTypes)
			throws ClassNotFoundException, NoSuchMethodException {

		return type(className).getDeclaredMethod(method, paramTypes);
	}

	private static Object getNativeRequest(ServletRequest request) {
		while (request instanceof ServletRequestWrapper) {
			request = ((ServletRequestWrapper) request).getRequest();
		}
		return request;
	}


	/**
	 * Helps to create and invoke {@code weblogic.servlet.internal.MuxableSocketHTTP}.
	 */
	private static class TyrusMuxableWebSocketHelper {

		private static final Class<?> type;

		private static final Constructor<?> constructor;

		private static final SubjectHelper subjectHelper;

		private static final Method upgradeMethod;

		private static final Method readEventMethod;

		static {
			try {
				type = type("weblogic.websocket.tyrus.TyrusMuxableWebSocket");

				if (WLS_12_1_3) {
					constructor = type.getDeclaredConstructor(type("weblogic.servlet.internal.MuxableSocketHTTP"));
					subjectHelper = null;
				}
				else {
					constructor = type.getDeclaredConstructor(
							type("weblogic.servlet.internal.MuxableSocketHTTP"),
							type("weblogic.websocket.tyrus.CoherenceServletFilterService"),
							type("weblogic.servlet.spi.SubjectHandle"));
					subjectHelper = new SubjectHelper();
				}

				upgradeMethod = type.getMethod("upgrade", type("weblogic.socket.MuxableSocket"), ServletContext.class);
				readEventMethod = type.getMethod("registerForReadEvent");
			}
			catch (Exception ex) {
				throw new IllegalStateException("No compatible WebSocket version found", ex);
			}
		}

		private Object newInstance(HttpServletRequest request, Object httpSocket) {
			try {
				Object[] args = (WLS_12_1_3 ? new Object[] {httpSocket} :
						new Object[] {httpSocket, null, subjectHelper.getSubject(request)});
				return constructor.newInstance(args);
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to create TyrusMuxableWebSocket", ex);
			}
		}

		private void upgrade(Object webSocket, Object httpSocket, ServletContext servletContext) {
			try {
				upgradeMethod.invoke(webSocket, httpSocket, servletContext);
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to upgrade TyrusMuxableWebSocket", ex);
			}
		}

		private void registerForReadEvent(Object webSocket) {
			try {
				readEventMethod.invoke(webSocket);
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to register WebSocket for read event", ex);
			}
		}
	}


	private static class SubjectHelper {

		private final Method securityContextMethod;

		private final Method currentUserMethod;

		private final Method providerMethod;

		private final Method anonymousSubjectMethod;

		public SubjectHelper() {
			try {
				String className = "weblogic.servlet.internal.WebAppServletContext";
				securityContextMethod = method(className, "getSecurityContext");

				className = "weblogic.servlet.security.internal.SecurityModule";
				currentUserMethod = method(className, "getCurrentUser",
						type("weblogic.servlet.security.internal.ServletSecurityContext"),
						HttpServletRequest.class);

				className = "weblogic.servlet.security.internal.WebAppSecurity";
				providerMethod = method(className, "getProvider");
				anonymousSubjectMethod = providerMethod.getReturnType().getDeclaredMethod("getAnonymousSubject");
			}
			catch (Exception ex) {
				throw new IllegalStateException("No compatible WebSocket version found", ex);
			}
		}

		public Object getSubject(HttpServletRequest request) {
			try {
				ServletContext servletContext = request.getServletContext();
				Object securityContext = securityContextMethod.invoke(servletContext);
				Object subject = currentUserMethod.invoke(null, securityContext, request);
				if (subject == null) {
					Object securityProvider = providerMethod.invoke(null);
					subject = anonymousSubjectMethod.invoke(securityProvider);
				}
				return subject;
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to obtain SubjectHandle", ex);
			}
		}
	}


	/**
	 * Helps to create and invoke {@code weblogic.websocket.tyrus.TyrusServletWriter}.
	 */
	private static class WebLogicServletWriterHelper {

		private static final Constructor<?> constructor;

		static {
			try {
				Class<?> writerType = type("weblogic.websocket.tyrus.TyrusServletWriter");
				Class<?> listenerType = type("weblogic.websocket.tyrus.TyrusServletWriter$CloseListener");
				Class<?> webSocketType = TyrusMuxableWebSocketHelper.type;
				Class<HttpServletResponse> responseType = HttpServletResponse.class;

				Class<?>[] argTypes = (WLS_12_1_3 ?
						new Class<?>[] {webSocketType, responseType, listenerType, boolean.class} :
						new Class<?>[] {webSocketType, listenerType, boolean.class});

				constructor = writerType.getDeclaredConstructor(argTypes);
				ReflectionUtils.makeAccessible(constructor);
			}
			catch (Exception ex) {
				throw new IllegalStateException("No compatible WebSocket version found", ex);
			}
		}

		private Writer newInstance(HttpServletResponse response, Object webSocket, boolean isProtected) {
			try {
				Object[] args = (WLS_12_1_3 ?
						new Object[] {webSocket, response, null, isProtected} :
						new Object[] {webSocket, null, isProtected});

				return (Writer) constructor.newInstance(args);
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to create TyrusServletWriter", ex);
			}
		}
	}

}
