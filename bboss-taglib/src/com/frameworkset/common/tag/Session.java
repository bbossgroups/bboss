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
package com.frameworkset.common.tag;

import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.frameworkset.common.tag.pager.tags.PagerDataSet;
import com.frameworkset.util.ValueObjectUtil;
/**
 * session对象的取值标签
 *
 * @author biaoping.yin
 */
public class Session extends BaseTag {
	/**
	 * session中保存的变量名称
	 */
	private String name;
	private String property;
	private String defaultValue;
	private String dateformat;
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}

	public int doStartTag()
	{
		HttpServletRequest request = getHttpServletRequest();
		HttpSession session = request.getSession(false) ;
		Object value = null;
		if (session != null && name != null )
		{
			try
			{
				JspWriter out = this.getJspWriter();
				if(getProperty() == null)
				{
					
					 value = session.getAttribute(name);
//					if(value == null)
//					{
////						out.print(defaultValue == null?"":defaultValue);
//					}
//					else
//						out.print(value);
				}
				else
				{
					 value = session.getAttribute(name);
					if(value == null)
					{
//						out.print(defaultValue == null?"":defaultValue);
					}
					else
					{
					    value = ValueObjectUtil.getValue(value,getProperty());
//					    if(propertyValue == null)
//					        out.print(defaultValue == null?"":defaultValue);
//					    else
//					        out.print(propertyValue);
					}

				}
				String output = null;
				if(value != null)
				{
					if(this.getDateformat() != null && 
							!this.getDateformat().equals(""))
					{
						if(value instanceof Date)
						{
							try
							{
								value = PagerDataSet.formatDate(request,value,  dateformat); 
							}
							catch (Exception e)
							{
								// TODO: handle exception
							}
						}
					}
					output = value.toString();
				}
				else
					output = "";
				 out.print(output);
				return SKIP_BODY;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return SKIP_BODY;
	}
	/**
	 *  Description:
	 * @return String
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  Description:
	 * @param output
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return String
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param string
	 */
	public void setProperty(String string) {
		property = string;
	}

	/**
	 * @return 缺省值
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param string
	 */
	public void setDefaultValue(String string) {
		defaultValue = string;
	}
	
	public String getDateformat()
	{
	
		return dateformat;
	}
	
	public void setDateformat(String dateformat)
	{
	
		this.dateformat = dateformat;
	}

}
