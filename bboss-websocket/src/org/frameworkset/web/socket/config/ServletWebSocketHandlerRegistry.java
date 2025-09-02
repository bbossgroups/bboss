package org.frameworkset.web.socket.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.schedule.TaskScheduler;
import org.frameworkset.schedule.ThreadPoolTaskScheduler;
import org.frameworkset.spi.LifecycleProcessor;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.web.HttpRequestHandler;
import org.frameworkset.web.servlet.HandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerMappingsTable;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.servlet.handler.annotations.DefaultAnnotationHandlerMapping;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.util.UrlPathHelper;

public class ServletWebSocketHandlerRegistry implements WebSocketHandlerRegistry {

	private final List<ServletWebSocketHandlerRegistration> registrations =
			new ArrayList<ServletWebSocketHandlerRegistration>();

	private TaskScheduler sockJsTaskScheduler;

	private int order = 1;

	private UrlPathHelper urlPathHelper;


	public ServletWebSocketHandlerRegistry(ThreadPoolTaskScheduler sockJsTaskScheduler) {
		this.sockJsTaskScheduler = sockJsTaskScheduler;
	}
	 
	@Override
	public WebSocketHandlerRegistration addHandler(WebSocketHandler webSocketHandler, String... paths) {
		ServletWebSocketHandlerRegistration registration =
				new ServletWebSocketHandlerRegistration(this.sockJsTaskScheduler);
		registration.addHandler(webSocketHandler, paths);
		this.registrations.add(registration);
		return registration;
	}

	/**
	 * Set the order for the resulting   SimpleUrlHandlerMapping relative to
	 * other handler mappings configured in Spring MVC.
	 * <p>The default value is 1.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the UrlPathHelper to configure on the {@code SimpleUrlHandlerMapping}
	 * used to map handshake requests.
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = urlPathHelper;
	}

	public UrlPathHelper getUrlPathHelper() {
		return this.urlPathHelper;
	}

	/**
	 * Return a {@link HandlerMapping} with mapped {@link HttpRequestHandler}s.
	 */
	public void registHandlerMapping(HandlerMappingsTable mapping) {
		Map<String, HandlerMeta> urlMap = new LinkedHashMap<String, HandlerMeta>();
		for (ServletWebSocketHandlerRegistration registration : this.registrations) {
			MultiValueMap<HandlerMeta, String> mappings = registration.getMappings(mapping);
			for (HandlerMeta httpHandler : mappings.keySet()) {
				for (String pattern : mappings.get(httpHandler)) {
					urlMap.put(pattern, httpHandler);
					
				}
			}
		}
		WebSocketHandlerMapping websocketHandlerMapping = new WebSocketHandlerMapping();
        HandlerMapping handlerMapping = mapping.getHandlerMapping();
        if(handlerMapping instanceof DefaultAnnotationHandlerMapping) {
            DefaultAnnotationHandlerMapping  defaultHandlerMapping =  (DefaultAnnotationHandlerMapping)handlerMapping;
            if(defaultHandlerMapping.getApplicationContext() != null)
                websocketHandlerMapping.setApplicationContext(defaultHandlerMapping.getApplicationContext());
            if(defaultHandlerMapping.getServletContext() != null)
                websocketHandlerMapping.setServletContext(defaultHandlerMapping.getServletContext());
           
        }
		websocketHandlerMapping.setUrlMap(urlMap);
//		hm.setOrder(this.order);
		if (this.urlPathHelper != null) {
			websocketHandlerMapping.setUrlPathHelper(this.urlPathHelper);
		}
		websocketHandlerMapping.initApplicationContext();
		mapping.setWebsocketHandlerMapping(websocketHandlerMapping);
		LifecycleProcessor lifecycleProcessor = new WebsocketLifecycleProcessor(websocketHandlerMapping);
		mapping.setWebsocketLifecycleProcessor(lifecycleProcessor);
//		return hm;
	}

	

}
