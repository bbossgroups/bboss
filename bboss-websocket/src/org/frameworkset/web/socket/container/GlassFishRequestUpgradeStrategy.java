package org.frameworkset.web.socket.container;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.web.socket.handler.HandshakeFailureException;
import org.glassfish.tyrus.core.TyrusUpgradeResponse;
import org.glassfish.tyrus.core.Utils;
import org.glassfish.tyrus.servlet.TyrusHttpUpgradeHandler;
import org.glassfish.tyrus.spi.WebSocketEngine.UpgradeInfo;
import org.glassfish.tyrus.spi.Writer;

/**
 * A WebSocket {@code RequestUpgradeStrategy} for Oracle's GlassFish 4.1 and higher.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Michael Irwin
 * @since 4.0
 */
public class GlassFishRequestUpgradeStrategy extends AbstractTyrusRequestUpgradeStrategy {

	private static final TyrusEndpointHelper endpointHelper = new Tyrus17EndpointHelper();

	private static final GlassFishServletWriterHelper servletWriterHelper = new GlassFishServletWriterHelper();


	@Override
	protected TyrusEndpointHelper getEndpointHelper() {
		return endpointHelper;
	}

	@Override
	protected void handleSuccess(HttpServletRequest request, HttpServletResponse response,
			UpgradeInfo upgradeInfo, TyrusUpgradeResponse upgradeResponse) throws IOException, ServletException {

		TyrusHttpUpgradeHandler handler = request.upgrade(TyrusHttpUpgradeHandler.class);
		Writer servletWriter = servletWriterHelper.newInstance(handler);
		handler.preInit(upgradeInfo, servletWriter, request.getUserPrincipal() != null);

		response.setStatus(upgradeResponse.getStatus());
		for (Map.Entry<String, List<String>> entry : upgradeResponse.getHeaders().entrySet()) {
			response.addHeader(entry.getKey(), Utils.getHeaderFromList(entry.getValue()));
		}
		response.flushBuffer();
	}


	/**
	 * Helps to create and invoke {@code org.glassfish.tyrus.servlet.TyrusServletWriter}.
	 */
	private static class GlassFishServletWriterHelper {

		private static final Constructor<?> constructor;

		static {
			try {
				ClassLoader classLoader = GlassFishRequestUpgradeStrategy.class.getClassLoader();
				Class<?> type = classLoader.loadClass("org.glassfish.tyrus.servlet.TyrusServletWriter");
				constructor = type.getDeclaredConstructor(TyrusHttpUpgradeHandler.class);
				ReflectionUtils.makeAccessible(constructor);
			}
			catch (Exception ex) {
				throw new IllegalStateException("No compatible Tyrus version found", ex);
			}
		}

		private Writer newInstance(TyrusHttpUpgradeHandler handler) {
			try {
				return (Writer) constructor.newInstance(handler);
			}
			catch (Exception ex) {
				throw new HandshakeFailureException("Failed to instantiate TyrusServletWriter", ex);
			}
		}
	}

}
