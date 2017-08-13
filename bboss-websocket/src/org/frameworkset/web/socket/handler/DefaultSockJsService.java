package org.frameworkset.web.socket.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.web.servlet.context.ServletContextAware;
import org.frameworkset.web.socket.sockjs.TransportHandler;
import org.frameworkset.web.socket.sockjs.transport.EventSourceTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.HtmlFileTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.JsonpPollingTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.JsonpReceivingTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.TransportHandlingSockJsService;
import org.frameworkset.web.socket.sockjs.transport.XhrPollingTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.XhrReceivingTransportHandler;
import org.frameworkset.web.socket.sockjs.transport.XhrStreamingTransportHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSockJsService  extends TransportHandlingSockJsService implements ServletContextAware {

	/**
	 * Create a DefaultSockJsService with default {@link TransportHandler handler} types.
	 * @param scheduler a task scheduler for heart-beat messages and removing
	 * timed-out sessions; the provided TaskScheduler should be declared as a
	 * Spring bean to ensure it is initialized at start up and shut down when the
	 * application stops.
	 */
	public DefaultSockJsService(TaskScheduler scheduler) {
		this(scheduler, getDefaultTransportHandlers(null));
	}

	/**
	 * Create a DefaultSockJsService with overridden {@link TransportHandler handler} types
	 * replacing the corresponding default handler implementation.
	 * @param scheduler a task scheduler for heart-beat messages and removing timed-out sessions;
	 * the provided TaskScheduler should be declared as a Spring bean to ensure it gets
	 * initialized at start-up and shuts down when the application stops
	 * @param handlerOverrides zero or more overrides to the default transport handler types
	 */
	public DefaultSockJsService(TaskScheduler scheduler, TransportHandler... handlerOverrides) {
		this(scheduler, Arrays.asList(handlerOverrides));
	}

	/**
	 * Create a DefaultSockJsService with overridden {@link TransportHandler handler} types
	 * replacing the corresponding default handler implementation.
	 * @param scheduler a task scheduler for heart-beat messages and removing timed-out sessions;
	 * the provided TaskScheduler should be declared as a Spring bean to ensure it gets
	 * initialized at start-up and shuts down when the application stops
	 * @param handlerOverrides zero or more overrides to the default transport handler types
	 */
	public DefaultSockJsService(TaskScheduler scheduler, Collection<TransportHandler> handlerOverrides) {
		super(scheduler, getDefaultTransportHandlers(handlerOverrides));
	}


	private static Set<TransportHandler> getDefaultTransportHandlers(Collection<TransportHandler> overrides) {
		Set<TransportHandler> result = new LinkedHashSet<TransportHandler>(8);
		result.add(new XhrPollingTransportHandler());
		result.add(new XhrReceivingTransportHandler());
		result.add(new XhrStreamingTransportHandler());
		result.add(new JsonpPollingTransportHandler());
		result.add(new JsonpReceivingTransportHandler());
		result.add(new EventSourceTransportHandler());
		result.add(new HtmlFileTransportHandler());
		try {
			result.add(new WebSocketTransportHandler(new DefaultHandshakeHandler()));
		}
		catch (Exception ex) {
			Logger logger = LoggerFactory.getLogger(DefaultSockJsService.class);
			if (logger.isWarnEnabled()) {
				logger.warn("Failed to create a default WebSocketTransportHandler", ex);
			}
		}
		if (overrides != null) {
			result.addAll(overrides);
		}
		return result;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		for (TransportHandler handler : getTransportHandlers().values()) {
			if (handler instanceof ServletContextAware) {
				((ServletContextAware) handler).setServletContext(servletContext);
			}
		}
	}

}
