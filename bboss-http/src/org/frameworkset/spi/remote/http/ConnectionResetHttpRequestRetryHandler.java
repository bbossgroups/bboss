package org.frameworkset.spi.remote.http;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.SocketException;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/12/18 22:04
 * @author biaoping.yin
 * @version 1.0
 */
public class ConnectionResetHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler{


	/**
	 * Determines if a method should be retried after an IOException
	 * occurs during execution.
	 *
	 * @param exception      the exception that occurred

	 * @return {@code true} if the method should be retried, {@code false}
	 * otherwise
	 */
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context, ClientConfiguration configuration) {
		if (super.retryRequest(  exception,   executionCount,   context,   configuration)) {
			return true;
		}
		else if(exception instanceof SocketException){
			String message = exception.getMessage();
			if(message != null && message.trim().equals("Connection reset")) {
				return true;
			}
		}
		return false;
	}
}
