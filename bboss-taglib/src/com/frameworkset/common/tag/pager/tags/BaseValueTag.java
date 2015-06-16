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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.frameworkset.util.ListInfo;


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
	
	
	protected Object evaluateActualValue()
	{
		Object temp = super.getObjectValue();
//		if(this.actual != null)
//		{
//			return actual;
//		}
//		if(this.requestKey == null && this.sessionKey == null && this.pageContextKey == null && parameter == null)
////			temp = getOutStr();
//			temp = super.getObjectValue();
//		else 
//		{
////			Object temp = null;
//			if(this.requestKey != null)
//			{
//				 temp = request.getAttribute(requestKey);
//				
//			}
//			else if(this.sessionKey != null)
//			{
//				temp = session.getAttribute(sessionKey);
//				
//			}
//			else if(this.pageContextKey != null)
//			{
//				temp = this.pageContext.getAttribute(pageContextKey);
//				
//			}
//			else if(this.parameter != null)
//			{
//				temp = this.request.getParameter(parameter);
//				
//			}
//			if(temp != null)
//			{
//				if(this.getProperty() != null)
//				{
//					temp = ValueObjectUtil.getValue(temp,this.getProperty());
//				}
//			}
////			if(temp == null)
////			{
////				actualValue = null;
////			}
////			else
////			{
////				actualValue = temp;
////			}
//		}
		return temp;
	}

	public int doEndTag() throws JspException 
	{
		int ret = super.doEndTag();
		
		
		
		return ret;
		
	}


	
	
}
