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
package org.frameworkset.web.enumtest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;

/**
 * <p>Title: EnumConvertController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-4-5
 * @author biaoping.yin
 * @version 1.0
 */

public class EnumConvertController {
	/**
	 * 测试单个字符串向枚举类型值转换
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	public  @ResponseBody(charset="UTF-8") String querySex(@RequestParam(name="sex") SexType type) 
	{
		if(type != null)
		{
//			if(type == SexType.F)
//			{
//				response.setContentType("text/html; charset=UTF-8");
//				response.getWriter().print("女");
//			}
//			else if(type == SexType.M)
//			{
//				response.setContentType("text/html; charset=UTF-8");
//				response.getWriter().print("男");
//			}
//			else if(type == SexType.UN)
//			{
//				response.setContentType("text/html; charset=UTF-8");
//				response.getWriter().print("未知");
//			}
			if(type == SexType.F)
			{
				return "女";
			}
			else if(type == SexType.M)
			{
				return "男";
			}
			else if(type == SexType.UN)
			{
				return "未知";
				
			}	
			
		}
		return "未知";
		
	}
	
	/**
	 * 测试单个字符串向枚举类型值转换
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	public  @ResponseBody(charset="UTF-8") String queryMutiSex(@RequestParam(name="sex") SexType[] types)
	{
		if(types != null)
		{
			if(types[0] == SexType.F)
			{
				return "女";
			}
			else if(types[0] == SexType.M)
			{
				return "男";
			}
			else if(types[0] == SexType.UN)
			{
				return "未知";
			}
		}
		return "未知";
	}
	
	public String selectSex()
	{
		return "path:enumpage";
	}
}
