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
package org.frameworkset.web.listener;

import java.beans.Introspector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.web.servlet.DispatchServlet;

/**
 * <p>Title: ApplicationLifeListener.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-5
 * @author biaoping.yin
 * @version 1.0
 */
public class ApplicationLifeListener implements ServletContextListener{
/**
 * since bboss 3.6 we remove load custom jars from other directory but /WEB-INF/lib,so if 
 * you update from lower version of bboss you should copy all jars from other lib directories to 
 * /WEB-INF/lib  		
 */
//	static
//	{
//		try {
//			JarUtil.loadCustomJars();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public void contextDestroyed(ServletContextEvent arg0) {
		
		BaseApplicationContext.shutdown();
		ClassUtil.destroy();
		DispatchServlet.destory();
		Introspector.flushCaches();
	}
	
	
	
	
	public void contextInitialized(ServletContextEvent arg0) {
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>public void contextInitialized(ServletContextEvent arg0) {");
	}

}
