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
package org.frameworkset.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.Assert;

/**
 * <p>Title: ServletServerHttpResponse.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-22
 * @author biaoping.yin
 * @version 1.0
 */
public class ServletServerHttpResponse  implements ServerHttpResponse {

	private final HttpServletResponse servletResponse;

	private final HttpHeaders headers = new HttpHeaders();

	private boolean headersWritten = false;


	/**
	 * Construct a new instance of the ServletServerHttpResponse based on the given {@link HttpServletResponse}.
	 * @param servletResponse the HTTP Servlet response
	 */
	public ServletServerHttpResponse(HttpServletResponse servletResponse) {
		Assert.notNull(servletResponse, "'servletResponse' must not be null");
		this.servletResponse = servletResponse;
	}


	public void setStatusCode(HttpStatus status) {
		this.servletResponse.setStatus(status.value());
	}

	public HttpHeaders getHeaders() {
		return headersWritten ? HttpHeaders.readOnlyHttpHeaders(headers) : this.headers;
	}

	public OutputStream getBody() throws IOException {
		writeHeaders();
		return this.servletResponse.getOutputStream();
	}

	public void close() {
		writeHeaders();
	}

	private void writeHeaders() {
		if (!this.headersWritten) {
			for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
				String headerName = entry.getKey();
				for (String headerValue : entry.getValue()) {
					this.servletResponse.addHeader(headerName, headerValue);
				}
			}
			this.headersWritten = true;
		}
	}


	public HttpServletResponse getResponse()
	{

		// TODO Auto-generated method stub
		return this.servletResponse;
	}

}
