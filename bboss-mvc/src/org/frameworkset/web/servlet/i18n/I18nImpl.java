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
package org.frameworkset.web.servlet.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.i18n.I18n;
import org.frameworkset.web.servlet.support.RequestContextUtils;

/**
 * <p>I18nImpl.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月23日
 * @author biaoping.yin
 * @version 1.0
 */
public class I18nImpl implements I18n{

	public I18nImpl() {
		// TODO Auto-generated constructor stub
	}
	public Locale getRequestContextLocal(HttpServletRequest request)
	{
		return RequestContextUtils.getRequestContextLocal( request);
	}
	
	public String getRequestContextLocalCode(HttpServletRequest request)
	{
		return RequestContextUtils.getLocaleCode(request);
	}

//	@Override
//	public String getI18nMessage(String code, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		return RequestContextUtils.getI18nMessage(code, request);
//	}

	
	
	
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值
	 * @param code
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code,HttpServletRequest request)
	{
		return RequestContextUtils.getI18nMessage( code, request);
		
		
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code,String defaultMessage,HttpServletRequest request)
	{
		return RequestContextUtils.getI18nMessage( code, defaultMessage, request);
		
		
	}
	
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code,String defaultMessage)
	{
		return RequestContextUtils.getI18nMessage(code,(Object[])null,defaultMessage,null);
		
		
	}
	
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code)
	{
		return RequestContextUtils.getI18nMessage(code,(Object[])null,(String)null,null);
		
		
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,并且将数组args中的每个元素替换到代码值中位置占位符，例如{0}会用数组的第一个元素替换
	 * @param code
	 * @param args
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code,Object[] args,HttpServletRequest request)
	{
		return RequestContextUtils.getI18nMessage(code,args,(String)null,request);
		
		
	}
	public  String getI18nMessage(String code,Object[] args)
	{
		return RequestContextUtils.getI18nMessage( code, args,(String )null);
	}
	public  String getI18nMessage(String code,Object[] args,String defaultMessage)
	{
		return  RequestContextUtils.getI18nMessage( code,args, defaultMessage,null);
	}
	/**
	 * 根据code从mvc的国际化配置文件中获取对应语言的代码值,如果代码值为空，则返回defaultMessage,并且将数组args中的每个元素替换到代码值中位置占位符，例如{0}会用数组的第一个元素替换
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param request
	 * @return
	 */
	public  String getI18nMessage(String code,Object[] args,String defaultMessage,HttpServletRequest request)
	{
		return RequestContextUtils. getI18nMessage( code,args,defaultMessage,request);
		
		
		
	}
	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, String locale) {
		RequestContextUtils.setLocale(request, response, locale);
		
	}
	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		RequestContextUtils.setLocale(request, response, locale);
		
	}

}
