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
package org.frameworkset.web.servlet.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.frameworkset.web.util.WebUtils;

/**
 * <p>Title: RequestMap.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestMap  extends HashMap
{
	private  ServletRequest request;
	private WebRequest webrequest;
	private Map parameters ;
	/** Default prefix separator */
	public static final String DEFAULT_PREFIX_SEPARATOR = "_";
	public RequestMap(ServletRequest request,String prefix,String prefixSeparator)
	{
		super();
		this.request = request;
		this.parameters = WebUtils.getParametersStartingWith(
				request, (prefix != null) ? prefix + prefixSeparator : null);
	}
	/**
	 * Create new ServletRequestPropertyValues using no prefix
	 * (and hence, no prefix separator).
	 * @param request HTTP request
	 */
	public RequestMap(ServletRequest request) {
		this(request, null, null);
	}

	/**
	 * Create new ServletRequestPropertyValues using the given prefix and
	 * the default prefix separator (the underscore character "_").
	 * @param request HTTP request
	 * @param prefix the prefix for parameters (the full prefix will
	 * consist of this plus the separator)
	 * @see #DEFAULT_PREFIX_SEPARATOR
	 */
	 RequestMap(ServletRequest request, String prefix) {
		this(request, prefix, DEFAULT_PREFIX_SEPARATOR);
	}
    public RequestMap(WebRequest webrequest,String prefix,String prefixSeparator) {
    	super();
		this.webrequest = webrequest;
		this.parameters = WebUtils.getParametersStartingWith(
				webrequest, (prefix != null) ? prefix + prefixSeparator : null);
	}
	public RequestMap(WebRequest webrequest) {
		this( webrequest,null,null);
	}
	//	public RequestMap(WebRequest request) {
//		super();
//		if (request instanceof NativeWebRequest) {
//		this.request = ((NativeWebRequest) request).getNativeRequest();
//		this.parameters = WebUtils.getParametersStartingWith(
//				request, (prefix != null) ? prefix + prefixSeparator : null);
//	}
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return parameters.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return parameters.containsValue(value);
	}
	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return this.parameters.entrySet();
	}
	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return this.parameters.get(key);
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return parameters.isEmpty();
	}
	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return parameters.keySet();
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return parameters.size();
	}
	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return parameters.values();
	}
	

}
