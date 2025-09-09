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
package org.frameworkset.util.annotations.wraper;

import com.frameworkset.util.BaseSimpleStringUtil;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.CookieValue;

import java.util.Locale;


/**
 * <p>CookieValueWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年9月30日
 * @author biaoping.yin
 * @version 1.0
 */
public class CookieValueWraper extends BaseWraper{
	private Locale locale;
	public CookieValueWraper( CookieValue  cookieValue,Class paramType) {
		super(paramType);
		name = cookieValue.name();
		required = cookieValue.required();
		editor = cookieValue.editor();

		convertValue(  cookieValue.defaultvalue());
		dateformat = AnnotationUtils.converDefaultValue(cookieValue.dateformat());
		if(BaseSimpleStringUtil.isNotEmpty(cookieValue.locale() ))
		 {
			 try
			 {
				 locale = new Locale(cookieValue.locale());
			 }
			 catch(Exception e)
			 {
				 
			 }
		 }
	}
	
	/**
	 * The name of the cookie to bind to.
	 */
	private String name;

	/**
	 * Whether the header is required.
	 * <p>Default is <code>true</code>, leading to an exception being thrown
	 * in case the header is missing in the request. Switch this to
	 * <code>false</code> if you prefer a <code>null</value> in case of the
	 * missing header.
	 * <p>Alternatively, provide a {@link #defaultValue() defaultValue},
	 * which implicitly sets this flag to <code>false</code>.
	 */
	private boolean required;
	
	private String editor;


	/**
	 * 指定日期格式
	 * @return
	 */
	private String dateformat;
	/**
	 * The name of the cookie to bind to.
	 */
	public String name()
	{
		return name;
	}

	/**
	 * Whether the header is required.
	 * <p>Default is <code>true</code>, leading to an exception being thrown
	 * in case the header is missing in the request. Switch this to
	 * <code>false</code> if you prefer a <code>null</value> in case of the
	 * missing header.
	 * <p>Alternatively, provide a {defaultValue() defaultValue},
	 * which implicitly sets this flag to <code>false</code>.
	 */
	public boolean required(){
		return required;
	}
	
	public String editor() {
		return editor;
	}

	/**
	 * 指定日期格式
	 * @return
	 */
	public String dateformat(){
		return dateformat;
	}

	public Locale getLocale() {
		return locale;
	}

}
