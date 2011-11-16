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

import java.util.Locale;

import org.frameworkset.util.Assert;



/**
 * <p>Title: SimpleLocaleContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 ÏÂÎç05:02:50
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleLocaleContext implements LocaleContext {

	private final Locale locale;


	/**
	 * Create a new SimpleLocaleContext that exposes the specified Locale.
	 * Every <code>getLocale()</code> will return this Locale.
	 * @param locale the Locale to expose
	 */
	public SimpleLocaleContext(Locale locale) {
		Assert.notNull(locale, "Locale must not be null");
		this.locale = locale;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public String toString() {
		return this.locale.toString();
	}
}
