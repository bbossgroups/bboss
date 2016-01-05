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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.InitializingBean;

import com.frameworkset.util.SimpleStringUtil;

/**
 * <p>CharsetLocaleResolver.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年11月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class LanguageLocaleResolver  extends AbstractLocaleResolver implements InitializingBean{
	private String language = "zh_CN";
	private Locale locale;
	public LanguageLocaleResolver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return locale != null?locale:defaultLocal;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.language == null || this.language.equals(""))
		{
			language = "zh_CN";
		}
		Map<String,Locale> locales = SimpleStringUtil.getAllLocales();	
		locale = locales.get(language);
		
	}
	
	public String resolveLocaleCode(HttpServletRequest request)
	{
		
		return this.language;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if(locale == null)
			return;
		this.language = locale.toString();
		this.locale = locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, String locale) {
		if(locale == null || locale.equals(""))
			return;
		this.language = locale;
		Map<String,Locale> locales = SimpleStringUtil.getAllLocales();	
		this.locale = locales.get(language);
	}

}
