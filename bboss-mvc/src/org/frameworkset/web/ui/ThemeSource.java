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
package org.frameworkset.web.ui;

import org.frameworkset.web.servlet.theme.AbstractThemeResolver;
import org.frameworkset.web.ui.context.Theme;



/**
 * <p>Title: ThemeSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface ThemeSource {
	/**
	 * Return the Theme instance for the given theme name.
	 * <p>The returned Theme will resolve theme-specific messages, codes,
	 * file paths, etc (e.g. CSS and image files in a web environment).
	 * @param themeName the name of the theme
	 * @return the corresponding Theme, or <code>null</code> if none defined.
	 * Note that, by convention, a ThemeSource should at least be able to
	 * return a default Theme for the default theme name "theme" but may also
	 * return default Themes for other theme names.
	 * @see AbstractThemeResolver#ORIGINAL_DEFAULT_THEME_NAME
	 */
	Theme getTheme(String themeName);
}
