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
package org.frameworkset.web.servlet;

import javax.servlet.ServletException;

import org.frameworkset.util.beans.NestedExceptionUtils;

/**
 * <p>Title: NestedServletException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-1
 * @author biaoping.yin
 * @version 1.0
 */
public class NestedServletException  extends ServletException {

	/** Use serialVersionUID from Bboss 1.2 for interoperability */
	private static final long serialVersionUID = -5292377985529381145L;


	/**
	 * Construct a <code>NestedServletException</code> with the specified detail message.
	 * @param msg the detail message
	 */
	public NestedServletException(String msg) {
		super(msg);
	}

	/**
	 * Construct a <code>NestedServletException</code> with the specified detail message
	 * and nested exception.
	 * @param msg the detail message
	 * @param cause the nested exception
	 */
	public NestedServletException(String msg, Throwable cause) {
		super(msg, cause);
		// Set JDK 1.4 exception chain cause if not done by ServletException class already
		// (this differs between Servlet API versions).
		if (getCause() == null) {
			initCause(cause);
		}
	}


	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}


}
