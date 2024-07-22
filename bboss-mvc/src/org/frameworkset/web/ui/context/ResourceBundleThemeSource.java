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

import org.frameworkset.spi.support.HierarchicalMessageSource;
import org.frameworkset.spi.support.MessageSource;
import org.frameworkset.spi.support.ReloadableResourceBundleMessageSource;
import org.frameworkset.spi.support.ResourceBundleMessageSource;
import org.frameworkset.web.ui.HierarchicalThemeSource;
import org.frameworkset.web.ui.ThemeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: ResourceBundleThemeSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceBundleThemeSource implements HierarchicalThemeSource {

	protected final Logger logger = LoggerFactory.getLogger(ResourceBundleThemeSource.class);

	private ThemeSource parentThemeSource;

	private String basenamePrefix = "";

	/** Map from theme name to Theme instance */
	private final Map themeCache = new ConcurrentHashMap();


	public void setParentThemeSource(ThemeSource parent) {
		this.parentThemeSource = parent;

		// Update existing Theme objects.
		// Usually there shouldn't be any at the time of this call.
		synchronized (this.themeCache) {
			Iterator it = this.themeCache.values().iterator();
			while (it.hasNext()) {
				initParent((Theme) it.next());
			}
		}
	}

	public ThemeSource getParentThemeSource() {
		return this.parentThemeSource;
	}

	/**
	 * Set the prefix that gets applied to the ResourceBundle basenames,
	 * i.e. the theme names.
	 * E.g.: basenamePrefix="test.", themeName="theme" -> basename="test.theme".
	 * <p>Note that ResourceBundle names are effectively classpath locations: As a
	 * consequence, the JDK's standard ResourceBundle treats dots as package separators.
	 * This means that "test.theme" is effectively equivalent to "test/theme",
	 * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public void setBasenamePrefix(String basenamePrefix) {
		this.basenamePrefix = (basenamePrefix != null ? basenamePrefix : "");
	}


	/**
	 * This implementation returns a SimpleTheme instance, holding a
	 * ResourceBundle-based MessageSource whose basename corresponds to
	 * the given theme name (prefixed by the configured "basenamePrefix").
	 * <p>SimpleTheme instances are cached per theme name. Use a reloadable
	 * MessageSource if themes should reflect changes to the underlying files.
	 * @see #setBasenamePrefix
	 * @see #createMessageSource
	 */
	public Theme getTheme(String themeName) {
		if (themeName == null) {
			return null;
		}
		synchronized (this.themeCache) {
			Theme theme = (Theme) this.themeCache.get(themeName);
			if (theme == null) {
				String basename = this.basenamePrefix + themeName;
				MessageSource messageSource = createMessageSource(basename);
				theme = new SimpleTheme(themeName, messageSource);
				initParent(theme);
				this.themeCache.put(themeName, theme);
				if (logger.isDebugEnabled()) {
					logger.debug("Theme created: name '" + themeName + "', basename [" + basename + "]");
				}
			}
			return theme;
		}
	}

	/**
	 * Create a MessageSource for the given basename,
	 * to be used as MessageSource for the corresponding theme.
	 * <p>Default implementation creates a ResourceBundleMessageSource.
	 * for the given basename. A subclass could create a specifically
	 * configured ReloadableResourceBundleMessageSource, for example.
	 * @param basename the basename to create a MessageSource for
	 * @return the MessageSource
	 * @see ResourceBundleMessageSource
	 * @see ReloadableResourceBundleMessageSource
	 */
	protected MessageSource createMessageSource(String basename) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(basename);
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	/**
	 * Initialize the MessageSource of the given theme with the
	 * one from the corresponding parent of this ThemeSource.
	 * @param theme the Theme to (re-)initialize
	 */
	protected void initParent(Theme theme) {
		if (theme.getMessageSource() instanceof HierarchicalMessageSource) {
			HierarchicalMessageSource messageSource = (HierarchicalMessageSource) theme.getMessageSource();
			if (getParentThemeSource() != null && messageSource.getParentMessageSource() == null) {
				Theme parentTheme = getParentThemeSource().getTheme(theme.getName());
				if (parentTheme != null) {
					messageSource.setParentMessageSource(parentTheme.getMessageSource());
				}
			}
		}
	}
	public String toString(){
//		StringBuilder builder = new StringBuilder();
//		builder.append(this.getClass().getCanonicalName());
		return this.getClass().getCanonicalName();
	}


}
