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

import org.frameworkset.spi.InitializingBean;



/**
 * <p>Title: AbstractUrlBasedView.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractUrlBasedView  extends AbstractView implements InitializingBean {

	private String url;


	/**
	 * Constructor for use as a bean.
	 */
	protected AbstractUrlBasedView() {
	}

	/**
	 * Create a new AbstractUrlBasedView with the given URL.
	 * @param url the URL to forward to
	 */
	protected AbstractUrlBasedView(String url) {
		this.url = url;
	}


	/**
	 * Set the URL of the resource that this view wraps.
	 * The URL must be appropriate for the concrete View implementation.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the URL of the resource that this view wraps.
	 */
	public String getUrl() {
		return this.url;
	}

	public void afterPropertiesSet() throws Exception {
		if (isUrlRequired() && getUrl() == null) {
			throw new IllegalArgumentException("Property 'url' is required");
		}
	}

	/**
	 * Return whether the 'url' property is required.
	 * <p>The default implementation returns <code>true</code.
	 * This can be overridden in subclasses.
	 */
	protected boolean isUrlRequired() {
		return true;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append("; URL [").append(getUrl()).append("]");
		return sb.toString();
	}

}
