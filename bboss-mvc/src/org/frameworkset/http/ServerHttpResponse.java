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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * <p>Title: ServerHttpResponse.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-22
 * @author biaoping.yin
 * @version 1.0
 */
public interface ServerHttpResponse  extends HttpOutputMessage , Flushable, Closeable {

	/**
	 * Set the HTTP status code of the response.
	 * @param status the HTTP status as an HttpStatus enum value
	 */
	void setStatusCode(HttpStatus status);

	/**
	 * Ensure that the headers and the content of the response are written out.
	 * <p>After the first flush, headers can no longer be changed.
	 * Only further content writing and content flushing is possible.
	 */
	@Override
	void flush() throws IOException;

	/**
	 * Close this response, freeing any resources created.
	 */
	@Override
	void close();
}
