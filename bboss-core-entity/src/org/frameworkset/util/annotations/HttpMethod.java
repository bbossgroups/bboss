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
package org.frameworkset.util.annotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: RequestMethod.java</p> 
 * <p>Description: 
 *  Java 5 enumeration of HTTP request methods. Intended for use
 * with the {@link HandlerMapping#method()} attribute of the
 * {@link HandlerMapping} annotation.
 *
 * <p>Note that, by default,  DispatchServlet
 * supports GET, HEAD, POST, PUT and DELETE only. DispatcherServlet will
 * process TRACE and OPTIONS with the default HttpServlet behavior unless
 * explicitly told to dispatch those request types as well: Check out
 * the "dispatchOptionsRequest" and "dispatchTraceRequest" properties,
 * switching them to "true" if necessary.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see HandlerMapping
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public enum HttpMethod {
	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    private static final Logger logger = LoggerFactory.getLogger(HttpMethod.class);
	private static final Map<String, HttpMethod> mappings = new HashMap<String, HttpMethod>(8);

	static {
		for (HttpMethod httpMethod : values()) {
			mappings.put(httpMethod.name(), httpMethod);
		}
	}


	/**
	 * Resolve the given method value to an {@code HttpMethod}.
	 * @param method the method value as a String
	 * @return the corresponding {@code HttpMethod}, or {@code null} if not found
	 * @since 4.2.4
	 */
	public static HttpMethod resolve(String method) {
        if(method == null)
            return null;

        HttpMethod httpMethod = mappings.get(method);
        if(httpMethod != null)
            return httpMethod;
        else {
            if(logger.isWarnEnabled()) {
                logger.warn("HttpMethod resolve warn: No HttpMethod found for method:{} ,should be:GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE", method);
            }
            return null;
        }
	}
    /**
     * Resolve the given method value to an {@code HttpMethod}.
     * @param methods the method value as a String
     * @return the corresponding {@code HttpMethod}, or {@code null} if not found
     * @since 4.2.4
     */
    public static HttpMethod[] resolveHttpMethods(String methods) {
        if(methods == null || methods.length() == 0) 
            return null;
        String[] methodsArray = methods.split(",");
        List<HttpMethod> httpMethods = new ArrayList<HttpMethod>();
        for (int i = 0; i < methodsArray.length; i++){
            HttpMethod httpMethod = resolve(methodsArray[i]);
            if(httpMethod != null){
                httpMethods.add(httpMethod);
            }
        }
        if(httpMethods.size() == 0){
            return null;
        }
        else{
            return httpMethods.toArray(new HttpMethod[httpMethods.size()]);
        }
         
    }




    /**
	 * Determine whether this {@code HttpMethod} matches the given
	 * method value.
	 * @param method the method value as a String
	 * @return {@code true} if it matches, {@code false} otherwise
	 * @since 4.2.4
	 */
	public boolean matches(String method) {
		return name().equals(method);
	}

}
