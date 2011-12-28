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
package com.frameworkset.util;

import org.frameworkset.util.beans.BeansException;


/**
 * <p>Title: BeanInstantiationException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-29
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanInstantiationException extends BeansException {

	private Class beanClass;



	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 */
	public BeanInstantiationException(Class beanClass, String msg) {
		this(beanClass, msg, null);
	}

	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BeanInstantiationException(Class beanClass, String msg, Throwable cause) {
		super("Could not instantiate bean class [" + beanClass.getName() + "]: " + msg, cause);
		this.beanClass = beanClass;
	}

	

	public BeanInstantiationException(String clazz, String msg,
			Throwable cause) {
		super("Could not instantiate bean class [" + clazz + "]: " + msg, cause);
	}

	/**
	 * Return the offending bean class.
	 */
	public Class getBeanClass() {
		return beanClass;
	}


}
