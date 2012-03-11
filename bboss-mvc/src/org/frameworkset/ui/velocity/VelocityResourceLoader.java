/*
 *  Copyright 2008-2010 biaoping.yin
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

package org.frameworkset.ui.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.frameworkset.util.StringUtil;


/**
 * Velocity ResourceLoader adapter that loads via a Bboss ResourceLoader.
 * Used by VelocityEngineFactory for any resource loader path that cannot
 * be resolved to a <code>java.io.File</code>.
 *
 * <p>Note that this loader does not allow for modification detection:
 * Use Velocity's default FileResourceLoader for <code>java.io.File</code>
 * resources.
 *
 * <p>Expects "Bboss.resource.loader" and "Bboss.resource.loader.path"
 * application attributes in the Velocity runtime: the former of type
 * <code>ResourceLoader</code>, the latter a String.
 *
 * @author Juergen Hoeller
 * @since 14.03.2004
 * @see VelocityEngineFactory#setResourceLoaderPath
 * @see ResourceLoader
 * @see org.apache.velocity.runtime.resource.loader.FileResourceLoader
 */
public class VelocityResourceLoader extends ResourceLoader {

	public static final String NAME = "bboss";

	public static final String RESOURCE_LOADER_CLASS = "resource.loader.class";

	public static final String RESOURCE_LOADER_CACHE = "resource.loader.cache";

	public static final String RESOURCE_LOADER = "resource.loader";

	public static final String RESOURCE_LOADER_PATH = "resource.loader.path";


	protected final static Logger logger = Logger.getLogger(VelocityResourceLoader.class);

	private org.frameworkset.util.io.ResourceLoader resourceLoader;

	private String[] resourceLoaderPaths;


	@Override
	public void init(ExtendedProperties configuration) {
		this.resourceLoader = (org.frameworkset.util.io.ResourceLoader)
				this.rsvc.getApplicationAttribute(RESOURCE_LOADER);
		String resourceLoaderPath = (String) this.rsvc.getApplicationAttribute(RESOURCE_LOADER_PATH);
		if (this.resourceLoader == null) {
			throw new IllegalArgumentException(
					"'resourceLoader' application attribute must be present for BbossResourceLoader");
		}
		if (resourceLoaderPath == null) {
			throw new IllegalArgumentException(
					"'resourceLoaderPath' application attribute must be present for BbossResourceLoader");
		}
		this.resourceLoaderPaths = StringUtil.commaDelimitedListToStringArray(resourceLoaderPath);
		for (int i = 0; i < this.resourceLoaderPaths.length; i++) {
			String path = this.resourceLoaderPaths[i];
			if (!path.endsWith("/")) {
				this.resourceLoaderPaths[i] = path + "/";
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("BbossResourceLoader for Velocity: using resource loader [" + this.resourceLoader +
					"] and resource loader paths " + Arrays.asList(this.resourceLoaderPaths));
		}
	}

	@Override
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for Velocity resource with name [" + source + "]");
		}
		for (int i = 0; i < this.resourceLoaderPaths.length; i++) {
			org.frameworkset.util.io.Resource resource =
					this.resourceLoader.getResource(this.resourceLoaderPaths[i] + source);
			try {
				return resource.getInputStream();
			}
			catch (IOException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Could not find Velocity resource: " + resource);
				}
			}
		}
		throw new ResourceNotFoundException(
				"Could not find resource [" + source + "] in Bboss resource loader path");
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}

}
