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
package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.DataInfo;

/**
 * 
 * 
 * 分页/列表tag所显示的数据是通过实现
 * com.frameworkset.common.tag.pager.DataInfo的类对象提供。
 * 分页/列表tag会从request中设置的dataInfo属性中获取该类对象，
 * BaseListTag的作用是把改类对象存放到dataInfo属性中， 
 * 如果dataInfo属性名称为空默认名称为“dataInfo”
 * 
 * @author biaoping.yin
 * @version 1.0
 */ 
 
public abstract class BaseListTag extends BaseTag{
	
	/**
	 * 数据获取接口在配置文件中存放的key名称
	 */   
	protected String dataInfo;
	/**设置数据提取对象存放到request中的属性名称,缺省值为dataInfo的值*/
	protected String keyName;
	/**
	 * 设置数据提取对象存放到request中的属性名称
	 * @return dataInfo 数据提取对象存放到request中的属性名称
	 */
	public String getDataInfo() {
		if(dataInfo == null)
			return "dataInfo";
		return dataInfo;
	}

	/**
	 * 设置数据提取对象存放到request中的属性名称
	 * @param string
	 */
	public void setDataInfo(String string) {
		dataInfo = string;
	}
	
	/**
	 * 初始化分页列表的数据获取接口
	 * @param dataInfo 具体的DataInfo接口的实现类
	 */
	public void initDatainfo(DataInfo dataInfo)
	{		
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		if(getKeyName() == null || getKeyName().trim().equals(""))
		{
			request.setAttribute(getDataInfo(),dataInfo);
		}
		else
		{
			request.setAttribute(getKeyName(),dataInfo);			
		}		
	}
	
	/* (non-Javadoc)
		 * @see com.frameworkset.common.tag.BaseTag#generateContent()
		 */
		public String generateContent() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
		 */
		public void write(OutputStream output) {
			// TODO Auto-generated method stub
		
		}
	

	/**
	 * @return String
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * @param string
	 */
	public void setKeyName(String string) {
		keyName = string;
	}

}
