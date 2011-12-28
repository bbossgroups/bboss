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
import org.frameworkset.util.SystemPropertyUtils;

import com.frameworkset.util.EditorInf;
import com.frameworkset.util.SimpleStringUtil;

/**
 * <p>Title: ResourceEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-14
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceEditor  implements EditorInf<Resource> {

	private final ResourceLoader resourceLoader;


	/**
	 * Create a new instance of the {@link ResourceEditor} class
	 * using a {@link DefaultResourceLoader}.
	 */
	public ResourceEditor() {
		this(new DefaultResourceLoader());
	}

	/**
	 * Create a new instance of the {@link ResourceEditor} class
	 * using the given {@link ResourceLoader}.
	 * @param resourceLoader the <code>ResourceLoader</code> to use
	 */
	public ResourceEditor(ResourceLoader resourceLoader) {
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}


	

	/**
	 * Resolve the given path, replacing placeholders with
	 * corresponding system property values if necessary.
	 * @param path the original file path
	 * @return the resolved file path

	 */
	protected String resolvePath(String path) {
		return SystemPropertyUtils.resolvePlaceholders(path);
	}


	

	public Resource getValueFromObject(Object fromValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource getValueFromString(String text) {
		if (SimpleStringUtil.hasText(text)) {
			String locationToUse = resolvePath(text).trim();
			return (this.resourceLoader.getResource(locationToUse));
		}
		else {
			return null;
		}
	}

}
