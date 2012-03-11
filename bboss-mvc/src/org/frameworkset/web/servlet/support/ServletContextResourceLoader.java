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
package org.frameworkset.web.servlet.support;

import javax.servlet.ServletContext;

import org.frameworkset.util.io.DefaultResourceLoader;
import org.frameworkset.util.io.Resource;



/**
 * <p>Title: ServletContextResourceLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class ServletContextResourceLoader  extends DefaultResourceLoader {

	private final ServletContext servletContext;


	/**
	 * Create a new ServletContextResourceLoader.
	 * @param servletContext the ServletContext to load resources with
	 */
	public ServletContextResourceLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * This implementation supports file paths beneath the root of the web application.
	 * @see ServletContextResource
	 */
	protected Resource getResourceByPath(String path) {
		return new ServletContextResource(this.servletContext, path);
	}

}
