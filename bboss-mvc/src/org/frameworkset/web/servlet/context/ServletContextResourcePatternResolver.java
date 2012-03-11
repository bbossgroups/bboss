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
package org.frameworkset.web.servlet.context;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.frameworkset.util.io.PathMatchingResourcePatternResolver;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.ResourceLoader;
import org.frameworkset.web.servlet.support.ServletContextResource;
import org.frameworkset.web.servlet.support.ServletContextResourceLoader;

import com.frameworkset.util.StringUtil;



/**
 * <p>Title: ServletContextResourcePatternResolver.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class ServletContextResourcePatternResolver extends PathMatchingResourcePatternResolver {

	/**
	 * Create a new ServletContextResourcePatternResolver.
	 * @param servletContext the ServletContext to load resources with
	 * @see ServletContextResourceLoader#ServletContextResourceLoader(javax.servlet.ServletContext)
	 */
	public ServletContextResourcePatternResolver(ServletContext servletContext) {
		super(new ServletContextResourceLoader(servletContext));
	}

	/**
	 * Create a new ServletContextResourcePatternResolver.
	 * @param resourceLoader the ResourceLoader to load root directories and
	 * actual resources with
	 */
	public ServletContextResourcePatternResolver(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}


	/**
	 * Overridden version which checks for ServletContextResource
	 * and uses <code>ServletContext.getResourcePaths</code> to find
	 * matching resources below the web application root directory.
	 * In case of other resources, delegates to the superclass version.
	 * @see #doRetrieveMatchingServletContextResources
	 * @see ServletContextResource
	 * @see javax.servlet.ServletContext#getResourcePaths
	 */
	protected Set doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) throws IOException {
		if (rootDirResource instanceof ServletContextResource) {
			ServletContextResource scResource = (ServletContextResource) rootDirResource;
			ServletContext sc = scResource.getServletContext();
			String fullPattern = scResource.getPath() + subPattern;
			Set result = new LinkedHashSet(8);
			doRetrieveMatchingServletContextResources(sc, fullPattern, scResource.getPath(), result);
			return result;
		}
		else {
			return super.doFindPathMatchingFileResources(rootDirResource, subPattern);
		}
	}

	/**
	 * Recursively retrieve ServletContextResources that match the given pattern,
	 * adding them to the given result set.
	 * @param servletContext the ServletContext to work on
	 * @param fullPattern the pattern to match against,
	 * with preprended root directory path
	 * @param dir the current directory
	 * @param result the Set of matching Resources to add to
	 * @throws IOException if directory contents could not be retrieved
	 * @see ServletContextResource
	 * @see javax.servlet.ServletContext#getResourcePaths
	 */
	protected void doRetrieveMatchingServletContextResources(
			ServletContext servletContext, String fullPattern, String dir, Set result) throws IOException {

		Set candidates = servletContext.getResourcePaths(dir);
		if (candidates != null) {
			boolean dirDepthNotFixed = (fullPattern.indexOf("**") != -1);
			for (Iterator it = candidates.iterator(); it.hasNext();) {
				String currPath = (String) it.next();
				if (!currPath.startsWith(dir)) {
					// Returned resource path does not start with relative directory:
					// assuming absolute path returned -> strip absolute path.
					int dirIndex = currPath.indexOf(dir);
					if (dirIndex != -1) {
						currPath = currPath.substring(dirIndex);
					}
				}
				if (currPath.endsWith("/") &&
						(dirDepthNotFixed ||
						StringUtil.countOccurrencesOf(currPath, "/") <= StringUtil.countOccurrencesOf(fullPattern, "/"))) {
					// Search subdirectories recursively: ServletContext.getResourcePaths
					// only returns entries for one directory level.
					doRetrieveMatchingServletContextResources(servletContext, fullPattern, currPath, result);
				}
				if (getPathMatcher().match(fullPattern, currPath)) {
					result.add(new ServletContextResource(servletContext, currPath));
				}
			}
		}
	}

}
