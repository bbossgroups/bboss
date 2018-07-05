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

package org.frameworkset.spi.remote.http;

/**
 * <p>Title: HttpRuntimeException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-16 下午12:33:15
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpRuntimeException extends RuntimeException {

	protected int httpStatusCode = -1;
	public HttpRuntimeException(){

	}
	public HttpRuntimeException(int httpStatusCode) {
		super();
		this.httpStatusCode = httpStatusCode;
	}

	public HttpRuntimeException(String message, Throwable cause,int httpStatusCode) {
		super(message, cause);
		this.httpStatusCode = httpStatusCode;
	}

	public HttpRuntimeException(String message,int httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public HttpRuntimeException(Throwable cause,int httpStatusCode) {
		super(cause);
		this.httpStatusCode = httpStatusCode;
	}


	public int getHttpStatusCode() {
		return httpStatusCode;
	}
}
