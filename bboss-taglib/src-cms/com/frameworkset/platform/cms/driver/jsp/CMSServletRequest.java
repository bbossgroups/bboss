/**
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
package com.frameworkset.platform.cms.driver.jsp;

import javax.servlet.jsp.PageContext;


/**
 * <p>CMSServletRequest.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public interface CMSServletRequest extends  javax.servlet.http.HttpServletRequest {

	boolean isIncluded();

	ContextInf getContext();

	void setIncluded(boolean b);

	void setIncludedQueryString(String queryString);

	Object getAttribute(String pagerContextid);

	PageContext getPageContext();

	void setAttribute(String pagerContextid, Object oldPagerContext);

}
