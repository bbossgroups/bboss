package org.frameworkset.web.socket.handler;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.http.ServerHttpRequest;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.ServletServerHttpRequest;
import org.frameworkset.http.ServletServerHttpResponse;
import org.frameworkset.util.Assert;
import org.frameworkset.web.HttpRequestHandler;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.frameworkset.web.socket.inf.Lifecycle;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.sockjs.CorsConfiguration;
import org.frameworkset.web.socket.sockjs.CorsConfigurationSource;
import org.frameworkset.web.socket.sockjs.SockJsException;
import org.frameworkset.web.socket.sockjs.SockJsService;
import javax.servlet.jsp.PageContext;

/**
 * An {@link HttpRequestHandler} that allows mapping a {@link SockJsService} to requests
 * in a Servlet container.
 *
 * @author Rossen Stoyanchev
 * @author Sebastien Deleuze
 * @since 4.0
 */
public class SockJsHttpRequestHandler
		implements HttpRequestHandler, CorsConfigurationSource, Lifecycle, ServletContextAware {

	// No logging: HTTP transports too verbose and we don't know enough to log anything of value

	private final SockJsService sockJsService;

	private final WebSocketHandler webSocketHandler;

	private volatile boolean running = false;


	/**
	 * Create a new SockJsHttpRequestHandler.
	 * @param sockJsService the SockJS service
	 * @param webSocketHandler the websocket handler
	 */
	public SockJsHttpRequestHandler(SockJsService sockJsService, WebSocketHandler webSocketHandler) {
		Assert.notNull(sockJsService, "SockJsService must not be null");
		Assert.notNull(webSocketHandler, "WebSocketHandler must not be null");
		this.sockJsService = sockJsService;
		this.webSocketHandler =
				new ExceptionWebSocketHandlerDecorator(new LoggingWebSocketHandlerDecorator(webSocketHandler));
	}


	/**
	 * Return the {@link SockJsService}.
	 */
	public SockJsService getSockJsService() {
		return this.sockJsService;
	}

	/**
	 * Return the {@link WebSocketHandler}.
	 */
	public WebSocketHandler getWebSocketHandler() {
		return this.webSocketHandler;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		if (this.sockJsService instanceof ServletContextAware) {
			((ServletContextAware) this.sockJsService).setServletContext(servletContext);
		}
	}


	@Override
	public void start() {
		if (!isRunning()) {
			this.running = true;
			if (this.sockJsService instanceof Lifecycle) {
				((Lifecycle) this.sockJsService).start();
			}
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			this.running = false;
			if (this.sockJsService instanceof Lifecycle) {
				((Lifecycle) this.sockJsService).stop();
			}
		}
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}


	@Override
	public void handleRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse,PageContext pageContext)
			throws ServletException, IOException {

		ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		ServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

		try {
			this.sockJsService.handleRequest(request, response, getSockJsPath(servletRequest), this.webSocketHandler);
		}
		catch (Throwable ex) {
			throw new SockJsException("Uncaught failure in SockJS request, uri=" + request.getURI(), ex);
		}
	}

	private String getSockJsPath(HttpServletRequest servletRequest) {
		String attribute = HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE;
		String path = (String) servletRequest.getAttribute(attribute);
		return (path.length() > 0 && path.charAt(0) != '/' ? "/" + path : path);
	}

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		if (this.sockJsService instanceof CorsConfigurationSource) {
			return ((CorsConfigurationSource) this.sockJsService).getCorsConfiguration(request);
		}
		return null;
	}

}
