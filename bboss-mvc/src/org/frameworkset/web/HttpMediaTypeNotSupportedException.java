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

import java.util.List;

import org.frameworkset.http.MediaType;

/**
 * <p>Title: HttpMediaTypeNotSupportedException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-23
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpMediaTypeNotSupportedException  extends HttpMediaTypeException {

	private final MediaType contentType;

	/**
	 * Create a new HttpMediaTypeNotSupportedException.
	 * @param message the exception message
	 */
	public HttpMediaTypeNotSupportedException(String message) {
		super(message);
		this.contentType = null;
	}

	/**
	 * Create a new HttpMediaTypeNotSupportedException.
	 * @param contentType the unsupported content type
	 * @param supportedMediaTypes the list of supported media types
	 */
	public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes) {
		this(contentType, supportedMediaTypes, "Content type '" + contentType + "' not supported");
	}

	/**
	 * Create a new HttpMediaTypeNotSupportedException.
	 * @param contentType the unsupported content type
	 * @param supportedMediaTypes the list of supported media types
	 * @param msg the detail message
	 */
	public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes, String msg) {
		super(msg, supportedMediaTypes);
		this.contentType = contentType;
	}

	/**
	 * Return the HTTP request content type method that caused the failure.
	 */
	public MediaType getContentType() {
		return contentType;
	}

}
