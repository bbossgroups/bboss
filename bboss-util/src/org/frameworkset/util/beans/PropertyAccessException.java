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
package org.frameworkset.util.beans;

import java.beans.PropertyChangeEvent;

/**
 * <p>Title: PropertyAccessException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-13
 * @author biaoping.yin
 * @version 1.0
 */
public class PropertyAccessException  extends BeansException implements ErrorCoded {

	private transient PropertyChangeEvent propertyChangeEvent;
	

	/**
	 * Create a new PropertyAccessException.
	 * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause) {
		super(msg, cause);
		this.propertyChangeEvent = propertyChangeEvent;
	}

	/**
	 * Create a new PropertyAccessException without PropertyChangeEvent.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public PropertyAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}


	/**
	 * Return the PropertyChangeEvent that resulted in the problem.
	 * May be <code>null</code>; only available if an actual bean property
	 * was affected.
	 */
	public PropertyChangeEvent getPropertyChangeEvent() {
		return this.propertyChangeEvent;
	}
	protected String errorCode;
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return errorCode;
	}
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

}
