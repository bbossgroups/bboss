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
package org.frameworkset.web.restful;

import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.ResponseBody;

/**
 * <p>Title: LoginName.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-11
 * @author biaoping.yin
 * @version 1.0
 */

public class LoginNameQuery {
	@HandlerMapping(value="/query/loginnamequery/{loginname}")
	public @ResponseBody 
				String  loginnamequery(@PathVariable(value="loginname",decodeCharset="UTF-8") String loginname)
	{
		
			
			if(loginname == null || loginname.trim().equals(""))			
				return "查询中的用户名为空，请重新输入用户名";
			
			if(loginname.equals("多多"))
			{
				return "用户名["+loginname+"]存在。";
			}
			else
				return "用户名["+loginname+"]不存在。";
			
			
		
	}
	
	@HandlerMapping(value="/query/loginName.htm")
	public String loginName()
	{
		return "path:loginName";
	}

}
