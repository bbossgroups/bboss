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
package org.frameworkset.web.servlet.view;

import org.frameworkset.util.ClassUtils;

/**
 * <p>Title: InternalResourceViewResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-26
 * @author biaoping.yin
 * @version 1.0
 */
public class InternalResourceViewResolver extends UrlBasedViewResolver {

	private static final boolean jstlPresent = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());

	private Boolean alwaysInclude;

	private Boolean exposeContextBeansAsAttributes;

	private String[] exposedContextBeanNames;


	/**
	 * Sets the default {@link #setViewClass view class} to {@link #requiredViewClass}:
	 * by default {@link InternalResourceView}, or {@link JstlView} if the JSTL API
	 * is present.
	 */
	public InternalResourceViewResolver() {
		Class viewClass = requiredViewClass();
		if (viewClass.equals(InternalResourceView.class) && jstlPresent) {
			viewClass = JstlView.class;
		}
		setViewClass(viewClass);
	}

	/**
	 * This resolver requires {@link InternalResourceView}.
	 */
	protected Class requiredViewClass() {
		return InternalResourceView.class;
	}


	/**
	 * Specify whether to always include the view rather than forward to it.
	 * <p>Default is "false". Switch this flag on to enforce the use of a
	 * Servlet include, even if a forward would be possible.
	 * @see InternalResourceView#setAlwaysInclude
	 */
	public void setAlwaysInclude(boolean alwaysInclude) {
		this.alwaysInclude = Boolean.valueOf(alwaysInclude);
	}

	/**
	 * Set whether to make all Bboss beans in the application context accessible
	 * as request attributes, through lazy checking once an attribute gets accessed.
	 * <p>This will make all such beans accessible in plain <code>${...}</code>
	 * expressions in a JSP 2.0 page, as well as in JSTL's <code>c:out</code>
	 * value expressions.
	 * <p>Default is "false".
	 * @see InternalResourceView#setExposeContextBeansAsAttributes
	 */
	public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes) {
		this.exposeContextBeansAsAttributes = Boolean.valueOf(exposeContextBeansAsAttributes);
	}

	/**
	 * Specify the names of beans in the context which are supposed to be exposed.
	 * If this is non-null, only the specified beans are eligible for exposure as
	 * attributes.
	 * @see InternalResourceView#setExposedContextBeanNames
	 */
	public void setExposedContextBeanNames(String[] exposedContextBeanNames) {
		this.exposedContextBeanNames = exposedContextBeanNames;
	}


	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		InternalResourceView view = (InternalResourceView) super.buildView(viewName);
		if (this.alwaysInclude != null) {
			view.setAlwaysInclude(this.alwaysInclude.booleanValue());
		}
		if (this.exposeContextBeansAsAttributes != null) {
			view.setExposeContextBeansAsAttributes(this.exposeContextBeansAsAttributes.booleanValue());
		}
		if (this.exposedContextBeanNames != null) {
			view.setExposedContextBeanNames(this.exposedContextBeanNames);
		}
		view.setPreventDispatchLoop(true);
		return view;
	}

}
