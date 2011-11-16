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
package org.frameworkset.spi.assemble;

/**
 * 
 * 
 * <p>Title: Context.java</p>
 *
 * <p>Description: 
 * This context is used to defend loopioc.
 * 依赖注入的上下文信息，
 * 如果注入类与被注入类之间存在循环注入的情况，则系统自动报错，是否存在循环注入
 * 是通过上下文信息来判断的
 * 
 * 
 * 
 *
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 14, 2008 4:37:33 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class Context {
	Context parent;
	String refid;
	boolean isroot = false;
	
	public Context(String refid)
	{
		isroot = true;
		this.refid = refid;
	}
	
	public Context(Context parent,String refid)
	{
		this.parent = parent;
		this.refid = refid;
	}
	
	public boolean isLoopIOC(String refid_)
	{
		if(refid_.equals(this.refid))
			return true;
		if(this.isroot)
		{
			return false;
		}
		else if(parent != null)
		{
			return parent.isLoopIOC(refid_);
		}
		return false;
		
	}
	
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		if(this.isroot)
		{
			ret.append(refid);
		}
		else
		{
			ret.append(parent)
			.append(">")
			.append(refid);
		}
		return ret.toString();
	}
	
}
