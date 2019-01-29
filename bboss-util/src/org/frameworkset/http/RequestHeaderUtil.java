package org.frameworkset.http;
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

import com.frameworkset.util.StringUtil;
import org.frameworkset.util.LinkedCaseInsensitiveMap;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/1/28 16:01
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestHeaderUtil {

	public static HttpHeaders getHeaders(HttpServletRequest servletRequest) {
		HttpHeaders headers = (HttpHeaders)servletRequest.getAttribute("org_frameworkset_http_HttpHeaders__");
		if(headers != null )
		{
			return headers;
		}

		headers = new HttpHeaders();
		for (Enumeration<?> headerNames =  servletRequest.getHeaderNames(); headerNames.hasMoreElements();) {
			String headerName = (String) headerNames.nextElement();
			for (Enumeration<?> headerValues =  servletRequest.getHeaders(headerName);
				 headerValues.hasMoreElements();) {
				String headerValue = (String) headerValues.nextElement();
				headers.add(headerName, headerValue);
			}
		}
		// HttpServletRequest exposes some headers as properties: we should include those if not already present
		MediaType contentType =  headers.getContentType();
		if (contentType == null) {
			String requestContentType =  servletRequest.getContentType();
			if (StringUtil.hasLength(requestContentType)) {
				contentType = MediaType.parseMediaType(requestContentType);
				headers.setContentType(contentType);
			}
		}
		if (contentType != null && contentType.getCharSet() == null) {
			String requestEncoding =  servletRequest.getCharacterEncoding();
			if (StringUtil.hasLength(requestEncoding)) {
				Charset charSet = Charset.forName(requestEncoding);
				Map<String, String> params = new LinkedCaseInsensitiveMap<String>();
				params.putAll(contentType.getParameters());
				params.put("charset", charSet.toString());
				MediaType newContentType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
				headers.setContentType(newContentType);
			}
		}
		if (headers.getContentLength() == -1) {
			int requestContentLength = servletRequest.getContentLength();
			if (requestContentLength != -1) {
				headers.setContentLength(requestContentLength);
			}
		}
		servletRequest.setAttribute("org_frameworkset_http_HttpHeaders__",headers);

		return headers;
	}

}
