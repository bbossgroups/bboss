package com.frameworkset.listener;
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
import com.frameworkset.orm.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * <p>Title: CallableDBUtil.java</p>
 *
 * <p>Description: 存储过程</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 3, 2008 11:48:32 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class BSServletRequestListener implements javax.servlet.ServletRequestListener
{
	private static final Logger log = LoggerFactory.getLogger(BSServletRequestListener.class);
	private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	
//	private static String[] interceptorURIs ;
//	private static final String[] defaultinterceptorURIs = new String[]{".jsp",".do",".page"};
//	public static boolean isInterceptResource(String uri)
//	{
//		init_();
//		for(String type:interceptorURIs)
//		{
//			if(uri.endsWith(type))
//				return true;
//		}
//		return false;
//		
//	}
//	private static final Object lock = new Object();
//	private static void init_()
//	{
//		if(interceptorURIs == null )
//		{
//			synchronized(lock)
//			{
//				if(interceptorURIs == null )
//				{
//					ApplicationContext context = ApplicationContext.getApplicationContext();
//					Pro pro = context.getProBean("transaction.leakcheck.files");
//					if(pro == null)
//						interceptorURIs = defaultinterceptorURIs;
//					else
//					{
//						String[] temp = (String[])pro.getTrueValue();
//						if(temp  == null || temp.length == 0)
//							interceptorURIs = defaultinterceptorURIs;
//						else
//							interceptorURIs = temp;
//					}
//				}
//			}
//		}
//	}
	public void requestDestroyed(ServletRequestEvent requestEvent) {
	
		if(requestEvent.getServletRequest() instanceof HttpServletRequest )
		{
			requestThreadLocal.set(null);
			HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
			boolean state = TransactionManager.destroyTransaction();
			if(state){
				if(log.isWarnEnabled()) {
					String uri = request.getRequestURI();
					log.warn("A DB transaction leaked in Page [{}] has been forcibly destoried. ",uri);
				}
			}
		}		

	}

	/**
	 * 从请求上下文中获取request对象
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest(){
		return requestThreadLocal.get();
	}

	public void requestInitialized(ServletRequestEvent requestEvent) {

		if(requestEvent.getServletRequest() instanceof HttpServletRequest )
		{
			HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
			requestThreadLocal.set(request);
			boolean state = TransactionManager.destroyTransaction();
			if(state){
				if(log.isWarnEnabled()) {
					String uri = request.getRequestURI();
					log.warn("A DB transaction leaked before Page [{}] has been forcibly destoried. ",uri);
				}
			}
		}	
		
	}

	public static void requestReset(HttpServletRequest request) {

		if(request != null && getHttpServletRequest() != null)
		{
			requestThreadLocal.set(request);
			if(log.isDebugEnabled()){
				log.debug("reset request[{}] completed.",request.getClass().getCanonicalName());
			}
		}

	}

}
