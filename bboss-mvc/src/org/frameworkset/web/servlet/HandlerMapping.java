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
package org.frameworkset.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: HandlerMapping.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface HandlerMapping {
	/**
	 * Name of the {@link HttpServletRequest} attribute that contains the path
	 * within the handler mapping, in case of a pattern match, or the full
	 * relevant URI (typically within the DispatcherServlet's mapping) else.
	 * <p>Note: This attribute is not required to be supported by all
	 * HandlerMapping implementations. URL-based HandlerMappings will
	 * typically support it, but handlers should not necessarily expect
	 * this request attribute to be present in all scenarios.
	 */
	public static String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";
	public static String HANDLER_MAPPING_PATH_ATTRIBUTE = HandlerMapping.class.getName() + ".HandlerMappingPath";
	public static String HANDLER_REQUESTURI_ATTRIBUTE = HandlerMapping.class.getName() + ".requesturi";
	
	/**
	 * 用来表示控制方法是否是分页方法
	 */
	public static String PAGER_METHOD_FLAG_ATTRIBUTE = HandlerMapping.class.getName() + ".PAGER_METHOD_FLAG";
	public static String PAGER_PAGESIZE_FLAG_ATTRIBUTE = HandlerMapping.class.getName() + ".PAGER_PAGESIZE_FLAG";
	public static String PAGER_COOKIEID_ATTRIBUTE = HandlerMapping.class.getName() + ".PAGER_COOKIEID";
	public static String PAGER_CUSTOM_PAGESIZE_ATTRIBUTE = HandlerMapping.class.getName() + ".PAGER_CUSTOM_PAGESIZE";
	
	public static final int DEFAULT_PAGE_SIZE = 10; 
	

	/**
	 * Return a handler and any interceptors for this request. The choice may be made
	 * on request URL, session state, or any factor the implementing class chooses.
	 * <p>The returned HandlerExecutionChain contains a handler Object, rather than
	 * even a tag interface, so that handlers are not constrained in any way.
	 * For example, a HandlerAdapter could be written to allow another framework's
	 * handler objects to be used.
	 * <p>Returns <code>null</code> if no match was found. This is not an error.
	 * The DispatcherServlet will query all registered HandlerMapping beans to find
	 * a match, and only decide there is an error if none can find a handler.
	 * @param request current HTTP request
	 * @return a HandlerExecutionChain instance containing handler object and
	 * any interceptors, or <code>null</code> if no mapping found
	 * @throws Exception if there is an internal error
	 */
	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;


	void destroy();

}
