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

package org.frameworkset.spi.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.frameworkset.util.Assert;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.io.ContextResource;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.ResourceLoader;
import org.frameworkset.util.io.UrlResource;

import com.frameworkset.util.SimpleStringUtil;



/**
 * <p>Title: DefaultResourceLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午05:29:08
 * @author biaoping.yin
 * @version 1.0
 */
public class HotResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;


	/**
	 * Create a new DefaultResourceLoader.
	 * <p>ClassLoader access will happen using the thread context class loader
	 * at the time of this ResourceLoader's initialization.
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public HotResourceLoader() {
		this.classLoader = HotResourceLoader.class.getClassLoader();
	}

	/**
	 * Create a new DefaultResourceLoader.
	 * @param classLoader the ClassLoader to load class path resources with, or <code>null</code>
	 * for using the thread context class loader at the time of actual resource access
	 */
	public HotResourceLoader(ClassLoader classLoader) {
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

	private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = (java.lang.Thread.class).getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return (ClassLoader)method.invoke(Thread.currentThread(), null);
    }
	public Resource getResource(String location) {
		Assert.notNull(location, "Location must not be null");
		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
			return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
		}
		else {
			try {
				// Try to parse the location as a URL...
//				
//				URL url = new URL(location);
//				return new UrlResource(url);
				String url = null;
				URL confURL = ServiceProviderManager.class.getClassLoader().getResource(location);
	            if (confURL == null)
	                confURL = ServiceProviderManager.class.getClassLoader().getResource("/" + location);
	
	            if (confURL == null)
	                confURL = getTCL().getResource(location);
	            if (confURL == null)
	                confURL = getTCL().getResource("/" + location);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource(location);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource("/" + location);
	
//	            if (confURL == null) {
//	                url = System.getProperty("user.dir");
//	                url += "/" + location;
//	            } else {
//	                url = confURL.toString();
//	            }
	            return new UrlResource(confURL); 
			}
			catch (Exception ex) {
				// No URL -> resolve as resource path.
				return getResourceByPath(location);
			}
		}
	}

	/**
	 * Return a Resource handle for the resource at the given path.
	 * <p>The default implementation supports class path locations. This should
	 * be appropriate for standalone implementations but can be overridden,
	 * e.g. for implementations targeted at a Servlet container.
	 * @param path the path to the resource
	 * @return the corresponding Resource handle
	 * @see ClassPathResource

	 */
	protected Resource getResourceByPath(String path) {
		return new ClassPathContextResource(path, getClassLoader());
	}


	/**
	 * ClassPathResource that explicitly expresses a context-relative path
	 * through implementing the ContextResource interface.
	 */
	private static class ClassPathContextResource extends ClassPathResource implements ContextResource {

		public ClassPathContextResource(String path, ClassLoader classLoader) {
			super(path, classLoader);
		}

		public String getPathWithinContext() {
			return getPath();
		}

		public Resource createRelative(String relativePath) {
			String pathToUse = SimpleStringUtil.applyRelativePath(getPath(), relativePath);
			return new ClassPathContextResource(pathToUse, getClassLoader());
		}
	}

}
