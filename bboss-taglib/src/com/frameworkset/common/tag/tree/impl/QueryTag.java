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
package com.frameworkset.common.tag.tree.impl;

import javax.servlet.jsp.JspException;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.util.VelocityUtil;


/**
 * <p>QueryTag.java</p>
 * <p> Description: 实现树标签的查询功能，适用于静态树和动静结合的树
 * 只能查找已经展示出来的树节点
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2010-1-3
 * @author biaoping.yin
 * @version 1.0
 */
public class QueryTag extends BaseTag
{
	private String rootid= "0";
	
	private String templatepath = "treequery.vm";
	
	
	
	public String getTemplatepath()
	{
	
		return templatepath;
	}


	
	public void setTemplatepath(String templatepath)
	{
	
		this.templatepath = templatepath;
	}


	public String getRootid()
	{
	
		return rootid;
	}

	
	public void setRootid(String rootid)
	{
	
		this.rootid = rootid;
	}

	public int doStartTag() throws JspException
	{
		
		Template template = VelocityUtil.getTemplate(templatepath);
		
		VelocityContext context = new VelocityContext();
		context.put("contextpath",request.getContextPath());
		context.put("rootid",rootid);
		try
		{
			template.merge(context,this.getJspWriter());
		}
		catch (ResourceNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseErrorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MethodInvocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	public int doEndTag() throws JspException
	{
		this.rootid = "0";
		this.templatepath = "treequery.vm";
		return super.doEndTag();
	}
}
