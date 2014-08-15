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

package org.frameworkset.spi;

import org.frameworkset.util.io.ResourceLoader;



/**
 * <p>Title: ResourceLoaderAware.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午05:27:00
 * @author biaoping.yin
 * @version 1.0
 */
public interface ResourceLoaderAware {
	/**
	 * Set the ResourceLoader that this object runs in.
	 * <p>This might be a ResourcePatternResolver, which can be checked
	 * through <code>instanceof ResourcePatternResolver</code>. See also the
	 * <code>ResourcePatternUtils.getResourcePatternResolver</code> method.
	 * <p>Invoked after population of normal bean properties but before an init callback
	 * like InitializingBean's <code>afterPropertiesSet</code> or a custom init-method.
	 * Invoked before ApplicationContextAware's <code>setApplicationContext</code>.
	 * @param resourceLoader ResourceLoader object to be used by this object

	 */
	void setResourceLoader(ResourceLoader resourceLoader);

}
