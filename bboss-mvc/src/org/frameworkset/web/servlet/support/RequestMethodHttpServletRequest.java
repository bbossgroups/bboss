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
package org.frameworkset.web.servlet.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.frameworkset.util.annotations.HttpMethod;

/**
 * <p>Title: RequestMethodHttpServletRequest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-7
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestMethodHttpServletRequest extends HttpServletRequestWrapper{
	private boolean delegateGetMethod = true;
	public static final String RequestMethod_PARAMETERNAME = "_org_fr_method_name";
	public static final String RequestMethod_delete = "delete";
	public static final String RequestMethod_put = "put";
	public static final String RequestMethod_head = "head";
	public static final String RequestMethod_options = "options";
	public static final String RequestMethod_trace = "trace";
	
	
	public RequestMethodHttpServletRequest(HttpServletRequest request) {
		super(request);
		

	}
	 
	
	public String getMethod()
	{
		if(!this.isDelegateGetMethod())
		{
			return super.getMethod();
		}
		String method = this.getParameter(RequestMethod_PARAMETERNAME);
		if(method == null || method.equals("") )
			return super.getMethod();
		else
		{
			if(method.equals(RequestMethod_delete))
				return HttpMethod.DELETE.toString();
			else if(method.equals(RequestMethod_put))
				return HttpMethod.PUT.toString();
			else if(method.equals(RequestMethod_head))
				return HttpMethod.HEAD.toString();
			else if(method.equals(RequestMethod_options))
				return HttpMethod.OPTIONS.toString();
			else if(method.equals(RequestMethod_trace))
				return HttpMethod.TRACE.toString();
			else
				return HttpMethod.GET.toString();
				
		}
	}
	public boolean isDelegateGetMethod() {
		return delegateGetMethod;
	}
	public void setDelegateGetMethod(boolean delegateGetMethod) {
		this.delegateGetMethod = delegateGetMethod;
	}

}
