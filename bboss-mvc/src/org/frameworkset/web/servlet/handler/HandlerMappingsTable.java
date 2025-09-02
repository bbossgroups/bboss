/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.LifecycleProcessor;
import org.frameworkset.web.servlet.HandlerExecutionChain;
import org.frameworkset.web.servlet.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: HandlerMappings.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-30
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerMappingsTable implements DisposableBean{
//	private List<HandlerMapping> handlerMappings;
	HandlerMapping handlerMapping;
	
	HandlerMapping websocketHandlerMapping;
	private static Logger logger = LoggerFactory.getLogger(HandlerMappingsTable.class);
	private boolean scanAllMappings = true;

	private LifecycleProcessor websocketLifecycleProcessor;

	public  HandlerMapping getHandlerMapping() {
		return handlerMapping;
	}
    
    

	public void setHandlerMapping(HandlerMapping handlerMapping) {
		
		this.handlerMapping = handlerMapping;
	}
//	public void registerWebSocketHandler(String pattern, HandlerMeta httpHandler) throws Exception
//	{
//		websocketHandlerMapping.registerWebSocketHandler(  pattern,   httpHandler);
//	}
	public HandlerExecutionChain getHandler(HttpServletRequest request,String servletName) throws Exception
	{
//		HandlerExecutionChain handler;
//		Iterator it = this.handlerMappings.iterator();
//		while (it.hasNext()) {
//			HandlerMapping hm = (HandlerMapping) it.next();
//			if (logger.isTraceEnabled()) {
//				logger.trace("Testing handler map [" + hm  + "] in DispatcherServlet with name '" +
//						servletName + "'");
//			}
//			handler = hm.getHandler(request);
//			if (handler != null) {
//				
//				return handler;
//			}
//			if(!this.scanAllMappings )
//				break;
//		}
		if (logger.isTraceEnabled()) {
			logger.trace("Get handler from map [" + handlerMapping.getClass().getName()  + "] in DispatcherServlet with name '" +
					servletName + "'");
		}
		HandlerExecutionChain handler = handlerMapping.getHandler(request);
		if(handler == null && websocketHandlerMapping != null)
			handler = this.websocketHandlerMapping.getHandler(request);
        if (handler == null) {
            if(logger.isInfoEnabled()) {
                String lookupPath = this.handlerMapping.getLookupPathForRequest(request);
                logger.info("No handler mapping found for [" + lookupPath + "]");
            }
        }
		return handler;
	}

	public boolean isScanAllMappings() {
		return scanAllMappings;
	}

	public void setScanAllMappings(boolean scanAllMappings) {
		this.scanAllMappings = scanAllMappings;
	}

	@Override
	public void destroy() throws Exception {
//		if(handlerMappings != null)
//		{
//			for(int i = 0; i < handlerMappings.size(); i ++)
//			{
//				HandlerMapping mp = handlerMappings.get(i);
//				mp.destroy();
//			}
//		}
		if(handlerMapping != null)
			handlerMapping.destroy();
		if(websocketHandlerMapping != null)
		{
			websocketHandlerMapping.destroy();
		}
		
	}

	public HandlerMapping getWebsocketHandlerMapping() {
		return websocketHandlerMapping;
	}

	public void setWebsocketHandlerMapping(HandlerMapping websocketHandlerMapping) {
		this.websocketHandlerMapping = websocketHandlerMapping;
	}
	
	public void setWebsocketLifecycleProcessor(LifecycleProcessor websocketLifecycleProcessor)
	{
		this.websocketLifecycleProcessor = websocketLifecycleProcessor;
	}

	public LifecycleProcessor getWebsocketLifecycleProcessor() {
		return websocketLifecycleProcessor;
	}


}
