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

/**
 * <p>Title: BeanClassLoaderAware.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午06:55:42
 * @author biaoping.yin
 * @version 1.0
 */
public interface BeanClassLoaderAware {
	/**
	 * Callback that supplies the bean {@link ClassLoader class loader} to
	 * a bean instance.
	 * <p>Invoked <i>after</i> the population of normal bean properties but
	 * <i>before</i> an initialization callback such as
	 * {@link org.frameworkset.spi.InitializingBean InitializingBean's}
	 * {@link org.frameworkset.spi.InitializingBean#afterPropertiesSet()}
	 * method or a custom init-method.
	 * @param classLoader the owning class loader; may be <code>null</code> in
	 * which case a default <code>ClassLoader</code> must be used, for example
	 * the <code>ClassLoader</code> obtained via
	 * {@link org.frameworkset.util.ClassUtils#getDefaultClassLoader()}
	 */
	void setBeanClassLoader(ClassLoader classLoader);

}
