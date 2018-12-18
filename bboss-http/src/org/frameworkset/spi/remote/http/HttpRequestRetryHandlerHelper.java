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

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/12/18 22:10
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpRequestRetryHandlerHelper implements HttpRequestRetryHandler {
	private static Logger logger = LoggerFactory.getLogger(DefaultHttpRequestRetryHandler.class);
	private CustomHttpRequestRetryHandler httpRequestRetryHandler;
	private ClientConfiguration configuration ;
	public HttpRequestRetryHandlerHelper(CustomHttpRequestRetryHandler httpRequestRetryHandler,ClientConfiguration configuration){
		this.httpRequestRetryHandler = httpRequestRetryHandler;
		this.configuration = configuration;
	}

	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		if (executionCount > configuration.getRetryTime()) {
			logger.warn("Maximum tries[" + configuration.getRetryTime() + "] reached for client http pool ");
			return false;
		}
		if(httpRequestRetryHandler.retryRequest(exception,executionCount,context,configuration)) {
			if (configuration.getRetryInterval() > 0) {
				try {
					Thread.sleep(configuration.getRetryInterval());
				} catch (InterruptedException e1) {
					return false;
				}
			}
			logger.warn(new StringBuilder().append(exception.getClass().getName()).append(" on ")
					.append(executionCount).append(" call").toString());
			return true;
		}
		return false;
	}
}
