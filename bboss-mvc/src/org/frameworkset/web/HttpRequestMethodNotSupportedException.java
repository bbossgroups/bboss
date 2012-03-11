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
package org.frameworkset.web;

import javax.servlet.ServletException;

/**
 * <p>Title: HttpRequestMethodNotSupportedException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpRequestMethodNotSupportedException  extends ServletException {

	private String method;

	private String[] supportedMethods;


	/**
	 * Create a new HttpRequestMethodNotSupportedException.
	 * @param method the unsupported HTTP request method
	 */
	public HttpRequestMethodNotSupportedException(String method) {
		this(method, (String[]) null);
	}

	/**
	 * Create a new HttpRequestMethodNotSupportedException.
	 * @param method the unsupported HTTP request method
	 * @param supportedMethods the actually supported HTTP methods
	 */
	public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods) {
		this(method, supportedMethods, "Request method '" + method + "' not supported");
	}

	/**
	 * Create a new HttpRequestMethodNotSupportedException.
	 * @param method the unsupported HTTP request method
	 * @param msg the detail message
	 */
	public HttpRequestMethodNotSupportedException(String method, String msg) {
		this(method, null, msg);
	}

	/**
	 * Create a new HttpRequestMethodNotSupportedException.
	 * @param method the unsupported HTTP request method
	 * @param supportedMethods the actually supported HTTP methods
	 * @param msg the detail message
	 */
	public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods, String msg) {
		super(msg);
		this.method = method;
		this.supportedMethods = supportedMethods;
	}


	/**
	 * Return the HTTP request method that caused the failure.
	 */
	public String getMethod() {
		return this.method;
	}

	/**
	 * Return the actually supported HTTP methods, if known.
	 */
	public String[] getSupportedMethods() {
		return this.supportedMethods;
	}

}
