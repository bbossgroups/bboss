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

import org.frameworkset.web.ui.HierarchicalThemeSource;
import org.frameworkset.web.ui.ThemeSource;



/**
 * <p>Title: DelegatingThemeSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class DelegatingThemeSource  implements HierarchicalThemeSource {

	private ThemeSource parentThemeSource;


	public void setParentThemeSource(ThemeSource parentThemeSource) {
		this.parentThemeSource = parentThemeSource;
	}

	public ThemeSource getParentThemeSource() {
		return parentThemeSource;
	}


	public Theme getTheme(String themeName) {
		if (this.parentThemeSource != null) {
			return this.parentThemeSource.getTheme(themeName);
		}
		else {
			return null;
		}
	}

}
