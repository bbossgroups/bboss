/*
 *  Copyright 2008-2010 biaoping.yin
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

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import org.frameworkset.http.MediaType;

/**
 * <p>Title: HttpMediaTypeException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-23
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpMediaTypeException  extends ServletException {

	private final List<MediaType> supportedMediaTypes;

	/**
	 * Create a new MediaTypeException.
	 * @param message the exception message
	 */
	protected HttpMediaTypeException(String message) {
		super(message);
		this.supportedMediaTypes = Collections.emptyList();
	}

	/**
	 * Create a new HttpMediaTypeNotSupportedException.
	 * @param supportedMediaTypes the list of supported media types
	 */
	protected HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes) {
		super(message);
		this.supportedMediaTypes = supportedMediaTypes;
	}

	/**
	 * Return the list of supported media types.
	 */
	public List<MediaType> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}

}
