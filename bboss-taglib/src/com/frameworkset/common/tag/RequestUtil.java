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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.frameworkset.util.ValueObjectUtil;

/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RequestUtil extends BaseTag {

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {

		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {

	}

	/**
	 * 属性或者参数名称
	 */
	private String name;
	private String property;
	private int scope;
	private String method;

	private String parameter;

	private String attribute;
	private String dateformat;

	private String defaultValue = null;

	/**
		 * 对输出进行编码
		 */
	private String encode = null;

	/**
	 * 对输出进行解码
	 */
	private String decode = null;

	public int doStartTag() {
		Object outStr = null;
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		if (name != null)
			try {
				//获取对象属性的值
				if (property != null)
					outStr = this.getBeanValue();
				else
					//获取参数值
					outStr = this.getParameter(name);
			} catch (Exception e) {
				e.printStackTrace();
				return SKIP_BODY;
			}

		if (method != null && method.trim().length() > 0) {
			try {
				//方法有参数时
				if (getParameter() != null)
					outStr =
						 ValueObjectUtil.getValueByMethodName(
							request,
							method,
							new Object[] { getParameter()});
				//方法无参数时
				else
					outStr =
						 (ValueObjectUtil
							.getValueByMethodName(request, method, null));

			} catch (Exception e) {
				e.printStackTrace();
				return SKIP_BODY;
			}
		}

		if (this.parameter != null) {
			try {
				outStr = (getParameterValue(parameter));

			} catch (Exception e1) {
				// TODO 自动生成 catch 块
				e1.printStackTrace();
				return SKIP_BODY;
			}
		}

		if (this.attribute != null) {
			try {
				outStr = (getAttributeValue(attribute));

			} catch (Exception e1) {
				// TODO 自动生成 catch 块
				return SKIP_BODY;
			}
		}
		if(outStr != null)
		{
			if(this.getDateformat() != null && 
					!this.getDateformat().equals(""))
			{
				if(outStr instanceof Date)
				{
					try
					{
						outStr = ValueObjectUtil.getDateFormat(dateformat)
								.format(outStr);
					}
					catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			}
		}
		String output = outStr == null?"":outStr.toString();
		JspWriter out = this.getJspWriter();
		try {
			if (this.getEncode() == null && this.getDecode() == null)
				out.print(output);
			else if (getEncode() != null && this.getEncode().trim().equals("true"))
				out.print(URLEncoder.encode(output));
			else if (getDecode() != null && this.getDecode().trim().equals("true"))
				out.print(URLDecoder.decode(output));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return SKIP_BODY;
	}

	private Object getBeanValue() {
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		Object obj = request.getAttribute(name);
		if (obj != null) {
			Object value = ValueObjectUtil.getValue(obj, property);
			if (value == null) {
				return this.defaultValue == null ? "" : defaultValue;
			}
			
			return value;
		}
		return this.defaultValue == null ? null : defaultValue;
	}

	private Object getParameter(String parameter) {
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		Object value = request.getParameter(parameter);
		if(value == null)
		{
			value = request.getAttribute(parameter);
		}
		if (value == null) {
			return this.defaultValue == null ? null : defaultValue;
		}
		return value;

	}
	private String getParameterValue(String parameter) {
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		String value = request.getParameter(parameter);
		if (value == null) {
			return this.defaultValue == null ? "" : defaultValue;
		}
		return value;

	}

	private Object getAttributeValue(String attribute) {
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		Object value =  request.getAttribute(attribute);
		if (value == null) {
			return this.defaultValue == null ? null : defaultValue;
		}
		return value;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return String
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return scope
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setProperty(String string) {
		property = string;
	}

	/**
	 * @param i
	 */
	public void setScope(int i) {
		scope = i;
	}

	/**
	 * @return String
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param string
	 */
	public void setMethod(String string) {
		method = string;
	}

	/**
	 * @return String
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * @return String
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * @param string
	 */
	public void setAttribute(String string) {
		attribute = string;
	}

	/**
	 * @param string
	 */
	public void setParameter(String string) {
		parameter = string;
	}

	/**
	 * @return 参数的缺省值
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

	/**
	 * Description: 是否编码，“true” or “false”  default :false
	 * @return 是否编码：“true” or “false”
	 * String
	 */
	public String getDecode() {
		return decode;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setDecode(String string) {
		decode = string;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setEncode(String string) {
		encode = string;
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
