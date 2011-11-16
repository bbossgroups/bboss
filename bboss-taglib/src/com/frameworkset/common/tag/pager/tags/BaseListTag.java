/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
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
