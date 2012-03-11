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
package org.frameworkset.web.ui.context;





import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.web.ui.HierarchicalThemeSource;
import org.frameworkset.web.ui.ThemeSource;


/**
 * <p>Title: UiApplicationContextUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class UiApplicationContextUtils {
	
	/**
	 * Name of the ThemeSource bean in the factory.
	 * If none is supplied, theme resolution is delegated to the parent.
	 * @see ThemeSource
	 */
	public static final String THEME_SOURCE_BEAN_NAME = "themeSource";


	private static final Logger logger = Logger.getLogger(UiApplicationContextUtils.class);


	/**
	 * Initialize the ThemeSource for the given application context,
	 * autodetecting a bean with the name "themeSource". If no such
	 * bean is found, a default (empty) ThemeSource will be used.
	 * @param context current application context
	 * @return the initialized theme source (will never be <code>null</code>)
	 * @see #THEME_SOURCE_BEAN_NAME
	 */
	public static ThemeSource initThemeSource(BaseApplicationContext context) {
		if (context.containsBean(THEME_SOURCE_BEAN_NAME)) {
			ThemeSource themeSource = (ThemeSource) context.getBeanObject(THEME_SOURCE_BEAN_NAME);
//			// Make ThemeSource aware of parent ThemeSource.
//			if (context instanceof ThemeSource && themeSource instanceof HierarchicalThemeSource) {
//				HierarchicalThemeSource hts = (HierarchicalThemeSource) themeSource;
//				if (hts.getParentThemeSource() == null) {
//					// Only set parent context as parent ThemeSource if no parent ThemeSource
//					// registered already.
//					hts.setParentThemeSource((ThemeSource) context.getParent());
//				}
//			}
			if (logger.isDebugEnabled()) {
				logger.debug("Using ThemeSource [" + themeSource + "]");
			}
			return themeSource;
		}
		else {
			// Use default ThemeSource to be able to accept getTheme calls, either
			// delegating to parent context's default or to local ResourceBundleThemeSource.
			HierarchicalThemeSource themeSource = null;
//			if (context.getParent() instanceof ThemeSource) {
//				themeSource = new DelegatingThemeSource();
//				themeSource.setParentThemeSource((ThemeSource) context.getParent());
//			}
//			else
			{
				themeSource = new ResourceBundleThemeSource();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to locate ThemeSource with name '" + THEME_SOURCE_BEAN_NAME +
						"': using default [" + themeSource + "]");
			}
			return themeSource;
		}
	}

}
