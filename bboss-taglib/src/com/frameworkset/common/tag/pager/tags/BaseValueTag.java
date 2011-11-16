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
package com.frameworkset.common.tag.pager.tags;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.common.util.ValueObjectUtil;


/**
 * <p>BaseValueTag.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-5
 * @author biaoping.yin
 * @version 1.0
 */
public class BaseValueTag extends CellTag
{
	protected String requestKey;
	protected String sessionKey;
	protected String pageContextKey;
	protected String parameter;
	
	public String getRequestKey()
	{
	
		return requestKey;
	}

	
	public void setRequestKey(String requestKey)
	{
	
		this.requestKey = requestKey;
	}

	
	public String getSessionKey()
	{
	
		return sessionKey;
	}

	
	public void setSessionKey(String sessionKey)
	{
	
		this.sessionKey = sessionKey;
	}

	
	public String getPageContextKey()
	{
	
		return pageContextKey;
	}

	
	public void setPageContextKey(String pageContextKey)
	{
	
		this.pageContextKey = pageContextKey;
	}

	
	public String getParameter()
	{
	
		return parameter;
	}

	
	public void setParameter(String parameter)
	{
	
		this.parameter = parameter;
	}
	
	protected Object evaluateActualValue()
	{
		Object temp = null;
		if(this.actual != null)
		{
			return actual;
		}
		if(this.requestKey == null && this.sessionKey == null && this.pageContextKey == null && parameter == null)
//			temp = getOutStr();
			temp = super.getObjectValue();
		else 
		{
//			Object temp = null;
			if(this.requestKey != null)
			{
				 temp = request.getAttribute(requestKey);
				
			}
			else if(this.sessionKey != null)
			{
				temp = session.getAttribute(sessionKey);
				
			}
			else if(this.pageContextKey != null)
			{
				temp = this.pageContext.getAttribute(pageContextKey);
				
			}
			else if(this.parameter != null)
			{
				temp = this.request.getParameter(parameter);
				
			}
			if(temp != null)
			{
				if(this.getProperty() != null)
				{
					temp = ValueObjectUtil.getValue(temp,this.getProperty());
				}
			}
//			if(temp == null)
//			{
//				actualValue = null;
//			}
//			else
//			{
//				actualValue = temp;
//			}
		}
		return temp;
	}

	public int doEndTag() throws JspException 
	{
		int ret = super.doEndTag();
		this.requestKey = null ;
		this.sessionKey= null ;
		this.pageContextKey= null ;
		this.parameter= null ;
		
		
		return ret;
		
	}


	
	
}
