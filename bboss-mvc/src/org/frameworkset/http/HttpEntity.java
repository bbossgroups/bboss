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
package org.frameworkset.http;

import org.frameworkset.util.MultiValueMap;

/**
 * <p>Title: HttpEntity.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-21
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpEntity<T> {

	/**
	 * The empty {@code HttpEntity}, with no body or headers.
	 */
	public static final HttpEntity EMPTY = new HttpEntity();


	private final HttpHeaders headers;

	private final T body;


	/**
	 * Create a new, empty {@code HttpEntity}.
	 */
	private HttpEntity() {
		this(null, null);
	}

	/**
	 * Create a new {@code HttpEntity} with the given body and no headers.
	 * @param body the entity body
	 */
	public HttpEntity(T body) {
		this(body, null);
	}

	/**
	 * Create a new {@code HttpEntity} with the given headers and no body.
	 * @param headers the entity headers
	 */
	public HttpEntity(MultiValueMap<String, String> headers) {
		this(null, headers);
	}

	/**
	 * Create a new {@code HttpEntity} with the given body and headers.
	 * @param body the entity body
	 * @param headers the entity headers
	 */
	public HttpEntity(T body, MultiValueMap<String, String> headers) {
		this.body = body;
		HttpHeaders tempHeaders = new HttpHeaders();
		if (headers != null) {
			tempHeaders.putAll(headers);
		}
		this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
	}


	/**
	 * Returns the headers of this entity.
	 */
	public HttpHeaders getHeaders() {
		return this.headers;
	}

	/**
	 * Returns the body of this entity.
	 */
	public T getBody() {
		return this.body;
	}

	/**
	 * Indicates whether this entity has a body.
	 */
	public boolean hasBody() {
		return (this.body != null);
	}

}
