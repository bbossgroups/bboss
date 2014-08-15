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





/**
 * <p>Title: MessageSourceAccessor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午04:52:28
 * @author biaoping.yin
 * @version 1.0
 */
public class MessageSourceAccessor {
	private final MessageSource messageSource;

	private final Locale defaultLocale;

	/**
	 * Create a new MessageSourceAccessor, using LocaleContextHolder's locale
	 * as default locale.
	 * @param messageSource the MessageSource to wrap

	 */
	public MessageSourceAccessor(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.defaultLocale = null;
	}

	/**
	 * Create a new MessageSourceAccessor, using the given default locale.
	 * @param messageSource the MessageSource to wrap
	 * @param defaultLocale the default locale to use for message access
	 */
	public MessageSourceAccessor(MessageSource messageSource, Locale defaultLocale) {
		this.messageSource = messageSource;
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Return the default locale to use if no explicit locale has been given.
	 * <p>The default implementation returns the default locale passed into the
	 * corresponding constructor, or LocaleContextHolder's locale as fallback.
	 * Can be overridden in subclasses.

	 */
	protected Locale getDefaultLocale() {
		return (this.defaultLocale != null ? this.defaultLocale : LocaleContextHolder.getLocale());
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage) {
		return this.messageSource.getMessage(code, null, defaultMessage, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * @param code code of the message
	 * @param defaultMessage String to return if the lookup fails
	 * @param locale Locale in which to do lookup
	 * @return the message
	 */
	public String getMessage(String code, String defaultMessage, Locale locale) {
		return this.messageSource.getMessage(code, null, defaultMessage, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return this.messageSource.getMessage(code, args, defaultMessage, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param defaultMessage String to return if the lookup fails
	 * @param locale Locale in which to do lookup
	 * @return the message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return this.messageSource.getMessage(code, args, defaultMessage, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * @param code code of the message
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, null, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * @param code code of the message
	 * @param locale Locale in which to do lookup
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, null, locale);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, getDefaultLocale());
	}

	/**
	 * Retrieve the message for the given code and the given Locale.
	 * @param code code of the message
	 * @param args arguments for the message, or <code>null</code> if none
	 * @param locale Locale in which to do lookup
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, locale);
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance)
	 * in the default Locale.
	 * @param resolvable the MessageSourceResolvable
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
		return this.messageSource.getMessage(resolvable, getDefaultLocale());
	}

	/**
	 * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance)
	 * in the given Locale.
	 * @param resolvable the MessageSourceResolvable
	 * @param locale Locale in which to do lookup
	 * @return the message
	 * @throws NoSuchMessageException if not found
	 */
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(resolvable, locale);
	}

}
