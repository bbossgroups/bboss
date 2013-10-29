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

package org.frameworkset.util.io;

import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;



/**
 * <p>Title: DefaultResourceLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午05:29:08
 * @author biaoping.yin
 * @version 1.0
 */
public class ClassPathResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;


	/**
	 * Create a new DefaultResourceLoader.
	 * <p>ClassLoader access will happen using the thread context class loader
	 * at the time of this ResourceLoader's initialization.
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public ClassPathResourceLoader() {
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}

	/**
	 * Create a new DefaultResourceLoader.
	 * @param classLoader the ClassLoader to load class path resources with, or <code>null</code>
	 * for using the thread context class loader at the time of actual resource access
	 */
	public ClassPathResourceLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	/**
	 * Specify the ClassLoader to load class path resources with, or <code>null</code>
	 * for using the thread context class loader at the time of actual resource access.
	 * <p>The default is that ClassLoader access will happen using the thread context
	 * class loader at the time of this ResourceLoader's initialization.
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Return the ClassLoader to load class path resources with,
	 * or <code>null</code> if using the thread context class loader on actual access
	 * (applying to the thread that constructs the ClassPathResource object).
	 * <p>Will get passed to ClassPathResource's constructor for all
	 * ClassPathResource objects created by this resource loader.
	 * @see ClassPathResource
	 */
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}


	public Resource getResource(String location) {
		Assert.notNull(location, "Location must not be null");
//		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
//			return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
//		}
//		else {
//			try {
//				// Try to parse the location as a URL...
//				URL url = new URL(location);
//				return new UrlResource(url);
//			}
//			catch (MalformedURLException ex) {
//				// No URL -> resolve as resource path.
//				return getResourceByPath(location);
//			}
//		}
		return new ClassPathResource(location,classLoader);
	}

	

}
