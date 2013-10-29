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
package com.frameworkset.platform.cms.driver.htmlconverter;

import org.htmlparser.util.ParserException;

/**
 * <p>CmsLinkProcessorInf.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public interface CmsLinkProcessorInf {
	/**
	 * 定义处理内容类型
	 */
	/** Processing TEMPLATE links". */
	public static final int PROCESS_TEMPLATE = 0;

	/** Processing CONTENT links". */
	public static final int PROCESS_CONTENT = 1;
	
	/** Processing mode "process links". */
	public static final int PROCESS_LINKS = 1;

	/** Processing mode "replace links". */
	public static final int REPLACE_LINKS = 0;
	
	public void setHandletype(int handletype);

	public String process(String content, String encoding) throws ParserException;
}
