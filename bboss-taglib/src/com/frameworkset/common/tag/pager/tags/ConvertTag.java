/*
 *  Copyright 2008-2010 biaoping.yin
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
package com.frameworkset.common.tag.pager.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: ConvertTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-4-20 下午04:33:19
 * @author biaoping.yin
 * @version 1.0
 */
public class ConvertTag extends BaseValueTag{
	public static final String scope_request = "request"; 
	public static final String scope_session = "session";
	public static final String scope_pageContext = "pageContext";
	/**
	 * 存储转换数据的key，数据存放的容器可能是Map
	 */
	private String convertData;
	private String scope = scope_request;
	public int doStartTag() throws JspException {
	    init();
	    Object actualValue =  evaluateActualValue();  
	    Object tovalue = getConvertValue(actualValue);
	    try {
	    	if(tovalue != null)
	    		super.getJspWriter().print(tovalue);
	    	else
	    		super.getJspWriter().print(actualValue);
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	    return SKIP_BODY;
	}
	private Object getConvertValue(Object actualValue)
	{
		if(convertData == null || actualValue == null)
			return actualValue;
//		String key = actualValue.toString();
//		Object key = actualValue;
		if(scope.equals(scope_request))
		{
			Map datas = (Map)request.getAttribute(this.convertData);
			if(datas == null) return null;
			return datas.get(actualValue);
		}
		else if(scope.equals(scope_session))
		{
			Map datas = (Map)session.getAttribute(this.convertData);
			if(datas == null) return null;
			return datas.get(actualValue);
		}
		else if(scope.equals(scope_pageContext))
		{
			Map datas = (Map)pageContext.getAttribute(this.convertData);
			if(datas == null) return null;
			return datas.get(actualValue);
		}
		else
		{
			Map datas = (Map)request.getAttribute(this.convertData);
			if(datas == null) return null;
			return datas.get(actualValue);
		}
		
			
		
	}
	/**
	 * @return the converData
	 */
	public String getConvertData() {
		return convertData;
	}
	/**
	 * @param converData the converData to set
	 */
	public void setConvertData(String converData) {
		this.convertData = converData;
	}
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.pager.tags.CellTag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		convertData = null;
		scope = scope_request;
		
		return super.doEndTag();
	}
	
	

}
