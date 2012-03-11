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
package org.frameworkset.web.servlet.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.frameworkset.web.servlet.support.WebApplicationObjectSupport;

/**
 * <p>Title: AbstractCachingViewResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-27
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractCachingViewResolver extends WebApplicationObjectSupport implements ViewResolver {
	
	/** Whether we should cache views, once resolved */
	private boolean cache = true;

	/** Map from view name to View instance */
	private final Map viewCache = new HashMap();


	/**
	 * Enable or disable caching.
	 * <p>Default is "true": caching is enabled.
	 * Disable this only for debugging and development.
	 * <p><b>Warning: Disabling caching can severely impact performance.</b>
	 */
	public void setCache(boolean cache) {
		this.cache = cache;
	}

	/**
	 * Return if caching is enabled.
	 */
	public boolean isCache() {
		return this.cache;
	}


	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if (!isCache()) {
			logger.warn("View caching is SWITCHED OFF -- DEVELOPMENT SETTING ONLY: This can severely impair performance");
			return createView(viewName, locale);
		}
		else {
			Object cacheKey = getCacheKey(viewName, locale);
			synchronized (this.viewCache) {
				View view = (View) this.viewCache.get(cacheKey);
				if (view == null) {
					// Ask the subclass to create the View object.
					view = createView(viewName, locale);
					this.viewCache.put(cacheKey, view);
					if (logger.isTraceEnabled()) {
						logger.trace("Cached view [" + cacheKey + "]");
					}
				}
				return view;
			}
		}
	}

	/**
	 * Return the cache key for the given view name and the given locale.
	 * <p>Default is a String consisting of view name and locale suffix.
	 * Can be overridden in subclasses.
	 * <p>Needs to respect the locale in general, as a different locale can
	 * lead to a different view resource.
	 */
	protected Object getCacheKey(String viewName, Locale locale) {
		return viewName + "_" + locale;
	}

	/**
	 * Provides functionality to clear the cache for a certain view.
	 * <p>This can be handy in case developer are able to modify views
	 * (e.g. Velocity templates) at runtime after which you'd need to
	 * clear the cache for the specified view.
	 * @param viewName the view name for which the cached view object
	 * (if any) needs to be removed
	 * @param locale the locale for which the view object should be removed
	 */
	public void removeFromCache(String viewName, Locale locale) {
		if (!this.cache) {
			logger.warn("View caching is SWITCHED OFF -- removal not necessary");			
		}
		else {
			
			Object cacheKey = getCacheKey(viewName, locale);
			Object cachedView = null;
			synchronized (this.viewCache) {
				cachedView = this.viewCache.remove(cacheKey);
			}
			if (cachedView == null) {
				// Some debug output might be useful...
				if (logger.isDebugEnabled()) {
					logger.debug("No cached instance for view '" + cacheKey + "' was found");
				}
			} 
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Cache for view " + cacheKey + " has been cleared");
				}
			}
		}
	}

	/**
	 * Clear the entire view cache, removing all cached view objects.
	 * Subsequent resolve calls will lead to recreation of demanded view objects.
	 */
	public void clearCache() {
		logger.debug("Clearing entire view cache");
		synchronized (this.viewCache) {
			this.viewCache.clear();
		}
	}


	/**
	 * Create the actual View object.
	 * <p>The default implementation delegates to {@link #loadView}.
	 * This can be overridden to resolve certain view names in a special fashion,
	 * before delegating to the actual <code>loadView</code> implementation
	 * provided by the subclass.
	 * @param viewName the name of the view to retrieve
	 * @param locale the Locale to retrieve the view for
	 * @return the View instance, or <code>null</code> if not found
	 * (optional, to allow for ViewResolver chaining)
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView
	 */
	protected View createView(String viewName, Locale locale) throws Exception {
		return loadView(viewName, locale);
	}

	/**
	 * Subclasses must implement this method, building a View object
	 * for the specified view. The returned View objects will be
	 * cached by this ViewResolver base class.
	 * <p>Subclasses are not forced to support internationalization:
	 * A subclass that does not may simply ignore the locale parameter.
	 * @param viewName the name of the view to retrieve
	 * @param locale the Locale to retrieve the view for
	 * @return the View instance, or <code>null</code> if not found
	 * (optional, to allow for ViewResolver chaining)
	 * @throws Exception if the view couldn't be resolved
	 * @see #resolveViewName
	 */
	protected abstract View loadView(String viewName, Locale locale) throws Exception;

}
