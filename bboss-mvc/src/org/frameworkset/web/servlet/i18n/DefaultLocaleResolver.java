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

/**
 * <p>DefaultLocaleResolver.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年12月9日
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultLocaleResolver extends AbstractLocaleResolver {
	 private Locale defaultLocale = null;
	 private String defaultLocaleCode = null;
	public DefaultLocaleResolver() {
		defaultLocale = Locale.getDefault();
		this.defaultLocaleCode = this.defaultLocale.toString();
	}
	

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return request == null?defaultLocale:request.getLocale();
	}

	@Override
	public String resolveLocaleCode(HttpServletRequest request) {
		Locale l =  request == null?null:request.getLocale();;
		return l == null?this.defaultLocaleCode:l.toString();
	}

}
