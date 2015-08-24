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
package org.frameworkset.web.servlet.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.ClassUtil;
import org.frameworkset.web.servlet.view.UrlBasedViewResolver;

/**
 * <p>Title: HandlerMeta.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */

public  class HandlerMeta
{
	private Object handler;
	private Map<String,String> pathNames ;
	private static final Object trace = new Object();
	public HandlerMeta()
	{
		
	}
	public HandlerMeta(Object handler,Map<String,String> pathNames)
	{
		this.handler = handler;
		this.pathNames = pathNames;
	}
	
	/**
	 * @return the handler
	 */
	public Object getHandler() {
		return handler;
	}
	
	public  Class<?> getHandlerClass()
	{
		return ClassUtil.getClassInfo(getHandler().getClass()).getClazz();
	}
	
	/**
	 * @return the handler name
	 */
	public String getHandlerName() {
		if(handler == null)
			return null;
		if(handler instanceof String)
			return (String)handler;
		else
			return getHandlerClass().getCanonicalName();
	}
	/**
	 * @return the pathNames
	 */
	public Map<String,String> getPathNames() {
		return pathNames;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(Object handler) {
		this.handler = handler;
	}
	/**
	 * @param pathNames the pathNames to set
	 */
	public void setPathNames(Map<String,String> pathNames) {
		this.pathNames = pathNames;
	}
	
	public String toString()
	{		
		return this.getHandlerName();
	}
	
	/**
	 * 有效处理死循环
	 * @param path
	 * @param method
	 * @param handler
	 * @param request
	 * @return
	 * @throws PathURLNotSetException
	 */
	public String getUrlPath(String path,String method,Object handler,HttpServletRequest request) throws PathURLNotSetException
	{
		/*
		 * 首先尝试2次连续获取，超过2次别名引用的情况不多，一旦超过两次
		 * 及功过do-while循环解决级联引用的问题
		 */
		//第一次尝试
		String tmp = this.getPathNames() != null? this.getPathNames().get(path):null;
		
		if(tmp == null)
			throw new PathURLNotSetException(path,method,handler,request);
		if(!UrlBasedViewResolver.isPathVariable(tmp))
			return tmp;
		//第二次尝试
		String old = tmp;
		tmp = this.getPathNames().get(tmp);
		if(tmp == null)
			throw new PathURLNotSetException(path,method,handler,request);
		if(!UrlBasedViewResolver.isPathVariable(tmp))
			return tmp;
		if(tmp.equals(old))
		{
			throw new PathURLNotSetException(path,old + "->" + tmp,method,handler,request);
		}
		
		//进入do-while循环环节，记录前两次的调用路径
		List<String> trace = new ArrayList<String>(getPathNames().size());
		trace.add(old);
		trace.add(tmp);
		try
		{
			do
			{	
				tmp = this.getPathNames().get(tmp);
				if(tmp == null)
					throw new PathURLNotSetException(path,method,handler,request);
				
				if(!UrlBasedViewResolver.isPathVariable(tmp))
				{
					break;
				}
				if(trace.contains(tmp))
				{
					trace.add(tmp);
					throw new PathURLNotSetException(path,PathURLNotSetException.buildLooppath(trace),method,handler,request);
				}
				else
					trace.add(tmp);
				
				
			}
			while(true);
		}
		finally
		{
			trace.clear();
			trace = null;
		}
		
		return tmp;
	}
}
