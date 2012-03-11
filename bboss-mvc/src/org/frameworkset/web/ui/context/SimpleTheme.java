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
import org.frameworkset.util.Assert;



/**
 * <p>Title: SimpleTheme.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleTheme  implements Theme {

	private final String name;

	private final MessageSource messageSource;


	/**
	 * Create a SimpleTheme.
	 * @param name the name of the theme
	 * @param messageSource the MessageSource that resolves theme messages
	 */
	public SimpleTheme(String name, MessageSource messageSource) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(messageSource, "MessageSource must not be null");
		this.name = name;
		this.messageSource = messageSource;
	}


	public final String getName() {
		return this.name;
	}

	public final MessageSource getMessageSource() {
		return this.messageSource;
	}

}
