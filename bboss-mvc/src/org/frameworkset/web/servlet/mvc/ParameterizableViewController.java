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
package org.frameworkset.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.web.servlet.ModelAndView;

/**
 * <p>Title: ParameterizableViewController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public class ParameterizableViewController extends AbstractController {
	
	private String viewName;


	/**
	 * Set the name of the view to delegate to.
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Return the name of the view to delegate to.
	 */
	public String getViewName() {
		return this.viewName;
	}

	protected void initApplicationContext() {
		if (this.viewName == null) {
			throw new IllegalArgumentException("Property 'viewName' is required");
		}
	}


	/**
	 * Return a ModelAndView object with the specified view name.
	 * @see #getViewName()
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response, PageContext pageContext)
			throws Exception {

		return new ModelAndView(getViewName());
	}

}
