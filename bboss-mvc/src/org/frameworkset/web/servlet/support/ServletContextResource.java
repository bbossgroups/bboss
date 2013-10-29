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

package org.frameworkset.web.servlet.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.frameworkset.util.Assert;
import org.frameworkset.util.io.AbstractResource;
import org.frameworkset.util.io.ContextResource;
import org.frameworkset.util.io.Resource;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.StringUtil;



/**
 * <p>Title: ServletContextResource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午10:08:35
 * @author biaoping.yin
 * @version 1.0
 */
public class ServletContextResource extends AbstractResource implements ContextResource {

	private final ServletContext servletContext;

	private final String path;


	/**
	 * Create a new ServletContextResource.
	 * <p>The Servlet spec requires that resource paths start with a slash,
	 * even if many containers accept paths without leading slash too.
	 * Consequently, the given path will be prepended with a slash if it
	 * doesn't already start with one.
	 * @param servletContext the ServletContext to load from
	 * @param path the path of the resource
	 */
	public ServletContextResource(ServletContext servletContext, String path) {
		// check ServletContext
		Assert.notNull(servletContext, "Cannot resolve ServletContextResource without ServletContext");
		this.servletContext = servletContext;

		// check path
		Assert.notNull(path, "Path is required");
		String pathToUse = StringUtil.cleanPath(path);
		if (!pathToUse.startsWith("/")) {
			pathToUse = "/" + pathToUse;
		}
		this.path = pathToUse;
	}

	/**
	 * Return the ServletContext for this resource.
	 */
	public final ServletContext getServletContext() {
		return this.servletContext;
	}

	/**
	 * Return the path for this resource.
	 */
	public final String getPath() {
		return this.path;
	}


	/**
	 * This implementation checks <code>ServletContext.getResource</code>.
	 * @see javax.servlet.ServletContext#getResource(String)
	 */
	public boolean exists() {
		try {
			URL url = this.servletContext.getResource(this.path);
			return (url != null);
		}
		catch (MalformedURLException ex) {
			return false;
		}
	}

	/**
	 * This implementation delegates to <code>ServletContext.getResourceAsStream</code>,
	 * but throws a FileNotFoundException if no resource found.
	 * @see javax.servlet.ServletContext#getResourceAsStream(String)
	 */
	public InputStream getInputStream() throws IOException {
		InputStream is = this.servletContext.getResourceAsStream(this.path);
		if (is == null) {
			throw new FileNotFoundException("Could not open " + getDescription());
		}
		return is;
	}

	/**
	 * This implementation delegates to <code>ServletContext.getResource</code>,
	 * but throws a FileNotFoundException if no resource found.
	 * @see javax.servlet.ServletContext#getResource(String)
	 */
	public URL getURL() throws IOException {
		URL url = this.servletContext.getResource(this.path);
		if (url == null) {
			throw new FileNotFoundException(
					getDescription() + " cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	/**
	 * This implementation delegates to <code>ServletContext.getRealPath</code>,
	 * but throws a FileNotFoundException if not found or not resolvable.
	 * @see javax.servlet.ServletContext#getRealPath(String)
	 */
	public File getFile() throws IOException {
		String realPath = WebUtils.getRealPath(this.servletContext, this.path);
		return new File(realPath);
	}

	/**
	 * This implementation creates a ServletContextResource, applying the given path
	 * relative to the path of the underlying file of this resource descriptor.
	 * @see StringUtil#applyRelativePath(String, String)
	 */
	public Resource createRelative(String relativePath) {
		String pathToUse = StringUtil.applyRelativePath(this.path, relativePath);
		return new ServletContextResource(this.servletContext, pathToUse);
	}

	/**
	 * This implementation returns the name of the file that this ServletContext
	 * resource refers to.
	 * @see StringUtil#getFilename(String)
	 */
	public String getFilename() {
		return StringUtil.getFilename(this.path);
	}

	/**
	 * This implementation returns a description that includes the ServletContext
	 * resource location.
	 */
	public String getDescription() {
		return "ServletContext resource [" + this.path + "]";
	}

	public String getPathWithinContext() {
		return this.path;
	}


	/**
	 * This implementation compares the underlying ServletContext resource locations.
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ServletContextResource) {
			ServletContextResource otherRes = (ServletContextResource) obj;
			return (this.servletContext.equals(otherRes.servletContext) && this.path.equals(otherRes.path));
		}
		return false;
	}

	/**
	 * This implementation returns the hash code of the underlying
	 * ServletContext resource location.
	 */
	public int hashCode() {
		return this.path.hashCode();
	}

}
