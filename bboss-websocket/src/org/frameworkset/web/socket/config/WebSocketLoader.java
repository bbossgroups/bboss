package org.frameworkset.web.socket.config;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;

import org.frameworkset.schedule.ThreadPoolTaskScheduler;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.web.servlet.handler.HandlerMappingsTable;
import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.util.StringUtil;

public class WebSocketLoader {
	private static Logger logger = LoggerFactory.getLogger(WebSocketLoader.class);
	private static String[] websocketattrs = new String[]{
			"enablewebsocket","websocketUrl","sockJSUrl"
	};

	public WebSocketLoader() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 需要确保mvc分派器在webservice服务引擎之前启动，否则获取不到任何在mvc框架中配置的webservice服务
	 */
	public static void loadMvcWebSocketService(ClassLoader classLoader,HandlerMappingsTable mapping,ServletConfig config)
	{
		
		try {
//			org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext
//					.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
			Class clas = Class.forName("org.frameworkset.web.servlet.support.WebApplicationContextUtils");
			Method m = clas.getMethod("getWebApplicationContext", null);
			org.frameworkset.spi.BaseApplicationContext context = (BaseApplicationContext)m.invoke(null, null);
			 
			loadWebSocketService(context,classLoader, mapping);
		} catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
	}
	private static ThreadPoolTaskScheduler defaultSockJsTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("SockJS-");
		scheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
		scheduler.setRemoveOnCancelPolicy(true);
		return scheduler;
	}
	
	private static  void registerWebSocketHandlers(BaseApplicationContext context,Pro wspro,WebSocketHandlerRegistry registry) {
		WebSocketRegistryMeta webSocketRegistryMeta  = context.getTBeanObject(wspro.getName(), WebSocketRegistryMeta.class);
		WebSocketHandler handler = webSocketRegistryMeta.getWebsocketHandler();
		String url = webSocketRegistryMeta.getWebsocketUrl();
		String[] urls = url.split(",");
		HandshakeInterceptor[] iterceptors = null;
		if( webSocketRegistryMeta.getInterceptors() != null)
		{
			iterceptors = new HandshakeInterceptor[webSocketRegistryMeta.getInterceptors().size()];
			iterceptors = webSocketRegistryMeta.getInterceptors().toArray(iterceptors);
		}
		String allowedOrigins = webSocketRegistryMeta.getAllowedOrigins();
		String[] allowedOrigins_ = !StringUtil.isEmpty(allowedOrigins)? allowedOrigins.split(","):null;
		WebSocketHandlerRegistration temp = registry.addHandler(handler, urls).addInterceptors(iterceptors);
		temp.setAllowedOrigins(allowedOrigins_);
	    if(!StringUtil.isEmpty(webSocketRegistryMeta.getSockJSUrl()) )
	    {
	    	urls = webSocketRegistryMeta.getSockJSUrl().split(",");
	    	temp =  registry.addHandler(handler, urls).addInterceptors(iterceptors);
	    	temp.withSockJS();
	    	temp.setAllowedOrigins(allowedOrigins_);
	    }
	}
	public static void loadWebSocketService(BaseApplicationContext context,ClassLoader classLoader,HandlerMappingsTable mapping)
	{
		if(context == null)
			return;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up and Load webservices in application context: " + context.getConfigfile());
		}
		
		ServletWebSocketHandlerRegistry registry = new ServletWebSocketHandlerRegistry(defaultSockJsTaskScheduler());
		
		Set<String> beanNames = context.getPropertyKeys();
		if(beanNames == null || beanNames.size() == 0)
			return ;
		// Take any bean name that we can determine URLs for.
		Iterator<String> beanNamesItr = beanNames.iterator();
		while(beanNamesItr.hasNext()) {
			String beanName = beanNamesItr.next();
			try
			{
				Pro wspro = context.getProBean(beanName);
				
				if(!wspro.getBooleanExtendAttribute(websocketattrs[0], false))
					continue;
				else {
					registerWebSocketHandlers(context,wspro,registry);
					
				}
			}
			catch(Exception e)
			{

				logger.error("Detect Handler bean name '" + beanName + "' failed: " + e.getMessage(),e);
				
			}
			
		}
		registry.registHandlerMapping(mapping);
		
	}

}
