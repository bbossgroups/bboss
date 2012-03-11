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
package org.frameworkset.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.theme.FixedThemeResolver;
import org.frameworkset.web.ui.ThemeSource;
import org.frameworkset.web.ui.context.Theme;

/**
 * <p>Title: ThemeResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 * @see FixedThemeResolver
 * @see Theme
 * @see ThemeSource
 * @see RequestContext#getTheme
 */
public interface ThemeResolver {
	/**
	   * Resolve the current theme name via the given request.
	   * Should return a default theme as fallback in any case.
	   * @param request request to be used for resolution
	   * @return the current theme name
	   */
		String resolveThemeName(HttpServletRequest request);

	  /**
	   * Set the current theme name to the given one.
	   * @param request request to be used for theme name modification
	   * @param response response to be used for theme name modification
	   * @param themeName the new theme name
		 * @throws UnsupportedOperationException if the ThemeResolver implementation
		 * does not support dynamic changing of the theme
	   */
		void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName);


}
