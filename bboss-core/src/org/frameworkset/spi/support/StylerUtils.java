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





/**
 * <p>Title: StylerUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-12 下午11:18:34
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class StylerUtils {
	/**
	 * Default ValueStyler instance used by the <code>style</code> method.
	 * Also available for the {@link ToStringCreator} class in this package.
	 */
	static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();

	/**
	 * Style the specified value according to default conventions.
	 * @param value the Object value to style
	 * @return the styled String
	 * @see DefaultValueStyler
	 */
	public static String style(Object value) {
		return DEFAULT_VALUE_STYLER.style(value);
	}

}
