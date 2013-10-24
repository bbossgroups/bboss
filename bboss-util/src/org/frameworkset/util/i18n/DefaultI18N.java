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
package org.frameworkset.util.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>DefaultI18N.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013Äê10ÔÂ24ÈÕ
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultI18N implements I18n {

	public DefaultI18N() {
		// TODO Auto-generated constructor stub
	}

	public Locale getRequestContextLocal(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return  request.getLocale();
	}

//	public String getI18nMessage(String code, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public String getI18nMessage(String code, String defaultMessage,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return defaultMessage;
	}

}
