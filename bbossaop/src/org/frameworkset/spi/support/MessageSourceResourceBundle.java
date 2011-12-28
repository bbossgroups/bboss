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

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.frameworkset.util.Assert;

/**
 * <p>Title: MessageSourceResourceBundle.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public class MessageSourceResourceBundle  extends ResourceBundle {

	private final MessageSource messageSource;

	private final Locale locale;


	/**
	 * Create a new MessageSourceResourceBundle for the given MessageSource and Locale.
	 * @param source the MessageSource to retrieve messages from
	 * @param locale the Locale to retrieve messages for
	 */
	public MessageSourceResourceBundle(MessageSource source, Locale locale) {
		Assert.notNull(source, "MessageSource must not be null");
		this.messageSource = source;
		this.locale = locale;
	}

	/**
	 * Create a new MessageSourceResourceBundle for the given MessageSource and Locale.
	 * @param source the MessageSource to retrieve messages from
	 * @param locale the Locale to retrieve messages for
	 * @param parent the parent ResourceBundle to delegate to if no local message found
	 */
	public MessageSourceResourceBundle(MessageSource source, Locale locale, ResourceBundle parent) {
		this(source, locale);
		setParent(parent);
	}


	/**
	 * This implementation resolves the code in the MessageSource.
	 * Returns <code>null</code> if the message could not be resolved.
	 */
	protected Object handleGetObject(String code) {
		try {
			return this.messageSource.getMessage(code, null, this.locale);
		}
		catch (NoSuchMessageException ex) {
			return null;
		}
	}

	/**
	 * This implementation returns <code>null</code>, as a MessageSource does
	 * not allow for enumerating the defined message codes.
	 */
	public Enumeration getKeys() {
		return null;
	}

	/**
	 * This implementation exposes the specified Locale for introspection
	 * through the standard <code>ResourceBundle.getLocale()</code> method.
	 */
	public Locale getLocale() {
		return this.locale;
	}

}
