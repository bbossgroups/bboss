package org.frameworkset.web.socket.config;

import org.frameworkset.spi.Lifecycle;
import org.frameworkset.spi.SmartLifecycle;
import org.frameworkset.util.beans.BeansException;
import org.frameworkset.web.servlet.handler.AbstractUrlHandlerMapping;
import org.frameworkset.web.servlet.handler.HandlerMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebSocketHandlerMapping  extends AbstractUrlHandlerMapping  implements SmartLifecycle {
	private volatile boolean running = false;
	public WebSocketHandlerMapping() {
	}
	
	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void start() {
		if (!isRunning()) {
			this.running = true;
//			for (HandlerMeta handler : getUrlMap().values()) {
//				if (handler.getHandler() instanceof Lifecycle) {
//					((Lifecycle) handler.getHandler()).start();
//				}
//			}

            for (Map.Entry<String, HandlerMeta> entry : getUrlMap().entrySet()) {
                HandlerMeta handlerMeta = entry.getValue();
                String key = entry.getKey();
                if (handlerMeta.getHandler() instanceof Lifecycle) {
                    ((Lifecycle) handlerMeta.getHandler()).start(key);
                }
            }
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			this.running = false;
			for (HandlerMeta handler : getUrlMap().values()) {
				if (handler.getHandler() instanceof Lifecycle) {
					((Lifecycle) handler.getHandler()).stop();
				}
			}
		}
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}
	
	private final Map<String, HandlerMeta> urlMap = new LinkedHashMap<String, HandlerMeta>();


//	/**
//	 * Map URL paths to handler bean names.
//	 * This is the typical way of configuring this HandlerMapping.
//	 * <p>Supports direct URL matches and Ant-style pattern matches. For syntax
//	 * details, see the {@link AntPathMatcher} javadoc.
//	 * @param mappings properties with URLs as keys and bean names as values
//	 * @see #setUrlMap
//	 */
//	public void setMappings(Properties mappings) {
//		CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
//	}

	/**
	 * Set a Map with URL paths as keys and handler beans (or handler bean names)
	 * as values. Convenient for population with bean references.
	 * <p>Supports direct URL matches and Ant-style pattern matches. For syntax
	 * details, see the {AntPathMatcher} javadoc.
	 * @param urlMap map with URLs as keys and beans as values
	 */
	public void setUrlMap(Map<String, HandlerMeta> urlMap) {
		this.urlMap.putAll(urlMap);
	}

	/**
	 * Allow Map access to the URL path mappings, with the option to add or
	 * override specific entries.
	 * <p>Useful for specifying entries directly, for example via "urlMap[myKey]".
	 * This is particularly useful for adding or overriding entries in child
	 * bean definitions.
	 */
	public Map<String, HandlerMeta> getUrlMap() {
		return this.urlMap;
	}


	/**
	 * Calls the {@link #registerHandlers} method in addition to the
	 * superclass's initialization.
	 */
	@Override
	public void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		registerHandlers(this.urlMap);
	}

	/**
	 * Register all handlers specified in the URL map for the corresponding paths.
	 * @param urlMap Map with URL paths as keys and handler beans or bean names as values
	 * @throws BeansException if a handler couldn't be registered
	 * @throws IllegalStateException if there is a conflicting handler registered
	 */
	public void registerHandlers(Map<String, HandlerMeta> urlMap) throws BeansException {
		if (urlMap.isEmpty()) {
			logger.warn("Neither 'urlMap' nor 'mappings' set on SimpleUrlHandlerMapping");
		}
		else {
			for (Map.Entry<String, HandlerMeta> entry : urlMap.entrySet()) {
				String url = entry.getKey();
				HandlerMeta handler = entry.getValue();
				// Prepend with slash if not already present.
				if (!url.startsWith("/")) {
					url = "/" + url;
				}
//				// Remove whitespace from handler bean name.
//				if (handler instanceof String) {
//					handler = ((String) handler).trim();
//				}
				try {
					this.registerHandler(url, handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
