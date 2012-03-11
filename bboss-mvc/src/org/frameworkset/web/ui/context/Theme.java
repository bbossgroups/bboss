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
package org.frameworkset.web.ui.context;

import org.frameworkset.spi.support.MessageSource;



/**
 * <p>Title: Theme.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface Theme {
	/**
	 * Return the name of the theme.
	 * @return the name of the theme (never <code>null</code>)
	 */
	String getName();

	/**
	 * Return the specific MessageSource that resolves messages
	 * with respect to this theme.
	 * @return the theme-specific MessageSource (never <code>null</code>)
	 */
	MessageSource getMessageSource();

}
