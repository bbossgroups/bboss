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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;


/**
 * <p>PageSizeTag.java</p>
 * <p> Description: 获取页面记录数</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-8-8
 * @author biaoping.yin
 * @version 1.0
 */
public class PageSizeTag extends PagerTagSupport
{
	
	public int doStartTag(){
		
		try
		{
			super.doStartTag();
			HttpServletRequest request = this.getHttpServletRequest();
			if(this.pagerContext == null)
			{
				String t_id = "pagerContext." ;
				
				if(request instanceof CMSServletRequest)
				{
					CMSServletRequest cmsRequest = (CMSServletRequest)request;
					t_id += cmsRequest.getContext().getID() ;
				}
				else if(this.id != null)
				{
					t_id += id;
				}
				else
					t_id += PagerContext.DEFAULT_ID;
				
				this.pagerContext = (PagerContext)request.getAttribute(t_id);
				
			}
			JspWriter out = this.getJspWriter();
			out.print(super.pagerContext.getDataSize());
		}
		catch (JspException e1)
		{
			e1.printStackTrace();
		}			
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		return SKIP_BODY;
	}

	/**
	 *  Description:
	 * @return String
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		return null;
	}

	/**
	 *  Description:
	 * @param output
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output)
	{
		
	}

	

}
