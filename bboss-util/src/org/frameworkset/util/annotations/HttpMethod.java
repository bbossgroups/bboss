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


/**
 * <p>Title: RequestMethod.java</p> 
 * <p>Description: 
 *  Java 5 enumeration of HTTP request methods. Intended for use
 * with the {@link RequestMapping#method()} attribute of the
 * {@link RequestMapping} annotation.
 *
 * <p>Note that, by default, {@link org.frameworkset.web.servlet.DispatchServlet}
 * supports GET, HEAD, POST, PUT and DELETE only. DispatcherServlet will
 * process TRACE and OPTIONS with the default HttpServlet behavior unless
 * explicitly told to dispatch those request types as well: Check out
 * the "dispatchOptionsRequest" and "dispatchTraceRequest" properties,
 * switching them to "true" if necessary.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see HandlerMapping
 * @see org.frameworkset.web.servlet.DispatchServlet#setDispatchOptionsRequest
 * @see org.frameworkset.web.servlet.DispatchServlet#setDispatchTraceRequest</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public enum HttpMethod {
	GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE;

}
