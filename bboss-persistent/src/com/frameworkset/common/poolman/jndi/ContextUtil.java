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
package com.frameworkset.common.poolman.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.util.JDBCPool;

public class ContextUtil {
	public static String INITIAL_CONTEXT_FACTORY = "INITIAL_CONTEXT_FACTORY".toLowerCase();
	private static Logger log = Logger.getLogger(ContextUtil.class);
	public static Context finaContext(Hashtable environment,ClassLoader loader)
	{
		Context ctx = null;
		
		{
			try {
				// org.apache.naming.java.URLContextFactory s;
	
				// System.setProperty(Context.INITIAL_CONTEXT_FACTORY,"com.frameworkset.common.poolman.management.JndiDataSourceFactory");
				if (environment != null) 
				{
//					Hashtable environment = new Hashtable();
					// environment.put("java.naming.factory.initial",
					// "weblogic.jndi.WLInitialContextFactory");
					// environment.put("java.naming.provider.url",
					// "t3://localhost:7001");
	
//					environment.put("java.naming.security.principal",
//							PoolManConfiguration.jndi_principal);
//					environment.put("java.naming.security.credentials",
//							PoolManConfiguration.jndi_credentials);
					ctx = new InitialContext(environment);
					if(isTomcat(loader))
					{
						ctx = (Context)ctx.lookup("java:comp/env"); 

					}
				} else {
					
					ctx = new InitialContext();
					if(isTomcat(loader))
					{
						ctx = (Context)ctx.lookup("java:comp/env"); 

					}
				}
				testJNDI(ctx);
	
			}
			catch(javax.naming.NoInitialContextException e)
			{
				try {
					System
							.setProperty(Context.INITIAL_CONTEXT_FACTORY,
									"com.frameworkset.common.poolman.jndi.DummyContextFactory");
					ctx = new InitialContext();
				} catch (NamingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			catch (NamingException e) {
				log.info(e.getMessage());
				
			}
			catch (Exception e) {
				
//				try {
//					System
//							.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//									"com.frameworkset.common.poolman.jndi.DummyContextFactory");
//					ctx = new InitialContext();
//					
//				} catch (NamingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				log.info(e.getMessage());
			}
			
			
		}
		return ctx;
	}
	
	public static String handleJndiName(String jndiname)
	{
		if(isTomcat(JDBCPool.class.getClassLoader()) && jndiname != null && jndiname.startsWith("java:comp/env/"))
		{
			return jndiname.substring("java:comp/env/".length());
		}
		return jndiname;
	}
	
	private static void testJNDI(Context ctx) throws NamingException {
		ctx.rebind("test", "1");
		ctx.unbind("test");

	}
	
	public static boolean isTomcat(ClassLoader classLoader) {
		try {
			do
			{
				if(classLoader.getClass().getName().startsWith("org.apache.catalina."))
				{
					return true;
				}
				ClassLoader classLoader_ = classLoader.getParent();
				if(classLoader_ == null || classLoader_ == classLoader)
					break;
				classLoader = classLoader_;
			}while(true);
//			WebappClassLoader classLoader_ = (WebappClassLoader) classLoader;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
